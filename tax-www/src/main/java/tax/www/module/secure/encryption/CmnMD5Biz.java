package tax.www.module.secure.encryption;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 암복호화(MD5) Business 로직
 * <p/>
 * User: 현재호
 * Date: 2016.04.14
 * Time: 오후 3:09
 */
@Component("CmnMD5Biz")
public class CmnMD5Biz {

    private static final Logger log = LogManager.getLogger(CmnMD5Biz.class);

    /**
     * 메시지 다이제스트 알고리즘
     * 128비트 해쉬값 생성, 단방향 암호화
     * 같은 입력값이면 항상 같은 출력값
     * 다른 입력값에서 같은 출력값이 나올 확률은 낮음 0은 아님
     * 현재는 네트워크로 전송된 큰 파일의 무결성 확인
     *
     * @param szAlgorithm 알고리즘 코드
     * @param val         입력 변수
     * @return the byte [ ]
     * @throws java.security.NoSuchAlgorithmException the no such algorithm exception
     */
    public static byte[] digest(String szAlgorithm, byte[] val) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance(szAlgorithm);

        return md.digest(val);
    }

    /**
     * MD5 변환한 byte 자료 문자열로 추출
     *
     * @param a_szSource the a _ sz source
     * @return the crypto mD 5 string
     * @throws Exception the exception
     */
    public static String getCryptoMD5String(String a_szSource) throws Exception {

        if (a_szSource == null) {

            throw new Exception("Can't conver to Message Digest String value!!");
        }

        byte[] bip = digest("MD5", a_szSource.getBytes());
        String eip;
        String result = "";
        int nSize = bip.length;

        for (int i = 0; i < nSize; i++) {

            eip = "" + Integer.toHexString((int) bip[i] & 0x000000ff);

            if (eip.length() < 2) {

                eip = "0" + eip;

            }

            result = result + eip;

        }

        return result;

    }

    public static byte[] encodeBase64(byte[] a_src) throws Exception {

        try {

            return Base64.encodeBase64(a_src);
        } catch (Exception e) {

            throw e;
        }
    }

    public static String encode(String a_src) throws Exception {

        try {

            return new String(encodeBase64(a_src.getBytes("8859_1")));
        } catch (Exception e) {

            throw e;
        }
    }

    public static byte[] decodeBase64(byte[] a_src) throws Exception {

        try {

            return Base64.decodeBase64(a_src);
        } catch (Exception e) {

            throw e;
        }
    }

    public static String decode(String a_src) throws Exception {

        try {

            return new String(decodeBase64(a_src.getBytes("8859_1")));
        } catch (Exception e) {

            throw e;
        }
    }
}
