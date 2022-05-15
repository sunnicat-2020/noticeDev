package com.notice.service;

import com.notice.domain.Notice;
import com.notice.repository.NoticeRepository;
import com.notice.util.ResponseNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Transactional
    public int registerOne(Notice notice) throws Exception{
        return noticeRepository.save(notice);
    }

    @Transactional
    public ResponseNotice findOne(int id) throws Exception {
        Notice response = noticeRepository.findById(id)
                .orElseThrow(() -> new Exception("해당 공지사항을 찾지 못했습니다."));

        response.setViewCnt(response.getViewCnt() + 1);
        try {
            noticeRepository.update(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("공지사항 조회수 처리시 문제가 발생했습니다.");
        }

        ResponseNotice pared = ResponseNotice.builder()
                .id(response.getId())
                .title(response.getTitle())
                .content(response.getContent())
                .create_dt(response.getCreateDt().toString())
                .view_count(response.getViewCnt())
                .writer(response.getWriter())
                .build();

        return pared;
    }

    public Notice findOne_noCount(int id) throws Exception {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new Exception("해당 공지사항을 찾지 못했습니다."));
    }

    public List<Notice> findAll() throws Exception {
        return noticeRepository.findAll();
    }

    @Transactional
    public int modifyOne(Notice notice) throws Exception {

        System.out.println(">>> notice.id="+notice.getId());

        Notice orginal = noticeRepository.findById(notice.getId())
                .orElseThrow(() -> new Exception("수정할 공지사항을 찾지 못했습니다."));

        if(notice.getTitle() == null) {
            notice.setTitle(orginal.getTitle());
        }
        if(notice.getWriter() == null) {
            notice.setWriter(orginal.getWriter());
        }
        if(notice.getContent() == null) {
            notice.setContent(orginal.getContent());
        }
        if(notice.getStartDt() == null) {
            notice.setStartDt(orginal.getStartDt());
        }
        if(notice.getEndDt() == null) {
            notice.setEndDt(orginal.getEndDt());
        }
        notice.setViewCnt(orginal.getViewCnt());
        notice.setCreateDt(orginal.getCreateDt());

        return noticeRepository.update(notice);
    }

    @Transactional
    public void removeOne(int id) throws Exception {
        Notice response = noticeRepository.findById(id)
                .orElseThrow(() -> new Exception("해당 공지사항을 찾지 못했습니다."));

        noticeRepository.delete(response);
    }

}
