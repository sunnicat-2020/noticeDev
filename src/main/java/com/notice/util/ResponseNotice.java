package com.notice.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
public class ResponseNotice {
    private int id;
    private String title;
    private String content;
    private String writer;
    private String create_dt;
    private int view_count;
}
