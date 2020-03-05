package tax.www.service.main;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.main.MonitorVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * GIS 상황관제(메인) 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 17. 11. 07
 * Time: 오후 1:42
 */
public interface MainMonitorService {

    /**
     * 상황처리 목록 총 갯수 가져오기
     *
     * @param vo          jqGrid 파라미터
     * @return List taxOcr list cnt
     * @throws Exception the exception
     */
    public int getSitPcsListCnt(SrcJQGridVO vo) throws Exception;

    /**
     * 상황처리 목록 가져오기
     *
     * @param vo          jqGrid 파라미터
     * @return List taxOcr list
     * @throws Exception the exception
     */
    public List<MonitorVO> getSitPcsList(SrcJQGridVO vo) throws Exception;

    /**
     * 상세정보 가져오기
     *
     * @param vo MonitorVO
     * @return MonitorVO
     * @throws Exception the exception
     */
    public MonitorVO getDetailInfoData(MonitorVO vo) throws Exception;

    /**
     * 담당자 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getManagerList() throws Exception;

    /**
     * 상세정보 적용
     *
     * @param vo MonitorVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setDetailInfoAct(MonitorVO vo) throws Exception;

    /**
     * 관심차량번호 가져오기
     *
     * @param car_no 차량번호
     * @return List interestCar list
     * @throws Exception the exception
     */
    public List<ListObjVO> getInterestCarList(String car_no) throws Exception;

    /**
     * 레이어(CCTV 종류) 가져오기
     *
     * @return List cctvType list
     * @throws Exception the exception
     */
    public List<ListObjVO> getCctvTypeList() throws Exception;

    /**
     * CCTV GIS 정보 가져오기
     *
     * @param vo MonitorVO
     * @return List cctvType list
     * @throws Exception the exception
     */
    public List<MonitorVO> getCctvGeoData(MonitorVO vo) throws Exception;

    /**
     * 분포도 GIS 정보 가져오기
     *
     * @return List cctvType list
     * @throws Exception the exception
     */
    public List<MonitorVO> getDistributionGeoData() throws Exception;

    /**
     * 관심차량 이동경로 목록 가져오기
     *
     * @param vo MonitorVO
     * @return List route list
     * @throws Exception the exception
     */
    public List<MonitorVO> getRouteList(MonitorVO vo) throws Exception;

    /**
     * 투망감시 설정 CCTV GIS 정보 가져오기
     *
     * @param vo MonitorVO
     * @return List netSetCctvGeoData list
     * @throws Exception the exception
     */
    public List<MonitorVO> getNetSetCctvGeoData(MonitorVO vo) throws Exception;

    /**
     * 실시간 상황처리 목록 가져오기
     *
     * @param sitOcrTime String
     * @return List realtimeSitPcs list
     * @throws Exception the exception
     */
    public MonitorVO getRealtimeSitPcsList(String sitOcrTime) throws Exception;

    /**
     * CCTV 정보 가져오기
     *
     * @param cctvId CCTV ID
     * @return MonitorVO
     * @throws Exception the exception
     */
    public EgovMap getCctvInfo(String cctvId) throws Exception;

    /**
     * 영상팝업 스크린샷 저장하기
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param memId 사용자 아이디
     * @param vurixIpPort 뷰릭스IpPort(IP:PORT)
     * @return
     * @throws Exception the exception
     */
    public void snapShot(HttpServletRequest request, HttpServletResponse response, String memId, String vurixIpPort) throws Exception;

    /**
     * 투망감시 RTSP URL 가져오기
     *
     * @param param  차량인식 일련번호, 발생 CCTV ID
     * @param userId 사용자아이디
     * @param vurixIpPort 뷰릭스IpPort(IP:PORT)
     * @return List RTSP URL list
     * @throws Exception the exception
     */
    public List<Map<String, Object>> castNetRtspUrl(Map<String, Object> param, String userId, String vurixIpPort) throws Exception;

    /**
     * 발생지점 CCTV(실시간 감시), 영상팝업 RTSP URL 가져오기,
     *
     * @param param  차량인식 일련번호, 발생 CCTV ID
     * @param userId 사용자 아이디
     * @param vurixIpPort 뷰릭스IpPort(IP:PORT)
     * @return Map<String, Object>
     * @throws Exception the exception
     */
    public Map<String, Object> rtspUrl(Map<String, Object> param, String userId, String vurixIpPort) throws Exception;
}
