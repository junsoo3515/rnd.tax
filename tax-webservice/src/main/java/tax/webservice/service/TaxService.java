package tax.webservice.service;

/**
 * 지방세체납관리 서비스 인터페이스 클래스
 * <p>
 * User: 이준수
 * Date: 18. 04. 11
 * Time: 오후 03:05
 */
public interface TaxService {

    /**
     * 최단거리 이동경로 가져오기
     *
     * @param point       시작좌표, 경유지좌표, 마지막좌표
     * @throws Exception the exception
     */
    public String movingRoute(String point) throws Exception;

    /**
     * VWORLD 지도 이미지 가져오기
     *
     * @param type      지도종류
     * @param x         x 좌표
     * @param y         y 좌표
     * @param z         z 좌표
     * @throws Exception the exception
     */
    public byte[] vworldMap(String apikey, String type, String x, String y, String z) throws Exception;
}
