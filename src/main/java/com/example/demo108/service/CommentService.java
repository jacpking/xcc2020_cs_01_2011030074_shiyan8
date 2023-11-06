package com.example.demo108.service;

import com.example.demo108.detail.AuthUserDetails;
import com.example.demo108.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addCommentsToAnArticle(final String slug, final CommentDto comment, final AuthUserDetails authUserDetails);

    void delete(final String slug, final Long commentId, final AuthUserDetails authUserDetails);

    List<CommentDto> getCommentsBySlug(final String slug, final AuthUserDetails authUserDetails);
}