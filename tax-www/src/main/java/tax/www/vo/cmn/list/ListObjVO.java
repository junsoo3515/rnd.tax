package tax.www.vo.cmn.list;

/**
 * 리스트 Obj VO
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:54
 */
public class ListObjVO {
    public String id;    // 키
    public String val;   // 값
    public String text;  // 값
    public String text2; // 값

    /**
     * 초기화
     */
    public ListObjVO() {

    }

    /**
     * Setting
     *
     * @param k Key
     * @param v Value
     */
    public ListObjVO(String k, String v) {
        this.id = k;
        this.val = v;
        this.text = v;
        this.text2 = v;
    }
}
