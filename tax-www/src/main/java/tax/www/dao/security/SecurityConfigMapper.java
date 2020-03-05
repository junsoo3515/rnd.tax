package tax.www.dao.security;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.security.ConfigVO;

import java.util.List;

/**
 * 체납차량 판별조건 관리 DAO
 * <p>
 * User: 이준수
 * Date: 18. 01. 08
 * Time: 오후 5:32
 */
@Mapper("securityConfigMapper")
public interface SecurityConfigMapper {

    /**
     * 판별조건 가져오기
     *
     * @return List<ListObjVO>
     */
    public List<ListObjVO> getDisConList();

    /**
     * 체납차량 판별조건 목록 총 갯수 가져오기
     *
     * @param srcDis        판별조건
     * @param vo            jqGrid 파라미터
     * @return List interest list cnt
     */
    public int getConfigListCnt(@Param("srcDis") String srcDis, @Param("vo") SrcJQGridVO vo);

    /**
     * 체납차량 판별조건 목록 가져오기
     *
     * @param srcDis        판별조건
     * @param vo            jqGrid 파라미터
     * @return List contactGrp list
     */
    public List<ConfigVO> getConfigList(@Param("srcDis") String srcDis, @Param("vo") SrcJQGridVO vo);

    /**
     * 체납차량 판별조건 Insert
     *
     * @param vo ConfigVO
     * @return 쿼리 결과
     */
    public int setConfigInsert(@Param("vo") ConfigVO vo);

    /**
     * 체납차량 판별조건 Update
     *
     * @param vo ConfigVO
     * @return 쿼리 결과
     */
    public int setConfigUpdate(@Param("vo") ConfigVO vo);

    /**
     * 체납차량 판별조건 Delete
     *
     * @param vo ConfigVO
     * @return 쿼리 결과
     */
    public void setConfigDelete(@Param("vo") ConfigVO vo);
}
