package com.example.demo108.service;

import com.example.demo108.detail.AuthUserDetails;
import com.example.demo108.dto.CommentDto;
import com.example.demo108.dto.ProfileDto;
import com.example.demo108.entity.ArticleEntity;
import com.example.demo108.entity.BaseEntity;
import com.example.demo108.entity.CommentEntity;
import com.example.demo108.entity.UserEntity;
import com.example.demo108.ex.AppException;
import com.example.demo108.repository.ArticleRepository;
import com.example.demo108.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo108.ex.Error;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ProfileService profileService;

    @Transactional
    @Override
    public CommentDto addCommentsToAnArticle(String slug, CommentDto comment, AuthUserDetails authUserDetails) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        CommentEntity commentEntity = CommentEntity.builder()
                .body(comment.getBody())
                .author(UserEntity.builder()
                        .id(authUserDetails.getId())
                        .build())
                .article(articleEntity)
                .build();
        commentRepository.save(commentEntity);

        return convertToDTO(authUserDetails, commentEntity);
    }

    @Transactional
    @Override
    public void delete(String slug, Long commentId, AuthUserDetails authUserDetails) {
        Long articleId = articleRepository.findBySlug(slug).map(BaseEntity::getId).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .filter(comment -> comment.getArticle().getId().equals(articleId))
                .orElseThrow(() -> new AppException(Error.COMMENT_NOT_FOUND));

        commentRepository.delete(commentEntity);
    }

    @Override
    public List<CommentDto> getCommentsBySlug(String slug, AuthUserDetails authUserDetails) {
        Long articleId = articleRepository.findBySlug(slug).map(BaseEntity::getId).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        List<CommentEntity> commentEntities = commentRepository.findByArticleId(articleId);
        return commentEntities.stream().map(commentEntity -> convertToDTO(authUserDetails, commentEntity)).collect(Collectors.toList());
    }

    private CommentDto convertToDTO(AuthUserDetails authUserDetails, CommentEntity commentEntity) {
        ProfileDto author = profileService.getProfileByUserId(commentEntity.getAuthor().getId(), authUserDetails);
        return CommentDto.builder()
                .id(commentEntity.getId())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .body(commentEntity.getBody())
                .author(author)
                .build();
    }
}