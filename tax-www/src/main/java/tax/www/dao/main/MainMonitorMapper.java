package tax.www.dao.main;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.main.MonitorVO;

import java.util.List;

/**
 * GIS 상황관제(메인) DAO
 * <p>
 * User: 이준수
 * Date: 18. 01. 22
 * Time: 오후 3:21
 */
@Mapper("mainMonitorMapper")
public interface MainMonitorMapper {

    /**
     * 상황처리 목록 총 갯수 가져오기
     *
     * @param vo      jqGrid 파라미터
     * @return 총 갯수
     */
    public int getSitPcsListCnt(@Param("vo") SrcJQGridVO vo);

    /**
     * 상황처리 목록 가져오기
     *
     * @param vo      jqGrid 파라미터
     * @return List
     */
    public List<MonitorVO> getSitPcsList(@Param("vo") SrcJQGridVO vo);

    /**
     * 상세정보 가져오기
     *
     * @param vo MonitorVO
     * @return MonitorVO
     */
    public MonitorVO getDetailInfoData(@Param("vo") MonitorVO vo);

    /**
     * 담당자 중복 확인을 위한 전체 담당자 가져오기
     *
     * @return List
     */
    public List<MonitorVO> getDupChkManagerList();

    /**
     * 담당자 가져오기
     *
     * @return 담당자
     */
    public List<ListObjVO> getManagerList();

    /**
     * 상세정보 적용
     *
     * @param vo MonitorVO
     * @return 쿼리 결과
     */
    public int setDetailInfoUpdate(@Param("vo") MonitorVO vo);

    /**
     * 영치 등록 시 체납처리 이력 테이블(TAX_PRC_HISTORY) 상세정보 등록
     *
     * @param vo MonitorVO
     * @return 쿼리 결과
     */
    public int setDetailInfoInsert(@Param("vo") MonitorVO vo);

    /**
     * 관심차량번호 가져오기
     *
     * @param car_no String
     * @return List
     */
    public List<ListObjVO> getInterestCarList(@Param("car_no") String car_no);

    /**
     * 레이어(CCTV 종류) 가져오기
     *
     * @return List
     */
    public List<ListObjVO> getCctvTypeList();

    /**
     * CCTV GIS 정보 가져오기
     *
     * @param vo MonitorVO
     * @return List
     */
    public List<MonitorVO> getCctvGeoData(@Param("vo") MonitorVO vo);

    /**
     * 분포도 GIS 정보 가져오기
     *
     * @return List
     */
    public List<MonitorVO> getDistributionGeoData();

    /**
     * 관심차량 이동경로 목록 가져오기
     *
     * @param vo MonitorVO
     * @return List
     */
    public List<MonitorVO> getRouteList(@Param("vo") MonitorVO vo);

    /**
     * 투망감시 설정 CCTV GIS 정보 가져오기
     *
     * @param vo MonitorVO
     * @return List
     */
    public List<MonitorVO> getNetSetCctvGeoData(@Param("vo") MonitorVO vo);

    /**
     * 실시간 상황처리 목록 가져오기
     *
     * @param sitOcrTime String
     * @return MonitorVO
     */
    public MonitorVO getRealtimeSitPcsList(@Param("sitOcrTime") String sitOcrTime);

    /**
     * CCTV 정보 가져오기
     *
     * @param cctvId String
     * @return List
     */
    public EgovMap getCctvInfo(@Param("cctvId") String cctvId);
}
