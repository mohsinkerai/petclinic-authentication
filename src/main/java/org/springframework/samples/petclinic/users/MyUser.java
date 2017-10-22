package org.springframework.samples.petclinic.users;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name="user")
@ToString
@EqualsAndHashCode
@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    private boolean isExpired;

    private boolean isLocked;

    private boolean enabled;

    private String authorities;

    public void setAuthorities(String authorities) {
        Arrays.stream(authorities.split(",")).map(UserAuthority::new)
            .collect(Collectors.toList());
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(",")).map(UserAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
