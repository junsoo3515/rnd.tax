package tax.www.vo.event;

import tax.www.vo.cmn.files.FilesVO;

import java.util.List;

/**
 * 상황처리 관리에서 사용되는 VO
 * <p>
 * User: 이준수
 * Date: 2017.11.06
 * Time: 오후 6:24
 */
public class ProcessVO {
    // 상황처리 목록, 상황전파 목록, 체납정보 및 목록, 체납차량 발견위치 목록
    public String tri_seq;
    public String tci_seq;
    public String nm_cnt;
    public String car_no;
    public String cctv_nm;
    public String reg_dts;
    public String seize_dt;
    public String prc_cont;
    public String state;
    public List<FilesVO> files;
    public String whole_cnt;
    public String occur_cnt;
    public String acpcs_cnt;
    public String end_cnt;
    public String tax_money_cnt;
    public String make_type_cd;
    public String make_type_cd_nm;
    public String cctv_id;
    public String cctv_adres;
    public String photo_seq;
    public String photo_exists;

    public String tot_money_cnt;

    public String tsp_seq;
    public String grp_nm;
    public String sms_conts;
    public String con_ct;

    // 승인처리, 영치결과
    public String prc_user_seq;
    public String seize_user_seq;
    public String prc_fl;
    public String prc_user_nm;
    public String seize_fl;
    public String seize_user_nm;

    public String srcSDate;
    public String srcEDate;
    public String srcMakeType;

    // 모든 담당자 가져오기
    public String nm;
    public String hp;
    public String part_nm;
    public String cm_seq;
    public String reg_mem_id;

    public void setNm(String nm) {
        this.nm = nm;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public void setPart_nm(String part_nm) {
        this.part_nm = part_nm;
    }

    public void setSeize_user_nm(String seize_user_nm) {
        this.seize_user_nm = seize_user_nm;
    }

    public void setSeize_fl(String seize_fl) {
        this.seize_fl = seize_fl;
    }

    public void setPrc_user_nm(String prc_user_nm) {
        this.prc_user_nm = prc_user_nm;
    }

    public void setPrc_fl(String prc_fl) {
        this.prc_fl = prc_fl;
    }

    public void setSeize_user_seq(String seize_user_seq) {
        this.seize_user_seq = seize_user_seq;
    }

    public void setPrc_user_seq(String prc_user_seq) {
        this.prc_user_seq = prc_user_seq;
    }
}
