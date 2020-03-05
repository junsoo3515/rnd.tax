package tax.www.dao.cmn;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.menu.MenuAuthVO;
import tax.www.vo.cmn.menu.MenuVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템 메뉴 DAO
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 5:16
 */
@Mapper("cmnMenuMapper")
public interface CmnMenuMapper {

    /**
     * 전체 시스템 메뉴 가져오기
     *
     * @param sysKey 시스템 코드
     * @return List<MenuVO> 패턴 데이터
     */
    public List<MenuVO> getAllMenu(@Param("sysKey") String sysKey);

    /**
     * 사용자 계정별 왼쪽 메뉴 가져오기
     *
     * @param sysKey 시스템 코드
     * @param folKey 폴더 코드
     * @param memId  사용자 아이디
     * @return List<MenuVO>  사용자 메뉴
     */
    public List<MenuVO> getLeftMenu(@Param("sysKey") String sysKey, @Param("folKey") String folKey, @Param("memId") String memId);

    /**
     * 권한/사용자별 메뉴별 접근 설정 가져오기
     *
     * @param jongCD 종류 코드 : A(권한) / B(사용자)
     * @param val    값
     * @return
     */
    public List<MenuAuthVO> getMenuData(@Param("jongCD") String jongCD, @Param("val") String val);

    /**
     * 사용자 접속 Log Insert
     *
     * @param id     사용자 아이디
     * @param mnu_cd 메뉴코드
     * @return 쿼리 결과
     */
    public int setUserLogInsert(@Param("id") String id, @Param("mnu_cd") String mnu_cd);

    /**
     * 사용자 별 + 해당 메뉴 접근 가능 체크
     *
     * @param memID 사용자 아이디
     * @param url   접근 URL
     * @return 체크 여부
     */
    public int getAccessMenu(@Param("memID") String memID, @Param("url") String url);

    /**
     * 사용자 별 + 시스템 처음 접근 메뉴 찾기
     *
     * @param sysKey 시스템 코드
     * @param folKey 접근 폴더
     * @param memID  사용자 아이디
     * @return 체크 여부
     */
    public HashMap<String, String> getAccessFirstMenu(@Param("sysKey") String sysKey, @Param("folKey") String folKey, @Param("memID") String memID);

    /**
     * 해당 CRUD 권한 가져오기
     *
     * @param id     사용자 아이디
     * @param mnu_cd 메뉴코드
     * @return HashMap<String, String> 권한
     */
    public Map<String, String> getAuthCrud(@Param("id") String id, @Param("mnu_cd") String mnu_cd);
}
