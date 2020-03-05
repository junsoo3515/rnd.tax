package tax.www.service.event;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tax.www.dao.cmn.CmnDataMapper;
import tax.www.dao.event.EventProcessMapper;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.event.ProcessVO;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 상황처리 관리 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 17. 11. 07
 * Time: 오전 9:36
 */
@Service("EventProcessService")
public class EventProcessServiceImpl extends EgovAbstractServiceImpl implements EventProcessService{

    /**
     * 상황처리 관리 DAO
     */
    @Resource(name = "eventProcessMapper")
    private EventProcessMapper eventProcessMapper;

    @Resource(name = "cmnDataMapper")
    private CmnDataMapper cmnDataMapper;

    /**
     * 발생유형 종류 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getMakeTypeList() throws Exception {

        return eventProcessMapper.getMakeTypeList();
    }

    /**
     * 담당자 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getManagerList() throws Exception {

        return eventProcessMapper.getManagerList();
    }

    /**
     * 상황처리 목록 건수 가져오기
     *
     * @return ProcessVO
     * @throws Exception the exception
     */
    public ProcessVO getSitPcsDataCnt(ProcessVO vo) {

        return eventProcessMapper.getSitPcsDataCnt(vo);
    }

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
    public int getSitPcsListCnt(String srcMakeType, String sit_type, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {

        return eventProcessMapper.getSitPcsListCnt(srcMakeType, sit_type, srcSDate, srcEDate, vo);
    }

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
    public List<ProcessVO> getSitPcsList(String srcMakeType, String sit_type, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {

        return eventProcessMapper.getSitPcsList(srcMakeType, sit_type, srcSDate, srcEDate, vo);
    }

    /**
     * 상세정보 가져오기
     *
     * @param vo ProcessVO
     * @return ProcessVO
     * @throws Exception the exception
     */
    public ProcessVO getDetailInfoData(ProcessVO vo) throws Exception {

        ProcessVO processVO = new ProcessVO();

        if(!vo.state.equals("발생")) {

            processVO = eventProcessMapper.getDetailInfoData(vo);
        }
        processVO.files = eventProcessMapper.getPhotoList("TAX_RECOG_INFO", vo.car_no, "B");

        return processVO;
    }

    /**
     * 상세정보 적용
     *
     * @param vo ProcessVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    @Transactional
    public int setDetailInfoAct(ProcessVO vo) throws Exception {

        int resCnt = 0;

        resCnt = eventProcessMapper.setDetailInfoUpdate(vo);
        resCnt += eventProcessMapper.setDetailInfoInsert(vo); // 영치 등록 시 체납처리 이력 테이블(TAX_PRC_HISTORY) 상세정보 등록

        return resCnt;
    }

    /**
     * 상황전파 목록 가져오기
     *
     * @param tri_seq 차량인식 일련번호
     * @return List sitPpg list
     * @throws Exception the exception
     */
    public List<ProcessVO> getPcsSitPpgList(String tri_seq) throws Exception {

        return eventProcessMapper.getPcsSitPpgList(tri_seq);
    }

    /**
     * 체납정보 목록 가져오기
     *
     * @param make_type_cd 발생유형
     * @param car_no    시작일
     * @param vo          jqGrid 파라미터
     * @return List taxInfo list
     * @throws Exception the exception
     */
    public List<ProcessVO> getTaxInfoList(String make_type_cd, String car_no, SrcJQGridVO vo) throws Exception {

        return eventProcessMapper.getTaxInfoList(make_type_cd, car_no, vo);
    }

    /**
     * 담당자 중복 확인을 위한 전체 담당자 가져오기
     *
     * @return List manager list
     * @throws Exception the exception
     */
    public List<ProcessVO> getDupChkManagerList() throws Exception {

        return eventProcessMapper.getDupChkManagerList();
    }

    /**
     * 담당자 추가
     *
     * @return 쿼리결과
     * @throws Exception the exception
     */
    @Transactional
    public Map setManagerInsert(ProcessVO vo) throws Exception {

        int resCnt = 0;
        long seq = 0;
        int tmpOk = 0;

        Map<String, Object> map = new HashMap<>();

        resCnt = eventProcessMapper.setManagerInsert(vo);
        seq = cmnDataMapper.getTableMaxSeq("COM_MANAGER","cm_seq", 0, null); // 특정 테이블의 최대 고유번호 가져오기

        if(resCnt > 0 && seq > 0) {

            tmpOk = 1;

            map.put("resCnt", resCnt);
            map.put("seq", seq);
        }

        map.put("tmpOk", tmpOk);

        return map;
    }

    /**
     * 체납차량 발견위치 목록 가져오기
     *
     * @param car_no      차량번호
     * @param vo          jqGrid 파라미터
     * @return List taxInfo list
     * @throws Exception the exception
     */
    public List<ProcessVO> getTaxCarFindLocList(String car_no, SrcJQGridVO vo) throws Exception {

        return eventProcessMapper.getTaxCarFindLocList(car_no, vo);
    }
}
