package tax.www.module.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Session Business 로직
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 5:08
 */
@Component("CmnSessionBiz")
public class CmnSessionBiz {

    private static final Logger log = LogManager.getLogger(CmnSessionBiz.class);

//    @Value("#{cfg['auth.session.context']}")
//    protected static String contextRoot;

    /**
     * 세션값 변경
     *
     * @param ses    HttpSession
     * @param params the params
     */
    public static void setSessionAttribute(HttpSession ses, Map<String, String> params) {

        for (Map.Entry<String, String> obj : params.entrySet()) {

            // TODO tomcat 자체의 context session 공유 로그아웃 문제 발생에 따른 원복 ses.getServletContext().getContext(contextRoot)
            ses.setAttribute(obj.getKey(), obj.getValue());
        }
    }

    /**
     * 세션 삭제
     *
     * @param ses HttpSession
     */
    public static void invalidateSession(HttpSession ses) {

        ses.invalidate();
    }
}
