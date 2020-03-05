package tax.www.service.security;

import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.menu.MenuAuthVO;
import tax.www.vo.security.UserVO;

import java.util.List;

/**
 * 사용자정보관리 서비스 인터페이스 클래스
 * <p>
 * User: 현재호
 * Date: 17. 11. 03
 * Time: 오후 2:51
 */
public interface SecurityUserService {

    /**
     * 사용자 목록 총 갯수 가져오기
     *
     * @param srcAuth 권한코드
     * @param srcID   사용자 아이디
     * @param srcEtc  검색어
     * @param vo      jqGrid 파라미터
     * @return List user list cnt
     * @throws Exception the exception
     */
    public int getUserListCnt(String srcAuth, String srcID, String srcEtc, SrcJQGridVO vo) throws Exception;

    /**
     * 사용자 목록 가져오기
     *
     * @param srcAuth 권한코드
     * @param srcID   사용자 아이디
     * @param srcEtc  검색어
     * @param vo      jqGrid 파라미터
     * @return List user list
     * @throws Exception the exception
     */
    public List<UserVO> getUserList(String srcAuth, String srcID, String srcEtc, SrcJQGridVO vo) throws Exception;

    /**
     * 사용자 상세정보 가져오기
     *
     * @param key 사용자 고유번호
     * @return UserVO
     */
    public UserVO getUserData(String key) throws Exception;

    /**
     * 사용자정보 Insert
     *
     * @param vo UserVO
     * @return 쿼리 결과
     */
    public int setUserInsert(UserVO vo) throws Exception;

    /**
     * 사용자정보 Update.
     *
     * @param vo UserVO
     * @return 쿼리 결과
     */
    public int setUserUpdate(UserVO vo) throws Exception;

    /**
     * 메뉴접근 설정 데이터 존재 유무 판단
     *
     * @param key 사용자 아이디
     * @return COM_MEM_MNU 존재 개수
     */
    public int getUserAuthCnt(String key) throws Exception;

    /**
     * 메뉴접근 설정 Insert
     *
     * @param firstSeq 가장 처음 들어가는 고유 SEQ
     * @param authCd   권한 코드
     * @param memId    사용자 코드
     * @param listData List<MenuAuthVO>
     * @return 쿼리 결과
     */
    public int setUserAuthInsert(long firstSeq, String authCd, String memId, List<MenuAuthVO> listData) throws Exception;

    /**
     * 메뉴접근 설정 Update
     *
     * @param authCd 권한 코드
     * @param memId  사용자 코드
     * @param arrVO  List<MenuAuthVO>
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setUserAuthUpdate(String authCd, String memId, List<MenuAuthVO> arrVO) throws Exception;

    /**
     * 아이디 중복확인
     *
     * @param key 아이디
     * @return 아이디 존재 개수
     */
    public int getIDCheck(String key) throws Exception;

    /**
     * 로그인 성공 시 비밀번호 틀린 횟수 초기화
     *
     * @param memID 사용자 아이디
     * @throws Exception the exception
     */
    public int setUserFailPwdClear(String memID) throws Exception;
}
