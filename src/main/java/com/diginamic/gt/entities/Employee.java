package com.diginamic.gt.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "employee")
@Getter
@Setter
@AllArgsConstructor
public class Employee implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private int id;


    private String firstName;
    private String lastName;
    @Getter(value = AccessLevel.NONE)
    private String userName;

    private String password;
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    /*@JoinTable(name = "employee_task",
            joinColumns = @JoinColumn(name = "id_emp", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_task", referencedColumnName = "id"))*/
    private List<Task> tasks;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee_role",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))

    private List<Role> roles;

    // warning here : if we choose to use @NoArgsConstructor, be careful to init before use
    public Employee() {
        tasks = new ArrayList<>();
    }

    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toList());
    }

    /**
     * @return
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * @return
     */
    @Override
    public String getUsername() {
        return this.userName;
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.enabled;
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.enabled;
    }

    /**
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.enabled;
    }

    /**
     * @return
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
