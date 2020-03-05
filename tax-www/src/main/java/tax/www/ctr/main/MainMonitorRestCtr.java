package tax.www.ctr.main;

import egovframework.com.cmn.service.EgovProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import tax.www.module.secure.filter.CmnFilterBiz;
import tax.www.service.cmn.CmnService;
import tax.www.service.main.MainMonitorService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.main.MonitorVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GIS 상황관제(메인)
 * <p>
 * User: 이준수
 * Date: 18. 01. 22
 * Time: 오후 3:02
 */
@RestController
@RequestMapping("/main")
public class MainMonitorRestCtr {

    private static final Logger log = LogManager.getLogger(MainMonitorRestCtr.class);

    public String vmsIp = EgovProperties.getProperty("Vms.Ip");
    public String vmsPort = EgovProperties.getProperty("Vms.Port");

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "MainMonitorService")
    private MainMonitorService mainMonitorService;

    private String mnu_cd = "T0M000";

    /**
     * 상황처리 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getSitPcsList", method = RequestMethod.POST)
    public ResultJQGridVO getSitPcsList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T0M000";
        List<MonitorVO> resData = new ArrayList<MonitorVO>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            int dataCnt = mainMonitorService.getSitPcsListCnt(vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = mainMonitorService.getSitPcsList(vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 상세정보 가져오기
     *
     * @param vo  MonitorVO
     * @param ses HttpSession
     * @return ProcessVO
     */
    @RequestMapping(value = "/getDetailInfoData", method = RequestMethod.POST)
    public MonitorVO getDetailInfoData(@RequestBody MonitorVO vo, HttpSession ses) {

        mnu_cd = "T0M000";

        String[] arr = {"R"};

        MonitorVO monitorVO = new MonitorVO();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            monitorVO = mainMonitorService.getDetailInfoData(vo);

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return null;
        }

        return monitorVO;
    }

    /**
     * 상세정보 적용
     *
     * @param vo  MonitorVO
     * @param ses HttpSession
     * @return 0 : 에러, 1 이상 : Update 성공 Count
     */
    @RequestMapping(value = "/setDetailInfoAct", method = RequestMethod.POST)
    public int setDetailInfoAct(@RequestBody MonitorVO vo, HttpSession ses) {

        mnu_cd = "T0M000";
        String[] arr = {"U"};

        int resCnt = 0;

        vo.reg_mem_id = ses.getAttribute("id").toString();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return resCnt;
        }

        try {

            CmnFilterBiz.filterSqlClass(vo);

            resCnt = mainMonitorService.setDetailInfoAct(vo);

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }
        return resCnt;
    }

    /**
     * 관심차량번호 가져오기
     *
     * @param car_no      String
     * @return List<ListObjVO>
     */
    @RequestMapping(value = "/getInterestCarList", method = RequestMethod.POST)
    public List<ListObjVO> getInterestCarList(@RequestBody String car_no) {

        List<ListObjVO> resData = new ArrayList<>();

        try {

            resData = mainMonitorService.getInterestCarList(car_no);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }
        return resData;
    }

    /**
     * 레이어(CCTV 종류) 가져오기
     *
     * @return List<MonitorVO>
     */
    @RequestMapping(value = "/getCctvTypeList", method = RequestMethod.POST)
    public List<ListObjVO> getCctvTypeList() {

        List<ListObjVO> resData = new ArrayList<>();

        try {

            resData = mainMonitorService.getCctvTypeList();
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }
        return resData;
    }

    /**
     * CCTV GIS 정보 가져오기
     *
     * @param vo      MonitorVO
     * @param ses     HttpSession
     * @return List<MonitorVO>
     */
    @RequestMapping(value = "/getCctvGeoData", method = RequestMethod.POST)
    public List<MonitorVO> getCctvGeoData(@RequestBody MonitorVO vo, HttpSession ses) {

        mnu_cd = "T0M000";
        String[] arr = {"R"};

        List<MonitorVO> getCctvGeoData = new ArrayList<>();


        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }


        try {

            getCctvGeoData = mainMonitorService.getCctvGeoData(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }


        return getCctvGeoData;
    }

    /**
     * 투망감시 설정 CCTV GIS 정보 가져오기
     *
     * @param vo      MonitorVO
     * @param ses     HttpSession
     * @return List<MonitorVO>
     */
    @RequestMapping(value = "/getNetSetCctvGeoData", method = RequestMethod.POST)
    public List<MonitorVO> getNetSetCctvGeoData(@RequestBody MonitorVO vo,  HttpSession ses) {

        mnu_cd = "T0M000";
        String[] arr = {"R"};

        List<MonitorVO> getNetSetCctvGeoData = new ArrayList<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {
            return null;
        }

        try {

            getNetSetCctvGeoData = mainMonitorService.getNetSetCctvGeoData(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return getNetSetCctvGeoData;
    }

    /**
     * 분포도 GIS 정보 가져오기
     *
     * @param ses     HttpSession
     * @return List<MonitorVO>
     */
    @RequestMapping(value = "/getDistributionGeoData", method = RequestMethod.POST)
    public List<MonitorVO> getDistributionGeoData(HttpSession ses) {

        mnu_cd = "T0M000";
        String[] arr = {"R"};

        List<MonitorVO> getDistributionGeoData = new ArrayList<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            getDistributionGeoData = mainMonitorService.getDistributionGeoData();
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return getDistributionGeoData;
    }

    /**
     * 관심차량 이동경로 목록 가져오기
     *
     * @param vo     MonitorVO
     * @param ses     HttpSession
     * @return List<MonitorVO>
     */
    @RequestMapping(value = "/getRouteList", method = RequestMethod.POST)
    public List<MonitorVO> getRouteList(@RequestBody MonitorVO vo, HttpSession ses) {

        mnu_cd = "T0M000";
        String[] arr = {"R"};

        List<MonitorVO> getRouteList = new ArrayList<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            getRouteList = mainMonitorService.getRouteList(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return getRouteList;
    }

    /**
     * 실시간 상황처리 목록 가져오기
     *
     * @param ses HttpSession
     * @return MonitorVO
     */
    @RequestMapping(value = "/getRealtimeSitPcsList", method = RequestMethod.POST)
    public MonitorVO getRealtimeSitPcsList(HttpSession ses, @RequestBody String sitOcrData) {

        mnu_cd = "T0M000";

        String[] arr = {"R"};

        String sitOcrTime = sitOcrData;

        MonitorVO monitorVO = new MonitorVO();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            monitorVO = mainMonitorService.getRealtimeSitPcsList(sitOcrTime);

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return monitorVO;
    }

    /**
     * 영상팝업 스크린샷 저장하기
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param ses      HttpSession
     * @return 쿼리 결과
     */
    @RequestMapping(value = "/setSnapShot", method = RequestMethod.GET)
    public void snapShot(HttpServletRequest request,  HttpServletResponse response, HttpSession ses) throws Exception {

        mnu_cd = "T0M000";

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

        }

        String vurixIpPort = vmsIp + ":" +vmsPort;

        try {

            mainMonitorService.snapShot(request, response, ses.getAttribute("id").toString(), vurixIpPort);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }
    }

    /**
     * 투망감시 RTSP URL 가져오기
     *
     * @param map      차량인식 일련번호, 발생 CCTV ID
     * @param ses      HttpSession
     * @return 쿼리 결과
     */
    @RequestMapping(value = "/castNetRtspUrl", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> castNetRtspUrl(@RequestBody Map<String, Object> map, HttpSession ses) throws Exception {

        mnu_cd = "T0M000";

        String[] arr = {"R"};

        List<Map<String, Object>> rtsUrl = new ArrayList<Map<String, Object>>();
        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        String vurixIpPort = vmsIp + ":" + vmsPort;

        try {

            rtsUrl = mainMonitorService.castNetRtspUrl(map, ses.getAttribute("id").toString(), vurixIpPort);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return rtsUrl;
    }

    /**
     * 발생지점 CCTV(실시간 감시), 영상팝업 RTSP URL 가져오기
     *
     * @param map      영상 종류, 차량인식 일련번호,
     * @param ses      HttpSession
     * @return 쿼리 결과
     */
    @RequestMapping(value = "/rtspUrl", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> rtspUrl(@RequestBody Map<String, Object> map, HttpSession ses) throws Exception {

        mnu_cd = "T0M000";

        String[] arr = {"R"};

        Map<String, Object> rtpUrl = new HashMap<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        String vurixIpPort = vmsIp + ":" + vmsPort;

        try {

            rtpUrl = mainMonitorService.rtspUrl(map, ses.getAttribute("id").toString(), vurixIpPort);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return rtpUrl;
    }
}
