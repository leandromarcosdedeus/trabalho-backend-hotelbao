package com.example.backend.dao;

import com.example.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
