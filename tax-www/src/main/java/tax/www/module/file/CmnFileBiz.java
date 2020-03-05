package tax.www.module.file;

import egovframework.com.cmn.service.EgovProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


/**
 * 파일 관리 Business 로직
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:58
 */
@Component("CmnFileBiz")
public class CmnFileBiz {

    private static final Logger log = LogManager.getLogger(CmnFileBiz.class);

    //@Value("#{cfg['file.upload.directory']}")
    public String fileUploadDirectory = EgovProperties.getProperty("file.upload.directory");

    public long MAX_FILE_SIZE = Long.valueOf(EgovProperties.getProperty("file.upload.maxsize"));

    /**
     * Gets absolute directory.
     *
     * @param req the req
     * @return the absolute directory
     */
    public String getAbsoluteDirectory(MultipartHttpServletRequest req) {

        return fileUploadDirectory;
        //return req.getSession().getServletContext().getRealPath(fileUploadDirectory);
    }

    /**
     * Gets absolute directory.
     *
     * @param req the req
     * @return the absolute directory
     */
    public String getAbsoluteDirectory(HttpServletRequest req) {

        return fileUploadDirectory;
        //return req.getSession().getServletContext().getRealPath(fileUploadDirectory);
    }

    /**
     * 폴더 생성
     *
     * @param gFolder   시스템 구분 폴더(ex : oms, fms...)
     * @param subFolder 서브 폴더
     * @return String 최종 폴더
     */
    public String createFolder(MultipartHttpServletRequest req, String gFolder, String subFolder) {

        // ROOT 저장경로 체크
        String targetPath = getAbsoluteDirectory(req);

        return createFolder(targetPath, gFolder, subFolder);
    }

    /**
     * 폴더 생성
     *
     * @param targetPath 설정 된 root 폴더
     * @param gFolder    시스템 구분 폴더(ex : oms, fms...)
     * @param subFolder  서브 폴더
     * @return String 최종 폴더
     */
    public String createFolder(String targetPath, String gFolder, String subFolder) {

        File savePath = new File(targetPath);

        if (!savePath.exists()) {
            savePath.mkdir();
        }

        if (StringUtils.isEmpty(gFolder) == false) {

            savePath = new File(savePath.getAbsolutePath() + File.separator + gFolder);

            if (!savePath.exists()) {
                savePath.mkdir();
            }
        }

        if (StringUtils.isEmpty(subFolder) == false) {

            savePath = new File(savePath.getAbsolutePath() + File.separator + subFolder);

            if (!savePath.exists()) {
                savePath.mkdir();
            }
        }

        return savePath.getAbsolutePath();
    }
}