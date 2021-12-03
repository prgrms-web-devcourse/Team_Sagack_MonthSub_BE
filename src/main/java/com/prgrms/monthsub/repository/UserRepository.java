package com.prgrms.monthsub.repository;

import com.prgrms.monthsub.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.part g left join fetch g.permissions gp join fetch gp.permission where u.email = :email")
    Optional<User> findByEmail(String email);

}
