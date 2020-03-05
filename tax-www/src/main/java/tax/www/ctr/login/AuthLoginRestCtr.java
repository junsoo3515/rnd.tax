package tax.www.ctr.login;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import tax.www.module.secure.encryption.CmnRsaBiz;
import tax.www.module.secure.encryption.CmnRsaOaepBiz;
import tax.www.module.secure.filter.CmnFilterBiz;
import tax.www.module.session.CmnSessionBiz;
import tax.www.service.login.AuthLoginService;
import tax.www.service.security.SecurityUserService;
import tax.www.vo.login.AuthLoginVO;
import tax.www.vo.login.UserVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 사용자 로그인
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:43
 */
@RestController
@RequestMapping("/login")
public class AuthLoginRestCtr {

    private static final Logger log = LogManager.getLogger(AuthLoginRestCtr.class);

    @Resource(name = "AuthLoginService")
    private AuthLoginService authLoginService;

    @Resource(name = "SecurityUserService")
    private SecurityUserService securityUserService;

    /**
     * 로그인 Process 처리
     * <p>
     * RequestBody는 JSON 수신시 사용
     *
     * @param vo 입력내역이 담긴 JSON형태의 VO
     * @return Map
     * - idState : 0 : 사용중, 1 : 없음, 2 : 탈퇴, 3 : 에러(DB 오류 및 Exception에 걸릴 경우), 4 : 비밀번호 실패 5회 사용자
     * - isSuccess : 정상 로그인 결과(true / false)
     * - returnUrl : 액션 처리 후의 이동 할 페이지 URL(이전 URL)
     */
    @RequestMapping(value = "/act", method = RequestMethod.POST)
    public Map act(@RequestBody AuthLoginVO vo, HttpSession ses, HttpServletRequest req) {

        Map<String, Object> map = new HashMap<String, Object>();

        boolean isSuccess = false;
        int idState = 0;
        String retURL = vo.returnUrl;
        int fail_pwd_cnt = 0;

        try {
            // 1. 필터링
            CmnFilterBiz.filterSqlClass(vo);

            // 1-1. RSA 복호화
            PrivateKey privateKey = (PrivateKey) ses.getAttribute("_RSA_WEB_Key_");

            vo.memID = CmnRsaBiz.decryptRsa(privateKey, vo.memID);
            vo.memPWD = CmnRsaBiz.decryptRsa(privateKey, vo.memPWD);

            // 2. 아이디 대문자 변환
            if (StringUtils.isEmpty(vo.memID) == false) {

                vo.memID = vo.memID.toUpperCase();
            }

            // 3. 사용자 정보 가져오기
            UserVO retVO = authLoginService.getMemberData(vo);

            // 4. 회원 사용 상태 체크
            if (retVO != null) {

                if (StringUtils.equals(retVO.use_fl, "N") == true) {

                    idState = 2;
                }

                if (retVO.fail_pwd_cnt > 4) {

                    idState = 4;
                }

                fail_pwd_cnt = retVO.fail_pwd_cnt;
            } else {

                idState = 1;
            }

            // 4. 사용자 등급 및 정보 Setting
            if (idState == 0) {

                if (StringUtils.equals(retVO.pwd, CmnRsaOaepBiz.encrypt(vo.memPWD)) == true) {

                    if (!StringUtils.isEmpty(vo.returnUrl) == false) {

                        retURL = "/";
                    }

                    try {
                        // TODO 클러스터링 지원을 위해 사용
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("id", retVO.mem_id);
                        params.put("authCD", retVO.auth_cd);

                        // 헤더에서 이름
                        params.put("member_profile_seq", String.valueOf(retVO.files_seq));
                        params.put("member_nm", retVO.nm);

                        CmnSessionBiz.setSessionAttribute(ses, params);

                        params.clear();
                        params.put("token", ses.getId());

                        CmnSessionBiz.setSessionAttribute(ses, params);

                        map.put("token", ses.getId());
                        map.put("expireIn", ses.getMaxInactiveInterval()); // 초
                        map.put("id", retVO.mem_id);

                        ses.removeAttribute("_RSA_WEB_Key_"); // 개인키 삭제

                        securityUserService.setUserFailPwdClear(retVO.mem_id.toUpperCase()); // 로그인 성공 시 비밀번호 틀린 횟수 초기화

                        isSuccess = true;
                    } catch (Exception ex) {

                        idState = 3;
                        log.error(ex.toString(), ex);
                    }
                } else {
                    // 비밀번호 틀릴 경우
                    authLoginService.setLoginFail(vo.memID);

                    fail_pwd_cnt++;
                }
            }
        } catch (Exception ex) {

            idState = 3;
            log.error(ex.toString(), ex);
        }

        map.put("isSuccess", isSuccess);
        map.put("idState", idState);
        map.put("fail_cnt", fail_pwd_cnt);
        map.put("returnUrl", retURL);

        return map;
    }

    /**
     * 로그인 상태 체크
     *
     * @param sessionid token 키
     * @return Map
     * - invalidated : OK || DIE(session이 유효한 상태 체크)
     */
    @RequestMapping(value = "/live/{sessionid:.+}", method = RequestMethod.GET)
    public Map get(@PathVariable String sessionid, HttpSession ses) {

        boolean isDie = true;
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            // TODO tomcat 자체의 context session 공유 로그아웃 문제 발생에 따른 원복 ses.getServletContext().getContext(contextRoot)
            if (ses.getAttribute("id") != null) {

                if (StringUtils.equals(ses.getId(), sessionid)) isDie = false;
            }
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        } finally {

            map.put("invalidated", isDie);
        }

        return map;
    }

    /**
     * 로그아웃
     *
     * @param req HttpServletRequest
     * @return Map
     * - invalidated : OK || DIE(session이 유효한 상태 체크)
     */
    @RequestMapping(value = "/invalidate", method = RequestMethod.GET)
    public Map invalidate(HttpServletRequest req) {

        boolean isDie = false;
        Map<String, Object> map = new HashMap<String, Object>();

        try {

            CmnSessionBiz.invalidateSession(req.getSession()); // Session 소멸

            isDie = true;
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        } finally {

            map.put("invalidated", isDie);
        }

        return map;
    }
}
