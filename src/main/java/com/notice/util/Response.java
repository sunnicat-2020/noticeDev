package com.notice.util;

import com.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Response {
    private boolean isSuccess;
    private String message;


    private Object datas;

}
