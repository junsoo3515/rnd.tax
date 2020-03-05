package tax.www.service.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import tax.www.dao.cmn.CmnDataMapper;
import tax.www.dao.main.MainMonitorMapper;
import tax.www.vo.cmn.files.FilesVO;
import tax.www.vo.cmn.jqgrid.SrcJQGridVO;
import tax.www.vo.cmn.list.ListObjVO;
import tax.www.vo.main.MonitorVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GIS 상황관제(메인) 서비스 인터페이스 클래스
 * <p>
 * User: 현재호
 * Date: 17. 11. 03
 * Time: 오후 2:52
 */
@Service("MainMonitorService")
public class MainMonitorServiceImpl extends EgovAbstractServiceImpl implements MainMonitorService {

    private static final Logger log = LogManager.getLogger(MainMonitorServiceImpl.class);

    /**
     * GIS 상황관제(메인) DAO
     */
    @Resource(name = "mainMonitorMapper")
    private MainMonitorMapper mainMonitorMapper;

    @Resource(name = "cmnDataMapper")
    private CmnDataMapper cmnDataMapper;

    /**
     * 상황처리 목록 총 갯수 가져오기
     *
     * @param vo          jqGrid 파라미터
     * @return List sitPcs list cnt
     * @throws Exception the exception
     */
    public int getSitPcsListCnt(SrcJQGridVO vo) throws Exception {

        return mainMonitorMapper.getSitPcsListCnt(vo);
    }

    /**
     * 상황처리 목록 가져오기
     *
     * @param vo          jqGrid 파라미터
     * @return List sitPcs list
     * @throws Exception the exception
     */
    public List<MonitorVO> getSitPcsList(SrcJQGridVO vo) throws Exception {

        return mainMonitorMapper.getSitPcsList(vo);
    }

    /**
     * 상세정보 가져오기
     *
     * @param vo MonitorVO
     * @return MonitorVO
     * @throws Exception the exception
     */
    public MonitorVO getDetailInfoData(MonitorVO vo) throws Exception {

        List<FilesVO> filesList = new ArrayList<>();
        MonitorVO monitorVO = new MonitorVO();

        monitorVO = mainMonitorMapper.getDetailInfoData(vo);

        filesList = cmnDataMapper.getFileList("TAX_RECOG_INFO", vo.tri_seq, "B");

        if(filesList.size() > 0) {
            monitorVO.files = filesList;
        }

        return monitorVO;
    }

    /**
     * 담당자 가져오기
     *
     * @return List<ListObjVO>
     * @throws Exception the exception
     */
    public List<ListObjVO> getManagerList() throws Exception {

        return mainMonitorMapper.getManagerList();
    }

    /**
     * 상세정보 적용
     *
     * @param vo MonitorVO
     * @return 쿼리 결과
     * @throws Exception the exception
     */
    @Transactional
    public int setDetailInfoAct(MonitorVO vo) throws Exception {

        int resCnt = 0;

        resCnt = mainMonitorMapper.setDetailInfoUpdate(vo);
        resCnt += mainMonitorMapper.setDetailInfoInsert(vo); // 영치 등록 시 체납처리 이력 테이블(TAX_PRC_HISTORY) 상세정보 등록

        return resCnt;
    }

    /**
     * 관심차량번호 가져오기
     *
     * @param car_no 차량번호
     * @return List interestCar list
     * @throws Exception the exception
     */
    public List<ListObjVO> getInterestCarList(String car_no) throws Exception {

        if(!car_no.equals("null")) {

            String[] carnoArr = car_no.split(",");

            car_no = "";

            for(String str : carnoArr) {
                car_no += (car_no.equals("")) ? "'" + str + "'" : ",'" + str + "'";
            }
        }

        if(car_no.equals("null")) {

            car_no = "'none'";
        }

        return mainMonitorMapper.getInterestCarList(car_no);
    }

    /**
     * 레이어(CCTV 종류) 가져오기
     *
     * @return List cctvType list
     * @throws Exception the exception
     */
    public List<ListObjVO> getCctvTypeList() throws Exception {

        return mainMonitorMapper.getCctvTypeList();
    }

    /**
     * CCTV GIS 정보 가져오기
     *
     * @param vo MonitorVO
     * @return List cctvType list
     * @throws Exception the exception
     */
    public List<MonitorVO> getCctvGeoData(MonitorVO vo) throws Exception {


        if (!vo.cctv_type.equals("null")) {

            String[] cctvTypeArr = vo.cctv_type.split(",");

            vo.cctv_type = "";

            for (String str : cctvTypeArr) {

                vo.cctv_type += (vo.cctv_type.equals("")) ? "'" + str + "'" : ",'" + str + "'";
            }
        }

        if (vo.cctv_type.equals("null")) {

            vo.cctv_type = "'none'";
        }

        if(!vo.cctvIds.equals("")) {

            String[] cctvIdsArr = vo.cctvIds.split(",");

            vo.cctvIds = "";

            for (String str : cctvIdsArr) {

                vo.cctvIds += (vo.cctvIds.equals("")) ? "'" + str + "'" : ",'" + str + "'";
            }
        }

        return mainMonitorMapper.getCctvGeoData(vo);
    }

    /**
     * 분포도 GIS 정보 가져오기
     *
     * @return List cctvType list
     * @throws Exception the exception
     */
    public List<MonitorVO> getDistributionGeoData() throws Exception {

        return mainMonitorMapper.getDistributionGeoData();
    }

    /**
     * 관심차량 이동경로 목록 가져오기
     *
     * @param vo MonitorVO
     * @return List route list
     * @throws Exception the exception
     */
    public List<MonitorVO> getRouteList(MonitorVO vo) throws Exception {

        return mainMonitorMapper.getRouteList(vo);
    }

    /**
     * 투망감시 설정 CCTV GIS 정보 가져오기
     *
     * @param vo MonitorVO
     * @return List netSetCctvGeoData list
     * @throws Exception the exception
     */
    public List<MonitorVO> getNetSetCctvGeoData(MonitorVO vo) throws Exception {

        if (!vo.cctv_type.equals("null")) {
            String[] cctvTypeArr = vo.cctv_type.split(",");

            vo.cctv_type = "";

            for (String str : cctvTypeArr) {
                vo.cctv_type += (vo.cctv_type.equals("")) ? "'" + str + "'" : ",'" + str + "'";
            }
        }
        if (vo.cctv_type.equals("null")) {
            vo.cctv_type = "'none'";
        }

        return mainMonitorMapper.getNetSetCctvGeoData(vo);
    }

    /**
     * 실시간 상황처리 목록 가져오기
     *
     * @param sitOcrTime String
     * @return List realtimeSitPcs list
     * @throws Exception the exception
     */
    public MonitorVO getRealtimeSitPcsList(String sitOcrTime) throws Exception {

        return mainMonitorMapper.getRealtimeSitPcsList(sitOcrTime);
    }

    /**
     * CCTV 정보 가져오기
     *
     * @param cctvId String
     * @return MonitorVO
     * @throws Exception the exception
     */
    public EgovMap getCctvInfo(String cctvId) throws Exception {

        return mainMonitorMapper.getCctvInfo(cctvId);
    }

    /**
     * 투망감시 RTSP URL 가져오기
     *
     * @param map 차량인식 일련번호, 발생 CCTV ID
     * @param userId 사용자아이디
     * @param vurixIpPort 뷰릭스IpPort(IP:PORT)
     * @return List RTSP URL list
     * @throws Exception the exception
     */
    public List<Map<String, Object>> castNetRtspUrl(Map<String, Object> map, String userId, String vurixIpPort) throws Exception {

        log.info("뷰릭스 RTSP URL 가져오기 시작");

        URL url;
        String vurixUrl = "";
        HttpURLConnection connection = null;
        String charset = "UTF-8";
        String result = "";
        Map<String, Object> param = new HashMap<>();
        List<Map<String, Object>> rtspUrl = new ArrayList<Map<String, Object>>();

        if(param != null) {
            param = map;
        }

		vurixUrl = "http://" + vurixIpPort + "/getTaxNearCCTV/" + param.get("triSeq") + "/" + param.get("ocrCctvId");
        // 테스트 용 URL(사건번호 고정)
//        vurixUrl = "http://" + vurixIpPort + "/getTaxNearCCTV/YU3024002888/30200CTVPOC858943018";

        try {
            url = new URL(vurixUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestProperty("content-Type", "application/json; charset=" + charset);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("userid", userId);
            connection.setRequestMethod("GET");

            // RECEIVE Response
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuffer response = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            bufferedReader.close();

            result = response.toString();

            ObjectMapper objectMapper = new ObjectMapper();

            rtspUrl = objectMapper.readValue(result, new TypeReference<List<Map<String, Object>>>() {
            });

            log.info("뷰릭스 RTSP URL 가져오기 성공");
        } catch (Exception e) {

            log.error("뷰릭스 RTSP URL 가져오기 실패", e);
        } finally {

            if(connection != null) {

                connection.disconnect();
                connection = null;
            }
        }

        return rtspUrl;
    }

    /**
     * 발생지점 CCTV(실시간 감시), 영상팝업 RTSP URL 가져오기
     *
     * @param map 요청영상 종류(실시간 영상, 영상팝업 현재 영상, 영상팝업 과거 영상), 차량인식 일련번호, CCTV ID, CCTV 명, 영상 사이즈, 영상배속, 영상 시작일
     * @param userId 사용자 아이디
     * @param vurixIpPort 뷰릭스IpPort(IP:PORT)
     * @return Map<String, Object>
     * @throws Exception the exception
     */
    public Map<String, Object> rtspUrl(Map<String, Object> map, String userId, String vurixIpPort) throws Exception {

        log.info("뷰릭스 RTSP URL 가져오기 시작");

        URL url;
        String vurixUrl = "";
        HttpURLConnection connection = null;
        String charset = "UTF-8";
        String result = "";
        Map<String, Object> rtspUrl = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<>();

        if(param != null) {
            param = map;
        }

		vurixUrl = "http://" + vurixIpPort + "/getTaxCCTVUrl/" + param.get("triSeq") + "/" + param.get("cctvId") + "?cctv_nm=" + param.get("cctvNm") + "&size=" + param.get("size");
        // 테스트 용 URL(사건번호, CCTV ID, CCTV 명 고정)
//        vurixUrl = "http://" + vurixIpPort + "/getTaxCCTVUrl/YU3024002888/30200CTVPOC858943018?cctv_nm=" + URLEncoder.encode("016.(377-2014-17) 문예공원", "UTF-8") + "&size=" + param.get("size");

        if(param.get("viewType").equals("past")) {

            vurixUrl += "&speed=" + param.get("speed") + "&timestamp=" + param.get("startDt");
        }

        try {
            url = new URL(vurixUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestProperty("content-Type", "application/json; charset=" + charset);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("userid", userId);
            connection.setRequestMethod("GET");

            // RECEIVE Response
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuffer response = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            bufferedReader.close();

            result = response.toString();

            ObjectMapper objectMapper = new ObjectMapper();

            rtspUrl = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
            });

            log.info("뷰릭스 RTSP URL 가져오기 성공");
        } catch (Exception e) {

            log.error("뷰릭스 RTSP URL 가져오기 실패", e);
        } finally {

            if(connection != null) {

                connection.disconnect();
                connection = null;
            }
        }

        return rtspUrl;
    }

    /**
     * 영상팝업 스크린샷 저장하기
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param memId 사용자 아이디
     * @param vurixIpPort 뷰릭스IpPort(IP:PORT)
     * @throws Exception the exception
     */
    public void snapShot(HttpServletRequest request, HttpServletResponse response, String memId, String vurixIpPort) throws Exception {

        log.info("뷰릭스 스냅샷 이미지 Byte 데이터 가져오기 시작");

        URL url;
        String vurixUrl = "";
        HttpURLConnection connection = null;
        String charset = "UTF-8";
        String strParam = "";
        String result = "";
        Map<String, Object> rtspUrl = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<>();

//		vurixUrl = "http://" + vurixIpPort + "/getTaxThumbnail/" + req.getParameter("creno") + "/" + req.getParameter("cctvId") + "?cctv_nm=" + URLEncoder.encode(req.getParameter("cctvNm"), "UTF-8") + "&timestamp=" + req.getParameter("timestamp");
        vurixUrl = "http://" + vurixIpPort + "/getTaxThumbnail/YU3024002888/30170CTVCPV11920921?cctv_nm=" + URLEncoder.encode("024-3.(서구불법-024)테크노월드 (기업은행)", "UTF-8") + "&timestamp=" + request.getParameter("timestamp");

        try {
            url = new URL(vurixUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestProperty("content-Type", "application/json; charset=" + charset);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("userid", memId);
            connection.setRequestMethod("GET");

            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // RECEIVE Response
            byte[] downloadBuffer = new byte[4096];
            int byteRead = 0;

            while((byteRead = is.read(downloadBuffer)) != -1) {

                baos.write(downloadBuffer, 0, byteRead);
            }

            log.info("뷰릭스 스냅샷 이미지 Byte 데이터 가져오기 성공");

            // 이미지 다운로드 처리
            download(request, response, baos);
        } catch (Exception e) {

            log.error("뷰릭스 스냅샷 이미지 Byte 데이터 가져오기 실패", e);
        } finally {

            if(connection != null) {

                connection.disconnect();
                connection = null;
            }
        }
    }

    /**
     * 이미지 다운로드 처리
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param baos ByteArrayOuputStream
     * @throws Exception the exception
     */
    public void download(HttpServletRequest request, HttpServletResponse response, ByteArrayOutputStream baos) throws Exception {

        log.info("이미지 다운로드 처리 시작");

        response.setContentType("application/octet-stream; charset=utf-8");
        response.setContentLength(baos.size());

        OutputStream outputstream = null;

        try {

            response.setHeader("Content-Disposition", getDisposition(request.getParameter("fileName"), check_browser(request)));
            response.setHeader("Content-Transfer-Encoding", "binary");

            outputstream = response.getOutputStream();

            FileCopyUtils.copy(baos.toByteArray(), outputstream);

            log.info("이미지 다운로드 처리 성공");
        } catch (Exception e) {

            log.error("이미지 다운로드 처리 실패", e);
        } finally {

            outputstream.flush();
            outputstream.close();
        }
    }

    /**
     * 브라우저 확인
     *
     * @param request HttpServletRequest
     * @return String 브라우저 종류
     * @throws Exception the exception
     */
    public String check_browser(HttpServletRequest request) {

        String browser = "";
        String header = request.getHeader("User-Agent");

        //신규추가된 indexof : Trident(IE11) 일반 MSIE로는 체크 안됨
        if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1){

            browser = "ie";
        }
        //크롬일 경우
        else if (header.indexOf("Chrome") > -1){

            browser = "chrome";
        }
        //오페라일경우
        else if (header.indexOf("Opera") > -1){

            browser = "opera";
        }
        //사파리일 경우
        else if (header.indexOf("Apple") > -1){

            browser = "sarari";
        } else {

            browser = "firfox";
        }

        return browser;
    }

    /**
     * Content-Disposition 헤더값 가져오기
     *
     * @param down_filename 파일명
     * @param browser_check 브라우저 종류
     * @return String Content-Disposition 헤더 값
     * @throws Exception the exception
     */
    public String getDisposition(String down_filename, String browser_check) throws UnsupportedEncodingException {

        String prefix = "attachment;filename=";
        String encodedfilename = null;

        System.out.println("browser_check:"+browser_check);

        if (browser_check.equals("ie")) {

            encodedfilename = URLEncoder.encode(down_filename, "UTF-8").replaceAll("\\+", "%20");
        }else if (browser_check.equals("chrome")) {

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < down_filename.length(); i++){

                char c = down_filename.charAt(i);

                if (c > '~') {

                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {

                    sb.append(c);
                }
            }

            encodedfilename = sb.toString();
        }else {

            encodedfilename = "\"" + new String(down_filename.getBytes("UTF-8"), "8859_1") + "\"";
        }

        return prefix + encodedfilename;
    }
}
