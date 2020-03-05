package tax.www.service.cmn;

import tax.www.vo.cmn.files.FilesVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.cmn.menu.MenuAuthVO;
import tax.www.vo.cmn.menu.MenuVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 기타 관련 Business 로직
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 5:09
 */
public interface CmnService {
    /**
     * 특정 테이블의 최대 고유번호 가져오기
     *
     * @param tb     테이블
     * @param col    고유 칼럼
     * @param addCnt 최대 + a
     * @return 고유번호 table max seq
     * @throws Exception the exception
     */
    public long getTableMaxSeq(String tb, String col, Integer addCnt) throws Exception;

    /**
     * 특정 테이블의 최대 고유번호 가져오기
     *
     * @param tb     테이블
     * @param col    고유 칼럼
     * @param addCnt 최대 + a
     * @param where  검색조건 쿼리문
     * @return 고유번호 table max seq
     * @throws Exception the exception
     */
    public long getTableMaxSeq(String tb, String col, Integer addCnt, String where) throws Exception;

    /**
     * Top Menu 가져오기
     *
     * @param ses        세션정보
     * @param rootFolder ROOT 경로(시스템 경로)
     * @return String Top Menu
     */
    public String getTopMenu(HttpSession ses, String rootFolder) throws Exception;

    /**
     * 전체 시스템 메뉴 가져오기
     *
     * @return List<MenuVO> 패턴 데이터
     */
    public List<MenuVO> getAllMenu() throws Exception;

    /**
     * 왼쪽 메뉴 가져오기
     *
     * @param folKey     폴더 코드(M : 메인, E : 이벤트관리, S : 시스템관리 )
     * @param ses        세션정보
     * @param rootFolder ROOT 경로(시스템 경로)
     * @param nowPageCd  현재 페이지 코드
     * @return the left menu
     * @throws Exception the exception
     */
    public String getLeftMenu(String folKey, HttpSession ses, String rootFolder, String nowPageCd) throws Exception;

    /**
     * 권한/사용자별 메뉴별 접근 설정 가져오기
     *
     * @param jongCD 종류 코드 : A(권한) / B(사용자)
     * @param val    값
     * @return String Top Menu
     * @throws Exception the exception
     */
    public List<MenuAuthVO> getMenuData(String jongCD, String val) throws Exception;

    /**
     * 해당 CRUD 권한 가져오기
     *
     * @param id     사용자 아이디
     * @param mnu_cd 메뉴코드
     * @return HashMap<String  권한>
     * @throws Exception the exception
     */
    public Map<String, String> getAuthCrud(String id, String mnu_cd) throws Exception;

    /**
     * 사용자 별 + 해당 메뉴 접근 가능 체크
     *
     * @param memID 사용자 아이디
     * @param url   접근 URL
     * @return 체크 여부
     */
    public int getAccessMenu(String memID, String url) throws Exception;

    /**
     * 사용자 별 + 시스템 처음 접근 메뉴 찾기
     *
     * @param folKey 접근 폴더
     * @param memID  사용자 아이디
     * @return 체크 여부
     * @throws Exception the exception
     */
    public HashMap<String, String> getAccessFirstMenu(String folKey, String memID) throws Exception;

    /**
     * 사용자 계정 별 CRUD 권한 체크
     *
     * @param ses      세션 가져오기
     * @param menuKey  대(상단)메뉴 코드
     * @param gubunKey 구분 키(C : reg_fl, R : read_fl, U : mod_fl, D : del_fl, V : use_fl)
     * @return
     * @throws Exception 접근 오류 Exception 발생
     */
    public void isUserAccessValidate(HttpSession ses, String menuKey, String[] gubunKey) throws Exception;

    /**
     * 코드 종류 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getCodeGubunList() throws Exception;

    /**
     * 공통 코드 가져오기
     *
     * @param jongCD 그룹코드
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getCodeList(String jongCD) throws Exception;

    /**
     * 권한 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getAuthList() throws Exception;

    /**
     * 공통 ModelObject 설정
     *
     * @param req    HttpServletRequest
     * @return Map
     * @throws Exception the exception
     */
    public Map<String, Object> setCmnModel(HttpServletRequest req) throws Exception;

    /**
     * 공통 ModelObject 설정
     *
     * @param req    HttpServletRequest
     * @param mnu_cd 메뉴 코드
     * @return Map
     * @throws Exception the exception
     */
    public Map<String, Object> setCmnModel(HttpServletRequest req, String mnu_cd) throws Exception;

    /**
     * 파일 정보 가져오기
     *
     * @param realTB  실 테이블
     * @param realSEQ 실제 SEQ
     * @param gubunCD 구분자 코드
     * @return COM_FILES DB 테이블 조회 결과
     * @throws Exception the exception
     */
    public List<FilesVO> getFileList(String realTB, String realSEQ, String gubunCD) throws Exception;

    /**
     * 파일 상세 정보 가져오기
     *
     * @param key SEQ
     * @return FilesVO
     */
    public FilesVO getFileInfo(String key) throws Exception;

    /**
     * 파일 DB 추가
     *
     * @param vo FilesVO
     * @return Insert 성공 건수
     */
    public int insertFile(FilesVO vo) throws Exception;

    /**
     * 파일 DB 삭제
     *
     * @param key SEQ
     * @return 삭제 성공 건수
     */
    public int deleteFile(String key) throws Exception;
}
