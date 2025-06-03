package com.example.backend.model;


import jakarta.persistence.*;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private String celphone;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
