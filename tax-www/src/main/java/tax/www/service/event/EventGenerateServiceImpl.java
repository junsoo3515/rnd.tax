package tax.www.service.event;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import tax.www.dao.cmn.CmnDataMapper;
import tax.www.dao.event.EventGenerateMapper;
import tax.www.vo.cmn.files.FilesVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.event.GenerateVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 상황발생 이력조회 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 17. 11. 07
 * Time: 오후 2:21
 */
@Service("EventGenerateService")
public class EventGenerateServiceImpl extends EgovAbstractServiceImpl implements EventGenerateService {

    /**
     * 상황발생 이력조회 DAO
     */
    @Resource(name = "eventGenerateMapper")
    private EventGenerateMapper eventGenerateMapper;

    @Resource(name = "cmnDataMapper")
    private CmnDataMapper cmnDataMapper;

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
    public int getTaxOcrListCnt(String srcCarNo, String srcCctvInfo, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {

        return eventGenerateMapper.getTaxOcrListCnt(srcCarNo, srcCctvInfo, srcSDate, srcEDate, vo);
    }

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
    public List<GenerateVO> getTaxOcrList(String srcCarNo, String srcCctvInfo, String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {

        List<GenerateVO> taxOcrList = new ArrayList<>();
        List<FilesVO> filesList = new ArrayList<>();
        FilesVO filesVO = new FilesVO();

        boolean flag = false;

        GenerateVO generateVO = new GenerateVO();

        taxOcrList = eventGenerateMapper.getTaxOcrList(srcCarNo, srcCctvInfo, srcSDate, srcEDate, vo);

        filesList = cmnDataMapper.getFileList("TAX_RECOG_INFO", "", "B");

        for(int i=0; i<taxOcrList.size(); i++) {

            for(int j=0; j<filesList.size(); j++) {

                if (taxOcrList.get(i).tri_seq.equals(filesList.get(j).real_seq)) {

                    flag = true;
                    generateVO.tri_seq = taxOcrList.get(i).tri_seq;
                    generateVO.car_no = taxOcrList.get(i).car_no;
                    generateVO.cctv_nm = taxOcrList.get(i).cctv_nm;
                    generateVO.cctv_adres = taxOcrList.get(i).cctv_adres;
                    generateVO.reg_dts = taxOcrList.get(i).reg_dts;
                    generateVO.state = taxOcrList.get(i).state;
                    generateVO.tax_money_cnt = taxOcrList.get(i).tax_money_cnt;

                    filesVO = new FilesVO();

                    filesVO.setFiles_seq(filesList.get(j).getFiles_seq());
                    filesVO.real_seq = filesList.get(j).real_seq;
                    filesVO.real_tb = filesList.get(j).real_tb;
                    filesVO.file_title = filesList.get(j).file_title;
                    generateVO.files.add(filesVO);
                }
            }

            if(flag == true) {

                taxOcrList.remove(i);
                taxOcrList.add(i, generateVO);
                generateVO = new GenerateVO();
                flag = false;
            }
        }

        return taxOcrList;
    }

    /**
     * 시간별 발생현황 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List timeTaxOcr list
     * @throws Exception the exception
     */
    public List<GenerateVO> getTimeTaxOcrList(String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {

        return eventGenerateMapper.getTimeTaxOcrList(srcSDate, srcEDate, vo);
    }

    /**
     * 지역별 발생현황 Top5 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List areaOcrTop list
     * @throws Exception the exception
     */
    public List<GenerateVO> getAreaOcrTopList(String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {

        return eventGenerateMapper.getAreaOcrTopList(srcSDate, srcEDate, vo);
    }

    /**
     * CCTV별 발생현황 Top5 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List cctvOcrTop list
     * @throws Exception the exception
     */
    public List<GenerateVO> getCctvOcrTopList(String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {

        return eventGenerateMapper.getCctvOcrTopList(srcSDate, srcEDate, vo);
    }
}
