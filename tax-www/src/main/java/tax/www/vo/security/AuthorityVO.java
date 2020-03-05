package tax.www.vo.security;

import tax.www.vo.cmn.menu.MenuAuthVO;

import java.util.Date;
import java.util.List;

/**
 * 권한관리에서 사용되는 VO
 * <p>
 * User: 현재호
 * Date: 2017.11.03
 * Time: 오후 4:17
 */
public class AuthorityVO {
    // 권한관리
    public String auth_cd;
    public String nm;
    public String etc;
    public Date reg_dts;
    public long reg_dts_ux;

    // 권한 메뉴접근 관리
    public List<MenuAuthVO> authData;
}
