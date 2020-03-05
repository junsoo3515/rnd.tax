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
import tax.www.module.secure.filter.CmnFilterBiz;
import tax.www.service.cmn.CmnService;
import tax.www.service.security.SecurityAuthorityService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.menu.MenuAuthVO;
import tax.www.vo.cmn.menu.MenuVO;
import tax.www.vo.security.AuthorityVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템관리 > 권한관리
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:43
 */
@RestController
@RequestMapping("/security")
public class SecurityAuthorityRestCtr {

    private static final Logger log = LogManager.getLogger(SecurityAuthorityRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "SecurityAuthorityService")
    private SecurityAuthorityService securityAuthorityService;

    private String mnu_cd = "T2S006";

    /**
     * 권한 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getAuthList", method = RequestMethod.POST)
    public ResultJQGridVO getAuthList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T2S006";
        List<AuthorityVO> resData = new ArrayList<AuthorityVO>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcAuth = null; // 권한
            String srcEtc = null; // 권한설명

            if (!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcAuth = CmnFilterBiz.filterSqlString(jsonFilter.get("srcAuth"));

                if (jsonFilter.containsKey("srcEtc")) {

                    srcEtc = CmnFilterBiz.filterSqlString(jsonFilter.get("srcEtc"));
                }
            }

            int dataCnt = securityAuthorityService.getAuthListCnt(srcAuth, srcEtc, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = securityAuthorityService.getAuthList(srcAuth, srcEtc, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 권한 코드 중복확인
     *
     * @param obj {auth_cd:권한 코드}
     * @param ses HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/getAuthCheck", method = RequestMethod.POST)
    public Map getAuthCheck(@RequestBody Map obj, HttpSession ses) {

        String[] arr = {"C"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        boolean tmpOK = false;
        String authCd = "";

        if (obj.containsKey("auth_cd")) {

            if (!StringUtils.isEmpty(obj.get("auth_cd").toString())) {

                authCd = CmnFilterBiz.filterSqlString(obj.get("auth_cd").toString()).toUpperCase();
            }
        }

        try {

            tmpOK = securityAuthorityService.getAuthChk(authCd) == 0 ? true : false;
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }

        Map<String, Boolean> map = new HashMap<String, Boolean>();

        map.put("isSuccess", tmpOK);

        return map;
    }

    /**
     * 권한 정보 저장(Insert, Update)
     * - 권한 정보, 권한별 메뉴 접근 관리
     *
     * @param vo  AuthVO
     * @param ses HttpSession
     * @return 0 : 에러, 1 이상 : Insert/Update 성공 Count
     */
    @RequestMapping(value = "/setAuthAct", method = RequestMethod.POST)
    public int setAuthAct(@RequestBody AuthorityVO vo, HttpSession ses) {

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

            // 2. 권한 코드 대문자 변환
            vo.auth_cd = vo.auth_cd.toUpperCase();

            // 3. COM_MEM_AUTH Insert/Update 처리
            resCnt += securityAuthorityService.setAuthAct(vo);

            // 4. 메뉴별 사용자 접근 권한 관리
            if (securityAuthorityService.getAuthMenuCnt(vo.auth_cd) > 0) {

                securityAuthorityService.setAuthMenuUpdate(vo.auth_cd, vo.authData);
            } else {

                long maxSeq = cmnService.getTableMaxSeq("COM_MEM_MNU", "seq", 1);

                resCnt += securityAuthorityService.setAuthMenuInsert(maxSeq, vo.auth_cd, vo.authData);
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }

        return resCnt;
    }

    /**
     * 메뉴별 접근 권한 목록 Ctr
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
    @RequestMapping(value = "/getMenuList", method = RequestMethod.POST)
    public ResultJQGridVO getMenuList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T2S006";
        List<MenuVO> resData = new ArrayList<MenuVO>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            resData = cmnService.getAllMenu();
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return new ResultJQGridVO(1, resData.size(), 1, resData);
    }

    /**
     * 권한/사용자별 메뉴별 접근 설정 가져오기
     *
     * @param data {jongCd : A(권한) / B(사용자), val: 해당 코드 값}
     * @param ses  HttpSession
     * @return the menu data
     */
    @RequestMapping(value = "/getMenuData", method = RequestMethod.POST)
    public List<MenuAuthVO> getMenuData(@RequestBody Map<String, Object> data, HttpSession ses) {

        mnu_cd = "T2S006";
        String[] arr = {"C", "R", "U"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            return cmnService.getMenuData(data.get("jongCd").toString(), data.get("val").toString());
        } catch (Exception ex) {

            log.error(ex.toString(), ex);

            return new ArrayList<MenuAuthVO>();
        }
    }
}
