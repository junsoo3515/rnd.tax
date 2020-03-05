package tax.www.service.security;

import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.menu.MenuAuthVO;
import tax.www.vo.security.AuthorityVO;
import tax.www.vo.security.CodeVO;

import java.util.List;

/**
 * 권한관리 서비스 인터페이스 클래스
 * <p>
 * User: 현재호
 * Date: 17. 11. 03
 * Time: 오후 5:09
 */
public interface SecurityCodeService {

    /**
     * 코드 총 갯수 가져오기
     *
     * @param srcJong 종류코드
     * @param vo      jqGrid 파라미터
     * @return List user list cnt
     * @throws Exception the exception
     */
    public int getCodeListCnt(String srcJong, SrcJQGridVO vo) throws Exception;

    /**
     * 코드 목록 가져오기
     *
     * @param srcJong 종류코드
     * @param vo      jqGrid 파라미터
     * @return List user list
     * @throws Exception the exception
     */
    public List<CodeVO> getCodeList(String srcJong, SrcJQGridVO vo) throws Exception;

    /**
     * 코드 설정 Insert
     *
     * @param vo CodeVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setCodeInsert(CodeVO vo) throws Exception;

    /**
     * 코드 설정 Update
     *
     * @param vo CodeVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setCodeUpdate(CodeVO vo) throws Exception;
}
