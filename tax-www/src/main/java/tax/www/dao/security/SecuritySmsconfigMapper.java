package tax.www.dao.security;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.SmsconfigVO;

import java.util.List;

/**
 * SMS 연락처 관리 DAO
 * <p>
 * User: 이준수
 * Date: 17. 11. 06
 * Time: 오후 5:19
 */
@Mapper("securitySmsconfigMapper")
public interface SecuritySmsconfigMapper {

    /**
     * 그룹 목록 총 갯수 가져오기
     *
     * @param srcContactGrp 그룹명
     * @param vo            jqGrid 파라미터
     * @return List contactGrp list cnt
     */
    public int getContactGrpListCnt(@Param("srcContactGrp") String srcContactGrp, @Param("vo") SrcJQGridVO vo);

    /**
     * 그룹 목록 가져오기
     *
     * @param srcContactGrp 그룹명
     * @param vo            jqGrid 파라미터
     * @return List contactGrp list
     */
    public List<SmsconfigVO> getContactGrpList(@Param("srcContactGrp") String srcContactGrp, @Param("vo")SrcJQGridVO vo);

    /**
     * 그룹 목록 Insert
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     */
    public int setContactGrpInsert(@Param("vo") SmsconfigVO vo);

    /**
     * 그룹 목록 Update
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     */
    public int setContactGrpUpdate(@Param("vo") SmsconfigVO vo);

    /**
     * 그룹 목록 Delete
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     */
    public int setContactGrpDelete(@Param("vo") SmsconfigVO vo);

    /**
     * 연락처 목록 가져오기
     *
     * @param srcContact 연락처
     * @param tcg_seq    그룹 일련번호
     * @param vo         jqGrid 파라미터
     * @return List contact list
     */
    public List<SmsconfigVO> getContactList(@Param("srcContact") String srcContact, @Param("tcg_seq") String tcg_seq, @Param("vo") SrcJQGridVO vo);

    /**
     * 연락처 목록(담당자 관리 테이블) Insert
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     */
    public int setManagerInsert(@Param("vo") SmsconfigVO vo);

    /**
     * 연락처 목록 Insert
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     */
    public int setContactInsert(@Param("vo") SmsconfigVO vo);

    /**
     * 연락처 목록(담당자 관리 테이블) Update
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     */
    public int setManagerUpdate(@Param("vo") SmsconfigVO vo);

    /**
     * 연락처 목록 Update
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     */
    public int setContactUpdate(@Param("vo") SmsconfigVO vo);

    /**
     * 연락처 목록 Delete
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     */
    public int setContactDelete(@Param("vo") SmsconfigVO vo);

    /**
     * 담당자 관리 테이블 데이터 가져오기
     *
     * @return List
     */
    public List<SmsconfigVO> chkDupContact();
}
