package tax.www.dao.security;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.security.FilecrollingVO;

import java.util.List;

/**
 * 파일수신현황 DAO
 * <p>
 * User: 이준수
 * Date: 17. 11. 06
 * Time: 오후 3:10
 */
@Mapper("securityFilecrollingMapper")
public interface SecurityFilecrollingMapper {

    /**
     * 파일수신현황 목록 가져오기
     *
     * @return List<ListObjVO>
     */
    public List<ListObjVO> getFileCltJongList();

    /**
     * 파일수신현황 목록 총 갯수 가져오기
     *
     * @param srcFileCltJong 파일수신현황 종류
     * @param srcSDate       시작일
     * @param srcSDate       종료일
     * @param vo      jqGrid 파라미터
     * @return List fileCltRes list cnt
     */
    public int getFileCltResListCnt(@Param("srcFileCltJong") String srcFileCltJong, @Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * 파일수신현황 목록 가져오기
     *
     * @param srcFileCltJong 파일수신현황 종류
     * @param srcSDate       시작일
     * @param srcSDate       종료일
     * @param vo      jqGrid 파라미터
     * @return List fileCltRes list
     */
    public List<FilecrollingVO> getFileCltResList(@Param("srcFileCltJong") String srcFileCltJong, @Param("srcSDate") String srcSDate, @Param("srcEDate") String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * 오류 세부 목록 총 갯수 가져오기
     *
     * @param tfcr_seq 파일수집결과 일련번호
     * @param vo       jqGrid 파라미터
     * @return List fileCltErr list cnt
     */
    public int getFileCltErrListCnt(@Param("tfcr_seq") String tfcr_seq, @Param("vo") SrcJQGridVO vo);

    /**
     * 오류 세부 목록 가져오기
     *
     * @param tfcr_seq 파일수집결과 일련번호
     * @param vo       jqGrid 파라미터
     * @return List fileCltErr list
     */
    public List<FilecrollingVO> getFileCltErrList(@Param("tfcr_seq") String tfcr_seq, @Param("vo") SrcJQGridVO vo);

}
