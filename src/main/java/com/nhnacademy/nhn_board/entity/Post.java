package com.nhnacademy.nhn_board.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="Posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_no")
    private Integer postNo;

    @OneToOne
    @JoinColumn(name = "user_no", referencedColumnName = "user_no")
    private User user;

    @Column(name = "post_title")
    private String title;

    @Column(name = "post_content")
    private String content;

    @Column(name = "post_write_datetime")
    private LocalDateTime writeDateTime;

    @Column(name = "post_modify_datetime")
    private LocalDateTime modifyDateTime;

    @Column(name = "post_check_hide")
    private Boolean checkHide;

    public Post(User user, String title, String content, LocalDateTime writeDateTime, LocalDateTime modifyDateTime, boolean checkHide) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.writeDateTime = writeDateTime;
        this.modifyDateTime = modifyDateTime;
        this.checkHide = checkHide;
    }
}
