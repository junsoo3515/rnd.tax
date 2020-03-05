package tax.www.service.onepage;

import tax.www.vo.security.UserVO;

/**
 * 개인정보수정 서비스 인터페이스 클래스
 * <p>
 * User: 현재호
 * Date: 17. 11. 03
 * Time: 오후 2:51
 */
public interface OnepageUserService {
    /**
     * 사용자 상세정보 가져오기
     *
     * @param key 사용자 아이디
     * @return UserVO
     */
    public UserVO getUserData(String key) throws Exception;

    /**
     * 사용자정보 Update.
     *
     * @param vo UserVO
     * @return 쿼리 결과
     */
    public int setUserUpdate(UserVO vo) throws Exception;
}
