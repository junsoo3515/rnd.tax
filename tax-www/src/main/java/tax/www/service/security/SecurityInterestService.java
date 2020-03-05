package tax.www.service.security;

import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.security.InterestVO;

import java.util.List;

/**
 * 관심차량관리 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 18. 01. 08
 * Time: 오후 3:15
 */
public interface SecurityInterestService {

    /**
     *  관심차량 목록 총 갯수 가져오기
     *
     * @param srcCarNo   차량번호
     * @param vo         jqGrid 파라미터
     * @return List user list cnt
     * @throws Exception the exception
     */
    public int getInterestListCnt(String srcCarNo, SrcJQGridVO vo) throws Exception;

    /**
     * 관심차량 목록 가져오기
     *
     * @param srcCarNo  차량번호
     * @param vo        jqGrid 파라미터
     * @return List user list
     * @throws Exception the exception
     */
    public List<InterestVO> getInterestList(String srcCarNo, SrcJQGridVO vo) throws Exception;

    /**
     * 관심차량 Insert
     *
     * @param vo InterestVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setInterestInsert(InterestVO vo) throws Exception;

    /**
     * 관심차량 Update
     *
     * @param vo InterestVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setInterestUpdate(InterestVO vo) throws Exception;

    /**
     * 관심차량 Delete
     *
     * @param vo InterestVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public void setInterestDelete(InterestVO vo) throws Exception;
}
