package me.untaini.sharingmemo.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "MemoSessions")
public class MemoSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(nullable = false, unique = true)
    private String memoUuid;

    @Column(nullable = false)
    private long ownerId;

}
