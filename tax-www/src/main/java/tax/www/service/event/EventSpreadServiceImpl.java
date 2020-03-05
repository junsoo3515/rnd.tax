package tax.www.service.event;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import tax.www.dao.event.EventSpreadMapper;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.event.SpreadVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 상황전파 이력조회 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 17. 11. 07
 * Time: 오후 12:51
 */
@Service("EventSpreadService")
public class EventSpreadServiceImpl extends EgovAbstractServiceImpl implements EventSpreadService {

    /**
     * 상황전파 이력조회 DAO
     */
    @Resource(name = "eventSpreadMapper")
    private EventSpreadMapper eventSpreadMapper;

    /**
     * 전파그룹 종류 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getTcgJongList() throws Exception {

        return eventSpreadMapper.getTcgJongList();
    }

    /**
     * 상황전파 이력관리 목록 총 갯수 가져오기
     *
     * @param srcTcgJong 전파그룹
     * @param srcSDate   시작일
     * @param srcEDate   종료일
     * @param vo         jqGrid 파라미터
     * @return List sitPpg list cnt
     * @throws Exception the exception
     */
    public int getSitPpgListCnt(String srcTcgJong, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {
        
        return eventSpreadMapper.getSitPpgListCnt(srcTcgJong, srcSDate, srcEDate, vo);
    }

    /**
     * 상황전파 이력관리 목록 가져오기
     *
     * @param srcTcgJong 전파그룹
     * @param srcSDate   시작일
     * @param srcEDate   종료일
     * @param vo         jqGrid 파라미터
     * @return List sitPpg list
     * @throws Exception the exception
     */
    public List<SpreadVO> getSitPpgList(String srcTcgJong, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {

        return eventSpreadMapper.getSitPpgList(srcTcgJong, srcSDate, srcEDate, vo);
    }
}
