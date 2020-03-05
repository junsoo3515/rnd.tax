package tax.www.service.security;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tax.www.dao.security.SecuritySmsconfigMapper;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.SmsconfigVO;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SMS 연락처 관리 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 17. 11. 06
 * Time: 오후 4:45
 */
@Service("SecuritySmsconfigService")
public class SecuritySmsconfigServiceImpl extends EgovAbstractServiceImpl implements SecuritySmsconfigService {

    /**
     * SMS 연락처 관리 DAO
     */
    @Resource(name = "securitySmsconfigMapper")
    private SecuritySmsconfigMapper securitySmsconfigMapper;

    /**
     * 그룹 목록 총 갯수 가져오기
     *
     * @param srcContactGrp 그룹명
     * @param vo      jqGrid 파라미터
     * @return List contactGrp list cnt
     * @throws Exception the exception
     */
    public int getContactGrpListCnt(String srcContactGrp, SrcJQGridVO vo) throws Exception {

        return securitySmsconfigMapper.getContactGrpListCnt(srcContactGrp, vo);
    }

    /**
     * 그룹 목록 가져오기
     *
     * @param srcContactGrp 그룹명
     * @param vo      jqGrid 파라미터
     * @return List contactGrp list
     * @throws Exception the exception
     */
    public List<SmsconfigVO> getContactGrpList(String srcContactGrp, SrcJQGridVO vo) throws Exception {

        return securitySmsconfigMapper.getContactGrpList(srcContactGrp, vo);
    }

    /**
     * 그룹 목록 저장(Insert, Update)
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public Map<String,Boolean> setContactGrpAct(String oper, SmsconfigVO vo) throws Exception {

        int resCnt = 0;

        boolean isSuccess = false;

        Map<String, Boolean> resMap = new HashMap<String, Boolean>();

        switch (oper) {

            case "add" :
                // 그룹 목록 추가 모드
                resCnt = securitySmsconfigMapper.setContactGrpInsert(vo);

                break;

            case "edit" :
                // 그룹 목록 수정 모드
                resCnt = securitySmsconfigMapper.setContactGrpUpdate(vo);

                break;
        }

        isSuccess = (resCnt > 0 ? true: false);

        resMap.put("isSuccess", isSuccess);

        return resMap;
    }

    /**
     * 그룹 목록 Delete
     *
     * @param vo SmsconfigVO
     * @return 쿼리결과
     * @throws Exception the exception
     */
    public Map setContactGrpDelete(SmsconfigVO vo) throws Exception {

        int resCnt = 0;

        boolean isSuccess = false;

        Map<String, Boolean> resMap = new HashMap<String, Boolean>();

        resCnt = securitySmsconfigMapper.setContactGrpDelete(vo);

        isSuccess = (resCnt > 0 ? true : false);

        resMap.put("isSuccess", isSuccess);

        return resMap;
    }

    /**
     * 연락처 목록 가져오기
     *
     * @param srcContact 연락처
     * @param tcg_seq    그룹 일련번호
     * @param vo         jqGrid 파라미터
     * @return List contact list
     * @throws Exception the exception
     */
    public List<SmsconfigVO> getContactList(String srcContact, String tcg_seq, SrcJQGridVO vo) throws Exception {

        return securitySmsconfigMapper.getContactList(srcContact, tcg_seq, vo);
    }

    /**
     * 연락처 목록 저장(담당자 관리 테이블 Insert, Update)
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    @Transactional
    public Map setContactAct(String oper, SmsconfigVO vo) throws Exception {

        int resCnt = 0;

        boolean isSuccess = false;

        Map<String, Boolean> resMap = new HashMap<String, Boolean>();

        switch (oper) {

            case "add":

                // 연락처 추가 모드
                resCnt = securitySmsconfigMapper.setManagerInsert(vo);
                resCnt += securitySmsconfigMapper.setContactInsert(vo);

                break;

            case "edit":

                // 연락처 수정 모드
                resCnt = securitySmsconfigMapper.setManagerUpdate(vo);
                resCnt += securitySmsconfigMapper.setContactUpdate(vo);

                break;
        }

        isSuccess = (resCnt > 1 ? true : false);

        resMap.put("isSuccess", isSuccess);

        return resMap;
    }

    /**
     * 연락처 목록 Delete
     *
     * @param vo SmsconfigVO
     * @return 쿼리결과
     * @throws Exception the exception
     */
    public Map setContactDelete(SmsconfigVO vo) throws Exception {

        int resCnt = 0;

        boolean isSuccess = false;

        Map<String, Boolean> resMap = new HashMap<String, Boolean>();

        resCnt = securitySmsconfigMapper.setContactDelete(vo);

        isSuccess = (resCnt > 0 ? true : false);

        resMap.put("isSuccess", isSuccess);

        return resMap;
    }

    /**
     * 담당자 관리 테이블 데이터 가져오기
     *
     * @return List
     * @throws Exception the exception
     */
    public List<SmsconfigVO> chkDupContact() throws Exception {

        return securitySmsconfigMapper.chkDupContact();
    }
}
