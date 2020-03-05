package tax.www.service.security;

import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.SmsconfigVO;

import java.util.List;
import java.util.Map;

/**
 * SMS 연락처 관리 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 17. 11. 06
 * Time: 오후 3:49
 */
public interface SecuritySmsconfigService {

    /**
     * 그룹 목록 총 갯수 가져오기
     *
     * @param srcContactGrp 그룹명
     * @param vo            jqGrid 파라미터
     * @return List contactGrp list cnt
     * @throws Exception the exception
     */
    public int getContactGrpListCnt(String srcContactGrp, SrcJQGridVO vo) throws Exception;

    /**
     * 그룹 목록 가져오기
     *
     * @param srcContactGrp 그룹명
     * @param vo            jqGrid 파라미터
     * @return List contactGrp list
     * @throws Exception the exception
     */
    public List<SmsconfigVO> getContactGrpList(String srcContactGrp, SrcJQGridVO vo) throws  Exception;

    /**
     * 그룹 목록 저장(Insert, Update)
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public Map<String,Boolean> setContactGrpAct(String oper, SmsconfigVO vo) throws Exception;

    /**
     * 그룹 목록 Delete
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public Map setContactGrpDelete(SmsconfigVO vo) throws Exception;

    /**
     * 연락처 목록 가져오기
     *
     * @param srcContact 연락처
     * @param tcg_seq    그룹 일련번호
     * @param vo         jqGrid 파라미터
     * @return List contact list
     * @throws Exception the exception
     */
    public List<SmsconfigVO> getContactList(String srcContact, String tcg_seq, SrcJQGridVO vo) throws Exception;

    /**
     * 연락처 목록 저장(담당자 관리 테이블 Insert, Update)
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public Map setContactAct(String oper, SmsconfigVO vo) throws Exception;

    /**
     * 연락처 목록 Delete
     *
     * @param vo SmsconfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public Map setContactDelete(SmsconfigVO vo) throws Exception;

    /**
     * 담당자 관리 테이블 데이터 가져오기
     *
     * @return List
     * @throws Exception the exception
     */
    public List<SmsconfigVO> chkDupContact() throws Exception;
}
