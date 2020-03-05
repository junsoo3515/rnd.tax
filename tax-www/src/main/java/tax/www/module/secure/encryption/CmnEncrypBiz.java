package tax.www.module.secure.encryption;

import egovframework.com.cmn.service.EgovProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 암복호화 공통 Business 로직
 * <p/>
 * User: 현재호
 * Date: 16. 4. 14
 * Time: 오후 3:10
 */
@Component("CmnEncrypBiz")
public class CmnEncrypBiz {

    private static final Logger log = LogManager.getLogger(CmnEncrypBiz.class);

    public static String secureKey = EgovProperties.getProperty("secure.instance.key");

    /**
     * hex to byte[] : 16진수 문자열을 바이트 배열로 변환한다.
     *
     * @param hex hex string
     * @return
     */
    public static byte[] hexToByteArray(String hex) {

        if (hex == null || hex.length() == 0) {

            return null;
        }

        byte[] ba = new byte[hex.length() / 2];

        for (int i = 0; i < ba.length; i++) {

            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return ba;
    }
}
