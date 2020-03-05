package tax.www.dao.security;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.security.CctvVO;

import java.util.List;
import java.util.Map;

/**
 * 모니터링 CCTV 관리 DAO
 * <p>
 * User: 이준수
 * Date: 17. 11. 28
 * Time: 오후 4:26
 */
@Mapper("securityCctvMapper")
public interface SecurityCctvMapper {

    /**
     * 모니터링 CCTV 총 개수 가져오기
     *
     * @param srcWord    검색어
     * @param vo         jqGrid 파라미터
     * @return int 총 개수
     */
    public int getCctvListCnt(@Param("srcWord") String srcWord, @Param("vo") SrcJQGridVO vo);

    /**
     * 모니터링 CCTV 목록 가져오기
     *
     * @param srcWord    검색어
     * @param vo         jqGrid 파라미터
     * @return List<CctvVO> 모니터링 CCTV 목록
     */
    public List<CctvVO> getCctvList(@Param("srcWord") String srcWord, @Param("vo") SrcJQGridVO vo);

    /**
     * 모니터링 CCTV Insert
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     */
    public int setCctvInsert(@Param("vo") CctvVO vo);

    /**
     * 모니터링 CCTV Update
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     */
    public int setCctvUpdate(@Param("vo") CctvVO vo);

    /**
     * 모니터링 CCTV Delete
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     */
    public int setCctvDelete(@Param("vo") CctvVO vo);

    /**
     * 투망감시 설정 가져오기
     *
     * @param cctv_id  모니터링 CCTV ID
     * @return List netSet list
     */
    public List<CctvVO> getNetSettingList(@Param("cctv_id") String cctv_id);

    /**
     * 투망감시 설정 갯수 가져오기
     *
     * @param cctv_id  모니터링 CCTV ID
     * @return List netSet list cnt
     */
    public int getNetSettingListCnt(@Param("cctv_id") String cctv_id);


    /**
     * CCTV ID 가져오기(Select2)
     *
     * @param word  모니터링 CCTV ID
     * @return List<ListObjVO> CCTV ID
     */
    public List<ListObjVO> getCctvIdList(@Param("word") String word);

    /**
     * Select2에서 선택한 CCTV ID에 대한 CCTV 정보 가져오기
     *
     * @param cctv_id  모니터링 CCTV ID
     * @return CctvVO
     */
    public CctvVO getCctvInfo(@Param("cctv_id") String cctv_id);

    /**
     * CCTV GIS 정보 가져오기
     *
     * @return List cctvGeoData list
     */
    public List<CctvVO> getCctvGeoData();

    /**
     * 반경 내(500m) 모든 프리셋 GIS 정보 가져오기
     *
     * @return List allPresetGeoData list
     */
    public List<CctvVO> getAllPresetGeoData(@Param("map") Map<String, Object> map);

    /**
     * 투망감시 설정된 프리셋 GIS 정보 가져오기
     *
     * @param cctv_id  모니터링 CCTV ID
     * @return List presetGeoData list
     */
    public List<CctvVO> getPresetGeoData(@Param("cctv_id") String cctv_id);

    /**
     * 투망감시 설정 Insert
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     */
    public int setNetSettingInsert(@Param("vo") CctvVO vo);

    /**
     * 투망감시 설정 임시 Delete
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     */
    public int setNetSettingTmpDelete(@Param("vo") CctvVO vo);

    /**
     * 투망감시 설정 완전 Delete
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     */
    public int setNetSettingDelete(@Param("vo") CctvVO vo);
}
