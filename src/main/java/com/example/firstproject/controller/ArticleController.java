package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/article/new")
    public String newArticleForm() {
        return "/article/new";
    }

    @PostMapping("/article/create")
    public String createArticle(ArticleForm form) {
        //System.out.println(form.toString());
        log.info(form.toString());

        // 1. DTO를 엔티티로 변환.
        Article article = form.toEntity();

        // 2. 리파지터리로 엔티티를 DB에 저장.
        Article saved = articleRepository.save(article);
        //System.out.println(saved.toString());
        log.info(saved.toString());

        return "";
    }

    @GetMapping("/articles/{id}") //데이터 조회 요청 접수
    public String show(@PathVariable Long id) {
        log.info("id = " + id); //id를 잘 받았는지 확인하는 로그 찍기.
        return "";
    }
}
