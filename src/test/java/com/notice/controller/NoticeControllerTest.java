package com.notice.controller;

import com.notice.domain.Attachment;
import com.notice.domain.Notice;
import com.notice.service.NoticeService;
import com.notice.util.ReqNoticeObj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(NoticeController.class)
@DisplayName("공지사항 API 테스트")
class NoticeControllerTest {

    MockMvc mockMvc;

    @MockBean
    NoticeService noticeService;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(new NoticeController(noticeService))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    void 전체조회() throws Exception {
        // given


        // when
        ResultActions resultActions = mockMvc.perform(get("/notice")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("datas").isArray());
    }

    @Test
    void 등록() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc
                .perform(RestDocumentationRequestBuilders
                        .multipart("/notice")
                        .file(getTestFile("test1.txt"))
                        .param("notice", ("{\"title\": \"테스트 타이틀\" ,"
                                + "\"content\": \"테스트 입니다.\" , "
                                + "\"writer\": \"홍길동\", "
                                + "\"start-dt\": \"2022-05-15 09:00:00\""
                                + "\"end-dt\": \"2022-06-01 20:00:00\"}"))
                );

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("isSuccess").value(true))
                .andExpect(jsonPath("id").isNumber());
    }

    private MockMultipartFile getTestFile(String fileName) throws Exception {
        Path path = Paths.get("D:\\test\\"+fileName);
        if(!Files.exists(path)) {
            Files.createFile(path);
            Files.write(path, "테스트 파일입니다.".getBytes(StandardCharsets.UTF_8));
        }
        return new MockMultipartFile("file", "D:\\test\\"+fileName, "text/plain", "테스트 파일입니다.".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void 수정() throws Exception {
        // given
        Notice notice = Notice.builder()
                .title("테스트 타이틀")
                .content("테스트 입니다.")
                .writer("홍길동")
                .createDt(LocalDateTime.now())
                .startDt(LocalDateTime.parse("2022-05-15 09:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .endDt(LocalDateTime.parse("2022-06-01 18:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        int id = noticeService.registerOne(notice);
        System.out.println(">>> id="+id);


        // when
        ResultActions resultActions = mockMvc
                .perform(put("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\""+id+"\", \"content\": \"수정합니다.\"}")
                        .characterEncoding("UTF-8"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("datas").isArray());
    }

    @Test
    void 삭제() throws Exception {
        // given


        // when
        ResultActions resultActions = mockMvc.perform(get("/notice")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("datas").isArray());
    }
}