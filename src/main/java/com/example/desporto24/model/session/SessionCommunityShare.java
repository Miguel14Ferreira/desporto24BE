package com.example.desporto24.model.session;

import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A public session posted to a community. Does not grant community membership
 * or create another copy of the session, participants, payments or rankings. */
@Entity
@Table(name = "session_community_share", schema = "desporto24",
       uniqueConstraints = @UniqueConstraint(name = "uq_session_community_share",
               columnNames = {"session_id", "community_id"}))
@Getter @Setter @NoArgsConstructor
public class SessionCommunityShare {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    public SessionCommunityShare(Session session, Community community) {
        this.session = session;
        this.community = community;
    }
}
