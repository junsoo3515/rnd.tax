package tax.webservice.ctr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tax.webservice.service.TaxService;

import javax.annotation.Resource;

/**
 * 지방세 체납관리 웹 서비스
 * <p>
 * User: 이준수
 * Date: 18. 04. 11
 * Time: 오후 03:05
 */
@RestController
@RequestMapping("/tax")
public class TaxRestCtr {

    private static final Logger log = LogManager.getLogger(TaxRestCtr.class);

    @Resource(name = "TaxService")
    private TaxService taxService;

    /**
     * 최단거리 이동경로 가져오기(다음 carset.json)
     *
     * @param point         X,Y 좌표
     * @param callback     콜백함수명
     * @return JSONObject
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/movingRoute", method = RequestMethod.GET)
    public String movingRoute(String point, String callback) {


        String jsonOstr = "";
        String callbackNm = "";

        try {

            callbackNm = callback;
            jsonOstr = taxService.movingRoute(point);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }


        return callbackNm + "(" + jsonOstr + ")";
    }

    /**
     * VWORLD 지도 이미지 가져오기
     *
     * @param apikey    지도 apikey
     * @param type      지도종류
     * @param x         x 좌표
     * @param y         y 좌표
     * @param z         z 좌표
     * @return byte[]
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/vworldMap", method = RequestMethod.GET)
    public byte[] vworldMap(String apikey, String type, String x, String y, String z) {

        byte[] imgDataUrl = new byte[4096];

        try {

            imgDataUrl = taxService.vworldMap(apikey, type, x, y, z);
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return imgDataUrl;
    }
}
