package com.example.demo108.service;

import com.example.demo108.detail.AuthUserDetails;
import com.example.demo108.dto.ArticleDto;
import com.example.demo108.entity.ArticleQueryParam;
import com.example.demo108.entity.FeedParams;

import java.util.List;

public interface ArticleService {
    List<ArticleDto> listArticle(final ArticleQueryParam articleQueryParam, final AuthUserDetails authUserDetails);

    List<ArticleDto> feedArticles(final AuthUserDetails authUserDetails, final FeedParams feedParams);

    ArticleDto favoriteArticle(final String slug, final AuthUserDetails authUserDetails);

    ArticleDto unfavoriteArticle(final String slug, final AuthUserDetails authUserDetails);
    ArticleDto createArticle(final ArticleDto article, final AuthUserDetails authUserDetails);

    ArticleDto getArticle(final String slug, final AuthUserDetails authUserDetails);

    ArticleDto updateArticle(final String slug, final ArticleDto.Update article, final AuthUserDetails authUserDetails);

    void deleteArticle(final String slug, final AuthUserDetails authUserDetails);

}