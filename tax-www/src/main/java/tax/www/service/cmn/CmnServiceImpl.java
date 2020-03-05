package tax.www.service.cmn;

import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.com.cmn.service.EgovProperties;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import tax.www.dao.cmn.CmnDataMapper;
import tax.www.dao.cmn.CmnDropdownMapper;
import tax.www.dao.cmn.CmnMenuMapper;
import tax.www.vo.cmn.files.FilesVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.cmn.menu.MenuAuthVO;
import tax.www.vo.cmn.menu.MenuVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 기타 관련 Business 로직
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 5:10
 */
@Service("CmnService")
public class CmnServiceImpl extends EgovAbstractServiceImpl implements CmnService {

    private static final Logger log = LogManager.getLogger(CmnServiceImpl.class);

    //    @Value("#{cfg['auth.session.context']}")
    private String contextRoot = EgovProperties.getProperty("auth.session.context");
    private String systemCd = EgovProperties.getProperty("system.code"); // 시스템 구분코드

    /**
     * 시스템 공통 DAO
     */
    @Resource(name = "cmnDataMapper")
    private CmnDataMapper cmnDataMapper;

    /**
     * 시스템 메뉴 DAO
     */
    @Resource(name = "cmnMenuMapper")
    private CmnMenuMapper cmnMenuMapper;

    /**
     * DropDown 유형 DAO
     */
    @Resource(name = "cmnDropdownMapper")
    private CmnDropdownMapper cmnDropdownMapper;

    /**
     * 특정 테이블의 최대 고유번호 가져오기
     *
     * @param tb     테이블
     * @param col    고유 칼럼
     * @param addCnt 최대 + a
     * @return 고유번호
     */
    public long getTableMaxSeq(String tb, String col, Integer addCnt) throws Exception {

        return cmnDataMapper.getTableMaxSeq(tb, col, addCnt, null);
    }

    /**
     * 특정 테이블의 최대 고유번호 가져오기
     *
     * @param tb     테이블
     * @param col    고유 칼럼
     * @param addCnt 최대 + a
     * @param where  검색조건 쿼리문
     * @return 고유번호
     */
    public long getTableMaxSeq(String tb, String col, Integer addCnt, String where) throws Exception {

        return cmnDataMapper.getTableMaxSeq(tb, col, addCnt, where);
    }

    /**
     * Top Menu 가져오기
     *
     * @param ses        세션정보
     * @param rootFolder ROOT 경로(시스템 경로)
     * @return String Top Menu
     */
    public String getTopMenu(HttpSession ses, String rootFolder) throws Exception {

        StringBuffer retV = new StringBuffer();

        try {

            Map<String, MenuVO> topList = new TreeMap<String, MenuVO>(); // 키 별 소팅이 필요해서 TreeMap으로 설정

            // 1. 왼쪽 메뉴 중 대메뉴 별로 처음 접속해야 하는 메뉴정보 Filter
            for (MenuVO vo : cmnMenuMapper.getLeftMenu(systemCd, null, ses.getAttribute("id").toString())) {

                if (!topList.containsKey(vo.grp_cd)) {

                    if (!StringUtils.endsWith(vo.mnu_cd, "000")) {

                        topList.put(vo.grp_cd, vo);
                    }
                }
            }

            // 2. Top Menu Html 생성
            for (String key : topList.keySet()) {

                retV.append("\n<li><a href='").append(rootFolder).append(topList.get(key).url).append("'>").append(topList.get(key).mnu_nm2).append("</a></li>");
            }

            topList = null;
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return retV.toString();
    }

    /**
     * 전체 시스템 메뉴 가져오기
     *
     * @return List<MenuVO> 패턴 데이터
     */
    public List<MenuVO> getAllMenu() throws Exception {

        return cmnMenuMapper.getAllMenu(systemCd);
    }

    /**
     * 왼쪽 메뉴 가져오기
     *
     * @param folKey     폴더 코드(M : 메인, E : 이벤트관리, S : 시스템관리 )
     * @param ses        세션정보
     * @param rootFolder ROOT 경로(시스템 경로)
     * @param nowPageCd  현재 페이지 코드
     * @return List<MenuVO>
     */
    public String getLeftMenu(String folKey, HttpSession ses, String rootFolder, String nowPageCd) {

        StringBuffer retV = new StringBuffer();

        try {

            int menuCnt = 0;

            for (MenuVO vo : cmnMenuMapper.getLeftMenu(systemCd, folKey, ses.getAttribute("id").toString())) {

                if (StringUtils.endsWith(vo.mnu_cd, "000")) {

                    if (menuCnt > 0) {

                        retV.append("\n    </ul>");
                        retV.append("\n</li>");
                    } else {
                        // 처음 왼쪽 메뉴 시스템 명 설정
                        retV.append("\n<li class='nav-header'>").append(vo.mnu_nm1).append("</li>");
                    }

                    retV.append("\n<li class='has-sub").append(StringUtils.equals(nowPageCd.substring(0, 3), vo.grp_cd) ? " active" : "").append("'>"); // expand 우선 없앰
                    retV.append("\n    <a href='").append(StringUtils.isEmpty(vo.url) ? "#" : vo.url).append("'>");
                    retV.append("\n        <b class='caret pull-right'></b>");
                    retV.append("\n        <i class='fa ").append(vo.etc).append("'></i>");
                    retV.append("\n        <span>").append(vo.mnu_nm2).append("</span>");
                    retV.append("\n    </a>");
                    retV.append("\n    <ul class='sub-menu'>");
                } else {

                    retV.append("\n        <li").append(StringUtils.equals(nowPageCd, vo.mnu_cd) ? " class='active'" : "").append("><a href='").append(StringUtils.isEmpty(vo.url) ? "#" : rootFolder + vo.url).append("'>").append(vo.mnu_nm3).append("</a></li>");
                }

                menuCnt++;
            }

            if (menuCnt > 0) {

                retV.append("\n    </ul>");
                retV.append("\n</li>");
            }
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return retV.toString();
    }

    /**
     * 권한/사용자별 메뉴별 접근 설정 가져오기
     *
     * @param jongCD 종류 코드 : A(권한) / B(사용자)
     * @param val    값
     * @return String Top Menu
     * @throws Exception the exception
     */
    public List<MenuAuthVO> getMenuData(String jongCD, String val) throws Exception {

        return cmnMenuMapper.getMenuData(jongCD, val);
    }

    /**
     * 해당 CRUD 권한 가져오기
     *
     * @param id     사용자 아이디
     * @param mnu_cd 메뉴코드
     * @return HashMap<String  권한>
     * @throws Exception the exception
     */
    public Map<String, String> getAuthCrud(String id, String mnu_cd) throws Exception {

        return cmnMenuMapper.getAuthCrud(id, mnu_cd);
    }

    /**
     * 사용자 별 + 해당 메뉴 접근 가능 체크
     *
     * @param memID 사용자 아이디
     * @param url   접근 URL
     * @return 체크 여부
     */
    public int getAccessMenu(String memID, String url) throws Exception {

        return cmnMenuMapper.getAccessMenu(memID, url);
    }

    /**
     * 사용자 별 + 시스템 처음 접근 메뉴 찾기
     *
     * @param folKey 접근 폴더
     * @param memID  사용자 아이디
     * @return 체크 여부
     * @throws Exception the exception
     */
    public HashMap<String, String> getAccessFirstMenu(String folKey, String memID) throws Exception {

        return cmnMenuMapper.getAccessFirstMenu(systemCd, folKey, memID);
    }

    /**
     * 사용자 계정 별 CRUD 권한 체크
     *
     * @param ses      세션 가져오기
     * @param menuKey  대(상단)메뉴 코드
     * @param gubunKey 구분 키(C : reg_fl, R : read_fl, U : mod_fl, D : del_fl, V : use_fl)
     * @return
     * @throws Exception 접근 오류 Exception 발생
     */
    public void isUserAccessValidate(HttpSession ses, String menuKey, String[] gubunKey) throws Exception {

        boolean isSuccess = false;
        String isFlag = "N";
        Map<String, String> getAccess;

        try {

            if (ses != null && menuKey != null && gubunKey != null) {

                // TODO tomcat 자체의 context session 공유 로그아웃 문제 발생에 따른 원복 ses.getServletContext().getContext(contextRoot)
                getAccess = getAuthCrud(ses.getAttribute("id").toString(), menuKey);

                if (getAccess.isEmpty() == false) {

                    for (int i = 0; i < gubunKey.length; i++) {

                        switch (gubunKey[i]) {
                            case "C":
                                isFlag = getAccess.get("REG_FL");
                                break;
                            case "R":
                                isFlag = getAccess.get("READ_FL");
                                break;
                            case "U":
                                isFlag = getAccess.get("MOD_FL");
                                break;
                            case "D":
                                isFlag = getAccess.get("DEL_FL");
                                break;
                            case "V":
                                isFlag = getAccess.get("USE_FL");
                                break;
                        }

                        if (StringUtils.equals(isFlag, "N")) {

                            isSuccess = false;
                            break;
                        }
                    }
                }

                if (StringUtils.equals(isFlag, "Y")) {

                    isSuccess = true;
                }
            }
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        } finally {

            isFlag = null;
            getAccess = null;
        }

        if (isSuccess == false) throw new Exception("접근 권한 오류 발생");
    }

    /**
     * 코드 종류 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getCodeGubunList() throws Exception {

        return cmnDropdownMapper.getCodeGubunList();
    }

    /**
     * 공통 코드 가져오기
     *
     * @param jongCD 그룹코드
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getCodeList(String jongCD) throws Exception {

        return cmnDropdownMapper.getCodeList(jongCD);
    }

    /**
     * 권한 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getAuthList() throws Exception {

        return cmnDropdownMapper.getAuthList();
    }

    /**
     * 공통 ModelObject 설정
     *
     * @param req    HttpServletRequest
     * @return Map
     * @throws Exception the exception
     */
    public Map<String, Object> setCmnModel(HttpServletRequest req) throws Exception {

        return setCmnModel(req, null);
    }

    /**
     * 공통 ModelObject 설정
     *
     * @param req    HttpServletRequest
     * @param mnu_cd 메뉴 코드
     * @return Map
     * @throws Exception the exception
     */
    public Map<String, Object> setCmnModel(HttpServletRequest req, String mnu_cd) throws Exception {

        Map<String, Object> resMap = new HashMap<String, Object>();

        try {

            String path = req.getContextPath();
            String id = req.getSession().getAttribute("id").toString();

            resMap.put("p", path);
            resMap.put("member_profile_seq", req.getSession().getAttribute("member_profile_seq")); // 헤더 - 사용자 사진 SEQ
            resMap.put("member_nm", req.getSession().getAttribute("member_nm")); // 헤더 - 사용자 이름
            resMap.put("topMenu", getTopMenu(req.getSession(), path)); // 상단메뉴 가져오기

            if (mnu_cd != null) {
                // 사용자 접속 로그 Insert
                cmnMenuMapper.setUserLogInsert(id, mnu_cd);

                resMap.put("authCrud", new ObjectMapper().writeValueAsString(getAuthCrud(id, mnu_cd))); // 사용자별 Crud 권한 가져오기
                resMap.put("leftMenu", getLeftMenu(null, req.getSession(), path, mnu_cd)); // 왼쪽메뉴 가져오기 mnu_cd.substring(2, 3)
            }

            resMap.put("sso", new ObjectMapper().writeValueAsString(
                    new HashMap<String, Object>() {
                        {
                            put("token", req.getSession().getAttribute("token"));
                        }

                        {
                            put("expireIn", req.getSession().getMaxInactiveInterval());
                        }

                        {
                            put("id", req.getSession().getAttribute("id"));
                        }
                    }
            ));
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return resMap;
    }

    /**
     * 파일 정보 가져오기
     *
     * @param realTB  실 테이블
     * @param realSEQ 실제 SEQ
     * @param gubunCD 구분자 코드
     * @return COM_FILES DB 테이블 조회 결과
     * @throws Exception the exception
     */
    public List<FilesVO> getFileList(String realTB, String realSEQ, String gubunCD) throws Exception {

        return cmnDataMapper.getFileList(realTB, realSEQ, gubunCD);
    }

    /**
     * 파일 상세 정보 가져오기
     *
     * @param key SEQ
     * @return FilesVO
     */
    public FilesVO getFileInfo(String key) throws Exception {

        return cmnDataMapper.getFileInfo(key);
    }

    /**
     * 파일 DB 추가
     *
     * @param vo FilesVO
     * @return Insert 성공 건수
     */
    public int insertFile(FilesVO vo) throws Exception {

        return cmnDataMapper.insertFile(vo);
    }

    /**
     * 파일 DB 삭제
     *
     * @param key SEQ
     * @return 삭제 성공 건수
     */
    public int deleteFile(String key) throws Exception {

        return cmnDataMapper.deleteFile(key);
    }
}
