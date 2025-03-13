package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.service.ProductService;
import org.zerock.mallapi.util.CustomFileUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {
    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    //파일업로드(저장->파일명)
    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO){
        log.info("Product Controller register : "+productDTO);
        //FileUtil로 파일을 먼저 저장
        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        // dto에 저장 파일명 전달
        productDTO.setUploadFileNames(uploadFileNames);
        // service로 dto 등록
        Long pno = productService.register(productDTO);
        return Map.of("result", pno);
    }

    //파일조회(파일명-> resource)
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable String fileName){
        return fileUtil.getFile(fileName);
    }

    //리스트 조회
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO){
        log.info("Product Controller-list 조회++++++++"+pageRequestDTO);
        return productService.getList(pageRequestDTO);
    }

    //상품조회
    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable(name = "pno")Long pno){
        return productService.get(pno);
    }

    //상품정보 변경
    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable(name = "pno") Long pno, ProductDTO productDTO){
        //DB 정보 불러오기 -> 기존파일명 저장
        productDTO.setPno(pno);
        ProductDTO oldDTO = productService.get(pno);
        // 기존 파일명
        List<String> oldFileNames = oldDTO.getUploadFileNames();

        // 신규 업로드 파일리스트
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);//저장완료

        // 화면에서 변화없이 유지된 파일들
        List<String> uploadFileNames = productDTO.getUploadFileNames();
        //저장파일리스트 = 유지파일+신규업로드 파일명
        if(currentUploadFileNames!=null && !currentUploadFileNames.isEmpty()){
            uploadFileNames.addAll(currentUploadFileNames);
        }
        //수정작업
        productService.modify(productDTO);
        if(oldFileNames!=null && !oldFileNames.isEmpty()){
            //지울 파일 목록 찾기
            List<String> removeFiles = oldFileNames.stream().filter(fileName -> uploadFileNames.indexOf(fileName)== -1).collect(Collectors.toList());
            //실제 파일 삭제
            fileUtil.deleteFiles(removeFiles);
        }
        return Map.of("RESULT", "SUCCESS");
    }

    //상품삭제처리
    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable(name = "pno")Long pno){
        //삭제할 파일 찾기
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();
        productService.remove(pno);
        fileUtil.deleteFiles(oldFileNames);
        return Map.of("RESULT", "SUCCESS");
    }

}
