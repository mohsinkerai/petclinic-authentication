package com.system.demo.users;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
//@Entity(name = "authority")
@NoArgsConstructor
@AllArgsConstructor
public class MyUserAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue
    private Long id;
    private String authority;
    @ManyToMany(mappedBy = "authority")
    private List<MyUser> users;

    public MyUserAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
