package me.untaini.sharingmemo.repository;

import me.untaini.sharingmemo.entity.MemoSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoSessionRepository extends JpaRepository<MemoSession, String> {

    MemoSession findByMemoId(Long memoId);
    Boolean existsByMemoId(Long memoId);
    Boolean existsMemoSessionsByMemoIdIn(List<Long> memoIdList);

}
