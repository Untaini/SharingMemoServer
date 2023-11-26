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
    private String uuid;

    @Column(nullable = false, unique = true)
    private Long memoId;

}
