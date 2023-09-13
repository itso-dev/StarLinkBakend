package com.jamie.home.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.model.common.FILE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static FILE fileUpload(MultipartFile file, String uploadDir) throws Exception {
        FILE result = new FILE();
        String uuid = UUID.randomUUID().toString();
        String oriName = file.getOriginalFilename();
        String fileType = oriName.substring(oriName.indexOf("."));
        String path = upload(uploadDir, uuid+fileType, file.getBytes());
        result.setName(file.getOriginalFilename());
        result.setUuid(uuid);
        result.setPath(path);
        return result;
    }

    public static String upload(String uploadPath, String originalName, byte[] fileData)throws Exception{

        String savedPath = calcPath(uploadPath);

        File target = new File(uploadPath + savedPath, originalName);

        FileCopyUtils.copy(fileData, target);

        return savedPath + File.separator + originalName;
    }

    private static String calcPath(String uploadPath){

        Calendar cal = Calendar.getInstance();

        String yearPath = File.separator+cal.get(Calendar.YEAR);

        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);

        String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

        makeDir(uploadPath, yearPath, monthPath, datePath);

        logger.info(datePath);

        return datePath;
    }

    private static void makeDir(String uploadPath, String... paths){

        if(new File(paths[paths.length-1]).exists()){
            return;
        }

        for (String path : paths) {

            File dirPath = new File(uploadPath + path);

            if(! dirPath.exists() ){
                dirPath.mkdir();
            }
        }
    }

    public static void deleteFile(String fileName, String uploadDir) throws Exception {

        logger.info("fileDelete start.....");

        new File(uploadDir + fileName.replace('/', File.separatorChar)).delete();
    }

    public static String saveFiles(ArrayList<MultipartFile> saveFiles, String uploadDir) {
        logger.info("saveFiles start.....");
        String result = "[]";
        try {
            if(saveFiles != null){
                List<FILE> fileList = new ArrayList<>();
                for(MultipartFile file : saveFiles){
                    if(file.getSize() != 0){
                        FILE fileVo = fileUpload(file, uploadDir);
                        fileList.add(fileVo);
                    }
                }

                if(fileList.size() != 0){
                    result = mapper.writeValueAsString(fileList);
                } else {
                    // 모든 input file에서 파일을 선택하지 않은경우
                }
            } else {
                // file input이 없는 경우
            }
        } catch (Exception e){
            result = "[]";
            logger.error("error save files.....");
        }

        return result;
    }

    public static String modiFiles(String ori_files, String deleteFiles, ArrayList<MultipartFile> saveFiles, String uploadDir){
        String result = "[]";
        // 사진 수정
        List<FILE> fileList = new ArrayList<>();
        try {
            if(ori_files != null && !"[]".equals(ori_files)){
                fileList = Arrays.asList(mapper.readValue(ori_files, FILE[].class));
                for(FILE file : fileList){
                    file.setDel(false);
                }
            }
            List<FILE> newFileList = new ArrayList<>();
            if(deleteFiles != null){
                // 기존 파일 삭제
                List<FILE> delFiles = Arrays.asList(mapper.readValue(deleteFiles, FILE[].class));
                for(FILE delFile : delFiles){
                    deleteFile(delFile.getPath(),uploadDir);
                    for (int i=0; i<fileList.size(); i++){
                        if(fileList.get(i).getPath().equals(delFile.getPath())){
                            fileList.get(i).setDel(true);
                            break;
                        }
                    }
                }
            }

            for(FILE file : fileList){
                if(!file.getDel()){
                    newFileList.add(file);
                }
            }

            if(saveFiles != null){
                for(MultipartFile file : saveFiles){
                    if(file.getSize() != 0){
                        FILE fileVo = FileUtils.fileUpload(file, uploadDir);
                        newFileList.add(fileVo);
                    }
                }
            }

            if(newFileList.size() != 0){
                result = mapper.writeValueAsString(newFileList);
            } else {
                // 파일을 다 삭제하고 추가하지 않은 경우
            }
        } catch (Exception e){
            result = "[]";
            logger.error("error modi files.....");
        }
        return result;
    }

    public static String modiOneFiles(String deleteFiles, ArrayList<MultipartFile> saveFiles, String uploadDir){
        String result = "[]";
        try {
            if(deleteFiles != null){
                // 기존 파일 삭제
                List<FILE> delFiles = Arrays.asList(mapper.readValue(deleteFiles, FILE[].class));
                for(FILE delFile : delFiles){
                    deleteFile(delFile.getPath(),uploadDir);
                }
            }
            if(saveFiles != null){
                List<FILE> fileList = new ArrayList<>();
                for(MultipartFile file : saveFiles){
                    if(file.getSize() != 0){
                        FILE fileVo = fileUpload(file, uploadDir);
                        fileList.add(fileVo);
                    }
                }

                if(fileList.size() != 0){
                    result = mapper.writeValueAsString(fileList);
                } else {
                    // 모든 input file에서 파일을 선택하지 않은경우
                }
            } else {
                // file input이 없는 경우
            }
        } catch (Exception e){
            result = "[]";
            logger.error("error modi one files.....");
        }

        return result;
    }
}
