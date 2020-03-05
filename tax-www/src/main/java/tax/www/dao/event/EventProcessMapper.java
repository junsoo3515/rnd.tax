package tax.www.dao.event;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.files.FilesVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.event.ProcessVO;

import java.util.List;

/**
 * 상황처리 관리 DAO
 * <p>
 * User: 이준수
 * Date: 17. 11. 07
 * Time: 오전 9:40
 */
@Mapper("eventProcessMapper")
public interface EventProcessMapper {

    /**
     * 발생유형 종류 가져오기
     *
     * @return 발생유형 종류
     */
    public List<ListObjVO> getMakeTypeList();

    /**
     * 담당자 가져오기
     *
     * @return 담당자
     */
    public List<ListObjVO> getManagerList();

    /**
     * 상황처리 목록 건수 가져오기
     *
     * @return 상황 상태 별 건수
     */
    public ProcessVO getSitPcsDataCnt(@Param("vo")ProcessVO vo);

    /**
     * 상황처리 목록 총 갯수 가져오기
     *
     * @param srcMakeType 발생유형
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return 총 갯수
     */
    public int getSitPcsListCnt(@Param("srcMakeType") String srcMakeType, @Param("sit_type") String sit_type, @Param("srcSDate") String srcSDate, @Param("srcEDate")String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * 상황처리 목록 가져오기
     *
     * @param srcMakeType 발생유형
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List
     */
    public List<ProcessVO> getSitPcsList(@Param("srcMakeType") String srcMakeType, @Param("sit_type") String sit_type, @Param("srcSDate") String srcSDate, @Param("srcEDate")String srcEDate, @Param("vo") SrcJQGridVO vo);

    /**
     * 상세정보 가져오기
     *
     * @param vo ProcessVO
     * @return ProcessVO
     */
    public ProcessVO getDetailInfoData(@Param("vo") ProcessVO vo);

    /**
     * 상세정보 적용
     *
     * @param vo ProcessVO
     * @return 쿼리 결과
     */
    public int setDetailInfoUpdate(@Param("vo") ProcessVO vo);

    /**
     * 영치 등록 시 체납처리 이력 테이블(TAX_PRC_HISTORY) 상세정보 등록
     *
     * @param vo ProcessVO
     * @return 쿼리 결과
     */
    public int setDetailInfoInsert(@Param("vo") ProcessVO vo);

    /**
     * 상황전파 목록 가져오기
     *
     * @param tri_seq 차량인식 일련번호
     * @return List
     */
    public List<ProcessVO> getPcsSitPpgList(@Param("tri_seq") String tri_seq);

    /**
     * 체납정보 목록 가져오기
     *
     * @param make_type_cd 발생유형
     * @param car_no      차량번호
     * @param vo          jqGrid 파라미터
     * @return List
     */
    public List<ProcessVO> getTaxInfoList(@Param("make_type_cd") String make_type_cd, @Param("car_no") String car_no, @Param("vo") SrcJQGridVO vo);

    /**
     * 담당자 중복 확인을 위한 전체 담당자 가져오기
     *
     * @return List
     */
    public List<ProcessVO> getDupChkManagerList();

    /**
     * 담당자 직접 입력 시 담당자 Insert
     *
     * @param vo ProcessVO0
     * @return 쿼리 결과
     */
    public int setManagerInsert(@Param("vo") ProcessVO vo);

    /**
     * 체납차량 발견위치 목록 가져오기
     *
     * @param car_no      차량번호
     * @param vo          jqGrid 파라미터
     * @return List
     */
    public List<ProcessVO> getTaxCarFindLocList(@Param("car_no") String car_no, @Param("vo") SrcJQGridVO vo);

    /**
     * 체납차량 사진 가져오기
     *
     * @param realTB      실 테이블
     * @param car_no      차량번호
     * @param gubunCD     구분자 코드
     * @return List
     */
    public List<FilesVO> getPhotoList(@Param("realTB") String realTB, @Param("car_no") String car_no, @Param("gubunCD") String gubunCD);
}
