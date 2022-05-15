package com.notice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table
public class Notice {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "notice_id")
        private int id;

        private String title;

        private String content;

        private String writer;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Column(name = "start_dt")
        private LocalDateTime startDt;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Column(name = "end_dt")
        private LocalDateTime endDt;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Column(name = "create_dt")
        private LocalDateTime createDt;

        @Builder.Default
        @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private List<Attachment> files = new ArrayList<>();

        @Column(name = "view_count")
        private int viewCnt;

        public void addFiles(final Attachment attachment) {
                files.add(attachment);
                attachment.setNotice(this);
        }

        public void removeFile(final Attachment attachment) {
                files.remove(attachment);
                attachment.setNotice(null);
        }

        public void removeFileAll() {
                files.removeAll(files);
        }
}
