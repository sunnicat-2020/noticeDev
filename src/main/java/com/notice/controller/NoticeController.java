package com.notice.controller;

import com.notice.domain.Attachment;
import com.notice.domain.Notice;
import com.notice.service.NoticeService;
import com.notice.util.Response;
import com.notice.util.ResponseNotice;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Response register(@RequestPart(value="notice") String noticeJson,
                         @RequestPart(value="file", required = false) List<MultipartFile> files) {
        // 등록
        int id = -1;
        try {
            System.out.println(">>> reqNoticeObj = "+noticeJson);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(noticeJson);

            String title = Optional.of(jsonObject.get("title"))
                    .orElseThrow(() -> new Exception("공지사항 타이틀을 입력하세요.")).toString();
            String content = Optional.of(jsonObject.get("content"))
                    .orElseThrow(() -> new Exception("공지사항 내용을 입력하세요.")).toString();
            String writer = Optional.of(jsonObject.get("writer"))
                    .orElseThrow(() -> new Exception("공지사항 작성자를 입력하세요.")).toString();
            String startDt = Optional.of(jsonObject.get("start_dt"))
                    .orElseThrow(() -> new Exception("공지사항 시작일시를 입력하세요.")).toString();
            String endDt = Optional.of(jsonObject.get("end_dt"))
                    .orElseThrow(() -> new Exception("공지사항 종료일시를 입력하세요.")).toString();

            Notice notice = Notice.builder()
                    .title(title)
                    .content(content)
                    .writer(writer)
                    .startDt(LocalDateTime.parse(startDt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .endDt(LocalDateTime.parse(endDt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .createDt(LocalDateTime.now())
                    .viewCnt(0)
                    .build();
            if(files != null && !files.isEmpty()) {
                for (MultipartFile mpf : files) {
                    System.out.println(">>> fileName="+mpf.getOriginalFilename());
                    System.out.println(">>> content="+new String(mpf.getBytes()));
                    Attachment attachment = Attachment.builder()
                            .fileName(mpf.getOriginalFilename())
                            .fileContent(mpf.getBytes())
                            .build();
                    notice.addFiles(attachment);
                }
            }
            id = noticeService.registerOne(notice);
            System.out.println(">>> id="+id);
        } catch(Exception e) {
            e.printStackTrace();
            return Response.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
        }
        return Response.builder()
                .isSuccess(true)
                .datas(id)
                .message("공지사항이 등록되었습니다.["+id+"]")
                .build();
    }

    @PutMapping
    public Response modify(@RequestPart(value="notice") String noticeJson,
                           @RequestPart(value="file", required = false) List<MultipartFile> files) {
        // 수정
        System.out.println(">>> reqNoticeObj = "+noticeJson);
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(noticeJson);

            String id = Optional.of(jsonObject.get("id"))
                    .orElseThrow(() -> new Exception("수정할 공지사항 아이디를 입력하세요.")).toString();

            Notice notice = Notice.builder()
                    .id(Integer.parseInt(id))
                    .title(jsonObject.get("title") == null ? null : jsonObject.get("title").toString())
                    .content(jsonObject.get("content") == null ? null : jsonObject.get("content").toString())
                    .writer(jsonObject.get("writer") == null ? null : jsonObject.get("writer").toString())
                    .build();
            if(jsonObject.get("start_dt") != null) {
                notice.setStartDt(LocalDateTime.parse(jsonObject.get("start_dt").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if(jsonObject.get("end_dt") != null) {
                notice.setStartDt(LocalDateTime.parse(jsonObject.get("end_dt").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if(files != null && !files.isEmpty()) {
                for (MultipartFile mpf : files) {
                    System.out.println(">>> fileName="+mpf.getOriginalFilename());
                    System.out.println(">>> content="+new String(mpf.getBytes()));
                    Attachment attachment = Attachment.builder()
                            .fileName(mpf.getOriginalFilename())
                            .fileContent(mpf.getBytes())
                            .build();
                    notice.addFiles(attachment);
                }
            }

            noticeService.modifyOne(notice);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
        }
        return Response.builder()
                .isSuccess(true)
                .message("수정했습니다.")
                .build();
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable int id) {
        //삭제
        try {
            noticeService.removeOne(id);
        } catch (Exception e) {
            return Response.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
        }
        return Response.builder()
                .isSuccess(true)
                .message("삭제했습니다.")
                .build();
    }

    @GetMapping("/{id}")
    public Response search(@PathVariable int id) {
        // 조회
        ResponseNotice notice;
        try {
            notice = noticeService.findOne(id);
        } catch (Exception e) {
            return Response.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
        }
        return Response.builder()
                .isSuccess(true)
                .message("조회에 성공했습니다.")
                .datas(notice)
                .build();
    }

    @GetMapping()
    public Response search() {
        // 전체 공지사항 조회
        List<Notice> notices;
        List<ResponseNotice> responseNotices = new ArrayList<>();
        try {
            notices = noticeService.findAll();
            for(Notice notice : notices) {
                ResponseNotice pared = ResponseNotice.builder()
                        .id(notice.getId())
                        .title(notice.getTitle())
                        .content(notice.getContent())
                        .create_dt(notice.getCreateDt().toString())
                        .view_count(notice.getViewCnt())
                        .writer(notice.getWriter())
                        .build();
                responseNotices.add(pared);
            }
        } catch (Exception e) {
            return Response.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
        }
        return Response.builder()
                .isSuccess(true)
                .message("총 "+notices.size()+"건의 조회에 성공했습니다.")
                .datas(responseNotices)
                .build();
    }

}
