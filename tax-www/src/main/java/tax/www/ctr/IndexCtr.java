package tax.www.ctr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 처음 Index 페이지 설정
 * <p>
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:30
 */
@Controller
@RequestMapping("/")
public class IndexCtr {

    private static final Logger log = LogManager.getLogger(IndexCtr.class);

    /**
     * 인덱스 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView invokeView(HttpServletRequest req, HttpSession ses) {

        ModelAndView mv = new ModelAndView("index");

        // Exception 처리는 컨트롤러 함수 내에서 (권장)
        try {

        } catch (Exception ex) {
            // error를 로깅할 시에는, 반드시 throwable이 가능하도록 인자 2개짜리 error 함수를 사용.
            log.error(ex.toString(), ex);
        }

        return mv;
    }

    /**
     * 에러 페이지 나올 경우 - colorAdmin Style
     *
     * @param httpRequest the http request
     * @return the model and view
     */
    @RequestMapping(value = "errors", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("errors");

        String errorMsg = "";
        int httpErrorCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");

        switch (httpErrorCode) {
            case 400:
                errorMsg = "Http Error Code: 400. Bad Request";
                break;
            case 401:
                errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            case 404:
                errorMsg = "Http Error Code: 404. Resource not found";
                break;
            case 500:
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
        }

        errorPage.addObject("p", httpRequest.getContextPath()); // PATH 가져오기
        errorPage.addObject("errorCode", httpErrorCode);
        errorPage.addObject("errorMsg", errorMsg);

        return errorPage;
    }
}
