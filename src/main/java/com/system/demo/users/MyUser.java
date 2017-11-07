package com.system.demo.users;

import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

@Entity(name = "user")
@ToString
@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
public class MyUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    private boolean isExpired;

    private boolean isLocked;

    private boolean enabled;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name = "user_authority",
//        joinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"),
//        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
//    private List<MyUserAuthority> authority;

    @ElementCollection(targetClass = UserAuthority.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING) // Possibly optional (I'm not sure) but defaults to ORDINAL.
    @CollectionTable(name = "user_authority")
    @Column(name = "authority") // Column name in person_interest
    private List<UserAuthority> authorities;

    public void setAuthority(List<UserAuthority> authority) {
        this.authorities = authority;
    }

    @Override
    public List<UserAuthority> getAuthorities() {
        return authorities;
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
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Builder
    public MyUser(Long id, String username, String password, boolean isExpired, boolean isLocked,
        boolean enabled, List<UserAuthority> authority) {
        this.id = id;
        this.username = username;
        this.isExpired = isExpired;
        this.isLocked = isLocked;
        this.enabled = enabled;
        this.authorities = authority;
        this.setPassword(password);
    }

    public void setPassword(String password) {
        Assert.hasLength(password, "password should has some length");
        Assert.isTrue(password.length() > 5, "Minimum password length should be 6");
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
