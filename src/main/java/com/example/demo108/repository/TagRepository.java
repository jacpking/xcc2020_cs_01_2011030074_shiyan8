package com.example.demo108.repository;

import com.example.demo108.entity.ArticleTagRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<ArticleTagRelationEntity, Long> {
}