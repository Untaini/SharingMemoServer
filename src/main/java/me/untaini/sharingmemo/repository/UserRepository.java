package me.untaini.sharingmemo.repository;

import me.untaini.sharingmemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findBySid(String sid);

}
