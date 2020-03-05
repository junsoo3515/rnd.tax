package tax.www.ctr.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.com.cmn.service.EgovProperties;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tax.www.service.cmn.CmnService;
import tax.www.service.main.MainMonitorService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * GIS 상황관제(메인)
 * <p>
 * <p>
 * User: 현재호
 * Date: 17. 11. 07
 * Time: 오후 2:31
 */
@Controller
@RequestMapping("/main")
public class MainMonitorCtr {

    private static final Logger log = LogManager.getLogger(MainMonitorCtr.class);

    // GIS
    public String gisEngine = EgovProperties.getProperty("Gis.Engine");
    public String gisApiKey = EgovProperties.getProperty("Gis.ApiKey");
    public String gisWebserviceIp = EgovProperties.getProperty("Gis.Webservice.Ip");
    public String gisWebservicePort = EgovProperties.getProperty("Gis.Webservice.Port");
    public String gisProjection = EgovProperties.getProperty("Gis.Projection");
    public String gisBoundsLeft = EgovProperties.getProperty("Gis.BoundsLeft");
    public String gisBoundsTop = EgovProperties.getProperty("Gis.BoundsTop");
    public String gisBoundsRight = EgovProperties.getProperty("Gis.BoundsRight");
    public String gisBoundsBottom = EgovProperties.getProperty("Gis.BoundsBottom");

    // 웹소켓
    public String wsRealtimeObserveIp = EgovProperties.getProperty("webSocket.realtimeObserveIp");
    public String wsRealtimeObservePort = EgovProperties.getProperty("webSocket.realtimeObservePort");

    // VMS
    public String vmsUseYn = EgovProperties.getProperty("Vms.Use.yn");
    public String vmsSoftware = EgovProperties.getProperty("Vms.Software");
    public String vmsIp = EgovProperties.getProperty("Vms.Ip");
    public String vmsPort = EgovProperties.getProperty("Vms.Port");
    public String vmsId = EgovProperties.getProperty("Vms.Id");
    public String vmsPwd = EgovProperties.getProperty("Vms.Password");
    public String vmsImgDir = EgovProperties.getProperty("Vms.img.dir");
    public String vmsPlayTime = EgovProperties.getProperty("Vms.PlayTime");
    public String vmsPlayBackTimeBase = EgovProperties.getProperty("Vms.PlaybackTime.Base");
    public String vmsPlayBackTimeMax = EgovProperties.getProperty("Vms.PlaybackTime.Max");
    public String vmsPlayBackTimeMaxAf = EgovProperties.getProperty("Vms.PlaybackTime.Max.Af");

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "MainMonitorService")
    private MainMonitorService mainMonitorService;

    private String mnu_cd = "T0M000";

    /**
     * GIS 상황관제 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public ModelAndView monitorView(HttpServletRequest req, HttpSession ses) {

        mnu_cd = "T0M001";

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

            mv.addObject("wsRealtimeObserveIp", wsRealtimeObserveIp);
            mv.addObject("wsRealtimeObservePort", wsRealtimeObservePort);
            mv.addObject("managerList", new ObjectMapper().writeValueAsString(mainMonitorService.getManagerList()));

            mv.addObject("vmsUseYn", vmsUseYn);
            mv.addObject("vmsSoftware", vmsSoftware);
            mv.addObject("vmsIp", vmsIp);
            mv.addObject("vmsPort", vmsPort);
            mv.addObject("vmsId", vmsIp);
            mv.addObject("vmsPwd", vmsPwd);

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }

    /**
     * CCTV 영상팝업 화면 호출 Controller
     *
     * @return View 객체
     */
    @RequestMapping(value = "/popupVms", method = RequestMethod.GET)
    public ModelAndView vmsplayerView(String cctvId, HttpServletRequest req, HttpSession ses) {


        ModelAndView mv = new ModelAndView();

        Map<String, Object> configure = new HashMap<>();

        try {

            EgovMap cctvInfo = mainMonitorService.getCctvInfo(cctvId);

            cctvInfo.put("triSeq", req.getParameter("triSeq"));

            configure.put("vmsSoftware", vmsSoftware);
            configure.put("vmsIp", vmsIp);
            configure.put("vmsPort", vmsPort);
            configure.put("vmsId", vmsId);
            configure.put("vmsPwd", vmsPwd);
            configure.put("vmsImgDir", vmsImgDir);
            configure.put("vmsPlayTime", vmsPlayTime);
            configure.put("vmsPlayBackTimeBase", vmsPlayBackTimeBase);
            configure.put("vmsPlayBackTimeMax", vmsPlayBackTimeMax);
            configure.put("vmsPlayBackTimeMaxAf", vmsPlayBackTimeMaxAf);
            configure.put("vmsUseYn", vmsUseYn);

            mv.addObject("configure", configure);
            mv.addObject("cctvInfo", cctvInfo);

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return mv;
    }
}
