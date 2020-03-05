package tax.www.dao.cmn;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.list.ListObjVO;

import java.util.List;

/**
 * DropDown 컴포넌트에서 사용하기 위한 데이터 유형 가져오기
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 5:16
 */
@Mapper("cmnDropdownMapper")
public interface CmnDropdownMapper {

    /**
     * 코드 종류 가져오기
     *
     * @return List<ListObjVO>
     */
    public List<ListObjVO> getCodeGubunList();

    /**
     * 공통코드 가져오기
     *
     * @param jongCD 그룹 코드
     * @return List<ListObjVO>   List
     */
    public List<ListObjVO> getCodeList(@Param("jongCD") String jongCD);

    /**
     * 권한 가져오기
     *
     * @return List<ListObjVO>
     */
    public List<ListObjVO> getAuthList();
}
