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
import tax.www.service.security.SecurityCctvService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.security.CctvVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템관리 > 모니터링 CCTV 관리
 * <p>
 * User: 이준수
 * Date: 17. 11. 28
 * Time: 오후 2:55
 */
@RestController
@RequestMapping("/security")
public class SecurityCctvRestCtr {

    private static final Logger log = LogManager.getLogger(SecurityCctvRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "SecurityCctvService")
    private SecurityCctvService securityCctvService;

    private String mnu_cd = "T2S001";

    /**
     * 모니터링 CCTV 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getCctvList", method = RequestMethod.POST)
    public ResultJQGridVO getCctvList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T2S001";

        String[] arr = {"R"};

        List<CctvVO> resData = new ArrayList<CctvVO>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcWord = null;

            if (!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcWord = CmnFilterBiz.filterSqlString(jsonFilter.get("srcWord"));
            }

            int dataCnt = securityCctvService.getCctvListCnt(srcWord, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = securityCctvService.getCctvList(srcWord, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 모니터링 CCTV 저장(Insert, Update)
     *
     * @param id   고유 아이디
     * @param oper 상태(add, edit)
     * @param vo   CctvVO
     * @param ses  HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/setCctvAct", method = RequestMethod.POST)
    public Map setCctvAct(String id, String oper, CctvVO vo, HttpSession ses) {

        mnu_cd = "T2S001";

        String[] arr = {"C", "U"};

        Map<String, Boolean> resMap = new HashMap<String, Boolean>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            vo.reg_mem_id = ses.getAttribute("id").toString();

            resMap = securityCctvService.setCctvAct(oper, vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return resMap;
    }

    /**
     * 모니터링 CCTV 삭제(Delete)
     *
     * @param vo   CctvVO
     * @param ses  HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/setCctvDel", method = RequestMethod.POST)
    public Map setCctvDel(@RequestBody CctvVO vo, HttpSession ses) {

        mnu_cd = "T2S001";

        String[] arr = {"D"};

        Map<String, Boolean> resMap = new HashMap<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        }catch (Exception ex) {

            return null;
        }

        try {

            resMap = securityCctvService.setCctvDelete(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return resMap;
    }

    /**
     * 투망감시 설정 가져오기
     *
     * @param cctv_id String
     * @param ses     HttpSession
     * @return List<CctvVO>
     */
    @RequestMapping(value = "/getNetSettingList", method = RequestMethod.POST)
    public List<CctvVO> getNetSettingList(@RequestBody String cctv_id, HttpSession ses) {

        mnu_cd = "T2S001";

        String[] arr = {"R"};

        List<CctvVO> getNetSettingList = new ArrayList<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            getNetSettingList = securityCctvService.getNetSettingList(cctv_id);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return getNetSettingList;
    }

    /**
     * 투망감시 설정(Insert)
     *
     * @param vo   CctvVO
     * @param ses  HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/setNetSettingAct", method = RequestMethod.POST)
    public Map setNetSettingAct(@RequestBody CctvVO vo, HttpSession ses) {

        mnu_cd = "T2S001";

        String[] arr = {"C", "D"};

        Map<String, Boolean> resMap = new HashMap<>();

        vo.reg_mem_id = ses.getAttribute("id").toString();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            resMap = securityCctvService.setNetSettingAct(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return resMap;
    }

    /**
     * 투망감시 설정 삭제(Delete)
     *
     * @param vo   CctvVO
     * @param ses  HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/setNetSettingDel", method = RequestMethod.POST)
    public Map setNetSettingDel(@RequestBody CctvVO vo, HttpSession ses) {

        mnu_cd = "T2S001";

        String[] arr = {"D"};

        Map<String, Boolean> resMap = new HashMap<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            resMap = securityCctvService.setNetSettingDelete(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return resMap;
    }

    /**
     * CCTV ID 가져오기(Select2)
     *
     * @param req     HttpServletRequest
     * @return List<ListObjVO>
     */
    @RequestMapping(value = "/getCctvIdList", method = RequestMethod.GET)
    public List<ListObjVO> getCctvIdList(HttpServletRequest req) throws Exception{

        String word = req.getParameter("q");

        if(word != null) word = new String(word.getBytes("8859_1"), "UTF-8");

        return  securityCctvService.getCctvIdList(word);
    }

    /**
     * Select2에서 선택한 CCTV ID에 대한 CCTV 정보 가져오기
     *
     * @param cctvVO     CctvVO
     * @return CctvVO
     */
    @RequestMapping(value = "/getCctvInfo", method = RequestMethod.POST)
    public CctvVO getCctvInfo(@RequestBody CctvVO cctvVO) throws Exception{

        return securityCctvService.getCctvInfo(cctvVO.cctv_id);
    }

    /**
     * CCTV GIS 정보 가져오기
     *
     * @param ses     HttpSession
     * @return List<CctvVO>
     */
    @RequestMapping(value = "/getCctvGeoData", method = RequestMethod.POST)
    public List<CctvVO> getCctvGeoData(HttpSession ses) {

        mnu_cd = "T2S001";

        String[] arr = {"R"};

        List<CctvVO> getCctvGeoData = new ArrayList<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            getCctvGeoData = securityCctvService.getCctvGeoData();
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }


        return getCctvGeoData;
    }

    /**
     * 반경 내(500m) 모든 프리셋 GIS 정보 가져오기
     *
     * @param map     Map<String, Object>
     * @param ses     HttpSession
     * @return List<CctvVO>
     */
    @RequestMapping(value = "/getAllPresetGeoData", method = RequestMethod.POST)
    public List<CctvVO> getAllPresetGeoData(@RequestBody Map<String, Object> map, HttpSession ses) {

        mnu_cd = "T2S001";

        String[] arr = {"R"};

        Map<String, Object> data = map;

        List<CctvVO> getAllPresetGeoData = new ArrayList<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            getAllPresetGeoData = securityCctvService.getAllPresetGeoData(map);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return getAllPresetGeoData;
    }

    /**
     *  투망감시 설정된 프리셋 GIS 정보 가져오기
     *
     * @param cctv_id     String
     * @param ses         HttpSession
     * @return List<CctvVO>
     */
    @RequestMapping(value = "/getPresetGeoData", method = RequestMethod.POST)
    public List<CctvVO> getPresetGeoData(@RequestBody String cctv_id, HttpSession ses) {

        mnu_cd = "T2S001";

        String[] arr = {"R"};

        List<CctvVO> getPresetGeoData = new ArrayList<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            getPresetGeoData = securityCctvService.getPresetGeoData(cctv_id);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return getPresetGeoData;
    }
}
