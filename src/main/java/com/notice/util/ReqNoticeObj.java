package com.notice.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class ReqNoticeObj {
    private String title;
    private String content;
    private String writer;
    private String start_dt;
    private String end_dt;
}
