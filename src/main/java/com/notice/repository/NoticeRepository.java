package com.notice.repository;

import com.notice.domain.Attachment;
import com.notice.domain.Notice;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class NoticeRepository {
    @PersistenceContext
    EntityManager em;

    public Optional<Notice> findById(int id) throws Exception {
        return Optional.ofNullable(em.find(Notice.class, id));
    }

    public List<Notice> findAll() throws Exception {
        return em.createQuery("select n from Notice n", Notice.class)
                .getResultList();
    }

    public int save(Notice notice) throws Exception {
        em.persist(notice);
        return notice.getId();
    }

    public int update(Notice notice) throws Exception {
        em.merge(notice);
        return notice.getId();
    }


    public void delete(Notice notice) {
        List<Attachment> list = notice.getFiles();
        for(Attachment attachment : list) {
            em.remove(attachment);
        }
        em.remove(notice);
    }


}
