package me.untaini.sharingmemo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Memos")
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(nullable = false)
    private long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIRECTORIES_ID", nullable = false)
    private Directory directory;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String body = "";

    @UpdateTimestamp
    private Timestamp lastModifiedTimestamp;

    @Builder
    public Memo(Long ownerId, Directory directory, String name, String body) {
        this.ownerId = ownerId;
        this.directory = directory;
        this.name = name;

        if (body != null) {
            this.body = body;
        }

        this.directory.addChildMemo(this);
    }
}
