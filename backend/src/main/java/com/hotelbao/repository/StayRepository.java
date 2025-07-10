package com.hotelbao.repository;

import com.hotelbao.entity.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StayRepository extends JpaRepository<Stay, Long> {
    @Query("SELECT MAX(r.valor) FROM Stay s JOIN s.room r WHERE s.user.id = :userId")
    Double findMaxStayValueByUserId(@Param("userId") Long userId);

    @Query("SELECT MIN(r.valor) FROM Stay s JOIN s.room r WHERE s.user.id = :userId")
    Double findMinStayValueByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(r.valor) FROM Stay s JOIN s.room r WHERE s.user.id = :userId")
    Double sumStayValueByUserId(@Param("userId") Long userId);
    
    @Query("SELECT s FROM Stay s WHERE s.room.id = :roomId AND " +
           "((s.checkIn <= :checkIn AND s.checkOut > :checkIn) OR " +
           "(s.checkIn < :checkOut AND s.checkOut >= :checkOut) OR " +
           "(s.checkIn >= :checkIn AND s.checkOut <= :checkOut))")
    List<Stay> findConflictingStays(@Param("roomId") Long roomId, 
                                   @Param("checkIn") LocalDate checkIn, 
                                   @Param("checkOut") LocalDate checkOut);
    
    @Query("SELECT s FROM Stay s WHERE s.user.id = :userId")
    List<Stay> findByUserId(@Param("userId") Long userId);
} 