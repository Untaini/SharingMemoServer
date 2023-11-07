package me.untaini.sharingmemo.repository;

import me.untaini.sharingmemo.entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    List<Directory> findAllByOwnerId(Long ownerId);
    Directory findByOwnerIdAndParentDirIsNull(Long ownerId);
}
