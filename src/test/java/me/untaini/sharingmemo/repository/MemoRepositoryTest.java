package me.untaini.sharingmemo.repository;

import me.untaini.sharingmemo.entity.Directory;
import me.untaini.sharingmemo.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemoRepositoryTest {

    @Autowired
    private MemoRepository memoRepository;

    @Autowired DirectoryRepository directoryRepository;

    @Test
    public void testSaveAndJoin() {

        Directory rootDir = Directory.builder()
                .name("root")
                .ownerId(1L)
                .build();

        directoryRepository.save(rootDir);

        Memo memo = Memo.builder()
                .ownerId(1L)
                .directory(rootDir)
                .name("test")
                .build();

        Memo savedMemo = memoRepository.save(memo);

        assertThat(savedMemo.getUuid()).isNotNull();
        assertThat(savedMemo.getName()).isEqualTo("test");
        assertThat(savedMemo.getDirectory().getName()).isEqualTo("root");
        assertThat(rootDir.getChildMemos().size()).isEqualTo(1);
    }

}
