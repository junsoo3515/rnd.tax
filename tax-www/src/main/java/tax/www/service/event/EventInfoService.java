package tax.www.service.event;

import org.json.simple.JSONObject;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.event.InfoVO;

import java.util.List;

/**
 * 관심차량정보현황 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 18. 01. 12
 * Time: 오전 10:05
 */
public interface EventInfoService {

    /**
     * 관심차량 목록 총 갯수 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List interest list cnt
     * @throws Exception the exception
     */
    public int getInterestListCnt(String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;

    /**
     * 관심차량 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List interest list
     * @throws Exception the exception
     */
    public List<InfoVO> getInterestList(String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;

    /**
     * 관심차량 이동경로 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List interestRoute list cnt
     * @throws Exception the exception
     */
    public List<InfoVO> getInterestRouteList(String srcSDate, String srcEDate, String car_no, SrcJQGridVO vo) throws Exception;

    /**
     * 관심차량 이동경로 좌표 가져오기
     *
     * @param vo          InfoVO
     * @return List routeGeoData list
     * @throws Exception the exception
     */
    public List<InfoVO> getRouteGeoData(InfoVO vo) throws Exception;

    /**
     * 최단거리 좌표 가져오기
     *
     * @param point       최초좌표, 최근좌표
     * @return JSONObject routeLine
     * @throws Exception the exception
     */
    public JSONObject getRouteLine(List<Object> point) throws Exception;
}
