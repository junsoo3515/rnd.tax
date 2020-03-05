package tax.www.service.security;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tax.www.dao.security.SecurityCctvMapper;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.security.CctvVO;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템관리 > 모니터링 CCTV 관리
 * <p>
 * User: 이준수
 * Date: 17. 11. 28
 * Time: 오후 4:25
 */
@Service("SecurityCctvService")
public class SecurityCctvServiceImpl extends EgovAbstractServiceImpl implements SecurityCctvService {

    /**
     * 모니터링 CCTV 관리 DAO
     */
    @Resource(name = "securityCctvMapper")
    private SecurityCctvMapper securityCctvMapper;

    /**
     * 모니터링 CCTV 총 갯수 가져오기
     *
     * @param srcWord    검색어
     * @param vo         jqGrid 파라미터
     * @return List mntrCctv list cnt
     * @throws Exception the exception
     */
    public int getCctvListCnt(String srcWord, SrcJQGridVO vo) throws Exception {

        return securityCctvMapper.getCctvListCnt(srcWord, vo);
    }

    /**
     * 모니터링 CCTV 목록 가져오기
     *
     * @param srcWord  검색어
     * @param vo       jqGrid 파라미터
     * @return List mntrCctv list
     * @throws Exception the exception
     */
    public List<CctvVO> getCctvList(String srcWord, SrcJQGridVO vo) throws Exception {

        return securityCctvMapper.getCctvList(srcWord, vo);
    }

    /**
     * 모니터링 CCTV 저장(Insert, Update)
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    @Transactional
    public Map setCctvAct(String oper, CctvVO vo) throws Exception {

        int resCnt = -1;
        int delRes = 0;
        int netSetCnt = 0;

        boolean isSuccess = false;

        Map<String, Boolean> resMap = new HashMap<String, Boolean>();

        switch (oper) {
            case "add":
                // 추가 모드
                resCnt = securityCctvMapper.setCctvInsert(vo);

                isSuccess = (resCnt > 0 ? true : false);

                break;

            case "edit":
                // 수정 모드
                netSetCnt = securityCctvMapper.getNetSettingListCnt(vo.prev_cctv_id);

                resCnt = securityCctvMapper.setCctvUpdate(vo);

                isSuccess = resCnt > 0 ? true : false;

                if(netSetCnt != 0) {
                    delRes = securityCctvMapper.setNetSettingTmpDelete(vo);
                    isSuccess = (resCnt > 0 && delRes != 0 ? true : false);
                }

                break;
        }

        resMap.put("isSuccess", isSuccess);

        return resMap;
    }

    /**
     * 모니터링 CCTV Delete
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    @Transactional
    public Map setCctvDelete(CctvVO vo) throws Exception {

        int resCnt = -1;
        int delRes = 0;
        int netSetCnt = 0;

        boolean isSuccess = false;

        Map<String, Boolean> resMap = new HashMap<>();

        netSetCnt = securityCctvMapper.getNetSettingListCnt(vo.prev_cctv_id);

        resCnt = securityCctvMapper.setCctvDelete(vo);

        isSuccess = resCnt > 0 ? true : false;

        if(netSetCnt != 0) {

            delRes = securityCctvMapper.setNetSettingTmpDelete(vo);

            isSuccess = (resCnt > 0 && delRes != 0 ? true : false);
        }

        resMap.put("isSuccess", isSuccess);

        return resMap;
    }

    /**
     * 투망감시 설정 가져오기
     *
     * @param cctv_id  모니터링 CCTV ID
     * @return List netSet list
     * @throws Exception the exception
     */
    public List<CctvVO> getNetSettingList(String cctv_id) throws Exception {

        return securityCctvMapper.getNetSettingList(cctv_id);
    }

    /**
     * CCTV ID 가져오기(Select2)
     *
     * @param word  모니터링 CCTV ID
     * @return List cctvId list
     * @throws Exception the exception
     */
    public List<ListObjVO> getCctvIdList(String word) throws Exception {

        return securityCctvMapper.getCctvIdList(word);
    }

    /**
     * Select2에서 선택한 CCTV ID에 대한 CCTV 정보 가져오기
     *
     * @param cctv_id  모니터링 CCTV ID
     * @return CctvVO
     * @throws Exception the exception
     */
    public CctvVO getCctvInfo(String cctv_id) throws Exception {

        return securityCctvMapper.getCctvInfo(cctv_id);
    }

    /**
     * 투망감시 설정 가져오기
     *
     * @return List netSet list
     * @throws Exception the exception
     */
    public List<CctvVO> getCctvGeoData() throws Exception {

        return securityCctvMapper.getCctvGeoData();
    }

    /**
     * 반경 내(500m) 모든 프리셋 GIS 정보 가져오기
     *
     * @param map  GIS에서 선택한 cctv 정보
     * @return List allPresetGeoData list
     * @throws Exception the exception
     */
    public List<CctvVO> getAllPresetGeoData(Map<String, Object> map) throws Exception {

        return securityCctvMapper.getAllPresetGeoData(map);
    }

    /**
     * 투망감시 설정된 프리셋 GIS 정보 가져오기
     *
     * @param cctv_id  모니터링 CCTV ID
     * @return List presetGeoData list
     * @throws Exception the exception
     */
    public List<CctvVO> getPresetGeoData(String cctv_id) throws Exception {

        return securityCctvMapper.getPresetGeoData(cctv_id);
    }

    /**
     * 투망감시 설정 Insert
     *
     * @param vo  CctvVO
     * @return List netSet list
     * @throws Exception the exception
     */
    @Transactional
    public Map setNetSettingAct(CctvVO vo) throws Exception {

        int resCnt = -1;
        int delRes = 0;

        boolean isSuccess = false;

        Map<String, Boolean> resMap = new HashMap<>();

        delRes = securityCctvMapper.setNetSettingTmpDelete(vo); // 투망감시 설정 임시 Delete
        resCnt = securityCctvMapper.setNetSettingInsert(vo);

        isSuccess = (resCnt > 0 && delRes != 0 ? true : false);

        resMap.put("isSuccess", isSuccess);

        return resMap;
    }

    /**
     * 투망감시 설정 완전 Delete
     *
     * @param vo CctvVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public Map setNetSettingDelete(CctvVO vo) throws Exception {

        int resCnt = 0;

        boolean isSuccess = false;

        Map<String, Boolean> resMap = new HashMap<>();

        resCnt = securityCctvMapper.setNetSettingDelete(vo);

        isSuccess = (resCnt > 0 ? true : false);

        resMap.put("isSuccess", isSuccess);


        return resMap;
    }
}
