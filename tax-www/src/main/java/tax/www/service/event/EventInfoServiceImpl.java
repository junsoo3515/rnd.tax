package tax.www.service.event;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import tax.www.dao.event.EventInfoMapper;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.event.InfoVO;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 관심차량정보현황 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 18. 01. 12
 * Time: 오전 10:12
 */
@Service("EventInfoService")
public class EventInfoServiceImpl extends EgovAbstractServiceImpl implements EventInfoService{

    /**
     * 관심차량정보현황 DAO
     */
    @Resource(name = "eventInfoMapper")
    private EventInfoMapper eventInfoMapper;

    /**
     * 관심차량 목록 총 갯수 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List interest list cnt
     * @throws Exception the exception
     */
    public int getInterestListCnt(String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {

        return eventInfoMapper.getInterestListCnt(srcSDate, srcEDate, vo);
    }

    /**
     * 관심차량 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List interest list
     * @throws Exception the exception
     */
    public List<InfoVO> getInterestList(String srcSDate, String srcEDate, SrcJQGridVO vo) throws Exception {

        return eventInfoMapper.getInterestList(srcSDate, srcEDate, vo);
    }

    /**
     * 관심차량 이동경로 목록 가져오기
     *
     * @param srcSDate    시작일
     * @param srcEDate    종료일
     * @param vo          jqGrid 파라미터
     * @return List interestRoute list cnt
     * @throws Exception the exception
     */
    public List<InfoVO> getInterestRouteList(String srcSDate, String srcEDate, String car_no, SrcJQGridVO vo) throws Exception {

        return eventInfoMapper.getInterestRouteList(srcSDate, srcEDate, car_no, vo);
    }

    /**
     * 관심차량 이동경로 좌표 가져오기
     *
     * @param vo          InfoVO
     * @return List routeGeoData list
     * @throws Exception the exception
     */
    public List<InfoVO> getRouteGeoData(InfoVO vo) throws Exception {

        List<InfoVO> getRouteGeoData = new ArrayList<>();

        getRouteGeoData = eventInfoMapper.getRouteGeoData(vo);

        return getRouteGeoData;

    }

    /**
     * 최단거리 좌표 가져오기
     *
     * @param point       최초좌표, 최근좌표
     * @return JSONObject routeLine
     * @throws Exception the exception
     */
    public JSONObject getRouteLine(List<Object> point) throws Exception {

        String vp = "";
        if(point.size() > 4) {

            for(int i = 2; i < point.size()-2; i+=2) {
                vp += "&pt=" + point.get(i) + "," + point.get(i+1) + ",경유지,NORMAL,";
            }
        }

        URL url;
        HttpURLConnection connection = null;
        String charset = "UTF-8";

        String result = "";
        JSONObject jsonObject = new JSONObject();

        url = new URL("http://map.daum.net/route/carset.json?roadside=ON&sp=" + point.get(0) + "," + point.get(1) + "," + URLEncoder.encode("출발", "UTF-8") + ",NORMAL," + vp + "&ep=" + point.get(point.size()-2) + "," + point.get(point.size()-1) + "," + URLEncoder.encode("도착","UTF-8") + ",NORMAL,&carMode=SHORTEST_REALTIME&carOption=NONE");
        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Encoding", charset);
        connection.setRequestProperty("Accept-Language", charset);
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Cookie", "__lnkrntdmcvrd=-1");
        connection.setRequestProperty("Host", "map.daum.net");
        connection.setRequestProperty("Pragma", "no-cache");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");

        //Get Response
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is,charset));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        result = response.toString();

        JSONParser jsonParser = new JSONParser();
        jsonObject = (JSONObject) jsonParser.parse(result);

        return jsonObject;
    }
}
