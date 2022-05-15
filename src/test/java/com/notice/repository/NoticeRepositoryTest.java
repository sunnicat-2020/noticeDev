package com.notice.repository;

import com.notice.domain.Attachment;
import com.notice.domain.Notice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NoticeRepositoryTest {
    @Autowired
    NoticeRepository noticeRepository;

    @BeforeEach
    public void beforeEach() {
       // noticeRepository = new NoticeRepository();
    }

    @Test
    @Transactional
    public void 저장() throws Exception{
        // given
        Attachment file1 = Attachment.builder()
                .fileName("file1.pdf")
                .build();
        ArrayList<Attachment> list = new ArrayList<>();
        list.add(file1);

        Notice notice = Notice.builder()
                .title("공지사항")
                .content("내용")
                .writer("작성자")
                .createDt(LocalDateTime.now())
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.parse("2022-06-01 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .files(list)
                .build();

        // when
        int id = noticeRepository.save(notice);
        System.out.println(">>> save id="+id);

        // then
        Notice temp = noticeRepository.findById(id).get();
        System.out.println(">>> find id="+temp.getId()+", "+temp.getContent()+", "+temp.getCreateDt());
        assertThat(temp.getTitle()).isEqualTo(notice.getTitle());

        List<Attachment> files = temp.getFiles();
        files.forEach(f -> System.out.println(">>> file id = "+f.getId()+", "+f.getFileName()));
        //assertThat(tempAttch.getNoticeId()).isEqualTo(id);
    }


}