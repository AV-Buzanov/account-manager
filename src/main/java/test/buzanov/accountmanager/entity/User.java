package test.buzanov.accountmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import test.buzanov.accountmanager.enumurated.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"accounts", "authorities"})
@Entity
@Data
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity implements UserDetails {
    @Column(unique = true, nullable = false)

    private String username;

    private String password;

    private String name;

    private boolean enabled;

    private String googleName;

    private String googleUsername;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Enumerated(EnumType.STRING)
    private Set<Role> authorities;

    @NotNull
    @ManyToMany(mappedBy = "users", cascade = {CascadeType.ALL})

    @JsonIgnore
    private Set<Account> accounts = new HashSet<>();

    @JsonIgnore
    @Transient
    private boolean isAccountNonExpired;
    @JsonIgnore
    @Transient
    private boolean isAccountNonLocked;
    @JsonIgnore
    @Transient
    private boolean isCredentialsNonExpired;

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
}
