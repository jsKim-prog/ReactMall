package org.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_todo")
@Getter @ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    private String title;

    private String writer;

    private boolean complete;

    private LocalDate dueDate;

    //method
    public void changeTitle(String title){
        this.title = title;
    }

    public void changeComplete(boolean complete){
        this.complete = complete;
    }

    public void changeDueDate(LocalDate dueDate){
        this.dueDate=dueDate;
    }
}

//Hibernate:
//create table tbl_todo (
//        tno bigint not null auto_increment,
//        complete bit not null,
//        due_date date,
//        title varchar(255),
//writer varchar(255),
//primary key (tno)
//    ) engine=InnoDB