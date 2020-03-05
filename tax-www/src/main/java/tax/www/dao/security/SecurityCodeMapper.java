package tax.www.dao.security;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.CodeVO;

import java.util.List;

/**
 * 코드관리 DAO
 * <p>
 * User: 현재호
 * Date: 17. 11. 03
 * Time: 오후 5:15
 */
@Mapper("securityCodeMapper")
public interface SecurityCodeMapper {

    /**
     * 코드 총 개수 가져오기
     *
     * @param srcJong 코드종류
     * @param vo      jqGrid 파라미터
     * @return int 총 개수
     */
    public int getCodeListCnt(@Param("srcJong") String srcJong, @Param("vo") SrcJQGridVO vo);

    /**
     * 코드 목록 가져오기
     *
     * @param srcJong 코드종류
     * @param vo      jqGrid 파라미터
     * @return List<CodeVO> 코드목록
     */
    public List<CodeVO> getCodeList(@Param("srcJong") String srcJong, @Param("vo") SrcJQGridVO vo);

    /**
     * 코드정보 Insert
     *
     * @param vo CodeVO
     * @return 쿼리 결과
     */
    public int setCodeInsert(@Param("vo") CodeVO vo);

    /**
     * 코드정보 Update
     *
     * @param vo CodeVO
     * @return 쿼리 결과
     */
    public int setCodeUpdate(@Param("vo") CodeVO vo);
}
