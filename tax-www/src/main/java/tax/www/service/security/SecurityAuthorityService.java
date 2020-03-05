package tax.www.service.security;

import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.menu.MenuAuthVO;
import tax.www.vo.security.AuthorityVO;

import java.util.List;

/**
 * 권한관리 서비스 인터페이스 클래스
 * <p>
 * User: 현재호
 * Date: 17. 11. 03
 * Time: 오후 5:09
 */
public interface SecurityAuthorityService {

    /**
     * 권한 목록 총 갯수 가져오기
     *
     * @param srcAuth 권한코드
     * @param srcEtc  검색어
     * @param vo      jqGrid 파라미터
     * @return List user list cnt
     * @throws Exception the exception
     */
    public int getAuthListCnt(String srcAuth, String srcEtc, SrcJQGridVO vo) throws Exception;

    /**
     * 권한 목록 가져오기
     *
     * @param srcAuth 권한코드
     * @param srcEtc  검색어
     * @param vo      jqGrid 파라미터
     * @return List user list
     * @throws Exception the exception
     */
    public List<AuthorityVO> getAuthList(String srcAuth, String srcEtc, SrcJQGridVO vo) throws Exception;

    /**
     * 권한코드 중복확인
     *
     * @param key 권한코드
     * @return 권한코드 존재 개수
     * @throws Exception the exception
     */
    public int getAuthChk(String key) throws Exception;

    /**
     * 권한 정보 Insert/Update 처리
     * : COM_MEM_AUTH
     * : MERGE INTO로 처리
     *
     * @param vo the vo
     * @return the auth act
     * @throws Exception the exception
     */
    public int setAuthAct(AuthorityVO vo) throws Exception;

    /**
     * 메뉴접근 설정 데이터 존재 유무 판단
     *
     * @param key 권한 코드
     * @return COM_MEM_MNU 존재 개수
     * @throws Exception the exception
     */
    public int getAuthMenuCnt(String key) throws Exception;

    /**
     * 메뉴접근 설정 Insert
     *
     * @param firstSeq 가장 처음 들어가는 고유 SEQ
     * @param authCd   권한 코드
     * @param arrData  List<MenuAuthVO>
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setAuthMenuInsert(long firstSeq, String authCd, List<MenuAuthVO> arrData) throws Exception;

    /**
     * 메뉴접근 설정 Update
     *
     * @param authCd 권한 코드
     * @param arrVO  List<MenuAuthVO>
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setAuthMenuUpdate(String authCd, List<MenuAuthVO> arrVO) throws Exception;
}
