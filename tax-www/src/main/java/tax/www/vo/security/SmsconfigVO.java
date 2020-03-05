package tax.www.vo.security;

/**
 * SMS 연락처 관리에서 사용되는 VO
 * <p>
 * User: 이준수
 * Date: 2017.11.06
 * Time: 오후 3:49
 */
public class SmsconfigVO {
    // 그룹 목록, 연락처 목록
    public String grp_nm;
    public String sms_st_time;
    public String sms_ed_time;
    public String reg_mem_id;
    public String tcg_seq;
    public String nm;
    public String hp;
    public String part_nm;
    public String email;
    public String tc_seq;
    public String cm_seq;
    public String con_ct;



    public void setGrp_nm(String grp_nm) {
        this.grp_nm = grp_nm;
    }

    public void setSms_st_time(String sms_st_time) {
        this.sms_st_time = sms_st_time;
    }

    public void setSms_ed_time(String sms_ed_time) {
        this.sms_ed_time = sms_ed_time;
    }

    public void setTcg_seq(String tcg_seq) {
        this.tcg_seq = tcg_seq;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public void setPart_nm(String part_nm) {
        this.part_nm = part_nm;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTc_seq(String tc_seq) {
        this.tc_seq = tc_seq;
    }

    public void setCm_seq(String cm_seq) {
        this.cm_seq = cm_seq;
    }
}
