package com.llzw.apigate.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Buyer extends User {
    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "owner")
    private List<Address> addresses;

    public Buyer(String username, String password, String nickname, String email, String phoneNumber, Set<IdType> identity_type, String identity_number) {
        super(username, password, nickname, email, phoneNumber, identity_type, identity_number);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_BUYER"));
    }
}
