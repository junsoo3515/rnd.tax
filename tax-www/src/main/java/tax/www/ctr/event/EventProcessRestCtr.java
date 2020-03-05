package tax.www.ctr.event;

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
import tax.www.service.event.EventProcessService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.event.ProcessVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 이벤트관리 > 상황처리 관리
 * <p>
 * User: 이준수
 * Date: 17. 11. 06
 * Time: 오후 6:19
 */
@RestController
@RequestMapping("/event")
public class EventProcessRestCtr {

    private static final  Logger log =  LogManager.getLogger(EventProcessRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "EventProcessService")
    private EventProcessService eventProcessService;

    private String mnu_cd = "T1E004";

    /**
     * 상황처리 목록 건수 가져오기
     *
     * @param ses      HttpSession
     * @return ProcessVO
     */
    @RequestMapping(value = "/getSitPcsDataCnt", method = RequestMethod.POST)
    public ProcessVO getSitPcsDataCnt(@RequestBody ProcessVO vo, HttpSession ses) {

        mnu_cd = "T1E004";
        String[] arr = {"R"};

        ProcessVO processVO = new ProcessVO();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {


            processVO = eventProcessService.getSitPcsDataCnt(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return processVO;
    }

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

        mnu_cd = "T1E004";
        List<ProcessVO> resData = new ArrayList<>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcMakeType = null;
            String srcSDate = null;
            String srcEDate = null;
            String sit_type = null;

            if (!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcMakeType = CmnFilterBiz.filterSqlString(jsonFilter.get("srcMakeType")); // 발생유형
                srcSDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcSDate")); // 시작일
                srcEDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcEDate")); // 종료일
                sit_type = CmnFilterBiz.filterSqlString(jsonFilter.get("sit_type")); // 탭 유형
            }

            int dataCnt = eventProcessService.getSitPcsListCnt(srcMakeType, sit_type, srcSDate, srcEDate, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double )dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = eventProcessService.getSitPcsList(srcMakeType, sit_type, srcSDate, srcEDate, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 상세정보 가져오기
     *
     * @param vo  ProcessVO
     * @param ses HttpSession
     * @return ProcessVO
     */
    @RequestMapping(value = "/getDetailInfoData", method = RequestMethod.POST)
    public ProcessVO getDetailInfoData(@RequestBody ProcessVO vo, HttpSession ses) {

        mnu_cd = "T1E004";

        String[] arr = {"R"};

        ProcessVO processVO = new ProcessVO();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            processVO = eventProcessService.getDetailInfoData(vo);

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return null;
        }

        return processVO;
    }

    /**
     * 상세정보 적용
     *
     * @param vo  ProcessVO
     * @param ses HttpSession
     * @return 0 : 에러, 1 이상 : Update 성공 Count
     */
    @RequestMapping(value = "/setDetailInfoAct", method = RequestMethod.POST)
    public int setDetailInfoAct(@RequestBody ProcessVO vo, HttpSession ses) {

        mnu_cd = "T1E004";
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

            resCnt = eventProcessService.setDetailInfoAct(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }
        return resCnt;
    }

    /**
     * 상황전파 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getPcsSitPpgList", method = RequestMethod.POST)
    public ResultJQGridVO getPcsSitPpgList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T1E004";
        List<ProcessVO> resData = new ArrayList<ProcessVO>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String tri_seq = null;

            if (!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                tri_seq = CmnFilterBiz.filterSqlString(jsonFilter.get("tri_seq")); // 차량인식 일련번호
            }

            resData = eventProcessService.getPcsSitPpgList(tri_seq);

            return new ResultJQGridVO(1, resData.size(), 1, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 체납정보 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getTaxInfoList", method = RequestMethod.POST)
    public ResultJQGridVO getTaxInfoList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T1E004";
        List<ProcessVO> resData = new ArrayList<ProcessVO>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String make_type_cd = null;
            String car_no = null;

            if(!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                make_type_cd = CmnFilterBiz.filterSqlString(jsonFilter.get("make_type_cd"));
                car_no = CmnFilterBiz.filterSqlString(jsonFilter.get("car_no"));
            }

            resData = eventProcessService.getTaxInfoList(make_type_cd, car_no, vo);

            return new ResultJQGridVO(1, resData.size(), 1, resData);

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 담당자명 중복체크
     *
     * @param ses          HttpSession
     * @return ResultJQGridVO
     */
    @RequestMapping(value = "/getDupChkManagerList", method = RequestMethod.POST)
    public List<ProcessVO> getDupChkManagerList(HttpSession ses) {

        mnu_cd = "T1E004";
        List<ProcessVO> resData = new ArrayList<>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses,mnu_cd,arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            resData = eventProcessService.getDupChkManagerList();
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return resData;
    }

    /**
     * 직접입력 시 담당자 등록(Insert)
     *
     * @param vo  ProcessVO
     * @param ses HttpSession
     * @return 0 : 에러, 1 이상 : Update 성공 Count
     */
    @RequestMapping(value = "/setManagerInsert", method = RequestMethod.POST)
    public Map<String,Object> setManagerInsert(@RequestBody ProcessVO vo, HttpSession ses) {

        mnu_cd = "T1E004";

        Map<String, Object> map = new HashMap<>();

        String[] arr = {"C"};

        vo.reg_mem_id = ses.getAttribute("id").toString();

        try {

            cmnService.isUserAccessValidate(ses,mnu_cd,arr);
        } catch (Exception ex) {

            return null;
        }

        try {

            map = eventProcessService.setManagerInsert(vo);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return map;
    }

    /**
     * 체납차량 발견위치 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getTaxCarFindLocList", method = RequestMethod.POST)
    public ResultJQGridVO getTaxCarFindLocList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T1E004";
        List<ProcessVO> resData = new ArrayList<ProcessVO>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String car_no = null;

            if(!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                car_no = CmnFilterBiz.filterSqlString(jsonFilter.get("car_no"));
            }

            resData = eventProcessService.getTaxCarFindLocList(car_no, vo);

            return new ResultJQGridVO(1, resData.size(), 1, resData);

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }
}
