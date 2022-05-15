package com.notice.service;

import com.notice.domain.Attachment;
import com.notice.domain.Notice;
import com.notice.util.ResponseNotice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class NoticeServiceTest {
    @Autowired NoticeService noticeService;

    @Test
    public void 조회() throws Exception {
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
        ResponseNotice response = noticeService.findOne(id);

        // then
        assertThat(response.getTitle()).isEqualTo("테스트 타이틀");
        assertThat(response.getContent()).isEqualTo("테스트 입니다.");
        assertThat(response.getWriter()).isEqualTo("홍길동");
        assertThat(response.getView_count()).isNotNull();
        assertThat(response.getCreate_dt()).isNotNull();
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void 등록() throws Exception {
        // given
        Attachment attachment = getTestFile();
        Notice notice = Notice.builder()
                .title("테스트 타이틀")
                .content("테스트 입니다.")
                .writer("홍길동")
                .createDt(LocalDateTime.now())
                .startDt(LocalDateTime.parse("2022-05-15 09:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .endDt(LocalDateTime.parse("2022-06-01 18:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        notice.addFiles(attachment);

        // when
        int id = noticeService.registerOne(notice);
        System.out.println(">>> id="+id);

        Notice response = noticeService.findOne_noCount(id);
        List<Attachment> responseList = response.getFiles();

        // then
        assertThat(response.getTitle()).isEqualTo("테스트 타이틀");
        assertThat(response.getContent()).isEqualTo("테스트 입니다.");
        assertThat(response.getWriter()).isEqualTo("홍길동");
        assertThat(responseList.size()).isEqualTo(1);
        assertThat(responseList.get(0).getFileName()).isEqualTo(attachment.getFileName());
    }

    @Test
    public void 수정() throws Exception {
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
        Notice modNotice = Notice.builder()
                .id(id)
                .content("수정합니다.")
                .build();
        int mid = noticeService.modifyOne(modNotice);

        // then
        assertThat(mid).isEqualTo(id);
    }



    @Test
    @Transactional
    @Rollback(value = false)
    public void 삭제() throws Exception {
        // given
        // given
        List<Notice> responseNotices = noticeService.findAll();
        assertThat(responseNotices.size()).isGreaterThan(0);

        Notice notice = responseNotices.get(0);

        // when
        noticeService.removeOne(notice.getId());

        // then
        try {
            noticeService.findOne_noCount(notice.getId());
        } catch (Exception e) {
            e.printStackTrace();
            assertThat("해당 공지사항을 찾지 못했습니다.").isEqualTo(e.getMessage());
        }
    }


    private Attachment getTestFile() throws Exception {
        Path path = Paths.get("D:\\test1.txt");
        if(!Files.exists(path)) {
            Files.createFile(path);
            Files.write(path, "테스트 파일입니다.".getBytes(StandardCharsets.UTF_8));
        }
        byte[] fileContent = Files.readAllBytes(path);

        Attachment attachment = Attachment.builder()
                .fileName(path.toString())
                .fileContent(fileContent)
                .build();
        return attachment;
    }
}