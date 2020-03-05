package tax.www.ctr.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tax.www.module.secure.filter.CmnFilterBiz;
import tax.www.service.cmn.CmnService;
import tax.www.service.event.EventGenerateService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.event.GenerateVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 이벤트관리 > 상황발생 이력조회
 * <p>
 * User: 이준수
 * Date: 17. 11. 07
 * Time: 오후 1:30
 */
@RestController
@RequestMapping("/event")
public class EventGenerateRestCtr {

    private static  final Logger log = LogManager.getLogger(EventGenerateRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "EventGenerateService")
    private EventGenerateService eventGenerateService;

    private String mnu_cd = "T1E002";

    /**
     * 상황발생 이력조회 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getTaxOcrList", method = RequestMethod.POST)
    public ResultJQGridVO getTaxOcrList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T1E002";
        String[] arr = {"R"};

        List<GenerateVO> resData = new ArrayList<GenerateVO>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcCarNo = null;
            String srcCctvInfo = null;
            String srcSDate = null;
            String srcEDate = null;

            if(!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcCarNo = CmnFilterBiz.filterSqlString(jsonFilter.get("srcCarNo")); // 차량번호
                srcCctvInfo = CmnFilterBiz.filterSqlString(jsonFilter.get("srcCctvInfo")); // CCTV 정보
                srcSDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcSDate")); // 시작일
                srcEDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcEDate")); // 종료일
            }

            int dataCnt = eventGenerateService.getTaxOcrListCnt(srcCarNo, srcCctvInfo, srcSDate, srcEDate, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = eventGenerateService.getTaxOcrList(srcCarNo, srcCctvInfo, srcSDate, srcEDate, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt,  resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 시간별 발생현황 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getTimeTaxOcrList", method = RequestMethod.POST)
    public ResultJQGridVO getTimeTaxOcrList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T1E002";
        String[] arr = {"R"};

        List<GenerateVO> resData = new ArrayList<GenerateVO>();

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

            resData = eventGenerateService.getTimeTaxOcrList(srcSDate,srcEDate,vo);

            return new ResultJQGridVO(1, resData.size(), 1, resData);

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 지역별 발생현황 Top5 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getAreaOcrTopList", method = RequestMethod.POST)
    public ResultJQGridVO getAreaOcrTopList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

         mnu_cd = "T1E004";
        String[] arr = {"R"};

        List<GenerateVO> resData = new ArrayList<GenerateVO>();

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

                srcSDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcSDate"));
                srcEDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcEDate"));
            }

            resData = eventGenerateService.getAreaOcrTopList(srcSDate, srcEDate, vo);

            return new  ResultJQGridVO(1, resData.size(), 1, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * CCTV별 발생현황 Top5 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getCctvOcrTopList", method = RequestMethod.POST)
    public ResultJQGridVO getCctvOcrTopList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T1E004";
        String[] arr = {"R"};

        List<GenerateVO> resData = new ArrayList<GenerateVO>();

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

                srcSDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcSDate"));
                srcEDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcEDate"));
            }

            resData = eventGenerateService.getCctvOcrTopList(srcSDate, srcEDate, vo);

            return new  ResultJQGridVO(1, resData.size(), 1, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }
}
