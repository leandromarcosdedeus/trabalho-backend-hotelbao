package com.example.backend.model;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private Date initial_date;
    private Date final_date;

    // Getters e Setters
}