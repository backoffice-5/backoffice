package com.sparta.backoffice.repository;

import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.entity.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.relation.Role;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByOrderByNickname();

    Optional<User> findByNickname(String nickname);

}
