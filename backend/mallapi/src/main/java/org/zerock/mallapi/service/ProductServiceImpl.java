package org.zerock.mallapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.domain.ProductImage;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("ProductService-getList:+++++++++++++++");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize(), Sort.by("pno").descending() );

        Page<Object[]> result = productRepository.selectList(pageable);

        List<ProductDTO> dtoList = result.get().map(arr->{
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            ProductDTO productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();
            String imgStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imgStr));
            return productDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();
        return PageResponseDTO.<ProductDTO>withAll().dtoList(dtoList).totalCount(totalCount).pageRequestDTO(pageRequestDTO).build();
    }

    @Override
    public Long register(ProductDTO productDTO) {
        Product product = dtoToEntity(productDTO);
        Product result = productRepository.save(product);
        return result.getPno();
    }

    @Override
    public ProductDTO get(Long pno) {
        Optional<Product> result = productRepository.selectOne(pno);
        Product product = result.orElseThrow();
        ProductDTO productDTO = entityToDto(product);
        return productDTO;
    }

    @Override
    public void modify(ProductDTO productDTO) {
        //pno로 db의 entity 정보 불러오기
        Optional<Product> result = productRepository.findById(productDTO.getPno());
        Product product = result.orElseThrow();

        //entity에 변경정보 입력
        product.changeName(productDTO.getPname());
        product.changeDesc(productDTO.getPdesc());
        product.changePrice(product.getPrice());

        //파일리스트 리셋-> 새리스트로 재저장
        product.clearList();
        List<String> uploadFileNames = productDTO.getUploadFileNames();
        if(uploadFileNames!=null&& !uploadFileNames.isEmpty()){
            uploadFileNames.stream().forEach(uploadMame->{product.addImageString(uploadMame);});
        }
        productRepository.save(product);
    }

    @Override
    public void remove(Long pno) {
        productRepository.updateToDelete(pno, true);
    }

    //dto->entity
    private Product dtoToEntity(ProductDTO productDTO){
        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();
        //업로드 완료 파일 이름 리스트
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if(uploadFileNames==null){
            return product;
        }
        uploadFileNames.stream().forEach(uploadName->{
            product.addImageString(uploadName);
        });
        return product;
    }

    //entity -> dto
    private ProductDTO entityToDto(Product product){
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();

        List<ProductImage> imageList = product.getImageList();
        if(imageList==null||imageList.isEmpty()){
            return productDTO;
        }
        List<String> fileNameList = imageList.stream().map(productImage-> productImage.getFileName()).toList();

        productDTO.setUploadFileNames(fileNameList);
        return productDTO;
    }
}
