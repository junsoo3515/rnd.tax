package tax.www.dao.login;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.login.AuthLoginVO;
import tax.www.vo.login.UserVO;

/**
 * 사용자 로그인 DAO
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:48
 */
@Mapper("authLoginMapper")
public interface AuthLoginMapper {

    /**
     * 사용자 정보 가져오기
     *
     * @param vo 인자
     * @return 사용자 DB 조회 결과
     */
    public UserVO getMemberData(@Param("vo") AuthLoginVO vo);

    /**
     * 계정이 틀려서 로그인 실패 할 경우 비밀번호 틀린 횟수 + 1
     *
     * @param memID 사용자 아이디
     * @return 사용자 DB 조회 결과
     */
    public int setLoginFail(@Param("memID") String memID);
}
