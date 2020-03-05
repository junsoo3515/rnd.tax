package tax.www.ctr.login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tax.www.module.secure.encryption.CmnRsaBiz;

import javax.servlet.http.HttpServletRequest;

/**
 * 사용자 로그인
 * <p>
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:30
 */
@Controller
@RequestMapping("/login")
public class AuthLoginCtr {

    private static final Logger log = LogManager.getLogger(AuthLoginCtr.class);

    /**
     * 로그인 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView invokeView(HttpServletRequest req) {

        ModelAndView mv = new ModelAndView();

        try {
            // RSA 키 생성
            CmnRsaBiz.initRsa(req);

            mv.addObject("p", req.getContextPath());
            mv.addObject("rsaModulus", req.getAttribute("RSAModulus"));
            mv.addObject("rsaExponent", req.getAttribute("RSAExponent"));

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }
}
