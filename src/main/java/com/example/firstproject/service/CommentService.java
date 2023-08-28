package com.example.firstproject.service;

import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Comment;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service // 서비스로 선언
public class CommentService {
    @Autowired
    private CommentRepository commentRepository; // 댓글 리파지터리 객체 주입
    @Autowired
    private ArticleRepository articleRepository; // 게시글 리파지터리 객체 주입, 대상 게시글(article)의 존재 여부를 파악하기 위해.

    public List<CommentDto> comments(Long articleId) {
        /*
        // 1. 댓글 조회
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        // 2. 엔티티 -> DTO 변환
        List<CommentDto> dtos = new ArrayList<CommentDto>();
        //for(int i=0; i<comments.size(); i++) { // 조회한 댓글 엔티티 수만큼 반복하기
        //    Comment c = comments.get(i); // 조회한 댓글 엔티티 하나씩 가져오기
        for(Comment c : comments) {
            CommentDto dto = CommentDto.createCommentDto(c); // 엔티티를 DTO로 변환
            dtos.add(dto);
        }
        // 3. 결과 반환
        return dtos;
        */

        // 3. 결과 반환
        return commentRepository.findByArticleId(articleId) // 댓글 엔티티 목록 조회
                .stream() // 댓글 엔티티 목록을 스트림으로 변환(컬렉션이나 리스트에 저장된 요소들을 하나씩 참조하여 반복처리시 사용)
                .map(comment -> CommentDto.createCommentDto(comment)) // 엔티티를 DTO로 매핑(스트림에서 요소를 꺼내 조건에 맞게 변환)
                     // .map(a -> b) 는 스트림의 각 요소(a)를 꺼내 b로 수행한 결과로 매핑한다는 의미로,
                     // 결과목록을 스트림으로 하나씩 comment 라는 변수로 정하고 그 변수를 createCommentDto 메서드의 파라미터로 사용함.
                .collect(Collectors.toList()); // 스트림 데이터를 리스트 자료형으로 변환.
    }

    @Transactional
    public CommentDto create(Long articleId, CommentDto dto) {
        // 1. 게시글 조회 및 예외 발생
        Article article = articleRepository.findById(articleId) // 부모 게시글 가져오기
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! 대상 게시글이 없습니다.")); // 없으면 에러 메시지 출력.
        // 2. 댓글 엔티티 생성
        Comment comment = Comment.createComment(dto, article);
        // 3. 댓글 엔티티를 DB에 저장
        Comment created = commentRepository.save(comment);
        // 4. DTO로 변환해 반환
        return CommentDto.createCommentDto(created);
    }
}
