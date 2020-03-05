package tax.www.service.security;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import tax.www.dao.security.SecurityConfigMapper;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.security.ConfigVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 시스템관리 > 체납차량 판별조건 관리
 * <p>
 * User: 이준수
 * Date: 18. 01. 08
 * Time: 오후 5:30
 */
@Service("SecurityConfigService")
public class SecurityConfigServiceImpl extends EgovAbstractServiceImpl implements SecurityConfigService {

    /**
     * 체납차량 판별조건 관리 DAO
     */
    @Resource(name = "securityConfigMapper")
    private SecurityConfigMapper securityConfigMapper;

    /**
     * 판별조건 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getDisConList() throws Exception {

        return securityConfigMapper.getDisConList();
    }

    /**
     * 체납차량 판별조건 목록 총 갯수 가져오기
     *
     * @param srcDis     판별조건
     * @param vo         jqGrid 파라미터
     * @return List user list cnt
     * @throws Exception the exception
     */
    public int getConfigListCnt(String srcDis, SrcJQGridVO vo) throws Exception {

        return securityConfigMapper.getConfigListCnt(srcDis, vo);
    }

    /**
     * 체납차량 판별조건 목록 가져오기
     *
     * @param srcDis    판별조건
     * @param vo        jqGrid 파라미터
     * @return List user list
     * @throws Exception the exception
     */
    public List<ConfigVO> getConfigList(String srcDis, SrcJQGridVO vo) throws Exception {

        return securityConfigMapper.getConfigList(srcDis, vo);
    }

    /**
     * 체납차량 판별조건 Insert
     *
     * @param vo ConfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setConfigInsert(ConfigVO vo) throws Exception {

        return securityConfigMapper.setConfigInsert(vo);
    }

    /**
     * 체납차량 판별조건 Update
     *
     * @param vo ConfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setConfigUpdate(ConfigVO vo) throws Exception {

        return securityConfigMapper.setConfigUpdate(vo);
    }

    /**
     * 체납차량 판별조건 Delete
     *
     * @param vo ConfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public void setConfigDelete(ConfigVO vo) throws Exception {

        securityConfigMapper.setConfigDelete(vo);
    }
}
