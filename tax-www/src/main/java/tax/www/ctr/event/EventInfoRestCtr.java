package tax.www.ctr.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tax.www.module.secure.filter.CmnFilterBiz;
import tax.www.service.cmn.CmnService;
import tax.www.service.event.EventInfoService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.event.InfoVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 이벤트관리 > 관심차량정보현황
 * <p>
 * User: 이준수
 * Date: 18. 01. 12
 * Time: 오전 10:03
 */
@RestController
@RequestMapping("/event")
public class EventInfoRestCtr {

    private static  final Logger log = LogManager.getLogger(EventInfoRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "EventInfoService")
    private EventInfoService eventInfoService;

    private String mnu_cd = "T1E001";

    /**
     * 관심차량 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getInterestList", method = RequestMethod.POST)
    public ResultJQGridVO getInterestList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T1E001";
        String[] arr = {"R"};

        List<InfoVO> resData = new ArrayList<InfoVO>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcSDate = null;
            String srcEDate = null;

            if(!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcSDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcSDate")); // 시작일
                srcEDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcEDate")); // 종료일
            }

            int dataCnt = eventInfoService.getInterestListCnt(srcSDate, srcEDate, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = eventInfoService.getInterestList(srcSDate, srcEDate, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt,  resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 관심차량 이동경로 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getInterestRouteList", method = RequestMethod.POST)
    public ResultJQGridVO getInterestRouteList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T1E001";
        String[] arr = {"R"};

        List<InfoVO> resData = new ArrayList<InfoVO>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcSDate = null;
            String srcEDate = null;
            String car_no = null;

            if(!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcSDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcSDate")); // 시작일
                srcEDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcEDate")); // 종료일
                car_no = CmnFilterBiz.filterSqlString(jsonFilter.get("car_no")); // 차량번호
            }


            resData = eventInfoService.getInterestRouteList(srcSDate, srcEDate, car_no, vo);

            return new ResultJQGridVO(vo.page, resData.size(), 1,  resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 이동경로 GIS 정보 가져오기
     *
     * @param vo      InfoVO
     * @param ses     HttpSession
     * @return List<CctvVO>
     */
    @RequestMapping(value = "/getRouteGeoData", method = RequestMethod.POST)
    public List<InfoVO> getRouteGeoData(@RequestBody InfoVO vo, HttpSession ses) {

        mnu_cd = "T1E001";
        String[] arr = {"R"};

        List<InfoVO> getRouteGeoData = new ArrayList<>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        if(vo.s_dts != null && vo.e_dts != null) {
            vo.s_dts = CmnFilterBiz.filterPureString(vo.s_dts);
            vo.e_dts = CmnFilterBiz.filterPureString(vo.e_dts);
        }

        try {

            getRouteGeoData = eventInfoService.getRouteGeoData(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }


        return getRouteGeoData;
    }

    /**
     * 최단거리 이동경로 가져오기(다음 carset.json)
     *
     * @param point     List<Object>
     * @return List<CctvVO>
     */
    @RequestMapping(value = "/getRouteLine", method = RequestMethod.POST)
    public JSONObject getRouteLine (@RequestBody List<Object> point) {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject = eventInfoService.getRouteLine(point);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return jsonObject;
    }
}
