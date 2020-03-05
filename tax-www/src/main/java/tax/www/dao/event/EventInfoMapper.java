package tax.www.dao.event;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.event.InfoVO;

import java.util.List;

/**
 * 관심차량저보현황 DAO
 * <p>
 * User: 이준수
 * Date: 18. 01. 12
 * Time: 오전 10:15
 */
@Mapper("eventInfoMapper")
public interface EventInfoMapper {

    /**
     * 관심차량 목록 총 갯수 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return 총 갯수
     */
    public int getInterestListCnt(@Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * 관심차량 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List
     */
    public List<InfoVO> getInterestList(@Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * 관심차량 이동경로 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param car_no      차량번호
     * @param vo          jqGrid 파라미터
     * @return List
     */
    public List<InfoVO> getInterestRouteList(@Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("car_no") String car_no, @Param("vo") SrcJQGridVO vo);

    /**
     * 관심차량 이동경로 좌표 가져오기
     *
     * @param vo          InfoVO
     * @return List
     */
    public List<InfoVO> getRouteGeoData(@Param("vo") InfoVO vo);
}
