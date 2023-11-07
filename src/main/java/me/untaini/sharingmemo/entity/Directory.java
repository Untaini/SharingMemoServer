package me.untaini.sharingmemo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Directories")
public class Directory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIRECTORIES_ID")
    private Directory parentDir;

    @Column(nullable = false)
    private long ownerId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "parentDir", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Directory> childDirectories = new ArrayList<>();

    @Builder
    public Directory(Directory parentDir, Long ownerId, String name) {
        this.parentDir = parentDir;
        this.ownerId = ownerId;
        this.name = name;

        if (parentDir != null) {
            parentDir.addChildDirectory(this);
        }
    }

    public void addChildDirectory(Directory dir) {
       childDirectories.add(dir);
    }
}