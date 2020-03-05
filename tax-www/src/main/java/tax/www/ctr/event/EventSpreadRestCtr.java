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
import tax.www.service.event.EventSpreadService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.event.SpreadVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 이벤트관리 > 상황전파 이력조회
 * <p>
 * User: 이준수
 * Date: 17. 11. 07
 * Time: 오전 11:57
 */
@RestController
@RequestMapping("/event")
public class EventSpreadRestCtr {

    private static final Logger log = LogManager.getLogger(EventSpreadRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "EventSpreadService")
    private EventSpreadService eventSpreadService;

    private String mnu_cd = "T1E003";

    /**
     * 상황전파 이력관리 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getSitPpgList", method = RequestMethod.POST)
    public ResultJQGridVO getSitPpgList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T1E003";
        String[] arr = {"R"};

        List<SpreadVO> resData = new ArrayList<SpreadVO>();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);

        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcTcgJong = null;
            String srcSDate = null;
            String srcEDate = null;

            if(!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcTcgJong = CmnFilterBiz.filterSqlString(jsonFilter.get("srcTcgJong")); // 전파그룹
                srcSDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcSDate")); // 시작일
                srcEDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcEDate")); // 종료일
            }

            int dataCnt = eventSpreadService.getSitPpgListCnt(srcTcgJong, srcSDate, srcEDate, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = eventSpreadService.getSitPpgList(srcTcgJong, srcSDate, srcEDate, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

}
