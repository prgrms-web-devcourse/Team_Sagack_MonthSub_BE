package com.prgrms.monthsub.module.part.user.domain;

import com.prgrms.monthsub.common.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User extends BaseEntity {

  public static final int POINT = 10000;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Pattern(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b")
  @Column(name = "email", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
  private String email;

  @Column(name = "username", columnDefinition = "VARCHAR(50)", nullable = false)
  private String username;

  @Column(name = "password", columnDefinition = "VARCHAR(100)", nullable = false)
  private String password;

  @Column(name = "nickname", columnDefinition = "VARCHAR(50)", unique = true)
  private String nickname;

  @Column(name = "profile_key", columnDefinition = "TEXT")
  private String profileKey;

  @Column(name = "profile_introduce", columnDefinition = "VARCHAR(300)")
  private String profileIntroduce;

  @PositiveOrZero
  @Column(name = "point", columnDefinition = "BIGINT")
  private int point;

  @ManyToOne
  @JoinColumn(name = "part_id")
  private Part part;

  @Version
  private Integer version;

  @Builder
  public User(
    String email,
    String username,
    String password,
    String nickname,
    int point,
    Part part
  ) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.point = point;
    this.part = part;
  }

  @Builder
  public User(
    String username,
    String password,
    Part part
  ) {
    this.username = username;
    this.password = password;
    this.part = part;
  }

  public void checkPassword(
    PasswordEncoder passwordEncoder,
    String credentials
  ) {
    if (!passwordEncoder.matches(credentials, password)) {
      throw new BadCredentialsException("Bad credential");
    }
  }

  public void changePart(Part part) {
    this.part = part;
  }

  public void changeProfileKey(String profileKey) {
    this.profileKey = profileKey;
  }

  public void editUser(
    String nickname,
    String profileIntroduce
  ) {
    this.nickname = nickname;
    this.profileIntroduce = profileIntroduce;
  }

}
