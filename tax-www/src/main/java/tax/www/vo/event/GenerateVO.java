package tax.www.vo.event;

import tax.www.vo.cmn.files.FilesVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 상황발생 이력조회에서 사용되는 VO
 * <p>
 * User: 이준수
 * Date: 2017.11.07
 * Time: 오후 1:36
 */
public class GenerateVO {

    public String car_no;
    public String cctv_nm;
    public String cctv_adres;
    public String tri_seq;
    public String state;
    public String tax_money_cnt;
    public List<FilesVO> files = new ArrayList<>();

    public String tax_time;
    public String tax_cnt;
    public String tax_tot_money;

    public String area_nm;
    public String cctv_cnt;

    public String reg_dts;
    public String mod_dts;
}
