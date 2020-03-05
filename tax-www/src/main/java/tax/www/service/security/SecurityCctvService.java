package tax.www.service.security;

import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.security.CctvVO;

import java.util.List;
import java.util.Map;

/**
 * 모니터링 CCTV 관리 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 17. 11. 28
 * Time: 오후 4:12
 */
public interface SecurityCctvService {

    /**
     * 모니터링 CCTV 총 갯수 가져오기
     *
     * @param srcWord    검색어
     * @param vo         jqGrid 파라미터
     * @return List mntrCctv list cnt
     * @throws Exception the exception
     */
    public int getCctvListCnt(String srcWord, SrcJQGridVO vo) throws Exception;

    /**
     * 모니터링 CCTV 목록 가져오기
     *
     * @param srcWord  검색어
     * @param vo       jqGrid 파라미터
     * @return List mntrCctv list
     * @throws Exception the exception
     */
    public List<CctvVO> getCctvList(String srcWord, SrcJQGridVO vo) throws  Exception;


    /**
     * 모니터링 CCTV 저장(Insert, Update)
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public Map setCctvAct(String oper, CctvVO vo) throws Exception;

    /**
     * 모니터링 CCTV Delete
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public Map setCctvDelete(CctvVO vo) throws  Exception;

    /**
     * 투망감시 설정 가져오기
     *
     * @param cctv_id  모니터링 CCTV ID
     * @return List netSet list
     * @throws Exception the exception
     */
    public List<CctvVO> getNetSettingList(String cctv_id) throws Exception;

    /**
     * CCTV ID 가져오기(Select2)
     *
     * @param word  모니터링 CCTV ID
     * @return List cctvId list
     * @throws Exception the exception
     */
    public List<ListObjVO> getCctvIdList(String word) throws Exception;

    /**
     * Select2에서 선택한 CCTV ID에 대한 CCTV 정보 가져오기
     *
     * @param cctv_id  모니터링 CCTV ID
     * @return CctvVO
     * @throws Exception the exception
     */
    public CctvVO getCctvInfo(String cctv_id) throws Exception;

    /**
     * CCTV GIS 정보 가져오기
     *
     * @return List cctvGeoData list
     * @throws Exception the exception
     */
    public List<CctvVO> getCctvGeoData() throws Exception;

    /**
     * 반경 내(500m) 모든 프리셋 GIS 정보 가져오기
     *
     * @param map  GIS에서 선택한 cctv 정보
     * @return List allPresetGeoData list
     * @throws Exception the exception
     */
    public List<CctvVO> getAllPresetGeoData(Map<String, Object> map) throws Exception;

    /**
     * 투망감시 설정된 프리셋 GIS 정보 가져오기
     *
     * @param cctv_id  모니터링 CCTV ID
     * @return List presetGeoData list
     * @throws Exception the exception
     */
    public List<CctvVO> getPresetGeoData(String cctv_id) throws Exception;

    /**
     * 투망감시 설정 Insert
     *
     * @param vo  CctvVO
     * @return List netSet list
     * @throws Exception the exception
     */
    public Map setNetSettingAct(CctvVO vo) throws Exception;

    /**
     * 투망감시 설정 완전 Delete
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public Map setNetSettingDelete(CctvVO vo) throws Exception;
}
