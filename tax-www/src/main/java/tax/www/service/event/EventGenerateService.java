package tax.www.service.event;

import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.event.GenerateVO;

import java.sql.SQLClientInfoException;
import java.util.List;

/**
 * 상황발생 이력조회 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 17. 11. 07
 * Time: 오후 1:42
 */
public interface EventGenerateService {

    /**
     * 체납차량 발생현황 목록 총 갯수 가져오기
     *
     * @param srcCarNo    차량번호
     * @param srcCctvInfo CCTV정보
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List taxOcr list cnt
     * @throws Exception the exception
     */
    public int getTaxOcrListCnt(String srcCarNo, String srcCctvInfo, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;

    /**
     * 체납차량 발생현황 목록 가져오기
     *
     * @param srcCarNo    차량번호
     * @param srcCctvInfo CCTV정보
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List taxOcr list
     * @throws Exception the exception
     */
    public List<GenerateVO> getTaxOcrList(String srcCarNo, String srcCctvInfo, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;

    /**
     * 시간별 발생현황 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List timeTaxOcr list
     * @throws Exception the exception
     */
    public List<GenerateVO> getTimeTaxOcrList(String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;

    /**
     * 지역별 발생현황 Top5 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List areaOcrTop list
     * @throws Exception the exception
     */
    public List<GenerateVO> getAreaOcrTopList(String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;

    /**
     * CCTV별 발생현황 Top5 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List cctvOcrTop list
     * @throws Exception the exception
     */
    public List<GenerateVO> getCctvOcrTopList(String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;
}
