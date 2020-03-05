package tax.www.ctr.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.com.cmn.service.EgovProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tax.www.service.cmn.CmnService;
import tax.www.service.security.SecurityCctvService;
import tax.www.service.security.SecurityConfigService;
import tax.www.service.security.SecurityFilecrollingService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 시스템 관리
 * <p>
 * <p>
 * User: 현재호
 * Date: 17. 11. 03
 * Time: 오후 2:23
 */
@Controller
@RequestMapping("/security")
public class SecurityCtr {

    private static final Logger log = LogManager.getLogger(SecurityCtr.class);

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

    @Resource(name = "SecurityFilecrollingService")
    private SecurityFilecrollingService securityFilecrollingService;

    @Resource(name = "SecurityConfigService")
    private SecurityConfigService securityConfigService;

    @Resource(name = "SecurityCctvService")
    private SecurityCctvService securityCctvService;

    private String mnu_cd = "T2S000";

    /**
     * 모니터링 CCTV 관리 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/cctv", method = RequestMethod.GET)
    public ModelAndView cctvView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T2S001";

        String[] arr = {"V"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return  new ModelAndView("redirect:/" + (req.getHeader("referer") != null ? req.getHeader("referer").toString() : ""));
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
     * 체납차량 판별조건 관리 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public ModelAndView configView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T2S002";

        String[] arr = {"V"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return  new ModelAndView("redirect:/" + (req.getHeader("referer") != null ? req.getHeader("referer").toString() : ""));
        }

        ModelAndView mv = new ModelAndView();

        try {

            mv.addAllObjects(cmnService.setCmnModel(req, mnu_cd));
            mv.addObject("disConList", new ObjectMapper().writeValueAsString(securityConfigService.getDisConList())); // 판별조건 가져오기
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }

    /**
     * 관심차량관리관리 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/interest", method = RequestMethod.GET)
    public ModelAndView interestView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T2S003";

        String[] arr = {"V"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return  new ModelAndView("redirect:/" + (req.getHeader("referer") != null ? req.getHeader("referer").toString() : ""));
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
     * SMS 연락처 관리 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/smsconfig", method = RequestMethod.GET)
    public ModelAndView smsconfigView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T2S004";

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
     * 사용자계정관리 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView userView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T2S005";

        String[] arr = {"V"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ModelAndView("redirect:/" + (req.getHeader("referer") != null ? req.getHeader("referer").toString() : ""));
        }

        ModelAndView mv = new ModelAndView();

        try {

            mv.addAllObjects(cmnService.setCmnModel(req, mnu_cd));
            mv.addObject("authList", new ObjectMapper().writeValueAsString(cmnService.getAuthList())); // 권한 가져오기

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }

    /**
     * 권한관리 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/authority", method = RequestMethod.GET)
    public ModelAndView authorityView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T2S006";

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
     * 코드관리 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public ModelAndView codeView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T2S007";

        String[] arr = {"V"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ModelAndView("redirect:/" + (req.getHeader("referer") != null ? req.getHeader("referer").toString() : ""));
        }

        ModelAndView mv = new ModelAndView();

        try {

            mv.addAllObjects(cmnService.setCmnModel(req, mnu_cd));
            mv.addObject("jongList", new ObjectMapper().writeValueAsString(cmnService.getCodeGubunList())); // 코드 종류 가져오기
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }

    /**
     * 파일수신현황 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/filecrolling", method = RequestMethod.GET)
    public ModelAndView filecrollingView(HttpServletRequest req,  HttpSession ses) {

        mnu_cd = "T2S008";

        String[] arr = {"V"};

        try {

            cmnService.isUserAccessValidate(ses, mnu_cd, arr);
        } catch (Exception ex) {

            return new ModelAndView("redirect:/" + (req.getHeader("referer") != null ? req.getHeader("referer").toString() : ""));
        }

        ModelAndView mv = new ModelAndView();

        try {

            mv.addAllObjects(cmnService.setCmnModel(req,  mnu_cd));
            mv.addObject("fileCltJongList", new ObjectMapper().writeValueAsString(securityFilecrollingService.getFileCltJongList())); // 파일수신형황 종류 가져오기
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }
}
