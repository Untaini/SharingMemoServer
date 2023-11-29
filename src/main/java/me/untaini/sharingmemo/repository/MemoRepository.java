package me.untaini.sharingmemo.repository;

import me.untaini.sharingmemo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findAllByDirectory_IdIn(List<Long> DirectoryIdList);
}
