package tax.www.service.security;

import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.security.FilecrollingVO;

import java.util.List;

/**
 * 파일수신현황 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 17. 11. 06
 * Time: 오후 2:19
 */
public interface SecurityFilecrollingService {

    /**
     * 파일수신현황 종류 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getFileCltJongList() throws Exception;

    /**
     * 파일수신현황 목록 총 갯수 가져오기
     *
     * @param srcFileCltJong 파일수신현황 종류
     * @param srcSDate       시작일
     * @param srcEDate       종료일
     * @param vo             jqGrid 파라미터
     * @return List fileCltRes list cnt
     * @throws Exception the exception
     */
    public int getFileCltResListCnt(String srcFileCltJong, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;

    /**
     * 파일수신현황 목록 가져오기
     *
     * @param srcFileCltJong 파일수신현황 종류
     * @param srcSDate       시작일
     * @param srcEDate       종료일
     * @param vo             jqGrid 파라미터
     * @return List fileCltRes list
     * @throws Exception the exception
     */
    public List<FilecrollingVO> getFileCltResList(String srcFileCltJong, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception;

    /**
     * 오류 세부 목록 총 갯수 가져오기
     *
     * @param tfcr_seq 파일수집결과 일련번호
     * @param vo       jqGrid 파라미터
     * @return List fileCltErr list cnt
     * @throws Exception the exception
     */
    public int getFileCltErrListCnt(String tfcr_seq, SrcJQGridVO vo) throws Exception;

    /**
     * 오류 세부 목록 가져오기
     *
     * @param tfcr_seq 파일수집결과 일련번호
     * @param vo       jqGrid 파라미터
     * @return List fileCltErr list
     * @throws Exception the exception
     */
    public List<FilecrollingVO> getFileCltErrList(String tfcr_seq, SrcJQGridVO vo) throws Exception;
}
