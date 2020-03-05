package tax.www.dao.event;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.event.GenerateVO;

import java.util.List;

/**
 * 상황발생 이력조회 DAO
 * <p>
 * User: 이준수
 * Date: 17. 11. 07
 * Time: 오후 2:26
 */
@Mapper("eventGenerateMapper")
public interface EventGenerateMapper {

    /**
     * 체납차량 발생현황 목록 총 갯수 가져오기
     *
     * @param srcCarNo    차량번호
     * @param srcCctvInfo CCTV 정보
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return 총 갯수
     */
    public int getTaxOcrListCnt(@Param("srcCarNo") String srcCarNo, @Param("srcCctvInfo") String srcCctvInfo, @Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * 체납차량 발생현황 목록 가져오기
     *
     * @param srcCarNo    차량번호
     * @param srcCctvInfo CCTV 정보
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List
     */
    public List<GenerateVO> getTaxOcrList(@Param("srcCarNo") String srcCarNo, @Param("srcCctvInfo") String srcCctvInfo, @Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * 시간별 발생현황 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List
     */
    public List<GenerateVO> getTimeTaxOcrList(@Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * 지역별 발생현황 Top5 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List
     */
    public List<GenerateVO> getAreaOcrTopList(@Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * CCTV별 발생현황 Top5 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List
     */
    public List<GenerateVO> getCctvOcrTopList(@Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);
}
