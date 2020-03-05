package tax.www.ctr.security;

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
import tax.www.service.security.SecurityFilecrollingService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.FilecrollingVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 시스템관리 > 파일수신현황
 * <p>
 * User: 이준수
 * Date: 17. 11. 06
 * Time: 오후 2:13
 */
@RestController
@RequestMapping("/security")
public class SecurityFilecrollingRestCtr {

    private static final Logger log = LogManager.getLogger(SecurityFilecrollingRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "SecurityFilecrollingService")
    private SecurityFilecrollingService securityFilecrollingService;

    private String mnu_cd = "T2S008";

    /**
     * 파일수신현황 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getFileCltResList", method = RequestMethod.POST)
    public ResultJQGridVO getFileCltResList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T2S008";
        List<FilecrollingVO> resData = new ArrayList<>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcFileCltJong = null; // 파일수신현황 종류
            String srcSDate = null; // 시작일
            String srcEDate = null; // 종료일

            if (!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcFileCltJong = CmnFilterBiz.filterSqlString(jsonFilter.get("srcFileCltJong"));
                srcSDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcSDate"));
                srcEDate = CmnFilterBiz.filterPureString(jsonFilter.get("srcEDate"));
            }

            int dataCnt = securityFilecrollingService.getFileCltResListCnt(srcFileCltJong, srcSDate, srcEDate, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = securityFilecrollingService.getFileCltResList(srcFileCltJong, srcSDate, srcEDate, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 오류 세부 목록 jqGrid 호출 Ctr
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
    @RequestMapping(value = "/getFileCltErrList", method = RequestMethod.POST)
    public ResultJQGridVO getFileCltErrList(String sidx, String sord, int rows, boolean _search, String searchField, String searchString, String searchOper, String filters, int page, HttpSession ses) {

        mnu_cd = "T2S008";
        List<FilecrollingVO> resData = new ArrayList<>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String tfcr_seq = null; // 파일수집결과 일련번호

            if (!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>(){
                });

                tfcr_seq = CmnFilterBiz.filterSqlString(jsonFilter.get("tfcr_seq"));
            }

            int dataCnt = securityFilecrollingService.getFileCltErrListCnt(tfcr_seq, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = securityFilecrollingService.getFileCltErrList(tfcr_seq, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }
}
