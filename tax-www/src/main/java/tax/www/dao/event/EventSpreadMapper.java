package tax.www.dao.event;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.event.SpreadVO;

import java.util.List;

/**
 * 상황전파 이력조회 DAO
 * <p>
 * User: 이준수
 * Date: 17. 11. 07
 * Time: 오전 9:40
 */
@Mapper("eventSpreadMapper")
public interface EventSpreadMapper {

    /**
     * 전파그룹 종류 가져오기
     *
     * @return 전파그룹
     */
    public List<ListObjVO> getTcgJongList();

    /**
     * 상황전파 이렵관리 목록 총 갯수 가져오기
     *
     * @param srcTcgJong 전파그룹
     * @param srcSDate   시작일
     * @param srcEDate   종료일
     * @param vo         jqGrid 파라미터
     * @return 총 갯수
     */
    public int getSitPpgListCnt(@Param("srcTcgJong") String srcTcgJong, @Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * 상황전파 이력관리 목록 가져오기
     *
     * @param srcTcgJong 전파그룹
     * @param srcSDate   시작일
     * @param srcEDate   종료일
     * @param vo         jqGrid 파라미터
     * @return List
     */
    public List<SpreadVO> getSitPpgList(@Param("srcTcgJong") String srcTcgJong, @Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);
}
