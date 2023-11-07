package me.untaini.sharingmemo.repository;

import me.untaini.sharingmemo.entity.Directory;
import me.untaini.sharingmemo.entity.Memo;
import me.untaini.sharingmemo.entity.MemoSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemoSessionRepositoryTest {

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private MemoSessionRepository memoSessionRepository;

    @Test
    public void testSaveAndFind() {
        Directory directory = Directory.builder()
                .ownerId(1L)
                .name("root")
                .build();

        directoryRepository.save(directory);

        Memo memo = Memo.builder()
                .ownerId(1L)
                .name("test_memo")
                .body("test_message")
                .directory(directory)
                .build();

        memoRepository.save(memo);

        MemoSession memoSession = MemoSession.builder()
                .ownerId(1L)
                .memoUuid(memo.getUuid())
                .build();

        MemoSession savedMemoSession = memoSessionRepository.save(memoSession);

        assertThat(savedMemoSession.getMemoUuid()).isEqualTo(memo.getUuid());

        MemoSession foundMemoSession = memoSessionRepository.findByMemoUuid(memo.getUuid());

        assertThat(foundMemoSession.getUuid()).isEqualTo(savedMemoSession.getUuid());
        assertThat(memoSessionRepository.existsByMemoUuid(memo.getUuid())).isTrue();
    }

}
