package tax.www.ctr.onepage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tax.www.service.cmn.CmnService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 싱글 페이지
 * - 개인정보 수정
 * <p>
 * User: 현재호
 * Date: 17. 11. 06
 * Time: 오후 5:02
 */
@Controller
@RequestMapping("/onepage")
public class OnepageCtr {

    private static final Logger log = LogManager.getLogger(OnepageCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    /**
     * 개인정보수정 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/myinfo", method = RequestMethod.GET)
    public ModelAndView myinfoView(HttpServletRequest req, HttpSession ses) {

        ModelAndView mv = new ModelAndView();

        try {

            mv.addAllObjects(cmnService.setCmnModel(req));

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }
}
