package tax.www.vo.security;

/**
 * 체납차량 판별조건 관리에서 사용되는 VO
 * <p>
 * User: 이준수
 * Date: 2018.01.08
 * Time: 오후 5:17
 */
public class ConfigVO {
    public String tcf_seq;
    public String filter_cd;
    public String fomula;
    public String reg_mem_id;

    // jqGrid C/R/U/D에서 사용됨
    public void setTcf_seq(String tcf_seq) {
        this.tcf_seq = tcf_seq;
    }

    public void setFilter_cd(String filter_cd) {
        this.filter_cd = filter_cd;
    }

    public void setFomula(String fomula) {
        this.fomula = fomula;
    }
}
