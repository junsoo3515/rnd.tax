package tax.www.service.onepage;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import tax.www.dao.security.SecurityUserMapper;
import tax.www.vo.security.UserVO;

import javax.annotation.Resource;

/**
 * 개인정보수정 서비스 인터페이스 클래스
 * <p>
 * User: 현재호
 * Date: 17. 11. 03
 * Time: 오후 2:52
 */
@Service("OnepageUserService")
public class OnepageUserServiceImpl extends EgovAbstractServiceImpl implements OnepageUserService {

    /**
     * 개인정보수정 DAO
     */
    @Resource(name = "securityUserMapper")
    private SecurityUserMapper securityUserMapper;

    /**
     * 사용자 상세정보 가져오기
     *
     * @param key 사용자 아이디
     * @return UserVO
     */
    public UserVO getUserData(String key) throws Exception {

        return securityUserMapper.getUserData(null, key);
    }

    /**
     * 사용자정보 Update.
     *
     * @param vo UserVO
     * @return 쿼리 결과
     */
    public int setUserUpdate(UserVO vo) throws Exception {

        return securityUserMapper.setUserUpdate(vo);
    }
}
