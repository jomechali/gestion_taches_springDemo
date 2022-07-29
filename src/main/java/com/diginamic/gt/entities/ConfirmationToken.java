package com.diginamic.gt.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    private String number; // can be renamed value

    private Instant creation; // creation date

    private Instant activation; // activation date, null on creation

    @OneToOne
    @JoinColumn(name = "id_employee")
    private Employee employee;

}
