package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ArticleApiController {

    @Autowired
    private ArticleRepository articleRepository;

    // GET
    // API Test에서 GET http://localhost:8080/api/articles Send
    @GetMapping("/api/articles")
    public List<Article> index() {
        return articleRepository.findAll();
    }

    // API Test에서 GET http://localhost:8080/api/articles/1 Send
    @GetMapping("/api/articles/{id}")
    public Article show(@PathVariable Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    // POST
    // API Test에서 POST http://localhost:8080/api/articles , BODY 에 { "title":"AAAAAAA", "content":"123123123" } Send.
    @PostMapping("/api/articles")
    public Article create(@RequestBody ArticleForm dto) {
        Article article = dto.toEntity();
        return articleRepository.save(article);
    }

    // PATCH
    // API Test에서 PATCH http://localhost:8080/api/articles/1 , BODY 에 { "id":1, "title":"AAAAAAA", "content":"123123123" } Send.
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody ArticleForm dto) {
        // 1. DTO -> 엔티티 변환하기
        Article article = dto.toEntity();
        log.info("id: {}, article: {}", id, article.toString());
        // 2. 타깃 조회하기
        Article target = articleRepository.findById(id).orElse(null);
        // 3. 잘못된 요청 처리하기
        if(target == null || !id.equals(article.getId())) {
            // 400, 잘못된 요청 응답!
            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        // 4. 업데이트 및 정상 응답(200)하기
        Article updated = articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // DELETE
}
