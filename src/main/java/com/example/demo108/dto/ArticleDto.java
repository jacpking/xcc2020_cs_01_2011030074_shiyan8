package com.example.demo108.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ArticleDto {
    private String slug;				// 文章的唯一标识符

    @NotNull
    private String title;				// 文章的标题
    @NotNull
    private String description;			// 文章的简要描述
    @NotNull
    private String body;  				// 文章的正文
    private List<String> tagList;		// 与文章关联的标签列表

    private LocalDateTime createdAt;	// 文章创建的日期和时间
    private LocalDateTime updatedAt;	// 文章上次更新的日期和时间
    private Boolean favorited;			// 用户是否点赞了该文章
    private Long favoritesCount;		// 文章被点赞的次数
    private ProfileDto author;			// 文章作者

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleArticle<T> {
        private T article;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultipleArticle {
        private List<ArticleDto> articles;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String title;
        private String description;
        private String body;
    }
}