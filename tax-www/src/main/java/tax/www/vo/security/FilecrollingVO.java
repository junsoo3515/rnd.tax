package tax.www.vo.security;

/**
 * 파일수신현황에서 사용되는 VO
 * <p>
 * User: 이준수
 * Date: 2017.11.06
 * Time: 오후 2:42
 */
public class FilecrollingVO {
    // 파일수신현황 목록, 오류 세부 목록
    public String tfcr_seq;
    public String make_dt;
    public int clt_cnt;
    public int prc_cnt;
    public int err_var_cnt;
    public int err_hpn_cnt;
    public int err_etc_cnt;
    public String prc_dt;
    public String tfce_seq;
    public String clt_data;
    public String err_type;
    public String err_type_cd;
}
