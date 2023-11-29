package me.untaini.sharingmemo.repository;

import me.untaini.sharingmemo.entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    @Query("select d from Directory d join fetch d.parentDir")
    List<Directory> findAllByOwnerIdAndParentDirIsNotNull(Long ownerId);
    Directory findByOwnerIdAndParentDirIsNull(Long ownerId);
    void deleteByOwnerIdAndParentDirIsNull(Long ownerId);

}
