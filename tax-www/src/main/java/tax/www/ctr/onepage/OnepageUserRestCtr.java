package tax.www.ctr.onepage;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tax.www.module.secure.encryption.CmnRsaOaepBiz;
import tax.www.module.secure.filter.CmnFilterBiz;
import tax.www.module.session.CmnSessionBiz;
import tax.www.service.cmn.CmnService;
import tax.www.service.onepage.OnepageUserService;
import tax.www.vo.security.UserVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 싱글 페이지
 * - 개인정보 수정
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:43
 */
@RestController
@RequestMapping("/onepage")
public class OnepageUserRestCtr {

    private static final Logger log = LogManager.getLogger(OnepageUserRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "OnepageUserService")
    private OnepageUserService onepageUserService;

    /**
     * 사용자 상세정보 가져오기
     *
     * @param ses HttpSession
     * @return UserVO
     */
    @RequestMapping(value = "/getUserData", method = RequestMethod.POST)
    public UserVO getUserData(HttpSession ses) {

        String[] arr = {"R"};

        try {
            // 1. 사용자 정보 불러오기
            UserVO userData = onepageUserService.getUserData(ses.getAttribute("id").toString());

            // 2. 세션 변경
            Map<String, String> params = new HashMap<String, String>();

            params.put("member_profile_seq", String.valueOf(userData.files_seq));
            params.put("member_nm", userData.nm);

            CmnSessionBiz.setSessionAttribute(ses, params);

            params = null;

            return userData;
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return null;
        }
    }

    /**
     * 사용자 정보 저장(Update)
     * - 사용자 정보
     *
     * @param vo  UserVO
     * @param ses HttpSession
     * @return 0 : 에러, 1 이상 : Insert/Update 성공 Count
     */
    @RequestMapping(value = "/setUserAct", method = RequestMethod.POST)
    public int setUserAct(@RequestBody UserVO vo, HttpSession ses) {

        int resCnt = 0;

        try {
            // 1. 필터링
            CmnFilterBiz.filterSqlClass(vo);

            // 2. 아이디 대문자 변환
            vo.mem_id = ses.getAttribute("id").toString();

            // 3. 암호 알고리즘 도입
            if (StringUtils.isEmpty(vo.pwd) == false) {

                vo.pwd = CmnRsaOaepBiz.encrypt(vo.pwd);
            }

            // 4. COM_MEM_INFO Update 처리
            resCnt += onepageUserService.setUserUpdate(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return resCnt;
    }
}
