package com.nhnacademy.nhn_board.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="Likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_no")
    private Integer likeNo;

    @ManyToOne
    @JoinColumn(name = "post_no", referencedColumnName = "post_no")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_no", referencedColumnName = "user_no")
    private User user;

    public Like(Post post, User user) {

        this.post = post;
        this.user = user;
    }
}
