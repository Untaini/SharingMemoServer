package me.untaini.sharingmemo.repository;

import me.untaini.sharingmemo.entity.Directory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DirectoryRepositoryTest {

    @Autowired
    private DirectoryRepository directoryRepository;

    @Test
    public void testSaveAndJoin() {

        Directory rootDir = Directory.builder()
                .name("root")
                .ownerId(1L)
                .build();

        Directory subDir = Directory.builder()
                .name("sub")
                .ownerId(1L)
                .parentDir(rootDir)
                .build();

        Directory savedRootDir = directoryRepository.save(rootDir);
        Directory savedSubDir = directoryRepository.save(subDir);

        assertThat(savedRootDir.getId()).isNotNull();
        assertThat(savedRootDir.getName()).isEqualTo("root");
        assertThat(savedRootDir.getChildDirectories().get(0).getName()).isEqualTo("sub");
        assertThat(savedRootDir.getId()).isGreaterThan(0);

        assertThat(savedSubDir.getId()).isNotNull();
        assertThat(savedSubDir.getName()).isEqualTo("sub");
        assertThat(savedSubDir.getParentDir().getName()).isEqualTo(rootDir.getName());
        assertThat(savedSubDir.getId()).isGreaterThan(0);

        Directory userRootDir = directoryRepository.findByOwnerIdAndParentDirIsNull(1L);

        assertThat(userRootDir.getName()).isEqualTo("root");

    }
}
