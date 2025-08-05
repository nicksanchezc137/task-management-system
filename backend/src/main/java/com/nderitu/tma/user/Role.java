package com.nderitu.tma.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.nderitu.tma.user.Permission.TASK_CREATE_ALL;
import static com.nderitu.tma.user.Permission.TASK_DELETE_ALL;
import static com.nderitu.tma.user.Permission.TASK_READ_ALL;
import static com.nderitu.tma.user.Permission.TASK_UPDATE_ALL;
import static com.nderitu.tma.user.Permission.TASK_ASSIGN;
import static com.nderitu.tma.user.Permission.TASK_READ_OWN;
import static com.nderitu.tma.user.Permission.TASK_UPDATE_OWN;
import static com.nderitu.tma.user.Permission.TASK_CREATE;
import static com.nderitu.tma.user.Permission.USER_READ_ALL;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

  USER(
          Set.of(
                  TASK_READ_OWN,
                  TASK_UPDATE_OWN,
                  TASK_CREATE,
                  USER_READ_ALL
          )
  ),
  ADMIN(
          Set.of(
                  TASK_READ_ALL,
                  TASK_UPDATE_ALL,
                  TASK_DELETE_ALL,
                  TASK_CREATE_ALL,
                  TASK_ASSIGN,
                  USER_READ_ALL
          )
  );

  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
