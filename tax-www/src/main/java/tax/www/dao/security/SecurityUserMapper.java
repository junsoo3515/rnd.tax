package tax.www.dao.security;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.menu.MenuAuthVO;
import tax.www.vo.security.UserVO;

import java.util.List;

/**
 * 사용자정보관리 DAO
 * <p>
 * User: 현재호
 * Date: 17. 11. 03
 * Time: 오후 2:55
 */
@Mapper("securityUserMapper")
public interface SecurityUserMapper {

    /**
     * 사용자 목록 총 갯수 가져오기
     *
     * @param srcAuth 권한코드
     * @param srcID   사용자 아이디
     * @param srcEtc  검색어
     * @param vo      jqGrid 파라미터
     * @return List user list cnt
     */
    public int getUserListCnt(@Param("srcAuth") String srcAuth, @Param("srcID") String srcID, @Param("srcEtc") String srcEtc, @Param("vo") SrcJQGridVO vo);

    /**
     * 사용자 목록 가져오기
     *
     * @param srcAuth 권한코드
     * @param srcID   사용자 아이디
     * @param srcEtc  검색어
     * @param vo      jqGrid 파라미터
     * @return List user list
     */
    public List<UserVO> getUserList(@Param("srcAuth") String srcAuth, @Param("srcID") String srcID, @Param("srcEtc") String srcEtc, @Param("vo") SrcJQGridVO vo);

    /**
     * 사용자 상세정보 가져오기
     *
     * @param memSEQ 사용자 고유번호
     * @param memID  사용자 아이디
     * @return UserVO user data
     */
    public UserVO getUserData(@Param("memSEQ") String memSEQ, @Param("memID") String memID);

    /**
     * 사용자정보 Insert
     *
     * @param vo UserVO
     * @return 쿼리 결과
     */
    public int setUserInsert(@Param("vo") UserVO vo);

    /**
     * 사용자정보 Update.
     *
     * @param vo UserVO
     * @return 쿼리 결과
     */
    public int setUserUpdate(@Param("vo") UserVO vo);

    /**
     * 메뉴접근 설정 데이터 존재 유무 판단
     *
     * @param key 사용자 아이디
     * @return COM_MEM_MNU 존재 개수
     */
    public int getUserAuthCnt(@Param("key") String key);

    /**
     * 메뉴접근 설정 Insert
     *
     * @param firstSeq 가장 처음 들어가는 고유 SEQ
     * @param authCd 권한 코드
     * @param memId 사용자 코드
     * @param listData List<MenuAuthVO>
     * @return 쿼리 결과
     */
    public int setUserAuthInsert(@Param("firstSeq") long firstSeq, @Param("authCd") String authCd, @Param("memId") String memId, @Param("listData") List<MenuAuthVO> listData);

    /**
     * 메뉴접근 설정 Update
     *
     * @param authCd 권한 코드
     * @param memId 사용자 코드
     * @param vo MenuAuthVO
     * @return 쿼리 결과
     */
    public int setUserAuthUpdate(@Param("authCd") String authCd, @Param("memId") String memId, @Param("vo") MenuAuthVO vo);

    /**
     * 아이디 중복확인
     *
     * @param key 아이디
     * @return 아이디 존재 개수
     */
    public int getIDCheck(@Param("key") String key);

    /**
     * 로그인 성공 시 비밀번호 틀린 횟수 초기화
     *
     * @param memID 사용자 아이디
     * @return DB 갱신 결과
     */
    public int setUserFailPwdClear(@Param("memID") String memID);
}
