package org.zerock.mallapi.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {
    @Value("${org.zerock.upload.path}")
    private String uploadPath;
    
    @PostConstruct //서버 시작시 실행
    public void init(){
        File tempFolder = new File(uploadPath);
        if(!tempFolder.exists()){
            tempFolder.mkdirs();
        }
        uploadPath = tempFolder.getAbsolutePath();
        log.info("+++++++++++++++++++++++++");
        log.info("FileUtil-업로드 경로:"+uploadPath);
    }
    
    //파일저장 -> 저장명(uuid_원본) 반환
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException{
        if (files == null || files.isEmpty()){
            return List.of();
        }
        List<String> uploadNames = new ArrayList<>();
        for (MultipartFile multipartFile:files){
            String savedName = UUID.randomUUID().toString()+"_"+multipartFile.getOriginalFilename();
            Path savePath = Paths.get(uploadPath, savedName);
            try {
                //파일저장
                Files.copy(multipartFile.getInputStream(), savePath);
                //이미지 여부 확인 후 이미지는 섬네일 저장
                String contentType = multipartFile.getContentType();
                if(contentType!=null&&contentType.startsWith("image")){
                    Path thumbnailPath = Paths.get(uploadPath, "s_"+savedName);
                    Thumbnails.of(savePath.toFile()).size(200,200).toFile(thumbnailPath.toFile());
                }
                uploadNames.add(savedName);
            }catch (IOException e){
                throw new RuntimeException(e.getMessage());
                
            }
        } //--for
        return uploadNames;
    }

    //파일 보여주기 : 파일명 -> 데이터 읽어서 org.springframework.core.io.Resource 타입으로 반환
    public ResponseEntity<Resource> getFile(String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        if(!resource.isReadable()){
            resource = new FileSystemResource(uploadPath+File.separator+"default.jpg");
        }
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type",Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    //서버 파일 삭제
    public void deleteFiles(List<String> fileNames){
        if(fileNames ==null|| fileNames.isEmpty()){
            return;
        }
        fileNames.forEach(fileName->{
            //썸네일 유무 확인 후 삭제
            String thumnailFileName = "s_"+fileName;
            Path thumbnailPath = Paths.get(uploadPath, thumnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);
            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

        });
    }
}
