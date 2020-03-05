package tax.www.dao.security;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.InterestVO;

import java.util.List;

/**
 * 관심차량관리 DAO
 * <p>
 * User: 이준수
 * Date: 18. 01. 08
 * Time: 오후 3:35
 */
@Mapper("securityInterestMapper")
public interface SecurityInterestMapper {

    /**
     * 관심차량 목록 총 갯수 가져오기
     *
     * @param srcCarNo      차량번호
     * @param vo            jqGrid 파라미터
     * @return List interest list cnt
     */
    public int getInterestListCnt(@Param("srcCarNo") String srcCarNo, @Param("vo") SrcJQGridVO vo);

    /**
     * 관심차량 목록 가져오기
     *
     * @param srcCarNo      차량번호
     * @param vo            jqGrid 파라미터
     * @return List contactGrp list
     */
    public List<InterestVO> getInterestList(@Param("srcCarNo") String srcCarNo, @Param("vo") SrcJQGridVO vo);

    /**
     * 관심차량 Insert
     *
     * @param vo InterestVO
     * @return 쿼리 결과
     */
    public int setInterestInsert(@Param("vo") InterestVO vo);

    /**
     * 관심차량 Update
     *
     * @param vo InterestVO
     * @return 쿼리 결과
     */
    public int setInterestUpdate(@Param("vo") InterestVO vo);

    /**
     * 관심차량 Delete
     *
     * @param vo InterestVO
     * @return 쿼리 결과
     */
    public void setInterestDelete(@Param("vo") InterestVO vo);
}