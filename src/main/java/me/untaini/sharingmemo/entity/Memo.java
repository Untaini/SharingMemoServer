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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIRECTORIES_ID", nullable = false)
    private Directory directory;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String content = "";

    @UpdateTimestamp
    private Timestamp lastModifiedTimestamp;

    @Builder
    public Memo(Long ownerId, Directory directory, String name, String content) {
        this.ownerId = ownerId;
        this.directory = directory;
        this.name = name;

        if (content != null) {
            this.content = content;
        }

        this.directory.addChildMemo(this);
    }

    public String updateName(String afterName) {
        String beforeName = this.name;
        this.name = afterName;
        return beforeName;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
