package com.jamie.home.api.controller;

import com.jamie.home.util.MediaUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/file/*")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Autowired
    private PasswordEncoder encoder;

    @RequestMapping("/download")
    public ResponseEntity<byte[]> filedownload(String path, String name, HttpServletRequest request) throws Exception {
        InputStream in = null;
        ResponseEntity<byte[]> entity = null;
        String header = request.getHeader("User-Agent");
        logger.info("FILE PATH : " + path);
        try {
            String formatName = path.substring(path.lastIndexOf(".") + 1);
            MediaType mType = MediaUtils.getMediaType(formatName);

            HttpHeaders headers = new HttpHeaders();

            in = new FileInputStream(uploadDir + path);

            if (mType != null) {
                //headers.setContentType(mType);
                if (header.contains("MSIE") || header.contains("Trident")) {
                    name = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
                } else {
                    name = new String(name.getBytes("UTF-8"), "ISO-8859-1");
                }
                name = name.substring(name.indexOf("/") + 1);
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.add("Content-Disposition", "attachment; filename=\"" + name + "\"");
            } else {
                if (header.contains("MSIE") || header.contains("Trident")) {
                    name = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
                } else {
                    name = new String(name.getBytes("UTF-8"), "ISO-8859-1");
                }
                name = name.substring(name.indexOf("/") + 1);
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.add("Content-Disposition", "attachment; filename=\"" + name + "\"");
            }
            entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        } finally {
            in.close();
        }
        return entity;
    }
}