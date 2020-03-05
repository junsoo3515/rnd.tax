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
import tax.www.service.security.SecurityInterestService;
import tax.www.vo.cmn.jqgrid.ResultJQGridVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.InterestVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템관리 > 관심차량관리
 * <p>
 * User: 이준수
 * Date: 18. 01. 08
 * Time: 오후 3:14
 */
@RestController
@RequestMapping("/security")
public class SecurityInterestRestCtr {

    private static final Logger log = LogManager.getLogger(SecurityInterestRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "SecurityInterestService")
    private SecurityInterestService securityInterestService;

    private String mnu_cd = "T2S003";

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

        mnu_cd = "T2S003";
        List<InterestVO> resData = new ArrayList<InterestVO>();

        String[] arr = {"R"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ResultJQGridVO(1, 0, 1, resData);
        }

        try {

            SrcJQGridVO vo = new SrcJQGridVO(sidx, sord, rows, _search, searchField, searchString, searchOper, filters, page);

            String srcCarNo = null;

            if (!StringUtils.isEmpty(vo.filters)) {

                Map<String, String> jsonFilter = new ObjectMapper().readValue(vo.filters, new TypeReference<Map<String, String>>() {
                });

                srcCarNo = CmnFilterBiz.filterSqlString(jsonFilter.get("srcCarNo"));
            }

            int dataCnt = securityInterestService.getInterestListCnt(srcCarNo, vo); // 총 갯수 가져오기
            int pageCnt = (int) Math.ceil((double) dataCnt / (double) vo.rows); // 갯수 기준 페이지 계산

            resData = securityInterestService.getInterestList(srcCarNo, vo);

            return new ResultJQGridVO(vo.page, dataCnt, pageCnt, resData);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return new ResultJQGridVO(1, 0, 1, resData);
        }
    }

    /**
     * 관심차량 저장(Insert, Update)
     *
     * @param id   고유 아이디
     * @param oper 상태(add, edit)
     * @param vo   InterestVO
     * @param ses  HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/setInterestAct", method = RequestMethod.POST)
    public Map setInterestAct(String id, String oper, InterestVO vo, HttpSession ses) {

        mnu_cd = "T2S003";
        String[] arr = {"C", "U"};

        vo.reg_mem_id = ses.getAttribute("id").toString();

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return null;
        }

        int resCnt = -1;
        boolean tmpOK = false;

        try {

            switch (oper) {
                case "add":
                    // 추가 모드
                    resCnt = securityInterestService.setInterestInsert(vo);

                    break;
                case "edit":
                    // 수정 모드
                    resCnt = securityInterestService.setInterestUpdate(vo);
                    break;
            }

            tmpOK = (resCnt > 0 ? true : false);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        Map<String, Boolean> map = new HashMap<String, Boolean>();

        map.put("isSuccess", tmpOK);

        return map;
    }

    /**
     * 관심차량 삭제(Delete)
     *
     * @param vo   InterestVO
     * @param ses  HttpSession
     * @return Map{isSuccess : true/false}
     */
    @RequestMapping(value = "/setInterestDel", method = RequestMethod.POST)
    public Map setInterestDel(@RequestBody InterestVO vo, HttpSession ses) {

        mnu_cd = "T2S003";
        String[] arr = {"D"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        }catch (Exception ex) {

            return null;
        }

        boolean tmpOk = false;

        try {

            securityInterestService.setInterestDelete(vo);

            tmpOk = true;
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        Map<String, Boolean> map = new HashMap<>();

        map.put("isSuccess", tmpOk);

        return map;
    }
}
