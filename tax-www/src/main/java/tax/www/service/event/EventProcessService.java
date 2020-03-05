package tax.www.service.event;

import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.event.ProcessVO;

import java.util.List;
import java.util.Map;

/**
 * 상황처리 관리 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 17. 11. 03
 * Time: 오후 6:17
 */
public interface EventProcessService {

    /**
     * 발생유형 종류 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getMakeTypeList() throws Exception;

    /**
     * 담당자 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getManagerList() throws Exception;

    /**
     * 상황처리 목록 건수 가져오기
     *
     * @return ProcessVO
     * @throws Exception the exception
     */
    public ProcessVO getSitPcsDataCnt(ProcessVO vo);

    /**
     * 상황처리 목록 총 갯수 가져오기
     *
     * @param srcMakeType 발생유형
     * @param sit_type    탭 유형
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List sitPcs list cnt
     * @throws Exception the exception
     */
    public int getSitPcsListCnt(String srcMakeType, String sit_type, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;

    /**
     * 상황처리 목록 가져오기
     *
     * @param srcMakeType 발생유형
     * @param sit_type    탭 유형
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List sitPcs list
     * @throws Exception the exception
     */
    public List<ProcessVO> getSitPcsList(String srcMakeType, String sit_type, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;

    /**
     * 상세정보 가져오기
     *
     * @param vo ProcessVO
     * @return ProcessVO
     * @throws Exception the exception
     */
    public ProcessVO getDetailInfoData(ProcessVO vo) throws Exception;

    /**
     * 상세정보 가져오기
     *
     * @param vo ProcessVO
     * @return ProcessVO
     * @throws Exception the exception
     */
    public int setDetailInfoAct(ProcessVO vo) throws Exception;

    /**
     * 상황전파 목록 가져오기
     *
     * @param tri_seq 차량인식 일련번호
     * @return List sitPpg list
     * @throws Exception the exception
     */
    public List<ProcessVO> getPcsSitPpgList(String tri_seq) throws Exception;

    /**
     * 체납정보 목록 가져오기
     *
     * @param make_type_cd 발생유형
     * @param car_no      차량번호
     * @param vo          jqGrid 파라미터
     * @return List taxInfo list
     * @throws Exception the exception
     */
    public List<ProcessVO> getTaxInfoList(String make_type_cd, String car_no, SrcJQGridVO vo) throws Exception;

    /**
     * 담당자 중복 확인을 위한 전체 담당자 가져오기
     *
     * @return List manager list
     * @throws Exception the exception
     */
    public List<ProcessVO> getDupChkManagerList() throws Exception;

    /**
     * 담당자 추가
     *
     * @return 쿼리결과
     * @throws Exception the exception
     */
    public Map setManagerInsert(ProcessVO vo) throws Exception;

    /**
     * 상황처리 목록 가져오기
     *
     * @param car_no    차량번호
     * @param vo          jqGrid 파라미터
     * @return List sitPcs list
     * @throws Exception the exception
     */
    public List<ProcessVO> getTaxCarFindLocList(String car_no, SrcJQGridVO vo) throws Exception;
}
