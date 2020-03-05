package tax.www.vo.main;

import tax.www.vo.cmn.files.FilesVO;

import java.util.ArrayList;
import java.util.List;

/**
 * GIS 상황관제(메인)에서 사용되는 VO
 * <p>
 * User: 이준수
 * Date: 2018.01.22
 * Time: 오후 3:15
 */
public class MonitorVO {
    public String cctv_id;
    public String cctvIds;
    public String cctv_type;
    public String cctv_rotate;
    public String tri_seq;
    public String tci_seq;
    public String car_no;
    public String cctv_nm;
    public String cctv_adres;
    public String tax_money_cnt;
    public String ocr_dts;
    public String reg_dts;
    public String reg_mem_id;
    public String state;
    public String generate_type;
    public String tot_money_cnt;
    public String generate_info;
    public String cctv_info;
    public String prc_fl;
    public String prc_user_seq;
    public String seize_fl;
    public String seize_user_seq;
    public String seize_dt;
    public String prc_cont;
    public String point_x;
    public String point_y;
    public String preset_num;
    public String cctv_uid;
    public List<FilesVO> files = new ArrayList<>();

    public void setCctv_info(String cctv_info) {
        this.cctv_info = cctv_info;
    }

    public void setGenerate_type(String generate_type) {
        this.generate_type = generate_type;
    }

    public void setTot_money_cnt(String tot_money_cnt) {
        this.tot_money_cnt = tot_money_cnt;
    }

    public void setGenerate_info(String generate_info) {
        this.generate_info = generate_info;
    }

    public void setPrc_fl(String prc_fl) {
        this.prc_fl = prc_fl;
    }

    public void setPrc_user_seq(String prc_user_seq) {
        this.prc_user_seq = prc_user_seq;
    }

    public void setSeize_fl(String seize_fl) {
        this.seize_fl = seize_fl;
    }

    public void setSeize_user_seq(String seize_user_seq) {
        this.seize_user_seq = seize_user_seq;
    }

    public void setSeize_dt(String seize_dt) {
        this.seize_dt = seize_dt;
    }

    public void setPrc_cont(String prc_cont) {
        this.prc_cont = prc_cont;
    }

    public void setTri_seq(String tri_seq) {
        this.tri_seq = tri_seq;
    }

    public void setTci_seq(String tci_seq) {
        this.tci_seq = tci_seq;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public void setTax_money_cnt(String tax_money_cnt) {
        this.tax_money_cnt = tax_money_cnt;
    }

    public void setReg_dts(String reg_dts) {
        this.reg_dts = reg_dts;
    }

    public void setState(String state) {
        this.state = state;
    }
}
