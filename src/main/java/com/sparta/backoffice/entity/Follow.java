package com.sparta.backoffice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower")
    private User follower; // 팔로우 받는 사람

    @ManyToOne
    @JoinColumn(name = "following")
    private User following; // 팔로우 하는 사람

    public Follow(User user, User following) {
        this.follower = user;
        this.following = following;
    }
}
