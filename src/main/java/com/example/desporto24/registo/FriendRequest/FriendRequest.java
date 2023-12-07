package com.example.desporto24.registo.FriendRequest;
import com.example.desporto24.model.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FriendRequest {
    @SequenceGenerator( name = "friend_request_sequence",
            sequenceName = "friend_request_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "friend_request_sequence")
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private String createdAt;

    private String confirmedAt;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Perfil perfil1;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Perfil perfil2;

    public FriendRequest(String token, String createdAt, Perfil perfil1, Perfil perfil2) {
        this.token = token;
        this.createdAt = createdAt;
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
    }

    public FriendRequest(String token) {
        this.token = token;
    }
}
