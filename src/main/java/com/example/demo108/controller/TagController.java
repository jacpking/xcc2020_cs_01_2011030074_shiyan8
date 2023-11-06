package com.example.demo108.controller;

import com.example.demo108.dto.TagDto;
import com.example.demo108.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public TagDto.TagList listOfTags() {
        return TagDto.TagList.builder().tags(tagService.listOfTags()).build();
    }
}