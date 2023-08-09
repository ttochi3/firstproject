package com.example.firstproject.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
@Setter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 id 자동생성
    private Long id;

    @Column
    private String title;
    @Column
    private String content;

    public Article(Object o, String title, String content) {
    }

    public void patch(Article article) {
        if(article.title != null) this.title = article.title;
        if(article.content != null) this.content = article.content;
    }
}
