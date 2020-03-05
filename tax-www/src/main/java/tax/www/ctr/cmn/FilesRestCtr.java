package tax.www.ctr.cmn;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import tax.www.module.file.CmnCompressBiz;
import tax.www.module.file.CmnFileBiz;
import tax.www.module.secure.filter.CmnFilterBiz;
import tax.www.service.cmn.CmnService;
import tax.www.vo.cmn.files.FilesVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 공통 Rest Controller
 * - 첨부파일 관련 로직
 * <p>
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:30
 */
@RestController
@RequestMapping("/files")
public class FilesRestCtr {

    private static final Logger log = LogManager.getLogger(FilesRestCtr.class);

    @Resource(name = "CmnService")
    private CmnService cmnService;

    @Resource(name = "CmnFileBiz")
    private CmnFileBiz cmnFileBiz;

    /**
     * List map.
     *
     * @param req      HttpServletRequest
     * @param rootPath 시스템 Path
     * @param tableKey 테이블 명
     * @param seq      해당 테이블 고유 코드
     * @param gubunKey 구분 코드
     * @return the map
     */
    @RequestMapping(value = "/getData/{rootPath}/{tableKey}/{seq}/{gubunKey}", method = RequestMethod.GET)
    public Map list(HttpServletRequest req, @PathVariable String rootPath, @PathVariable String tableKey, @PathVariable String seq, @PathVariable String gubunKey) {

        log.debug("uploadGet called");
        List<FilesVO> list = new ArrayList<FilesVO>();

        try {

            list = cmnService.getFileList(tableKey, seq, gubunKey);

            for (FilesVO vo : list) {
                // json 결과 저장
                vo.real_tb = tableKey;
                vo.real_seq = seq;
                vo.gubun_cd = gubunKey;

                vo.setName(vo.file_title);
                vo.setNewFilename(vo.file_nm);
                vo.setContentType(vo.file_type);
                vo.setSize(Long.valueOf(vo.file_size));
                //vo.setUrl("/" + rootPath + fileBiz.fileUploadDirectory + vo.file_url + "/" + vo.file_nm);
                vo.setUrl("/" + rootPath + "/files/download/" + vo.getFiles_seq());

                if (vo.file_type.indexOf("image") > -1) {

                    vo.setThumbnailSize(Long.valueOf(vo.file_size));
                    vo.setThumbnailFilename(vo.file_nm);
                    vo.setThumbnailUrl(vo.getUrl());
                }

                vo.setDeleteUrl("/" + rootPath + "/files/delete/" + vo.getFiles_seq());
                vo.setDeleteType("DELETE");
            }
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        Map<String, Object> files = new HashMap<>();
        files.put("files", list);

        log.debug("Returning: {}", files);

        return files;
    }

    /**
     * File Upload(Single)
     *
     * @param req MultipartHttpServletRequest auto passed
     * @return Map<String, List<FilesVO>> json Format
     */
    @RequestMapping(value = "/singleUpload", method = RequestMethod.POST) // , headers = "Accept=application/json"
    public long singleUpload(MultipartHttpServletRequest req, HttpServletResponse res) {

        log.debug("uploadPost called");

        long ret = 0;

//        List<FilesVO> arrList = new ArrayList<FilesVO>();

        // 1. build an iterator
        Iterator<String> itr = req.getFileNames();
        MultipartFile mf;

        String rootFolder = req.getParameter("rootPath");
        String sysFolder = req.getParameter("systemPath");
        String folFolder = req.getParameter("folPath");

        String filePath = ""; // 프로젝트 경로-파일 Upload Root

        if (StringUtils.isEmpty(sysFolder) == false) {
            filePath += "/" + sysFolder;
        }
        if (StringUtils.isEmpty(folFolder) == false) {
            filePath += "/" + folFolder;
        }

        String storageDirectory = cmnFileBiz.createFolder(req, sysFolder, folFolder);

        //2. get each file
        while (itr.hasNext()) {

            //2.1 get next MultipartFile
            mf = req.getFile(itr.next());
            log.debug("Uploading {}", mf.getOriginalFilename());

            String newFilenameBase = UUID.randomUUID().toString();
            String originalFileExtension = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf("."));
            String newFilename = newFilenameBase + originalFileExtension;

            File newFile = new File(storageDirectory + "/" + newFilename);

            try {

                mf.transferTo(newFile); // 파일 물리경로에 저장

//                // TODO thumbnail 이미지 생성 하는 건데... 우선 제외..
//                BufferedImage thumbnail = Scalr.resize(ImageIO.read(newFile), 60);
//                String thumbnailFilename = newFilenameBase + "-thumbnail.png";
//                File thumbnailFile = new File(storageDirectory + "/" + thumbnailFilename);
//                ImageIO.write(thumbnail, "png", thumbnailFile);

                // JSON으로 리턴 결과 보내기 위한 값 Setting
                FilesVO filesvo = new FilesVO();

                // DB에 저장
                filesvo.file_type = mf.getContentType();
                filesvo.file_title = mf.getOriginalFilename();
                filesvo.file_nm = newFilename;
                filesvo.file_size = String.valueOf(mf.getSize());
                filesvo.file_url = filePath;
                filesvo.real_tb = req.getParameter("tableKey");
                filesvo.real_seq = req.getParameter("seq");
                filesvo.gubun_cd = req.getParameter("gubunKey");

                cmnService.insertFile(filesvo);

                ret = cmnService.getTableMaxSeq("COM_FILES", "files_seq", 0);

                filesvo = null;

            } catch (Exception e) {

                log.error("Could not upload file " + mf.getOriginalFilename(), e);
            }
        }

        return ret;
    }

    /**
     * File Upload(Multi)
     *
     * @param req MultipartHttpServletRequest auto passed
     * @return Map<String, List<FilesVO>> json Format
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST) // , headers = "Accept=application/json"
    public void upload(MultipartHttpServletRequest req, HttpServletResponse res) {

        log.debug("uploadPost called");

        // 1. build an iterator
        Iterator<String> itr = req.getFileNames();
        MultipartFile mf;
        List<FilesVO> list = new LinkedList<>();

        String rootFolder = req.getParameter("rootPath");
        String sysFolder = req.getParameter("systemPath");
        String folFolder = req.getParameter("folPath");

        String filePath = ""; // 프로젝트 경로-파일 Upload Root

        if (StringUtils.isEmpty(sysFolder) == false) {
            filePath += "/" + sysFolder;
        }
        if (StringUtils.isEmpty(folFolder) == false) {
            filePath += "/" + folFolder;
        }

        String storageDirectory = cmnFileBiz.createFolder(req, sysFolder, folFolder);

        //2. get each file
        while (itr.hasNext()) {

            //2.1 get next MultipartFile
            mf = req.getFile(itr.next());
            log.debug("Uploading {}", mf.getOriginalFilename());

            String newFilenameBase = UUID.randomUUID().toString();
            String originalFileExtension = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf("."));
            String newFilename = newFilenameBase + originalFileExtension;

            File newFile = new File(storageDirectory + "/" + newFilename);

            try {

                mf.transferTo(newFile); // 파일 물리경로에 저장

//                // TODO thumbnail 이미지 생성 하는 건데... 우선 제외..
//                BufferedImage thumbnail = Scalr.resize(ImageIO.read(newFile), 60);
//                String thumbnailFilename = newFilenameBase + "-thumbnail.png";
//                File thumbnailFile = new File(storageDirectory + "/" + thumbnailFilename);
//                ImageIO.write(thumbnail, "png", thumbnailFile);

                // JSON으로 리턴 결과 보내기 위한 값 Setting
                FilesVO filesvo = new FilesVO();

                // DB에 저장
                filesvo.file_type = mf.getContentType();
                filesvo.file_title = mf.getOriginalFilename();
                filesvo.file_nm = newFilename;
                filesvo.file_size = String.valueOf(mf.getSize());
                filesvo.file_url = filePath;
                filesvo.real_tb = req.getParameter("tableKey");
                filesvo.real_seq = req.getParameter("seq");
                filesvo.gubun_cd = req.getParameter("gubunKey");

                cmnService.insertFile(filesvo);
                filesvo.setFiles_seq(cmnService.getTableMaxSeq("COM_FILES", "files_seq", 0));

                // json 결과 저장
                filesvo.setName(filesvo.file_title);
                filesvo.setNewFilename(filesvo.file_nm);
                filesvo.setContentType(filesvo.file_type);
                filesvo.setSize(Long.valueOf(filesvo.file_size));
                //filesvo.setUrl(rootFolder + fileBiz.fileUploadDirectory + "/" + filePath + "/" + newFilename);
                filesvo.setUrl(rootFolder + "/files/download/" + filesvo.getFiles_seq());

                if (filesvo.file_type.indexOf("image") > -1) {

                    filesvo.setThumbnailFilename(filesvo.getNewFilename()); // thumbnailFilename
                    filesvo.setThumbnailSize(filesvo.getSize()); // thumbnailFile.length()
                    filesvo.setThumbnailUrl(filesvo.getUrl()); // rootFolder + fileBiz.fileUploadDirectory + "/" + filePath + "/" + thumbnailFilename
                }

                filesvo.setDeleteUrl(rootFolder + "/files/delete/" + filesvo.getFiles_seq());
                filesvo.setDeleteType("DELETE");

                list.add(filesvo);

                filesvo = null;

            } catch (Exception e) {

                log.error("Could not upload file " + mf.getOriginalFilename(), e);
            }
        }

        Map<String, Object> files = new HashMap<>();
        files.put("files", list);

        if (req.getHeader("accept").indexOf("application/json") != -1) {

            res.setContentType("application/json; charset=UTF-8");
        } else {
            // IE workaround
            res.setContentType("text/plain; charset=UTF-8");
            res.setCharacterEncoding("UTF-8");
            res.setHeader("Content-Type", "text/plain; charset=UTF-8");
        }

        // ie9 이하에서 되게 하기 위해서... 아래와 같이 변경.. ㅜ.ㅜ;; 3일 삽질 끝에...
        ObjectMapper mapper = new ObjectMapper();

        try {

            JsonGenerator generator = mapper.getJsonFactory().createJsonGenerator(res.getOutputStream(), JsonEncoding.UTF8);
            mapper.writeValue(generator, files);
            generator.flush();

        } catch (Exception e) {

            log.error(e.toString(), e);
        }
//        return files;
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public void download(HttpServletResponse res, HttpServletRequest req, @PathVariable Long id) {

        try {

            FilesVO files = cmnService.getFileInfo(id.toString());

            if (files != null) {

                String fileExt = files.file_nm.substring(files.file_nm.lastIndexOf("."));

                files.file_nm = CmnFilterBiz.filterFileUrlString(files.file_nm.replace(fileExt, "")); // 파일명에서 경로 관련 특수문자 제거

                String file = cmnFileBiz.getAbsoluteDirectory(req) + files.file_url + "/" + URLDecoder.decode(files.file_nm + fileExt, "UTF-8");

                File f = new File(file);

                if (f.exists()) {
                    // TODO 개선필요, POI 다운로드 작업시 개선.
//                String fileName = URLEncoder.encode(f.getName(), "utf-8");
                    String fileName = URLEncoder.encode(files.file_title, "utf-8");

                    res.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";", fileName));
                    res.setHeader("Content-Transfer-Encoding", "binary");

                    FileCopyUtils.copy(new FileInputStream(f), res.getOutputStream());

                } else {

                    log.debug(String.format("파일없음 : %s", f.getAbsolutePath()));
                }
            } else {

                log.warn("파일없음");
            }

        } catch (Exception e) {

            log.error(e.toString(), e);
        }
    }

    /**
     * File Delete
     *
     * @param req HttpServletRequest
     * @param id  파일 SEQ
     * @return the list
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public List delete(HttpServletRequest req, @PathVariable Long id) {

        boolean isSuccess = false;

        try {

            FilesVO files = cmnService.getFileInfo(id.toString()); // 파일 정보 불러오기

            String fileExt = files.file_nm.substring(files.file_nm.lastIndexOf("."));

            files.file_nm = CmnFilterBiz.filterFileUrlString(files.file_nm.replace(fileExt, ""));

            File f = new File(cmnFileBiz.getAbsoluteDirectory(req) + files.file_url + "/" + files.file_nm + fileExt);

            if (f.exists()) {

                f.delete();
                //        File thumbnailFile = new File(files.file_url + "/" + files.file_nm);
                //        thumbnailFile.delete();
            }

            isSuccess = (cmnService.deleteFile(id.toString()) > 0 ? true : false);
        } catch (Exception e) {

            log.error(e.toString(), e);
        }

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> success = new HashMap<>();
        success.put("success", isSuccess);
        results.add(success);

        return results;
    }

    /**
     * File All Delete
     *
     * @param req HttpServletRequest
     * @param rvo FilesVO
     * @return the int
     */
    @RequestMapping(value = "/deleteAll", method = RequestMethod.POST)
    public int deleteAll(HttpServletRequest req, @RequestBody FilesVO rvo) {

        try {
            // DB에서 파일 찾아서 파일 삭제
            List<FilesVO> delList = cmnService.getFileList(rvo.real_tb, rvo.real_seq, rvo.gubun_cd);

            for (FilesVO vo : delList) {

                try {

                    String fileExt = vo.file_nm.substring(vo.file_nm.lastIndexOf("."));

                    vo.file_nm = CmnFilterBiz.filterFileUrlString(vo.file_nm.replace(fileExt, ""));

                    File f = new File(cmnFileBiz.getAbsoluteDirectory(req) + vo.file_url + "/" + vo.file_nm + fileExt);

                    if (f.exists()) {

                        f.delete(); // 물리 경로 삭제
                        cmnService.deleteFile(vo.getFiles_seq().toString()); // DB 파일 삭제
                    }

                    return 1;
                } catch (Exception e) {

                    log.debug("파일 삭제 실패");
                }
            }
        } catch (Exception e) {

            log.error(e.toString(), e);
        }

        return 0;
    }

    @RequestMapping(value = "/compress", method = RequestMethod.POST)
    public int compress(HttpServletRequest req, @RequestBody FilesVO rvo) {

        try {
            // DB에서 압축 파일 찾아서 파일 삭제
            List<FilesVO> delList = cmnService.getFileList(rvo.real_tb, rvo.real_seq, "Z");

            for (FilesVO vo : delList) {

                try {

                    String fileExt = vo.file_nm.substring(vo.file_nm.lastIndexOf("."));

                    vo.file_nm = CmnFilterBiz.filterFileUrlString(vo.file_nm.replace(fileExt, ""));

                    File f = new File(cmnFileBiz.getAbsoluteDirectory(req) + vo.file_url + "/" + vo.file_nm + fileExt);

                    if (f.exists()) {

                        f.delete(); // 물리 경로 삭제
                        cmnService.deleteFile(vo.getFiles_seq().toString()); // DB 파일 삭제
                    }
                } catch (Exception e) {

                    log.debug("파일 삭제 실패");
                }
            }

            // 압축파일 생성 및 DB 추가
            List<FilesVO> zipList = cmnService.getFileList(rvo.real_tb, rvo.real_seq, rvo.gubun_cd);

            if (zipList.size() > 0) {

                String filePath = "";
                List<File> tmpList = new ArrayList<File>();

                for (FilesVO vo : zipList) {

                    filePath = vo.file_url;
                    tmpList.add(new File(cmnFileBiz.getAbsoluteDirectory(req) + vo.file_url + "/" + vo.file_nm));
                }

                zipList = null;

                if (tmpList.size() > 0) {

                    try {

                        CmnCompressBiz comp = new CmnCompressBiz();

                        File newFile = new File(cmnFileBiz.getAbsoluteDirectory(req) + filePath + "/" + UUID.randomUUID().toString() + ".zip");

                        comp.zip(tmpList, new FileOutputStream(newFile), Charset.defaultCharset().name(), false);

                        FilesVO filesvo = new FilesVO();
                        Long maxSeq = cmnService.getTableMaxSeq("COM_FILES", "files_seq", 1);

                        // DB에 저장
                        filesvo.setFiles_seq(maxSeq);
                        filesvo.file_type = "application/zip";
                        filesvo.file_title = newFile.getName();
                        filesvo.file_nm = filesvo.file_title;
                        filesvo.file_size = String.valueOf(newFile.length());
                        filesvo.file_url = filePath;
                        filesvo.real_tb = rvo.real_tb;
                        filesvo.real_seq = rvo.real_seq;
                        filesvo.gubun_cd = "Z";

                        cmnService.insertFile(filesvo);

                        filesvo = null;
                        tmpList = null;

                        return maxSeq.intValue();
                    } catch (Exception e) {

                        log.error("압축 파일 생성 실패");
                    } finally {

                        tmpList = null;
                    }
                }
            }
        } catch (Exception e) {

            log.error(e.toString(), e);
        }

        return 0;
    }
}
