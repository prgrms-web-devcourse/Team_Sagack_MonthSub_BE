package com.prgrms.monthsub.module.part.user.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Getter
@Table(name = "part")
public class Part {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "part")
  private List<PartPermission> permissions = new ArrayList<>();

  public List<GrantedAuthority> getAuthorities() {
    return permissions.stream()
      .map(gp -> new SimpleGrantedAuthority(gp.getPermission().getName()))
      .collect(toList());
  }

  public enum Name {
    ADMIN_GROUP,
    AUTHOR_GROUP,
    USER_GROUP
  }

}
