package tax.www.service.security;

import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.security.ConfigVO;

import java.util.List;

/**
 * 체납차량 판별조건 관리 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 18. 01. 08
 * Time: 오후 5:14
 */
public interface SecurityConfigService {

    /**
     * 판별조건 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getDisConList() throws Exception;

    /**
     * 체납차량 판별조건 목록 총 갯수 가져오기
     *
     * @param srcDis     판별조건
     * @param vo         jqGrid 파라미터
     * @return List user list cnt
     * @throws Exception the exception
     */
    public int getConfigListCnt(String srcDis, SrcJQGridVO vo) throws Exception;

    /**
     * 체납차량 판별조건 목록 가져오기
     *
     * @param srcDis    판별조건
     * @param vo        jqGrid 파라미터
     * @return List user list
     * @throws Exception the exception
     */
    public List<ConfigVO> getConfigList(String srcDis, SrcJQGridVO vo) throws Exception;

    /**
     * 체납차량 판별조건 Insert
     *
     * @param vo ConfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setConfigInsert(ConfigVO vo) throws Exception;

    /**
     * 체납차량 판별조건 Update
     *
     * @param vo ConfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public int setConfigUpdate(ConfigVO vo) throws Exception;

    /**
     * 체납차량 판별조건 Delete
     *
     * @param vo ConfigVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    public void setConfigDelete(ConfigVO vo) throws Exception;
}
