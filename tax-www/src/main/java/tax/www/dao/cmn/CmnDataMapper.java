package tax.www.dao.cmn;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import tax.www.vo.cmn.files.FilesVO;

import java.util.List;

/**
 * 시스템 공통 DAO
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 5:15
 */
@Mapper("cmnDataMapper")
public interface CmnDataMapper {

    /**
     * 특정 테이블의 최대 고유번호 가져오기
     *
     * @param tb     테이블
     * @param col    고유 칼럼
     * @param addCnt 최대 + a
     * @param where  검색조건 쿼리문
     * @return 고유번호
     */
    public long getTableMaxSeq(@Param("tb") String tb, @Param("col") String col, @Param("addCnt") Integer addCnt, @Param("where") String where);

    /**
     * 파일 정보 가져오기
     *
     * @param realTB  실 테이블
     * @param realSEQ 실제 SEQ
     * @param gubunCD 구분자 코드
     * @return COM_FILES DB 테이블 조회 결과
     */
    public List<FilesVO> getFileList(@Param("realTB") String realTB, @Param("realSEQ") String realSEQ, @Param("gubunCD") String gubunCD);

    /**
     * 파일 상세 정보 가져오기
     *
     * @param key SEQ
     * @return FilesVO
     */
    public FilesVO getFileInfo(@Param("key") String key);

    /**
     * 파일 DB 추가
     *
     * @param vo FilesVO
     * @return Insert 성공 건수
     */
    public int insertFile(@Param("vo") FilesVO vo);

    /**
     * 파일 DB 삭제
     *
     * @param key SEQ
     * @return 삭제 성공 건수
     */
    public int deleteFile(@Param("key") String key);
}
