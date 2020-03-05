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
import tax.www.service.security.SecuritySmsconfigService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.SmsconfigVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템관리 > SMS 연락처 관리
 * <p>
 * User: 이준수
 * Date: 17. 11. 06
 * Time: 오후 3:42
 */
@RestController
@RequestMapping("/security")
public class SecuritySmsconfigRestCtr {

    private static final Logger log = LogManager.getLogger(SecuritySmsconfigRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "SecuritySmsconfigService")
    private SecuritySmsconfigService securitySmsconfigService;

    private String mnu_cd = "T2S004";

    /**
     * 그룹 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getContactGrpList", method = RequestMethod.POST)
    public ResultJQGridVO getContactGrpList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T2S004";

        String[] arr = {"R"};

        List<SmsconfigVO> resData = new ArrayList<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcContactGrp = null; // 그룹명

            if (!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcContactGrp = CmnFilterBiz.filterSqlString(jsonFilter.get("srcContactGrp"));
            }

            int dataCnt = securitySmsconfigService.getContactGrpListCnt(srcContactGrp, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = securitySmsconfigService.getContactGrpList(srcContactGrp, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);

            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 그룹 목록 저장(Insert, Update)
     *
     * @param oper  상태(add, edit)
     * @param vo    SmsconfigVO
     * @param ses   HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/setContactGrpAct", method = RequestMethod.POST)
    public Map setContactGrpAct(String oper, SmsconfigVO vo, HttpSession ses) {

        mnu_cd = "T2S004";

        String[] arr = {"C", "U"};

        Map<String, Boolean> resMap = new HashMap<String, Boolean>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            vo.reg_mem_id = (String) ses.getAttribute("id");

            // 필터링(특수문자)
            CmnFilterBiz.filterSqlClass(vo);

            resMap = securitySmsconfigService.setContactGrpAct(oper, vo);

        } catch (Exception ex)  {

            log.error(ex.toString(), ex);
        }

        return resMap;
    }

    /**
     * 그룹 목록 삭제(Delete)
     *
     * @param vo    SmsconfigVO
     * @param ses   HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/setContactGrpDel", method = RequestMethod.POST)
    public Map setContactGrpDel(@RequestBody SmsconfigVO vo, HttpSession ses) {

        mnu_cd = "T2S004";

        String[] arr = {"D"};

        Map<String, Boolean> resMap = new HashMap<String, Boolean>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            resMap = securitySmsconfigService.setContactGrpDelete(vo);

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return resMap;
    }

    /**
     * 연락처 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getContactList", method = RequestMethod.POST)
    public ResultJQGridVO getContactList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T2S004";

        String[] arr = {"R"};

        List<SmsconfigVO> resData = new ArrayList<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcContact = null; // 연락처
            String tcg_seq = null; // 그룹 일련번호

            if (!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcContact = CmnFilterBiz.filterSqlString(jsonFilter.get("srcContact"));
                tcg_seq = CmnFilterBiz.filterSqlString(jsonFilter.get("tcg_seq"));
            }

            resData = securitySmsconfigService.getContactList(srcContact, tcg_seq, vo);

            return new ResultJQGridVO(1, resData.size(), 1, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 연락처목록 저장(Insert, Update)
     *
     * @param oper 상태(add, edit)
     * @param vo   SmsconfigVO
     * @param ses  HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/setContactAct", method = RequestMethod.POST)
    public Map setContactAct(String oper, SmsconfigVO vo, HttpSession ses) {

        mnu_cd = "T2S004";

        String[] arr = {"C", "U"};

        Map<String, Boolean> resMap = new HashMap<String, Boolean>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }


        try {

            vo.reg_mem_id = (String) ses.getAttribute("id");

            // 필터링(특수문자)
            CmnFilterBiz.filterSqlClass(vo);

            resMap = securitySmsconfigService.setContactAct(oper, vo);

        } catch (Exception ex) {

            log.error(ex.toString(),ex);
        }

        return resMap;
    }

    /**
     * 연락처목록 삭제(Delete)
     *
     * @param vo   SmsconfigVO
     * @param ses  HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/setContactDel", method = RequestMethod.POST)
    public Map setContactDel(@RequestBody SmsconfigVO vo,  HttpSession ses) {

        mnu_cd = "T2S004";

        String[] arr = {"D"};

        Map<String, Boolean> resMap = new HashMap<String, Boolean>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {
            return null;
        }


        try {

            resMap = securitySmsconfigService.setContactDelete(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return resMap;
    }

    /**
     * 담당자 테이블 데이터 가져오기
     *
     * @param ses HttpSession
     * @return List
     */
    @RequestMapping(value = "/chkDupContact", method = RequestMethod.POST)
    public List<SmsconfigVO> chkDupContact(HttpSession ses) {

        mnu_cd = "T2S004";

        String[] arr = {"R"};

        List<SmsconfigVO> list = new ArrayList<SmsconfigVO>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            list = securitySmsconfigService.chkDupContact();
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return list;
    }
}
