package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository; //게시글 리파지터리 객체 주입

    public List<Article> index() {
        return articleRepository.findAll();
    }
    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        if(article.getId() != null) {
            return null;
        }
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        // 1. DTO -> 엔티티 변환하기
        Article article = dto.toEntity();
        log.info("id: {}, article: {}", id, article.toString());
        // 2. 타깃 조회하기
        Article target = articleRepository.findById(id).orElse(null);
        // 3. 잘못된 요청 처리하기
        if (target == null || !id.equals(article.getId())) {
            // 400, 잘못된 요청 응답!
            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
            return null;
        }
        // 4. 업데이트 및 정상 응답(200)하기
        target.patch(article);
        Article updated = articleRepository.save(target);
        return updated;
    }

    public Article delete(Long id) {
        // 1. 대상 찾기
        Article target = articleRepository.findById(id).orElse(null);
        // 2. 잘못된 요청 처리하기
        if (target == null) {
            return null;
        }
        // 3. 대상 삭제하기
        articleRepository.delete(target);
        return target;
    }

    @Transactional //begin tran, 종료때까지 에러가 없으면 commit, 에러 발생하면 rollback.
    public List<Article> createArticles(List<ArticleForm> dtos) {
        // 1. dto 묶음을 엔티티 묶음으로 변환하기
        List<Article> articleList = dtos.stream() // dto를 스트림화한다.
                .map(dto -> dto.toEntity()) // map()으로 dto가 하나나 올 때마다 dto.toEntity()를 수행해 매핑한다.
                .collect(Collectors.toList()); // 매핑한 것을 리스트로 묶는다.
        // 스트림(stream) 문법은 리스트와 같은 자료구조에 저장된 요소를 하나씩 순회하면서 처리하는 코드 패턴이다.
        // 위 코드를 for()문으로 작성하면 다음과 같이 총 6행에 걸쳐 작성된다.
        // 이를 스트림 문법으로 작성하면 3해으로 줄일 수 있다.
        // List<Article> articleList = new ArrayList<>();
        // for(int i=0; i<dtos.size(); i++) {
        //     ArticleForm dto = dtos.get(i);
        //     Article entity = dto.toEntity();
        //     articleList.add(entity);
        // }

        // 2. 엔티티 묶음을 DB에 저장하기
        articleList.stream().forEach(article -> articleRepository.save(article));
        // 이 코드도 for() 문으로 작성하면 다음과 같다. 하지만 위와 같이 작성하는게 훨씬 효츌적이다.
        // for(int i=0; i<articleList.size(); i++) {
        //     Article article = articleList.get(i);
        //     articleRepository.save(article);
        // }

        // 3. 강제 예외 발생시키기
        articleRepository.findById(-1L) // id가 -1인 데이터 찾기
                .orElseThrow(() -> new IllegalArgumentException("결과 실패!")); // 찾는 데이터가 없으면 예외 발생.

        // 4. 결과 값 반환하기
        return articleList;
    }
}
