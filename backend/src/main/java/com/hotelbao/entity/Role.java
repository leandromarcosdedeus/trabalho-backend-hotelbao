package com.hotelbao.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleType name;
    
    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();
    
    public enum RoleType {
        ROLE_ADMIN,
        ROLE_USER,
        ROLE_MANAGER,
        ROLE_CLIENT, 
    }
    
    public Role() {}
    
    public Role(RoleType name) {
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public RoleType getName() {
        return name;
    }
    
    public void setName(RoleType name) {
        this.name = name;
    }
    
    public Set<User> getUsers() {
        return users;
    }
    
    public void setUsers(Set<User> users) {
        this.users = users;
    }
} 