package org.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;
    private int price;
    private String pdesc;
    private boolean delFlag;

    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    //method
    public void changePrice(int price){
        this.price = price;
    }
    public void changeDesc(String desc){
        this.pdesc = desc;
    }
    public void changeName(String name){
        this.pname = name;
    }

    public void addImage(ProductImage image){
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName){
        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .build();
        addImage(productImage);
    }

    public void clearList(){
        this.imageList.clear();
    }
}
//Hibernate:
//create table tbl_product (
//        pno bigint not null auto_increment,
//        del_flag bit not null,
//        pdesc varchar(255),
//pname varchar(255),
//price integer not null,
//primary key (pno)
//    ) engine=InnoDB
//Hibernate:
//alter table if exists product_image_list
//add constraint FKfqvvs4dg13jiki1fur4s3qa43
//foreign key (product_pno)
//references tbl_product (pno)