package tax.www.vo.security;

/**
 * 관심차량관리에서 사용되는 VO
 * <p>
 * User: 이준수
 * Date: 2018.01.08
 * Time: 오후 3:24
 */
public class InterestVO {
    public String tci_seq;
    public String car_no;
    public String reg_mem_id;

    // jqGrid C/R/U/D에서 사용됨
    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public void setTci_seq(String tci_seq) {
        this.tci_seq = tci_seq;
    }
}
