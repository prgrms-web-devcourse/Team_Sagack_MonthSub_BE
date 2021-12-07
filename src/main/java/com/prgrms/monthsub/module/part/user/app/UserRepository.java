package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.module.part.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickName);

    @Query("select u from User u join fetch u.part g left join fetch g.permissions gp join fetch gp.permission where u.email = :email")
    Optional<User> findByEmail(String email);

}
