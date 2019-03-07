package com.llzw.apigate.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    protected String username;

    @Column(nullable = false)
    @NonNull
    protected String password;

    @Column(nullable = false)
    @NonNull
    protected String nickname;

    @Column(nullable = false)
    @NonNull
    protected String email;

    @Column(nullable = false)
    @NonNull
    protected String phoneNumber;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    protected Set<IdType> identity_type;

    @Column(nullable = false)
    @NonNull
    protected String identity_number;

    enum IdType {
        PRC_ID,
        PASSPORT
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}