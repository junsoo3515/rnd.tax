package tax.www.service.security;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import tax.www.dao.security.SecurityCodeMapper;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.CodeVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 시스템관리 > 코드관리
 * <p>
 * User: 현재호
 * Date: 17. 11. 06
 * Time: 오후 3:06
 */
@Service("SecurityCodeService")
public class SecurityCodeServiceImpl extends EgovAbstractServiceImpl implements SecurityCodeService {

    /**
     * 코드관리 DAO
     */
    @Resource(name = "securityCodeMapper")
    private SecurityCodeMapper securityCodeMapper;

    /**
     * 코드 총 갯수 가져오기
     *
     * @param srcJong 종류코드
     * @param vo      jqGrid 파라미터
     * @return List user list cnt
     * @throws Exception the exception
     */
    public int getCodeListCnt(String srcJong, SrcJQGridVO vo) throws Exception {

        return securityCodeMapper.getCodeListCnt(srcJong, vo);
    }

    /**
     * 코드 목록 가져오기
     *
     * @param srcJong 종류코드
     * @param vo      jqGrid 파라미터
     * @return List user list
     * @throws Exception the exception
     */
    public List<CodeVO> getCodeList(String srcJong, SrcJQGridVO vo) throws Exception {

        return securityCodeMapper.getCodeList(srcJong, vo);
    }

    /**
     * 코드 설정 Insert
     *
     * @param vo CodeVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setCodeInsert(CodeVO vo) throws Exception {

        return securityCodeMapper.setCodeInsert(vo);
    }

    /**
     * 코드 설정 Update
     *
     * @param vo CodeVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setCodeUpdate(CodeVO vo) throws Exception {

        return securityCodeMapper.setCodeUpdate(vo);
    }
}
