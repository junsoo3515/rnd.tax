package tax.www.ctr.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tax.www.module.secure.encryption.CmnRsaOaepBiz;
import tax.www.module.secure.filter.CmnFilterBiz;
import tax.www.service.cmn.CmnService;
import tax.www.service.security.SecurityUserService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.UserVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템관리 > 사용자계정관리
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:43
 */
@RestController
@RequestMapping("/security")
public class SecurityUserRestCtr {

    private static final Logger log = LogManager.getLogger(SecurityUserRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "SecurityUserService")
    private SecurityUserService securityUserService;

    private String mnu_cd = "T2S005";

    /**
     * 사용자 목록 jqGrid 호출 Ctr
     *
     * @param sidx         소팅 헤더 아이디
     * @param sord         소팅 (asc / desc)
     * @param rows         표출 Row 수
     * @param _search      검색 여부
     * @param searchField  검색어 필드 아이디
     * @param searchString 검색어 값
     * @param searchOper   검색어 조건
     * @param filters      필터(Model로 컨버팅 하기 위한 기타 조건들..)
     * @param page         현재 페이지 No
     * @param ses          HttpSession
     * @return ResultJQGridVO
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    public ResultJQGridVO getUserList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T2S005";
        List<UserVO> resData = new ArrayList<UserVO>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcAuth = null; // 권한
            String srcId = null; // 아이디
            String srcEtc = null; // 이름/이메일

            if (!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcAuth = CmnFilterBiz.filterSqlString(jsonFilter.get("srcAuth"));

                if (jsonFilter.containsKey("srcNm")) {

                    srcId = CmnFilterBiz.filterSqlString(jsonFilter.get("srcNm")).toUpperCase(); // 아이디는 무조건 대문자
                    srcEtc = CmnFilterBiz.filterSqlString(jsonFilter.get("srcNm")); // 아이디는 무조건 대문자
                }
            }

            int dataCnt = securityUserService.getUserListCnt(srcAuth, srcId, srcEtc, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = securityUserService.getUserList(srcAuth, srcId, srcEtc, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 사용자 아이디 중복확인
     *
     * @param obj {mem_id:사용자 아이디}
     * @param ses HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/getIDCheck", method = RequestMethod.POST)
    public Map getIDCheck(@RequestBody Map obj, HttpSession ses) {

        mnu_cd = "T2S005";
        String[] arr = {"C"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        boolean tmpOK = false;
        String memId = "";

        if (obj.containsKey("mem_id")) {

            if (!StringUtils.isEmpty(obj.get("mem_id").toString())) {

                memId = CmnFilterBiz.filterSqlString(obj.get("mem_id").toString()).toUpperCase();
            }
        }

        try {

            tmpOK = (securityUserService.getIDCheck(memId) == 0 ? true : false);
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }

        Map<String, Boolean> map = new HashMap<String, Boolean>();

        map.put("isSuccess", tmpOK);

        return map;
    }

    /**
     * 로그인 실패횟수 초기화
     *
     * @param obj {mem_id:사용자 아이디}
     * @param ses HttpSession
     * @return the pwd fail clear
     */
    @RequestMapping(value = "/setPwdFailClear", method = RequestMethod.POST)
    public Map setPwdFailClear(@RequestBody Map obj, HttpSession ses) {

        mnu_cd = "T2S005";
        String[] arr = {"C", "U"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        boolean tmpOK = false;
        String memId = "";

        if (obj.containsKey("mem_id")) {

            if (!StringUtils.isEmpty(obj.get("mem_id").toString())) {

                memId = CmnFilterBiz.filterSqlString(obj.get("mem_id").toString()).toUpperCase();
            }
        }

        try {

            tmpOK = (securityUserService.setUserFailPwdClear(memId) > 0 ? true : false);
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }

        Map<String, Boolean> map = new HashMap<String, Boolean>();

        map.put("isSuccess", tmpOK);

        return map;
    }

    /**
     * 사용자 상세정보 가져오기
     *
     * @param obj {mem_seq:사용자 고유 SEQ}
     * @param ses HttpSession
     * @return UserVO
     */
    @RequestMapping(value = "/getUserData", method = RequestMethod.POST)
    public UserVO getUserData(@RequestBody Map obj, HttpSession ses) {

        mnu_cd = "T2S005";
        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            String mem_seq = "";

            if (obj.containsKey("mem_seq")) {

                if (!StringUtils.isEmpty(obj.get("mem_seq").toString())) {

                    mem_seq = CmnFilterBiz.filterPureString(obj.get("mem_seq").toString());
                }
            }

            return securityUserService.getUserData(mem_seq);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return null;
        }
    }

    /**
     * 사용자 정보 저장(Insert, Update)
     * - 사용자 정보, 메뉴별 사용자 접근 권한 관리
     *
     * @param vo  UserVO
     * @param ses HttpSession
     * @return 0 : 에러, 1 이상 : Insert/Update 성공 Count
     */
    @RequestMapping(value = "/setUserAct", method = RequestMethod.POST)
    public int setUserAct(@RequestBody UserVO vo, HttpSession ses) {

        mnu_cd = "T2S005";
        String[] arr = {"C", "U"};

        int resCnt = 0;

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return resCnt;
        }

        try {
            // 1. 필터링
            CmnFilterBiz.filterSqlClass(vo);

            // 2. 아이디 대문자 변환
            vo.mem_id = vo.mem_id.toUpperCase();

            // 3. 암호 알고리즘 도입
            if (StringUtils.isEmpty(vo.pwd) == false) {

                vo.pwd = CmnRsaOaepBiz.encrypt(vo.pwd);
            }

            // 4. 등록자 아이디 set
            vo.reg_mem_id = ses.getAttribute("id").toString();

            // 5. 사용자 일련번호로 구분하여 COM_MEM_INFO Insert/Update 처리
            if (vo.mem_seq > 0) {

                resCnt += securityUserService.setUserUpdate(vo);
            } else {

                resCnt += securityUserService.setUserInsert(vo);
            }

            // 5. 메뉴별 사용자 접근 권한 관리
            if (securityUserService.getUserAuthCnt(vo.mem_id) > 0) {

                securityUserService.setUserAuthUpdate(vo.auth_cd, vo.mem_id, vo.authData);
            } else {

                long maxSeq = cmnService.getTableMaxSeq("COM_MEM_MNU", "seq", 1);

                resCnt += securityUserService.setUserAuthInsert(maxSeq, vo.auth_cd, vo.mem_id, vo.authData);
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }

        return resCnt;
    }
}
