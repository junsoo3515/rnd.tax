package tax.www.vo.security;

/**
 * 모니터링 CCTV 관리에서 사용되는 VO
 * <p>
 * User: 이준수
 * Date: 2017.11.28
 * Time: 오후 4:14
 */
public class CctvVO {

    // 모니터링 CCTV 목록
    public String tmc_seq;
    public String tns_seq;
    public String cctv_id;
    public String prev_cctv_id;
    public String cctv_adres;
    public String point_xy;
    public String point_x;
    public String point_y;
    public String reg_mem_id;
    public String preset_num;
    public String cctv_type;
    public String cctv_rotate;

    // 투망감시 설정
    public String lnk_cctv_id1;
    public String lnk_preset_num1;
    public String lnk_cctv_id2;
    public String lnk_preset_num2;
    public String lnk_cctv_id3;
    public String lnk_preset_num3;
    public String lnk_cctv_id4;
    public String lnk_preset_num4;
    public String lnk_cctv_id5;
    public String lnk_preset_num5;
    public String sort_seq;

    // jqGrid C/R/U/D에서 사용됨
    public void setTmc_seq(String tmc_seq) {
        this.tmc_seq = tmc_seq;
    }

    public void setCctv_id(String cctv_id) {
        this.cctv_id = cctv_id;
    }

    public void setPrev_cctv_id(String prev_cctv_id) {
        this.prev_cctv_id = prev_cctv_id;
    }
}
