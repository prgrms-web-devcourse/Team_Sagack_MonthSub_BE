package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.module.part.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByNickname(String nickName);

  Optional<User> findById(Long id);

  @Query("select u from User u join fetch u.part g left join fetch g.permissions gp join fetch gp.permission where u.email = :email")
  Optional<User> findByEmail(String email);

  List<User> findByIdIn(List<Long> ids);

}
