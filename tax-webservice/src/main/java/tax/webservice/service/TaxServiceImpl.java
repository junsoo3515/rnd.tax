package tax.webservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.com.cmn.service.EgovProperties;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 지방세체납관리 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 18. 04. 11
 * Time: 오후 03:06
 */
@Service("TaxService")
public class TaxServiceImpl extends EgovAbstractServiceImpl implements TaxService {

    private static final Logger log = LogManager.getLogger(TaxServiceImpl.class);

    private String mapImgPath = EgovProperties.getProperty("Map.Img.Path");

    /**
     * 최단거리 이동경로 가져오기
     *
     * @param pt       시작좌표, 경유지좌표, 마지막좌표
     * @throws Exception the exception
     */
    public String movingRoute(String pt) throws Exception {


        List<Object> point = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectMapper jsonToStr = new ObjectMapper();
        String rtnJsonstr = "";

        String vp = "";
        point = objectMapper.readValue(pt, List.class);

        if(point.size() > 4) {

            for(int i = 2; i < point.size()-2; i+=2) {
                vp += "&pt=" + point.get(i) + "," + point.get(i+1) + "," + URLEncoder.encode("경유지", "UTF-8");
            }
        }

        URL url;
        HttpURLConnection connection = null;
        String charset = "UTF-8";

        String result = "";
        JSONObject resJsonObj = new JSONObject();
        JSONObject rtnJsonObj = new JSONObject();

        try {
            url = new URL("https://map.kakao.com/route/carset.json?roadside=ON&sp=" + point.get(0) + "," + point.get(1) + "," + URLEncoder.encode("출발", "UTF-8") + vp + "&ep=" + point.get(point.size() - 2) + "," + point.get(point.size() - 1) + "," + URLEncoder.encode("도착", "UTF-8") + "&carMode=SHORTEST_REALTIME&carOption=NONE");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Encoding", charset);
            connection.setRequestProperty("Accept-Language", charset);
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cookie", "__lnkrntdmcvrd=-1");
            connection.setRequestProperty("Host", "map.kakao.com");
            connection.setRequestProperty("Pragma", "no-cache");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, charset));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            result = response.toString();

            JSONParser jsonParser = new JSONParser();
            resJsonObj = (JSONObject) jsonParser.parse(result);

            JSONArray listJarr = new JSONArray();
            JSONObject listJobj = new JSONObject();
            JSONArray nodesJarr = new JSONArray();
            JSONObject nodesJobj = new JSONObject();
            JSONArray sectionsJarr = new JSONArray();
            JSONObject sectionsJobj = new JSONObject();
            JSONArray linksJarr = new JSONArray();
            JSONObject linksJobj = new JSONObject();

            listJarr = (JSONArray) resJsonObj.get("list");
            listJobj = (JSONObject) listJarr.get(0);

            nodesJarr = (JSONArray) listJobj.get("nodes");
            sectionsJarr = (JSONArray) listJobj.get("sections");

            int nodesJarrSize = nodesJarr.size();
            int idx = 0;

            ArrayList<double[]> pathArrList = new ArrayList<>();
            ArrayList<double[]> nodesArrList = new ArrayList<>();

            for(int i = 0; i < nodesJarrSize; i++) {

                nodesJobj = null;
                nodesJobj = (JSONObject) nodesJarr.get(i);

                if(nodesJobj != null) {

                    double[] pathArr = new double[2];

                    pathArr[0] = ((long) nodesJobj.get("x") / 2.5);
                    pathArr[1] = ((long) nodesJobj.get("y") / 2.5);

                    if (pathArrList.size() == 0) {

                        pathArrList.add(pathArr);
                    }

                    if (pathArrList.size() > 0 && (pathArrList.get(pathArrList.size()-1)[0] != pathArr[0] || pathArrList.get(pathArrList.size()-1)[1] != pathArr[1])) {

                        pathArrList.add(pathArr);
                    }

                    if(nodesJobj.get("type") != null && nodesJobj.get("type").toString().equals("VIA")) {

                        double[] nodesArr = new double[2];

                        nodesArr[0] = ((long) nodesJobj.get("x") / 2.5);
                        nodesArr[1] = ((long) nodesJobj.get("y") / 2.5);

                        nodesArrList.add(nodesArr);
                    }

                    if(nodesJobj.get("distance") != null && !nodesJobj.get("distance").toString().equals("0")) {

                        String[] splitPointsArr;

                        idx++;

                        sectionsJobj = (JSONObject) sectionsJarr.get(nodesArrList.size());
                        linksJarr = (JSONArray) sectionsJobj.get("links");
                        linksJobj = (JSONObject) linksJarr.get(idx-1);
                        splitPointsArr = linksJobj.get("points").toString().replace(" ", ",").split(",");

                        for(int j = 0; j < splitPointsArr.length; j+=2) {

                            double[] pointsArr = new double[2];

                            pointsArr[0] = Double.parseDouble(splitPointsArr[j]) / 2.5;
                            pointsArr[1] = Double.parseDouble(splitPointsArr[j + 1]) / 2.5;

                            if(pathArrList.get(pathArrList.size()-1)[0] != pointsArr[0] || pathArrList.get(pathArrList.size()-1)[1] != pointsArr[1]) {

                                pathArrList.add(pointsArr);
                            }
                        }
                    }

                    if(nodesJobj.get("distance") != null && nodesJobj.get("distance").toString().equals("0")) {

                        idx = 0;
                    }
                }
            }

            rtnJsonObj.put("path", pathArrList);
            rtnJsonObj.put("nodes", nodesArrList);

            rtnJsonstr = jsonToStr.writeValueAsString(rtnJsonObj);

            log.info("경로 가져오기 성공");
        } catch (Exception e) {

            log.error("경로 가져오기 실패",e);
        } finally {

            if(connection != null) {

                connection.disconnect();
            }
        }

        return rtnJsonstr;
    }

    /**
     * VWORLD 지도 이미지 가져오기
     *
     * @param type      지도종류
     * @param x         x 좌표
     * @param y         y 좌표
     * @param z         z 좌표
     * @throws Exception the exception
     */
    public byte[] vworldMap(String apikey, String type, String x, String y, String z) throws Exception {

        String vworldUrl = "";
        String localUrl = mapImgPath +"/" + type + "/" + z + "/" + y;

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        URL url;
        HttpURLConnection connection = null;

        File dir = new File(localUrl);

        if(!dir.exists()) {

            dir.mkdirs();

            dir = null;
        }

        if(type.equals("Satellite")) {

            localUrl += "/" + x + ".jpeg";
        } else {

            localUrl += "/" + x + ".png";
        }

        dir = new File(localUrl);

        if(!dir.exists()){

            if(type.equals("Satellite")) {

                vworldUrl = "http://api.vworld.kr/req/wmts/1.0.0/" + apikey + "/" + type + "/" + z + "/" + y + "/" + x + ".jpeg";
            }
            else {

                vworldUrl = "http://api.vworld.kr/req/wmts/1.0.0/" + apikey + "/" + type + "/" + z + "/" + y + "/" + x + ".png";
            }

            try {

                fileOutputStream = new FileOutputStream(localUrl);

                url = new URL(vworldUrl);
                connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();

                byte[] downloadBuffer = new byte[4096];
                int byteRead = 0;
                int byteWritten = 0;

                while((byteRead = inputStream.read(downloadBuffer)) != -1) {

                    fileOutputStream.write(downloadBuffer, 0, byteRead);
                    byteWritten += byteRead;
                }

                log.info("지도 이미지 크기 : " + byteWritten + "byte");
                log.info("지도 이미지 다운로드 완료");
            } catch (Exception e) {

                log.error("지도 이미지 다운로드 실패", e);
            } finally {

                if(inputStream != null) {

                    inputStream.close();
                }

                if (fileOutputStream != null) {

                    fileOutputStream.close();
                }

                if(connection != null) {

                    connection.disconnect();
                }
            }
        }

        try {

            inputStream = new BufferedInputStream(new FileInputStream(localUrl));

            byte[] readBuffer = new byte[4096];

            int n;

            while ((n = inputStream.read(readBuffer)) > 0) {

                byteArrayOutputStream.write(readBuffer, 0, n);
            }

        } catch (Exception e) {

            log.error(e.toString(), e);
        } finally {

            if(inputStream != null) {

                inputStream.close();
            }
            if(connection != null) {

                connection.disconnect();
            }
        }


        return byteArrayOutputStream.toByteArray();
    }
}
