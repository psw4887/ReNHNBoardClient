package com.nhnacademy.nhn_board.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_no")
    private Integer commentNo;

    @ManyToOne
    @JoinColumn(name = "post_no")
    private Post post;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "user_no", referencedColumnName = "user_no")
    private User user;

    @Column(name = "comment_content")
    private String content;

    public Comment(Post post, User user, String content) {

        this.post = post;
        this.user = user;
        this.content = content;
    }
}
