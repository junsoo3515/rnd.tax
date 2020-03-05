package tax.www.service.login;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import tax.www.dao.login.AuthLoginMapper;
import tax.www.vo.login.AuthLoginVO;
import tax.www.vo.login.UserVO;

import javax.annotation.Resource;

/**
 * 사용자 로그인 서비스 인터페이스 클래스
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:52
 */
@Service("AuthLoginService")
public class AuthLoginServiceImpl extends EgovAbstractServiceImpl implements AuthLoginService {

    /**
     * 공통 DAO
     */
    @Resource(name = "authLoginMapper")
    AuthLoginMapper authLoginMapper;

    /**
     * 사용자 정보 가져오기
     *
     * @param vo AuthLoginVO
     * @return String Top Menu
     * @throws Exception the exception
     */
    public UserVO getMemberData(AuthLoginVO vo) throws Exception {

        return authLoginMapper.getMemberData(vo);
    }

    /**
     * 비밀번호 틀릴 경우 틀린 횟수 증가
     *
     * @param memID 사용자 아이디
     * @throws Exception the exception
     */
    public int setLoginFail(String memID) throws Exception {

        return authLoginMapper.setLoginFail(memID);
    }
}
