package com.example.demo108.entity;

import lombok.*;

import javax.validation.constraints.AssertTrue;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedParams {
    protected Integer offset=0;
    protected Integer limit=10;

    @AssertTrue
    protected boolean getValidPage() {
        return (offset != null && limit != null) || (offset == null && limit == null);
    }
}