package tax.www.ctr.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.com.cmn.service.EgovProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tax.www.service.cmn.CmnService;
import tax.www.service.event.EventProcessService;
import tax.www.service.event.EventSpreadService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 이벤트 관리
 * <p>
 * <p>
 * User: 이준수
 * Date: 17. 11. 06
 * Time: 오후 6:05
 */
@Controller
@RequestMapping("/event")
public class EventCtr {

    private  static final Logger log = LogManager.getLogger(EventCtr.class);

    public String gisEngine = EgovProperties.getProperty("Gis.Engine");
    public String gisApiKey = EgovProperties.getProperty("Gis.ApiKey");
    public String gisWebserviceIp = EgovProperties.getProperty("Gis.Webservice.Ip");
    public String gisWebservicePort = EgovProperties.getProperty("Gis.Webservice.Port");
    public String gisProjection = EgovProperties.getProperty("Gis.Projection");
    public String gisBoundsLeft = EgovProperties.getProperty("Gis.BoundsLeft");
    public String gisBoundsTop = EgovProperties.getProperty("Gis.BoundsTop");
    public String gisBoundsRight = EgovProperties.getProperty("Gis.BoundsRight");
    public String gisBoundsBottom = EgovProperties.getProperty("Gis.BoundsBottom");

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "EventProcessService")
    private EventProcessService eventProcessService;

    @Resource(name = "EventSpreadService")
    private EventSpreadService eventSpreadService;

    private String mnu_cd = "T1E000";

    /**
     * 관심차량정보현황 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ModelAndView infoView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T1E001";

        String[] arr = {"V"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ModelAndView("redirect:/" + (req.getHeader("referer") != null ? req.getHeader("referer").toString() : ""));
        }

        ModelAndView mv = new ModelAndView();

        try {

            mv.addAllObjects(cmnService.setCmnModel(req, mnu_cd));
            mv.addObject("gisEngine", gisEngine); // GIS 엔진
            mv.addObject("gisApiKey", gisApiKey); // GIS API KEY
            mv.addObject("gisWebserviceIp", gisWebserviceIp); // GIS Webservice IP
            mv.addObject("gisWebservicePort", gisWebservicePort); // GIS Webservice PORT
            mv.addObject("gisProjection", gisProjection); // GIS 좌표계

            // 지도 범위
            mv.addObject("gisBoundsLeft", gisBoundsLeft);
            mv.addObject("gisBoundsTop", gisBoundsTop);
            mv.addObject("gisBoundsRight", gisBoundsRight);
            mv.addObject("gisBoundsBottom", gisBoundsBottom);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }

    /**
     * 상황발생 이력조회 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    public ModelAndView generateView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T1E002";

        String[] arr = {"V"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ModelAndView("redirect:/" + (req.getHeader("referer") != null ? req.getHeader("referer").toString() : ""));
        }

        ModelAndView mv = new ModelAndView();

        try {

            mv.addAllObjects(cmnService.setCmnModel(req, mnu_cd));
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }

    /**
     * 상황전파 이력조회 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/spread", method = RequestMethod.GET)
    public ModelAndView spreadView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T1E003";

        String[] arr = {"V"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ModelAndView("redirect:/" + (req.getHeader("referer") != null ? req.getHeader("referer").toString() : ""));
        }

        ModelAndView mv = new ModelAndView();

        try {

            mv.addAllObjects(cmnService.setCmnModel(req, mnu_cd));
            mv.addObject("tcgJongList", new ObjectMapper().writeValueAsString(eventSpreadService.getTcgJongList())); // 전파그룹 종류 가져오기
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }

    /**
     * 상황처리 관리 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public ModelAndView processView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T1E004";

        String[] arr = {"V"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ModelAndView("redirect:/" + (req.getHeader("referer") != null ? req.getHeader("referer").toString() : ""));
        }

        ModelAndView mv = new ModelAndView();

        try {

            mv.addAllObjects(cmnService.setCmnModel(req, mnu_cd));
            mv.addObject("makeTypeList", new ObjectMapper().writeValueAsString(eventProcessService.getMakeTypeList()));
            mv.addObject("managerList", new ObjectMapper().writeValueAsString(eventProcessService.getManagerList()));
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }
}
