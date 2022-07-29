package com.diginamic.gt.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.IdentityHashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Transactional
@Table(name = "auth_data")
public class AuthData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String securityToken;
    private String authentication;

    @OneToOne
    @JoinColumn(name = "id_employee")
    private Employee employee;
}
