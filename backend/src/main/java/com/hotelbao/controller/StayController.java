package com.hotelbao.controller;

import com.hotelbao.entity.Room;
import com.hotelbao.entity.Stay;
import com.hotelbao.entity.User;
import com.hotelbao.repository.RoomRepository;
import com.hotelbao.repository.StayRepository;
import com.hotelbao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/stays")
public class StayController {
    @Autowired
    private StayRepository stayRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping
    public ResponseEntity<?> getAllStays(@RequestParam(required = false) Long userId) {
        try {
            List<Stay> stays;
            
            // Se userId foi fornecido, buscar apenas estadias desse usuário
            if (userId != null) {
                stays = stayRepository.findByUserId(userId);
            } else {
                // Se não foi fornecido, buscar todas as estadias (para admins)
                stays = stayRepository.findAll();
            }
            
            List<Map<String, Object>> stayDTOs = stays.stream().map(stay -> {
                Map<String, Object> dto = new java.util.HashMap<>();
                dto.put("id", stay.getId());
                dto.put("checkIn", stay.getCheckIn());
                dto.put("checkOut", stay.getCheckOut());
                
                // User info
                if (stay.getUser() != null) {
                    Map<String, Object> userInfo = new java.util.HashMap<>();
                    userInfo.put("id", stay.getUser().getId());
                    userInfo.put("name", stay.getUser().getName());
                    dto.put("user", userInfo);
                }
                
                // Room info
                if (stay.getRoom() != null) {
                    Map<String, Object> roomInfo = new java.util.HashMap<>();
                    roomInfo.put("id", stay.getRoom().getId());
                    roomInfo.put("descricao", stay.getRoom().getDescricao());
                    roomInfo.put("valor", stay.getRoom().getValor());
                    dto.put("room", roomInfo);
                }
                
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok().body(stayDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao carregar estadias: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStayById(@PathVariable Long id) {
        return stayRepository.findById(id)
            .map(stay -> {
                Map<String, Object> dto = new java.util.HashMap<>();
                dto.put("id", stay.getId());
                dto.put("checkIn", stay.getCheckIn());
                dto.put("checkOut", stay.getCheckOut());

                // User info
                if (stay.getUser() != null) {
                    Map<String, Object> userInfo = new java.util.HashMap<>();
                    userInfo.put("id", stay.getUser().getId());
                    userInfo.put("name", stay.getUser().getName());
                    dto.put("user", userInfo);
                }

                // Room info
                if (stay.getRoom() != null) {
                    Map<String, Object> roomInfo = new java.util.HashMap<>();
                    roomInfo.put("id", stay.getRoom().getId());
                    roomInfo.put("descricao", stay.getRoom().getDescricao());
                    roomInfo.put("valor", stay.getRoom().getValor());
                    dto.put("room", roomInfo);
                }

                return ResponseEntity.ok(dto);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStay(@PathVariable Long id, @RequestBody Stay updatedStay) {
        return stayRepository.findById(id)
            .map(stay -> {
                stay.setCheckIn(updatedStay.getCheckIn());
                stay.setCheckOut(updatedStay.getCheckOut());
                // Atualizar user e room se vierem no body
                if (updatedStay.getUser() != null) {
                    User user = userRepository.findById(updatedStay.getUser().getId()).orElse(null);
                    if (user != null) stay.setUser(user);
                }
                if (updatedStay.getRoom() != null) {
                    Room room = roomRepository.findById(updatedStay.getRoom().getId()).orElse(null);
                    if (room != null) stay.setRoom(room);
                }
                Stay saved = stayRepository.save(stay);
                Map<String, Object> dto = new java.util.HashMap<>();
                dto.put("id", saved.getId());
                dto.put("checkIn", saved.getCheckIn());
                dto.put("checkOut", saved.getCheckOut());
                if (saved.getUser() != null) {
                    Map<String, Object> userInfo = new java.util.HashMap<>();
                    userInfo.put("id", saved.getUser().getId());
                    userInfo.put("name", saved.getUser().getName());
                    dto.put("user", userInfo);
                }
                if (saved.getRoom() != null) {
                    Map<String, Object> roomInfo = new java.util.HashMap<>();
                    roomInfo.put("id", saved.getRoom().getId());
                    roomInfo.put("descricao", saved.getRoom().getDescricao());
                    roomInfo.put("valor", saved.getRoom().getValor());
                    dto.put("room", roomInfo);
                }
                return ResponseEntity.ok(dto);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> createStay(@RequestBody StayRequest request) {
        User user = userRepository.findById(request.userId).orElse(null);
        Room room = roomRepository.findById(request.roomId).orElse(null);
        if (user == null || room == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuário ou quarto não encontrado"));
        }
        // Calcular data de checkout (check-in + 1 dia)
        LocalDate checkOut = request.checkIn.plusDays(1);
        // Verificar se existe conflito de datas para este quarto
        List<Stay> conflictingStays = stayRepository.findConflictingStays(request.roomId, request.checkIn, checkOut);
        if (!conflictingStays.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", 
                "Quarto já está reservado para esta data. Por favor, escolha outra data ou outro quarto."));
        }
        Stay stay = new Stay();
        stay.setUser(user);
        stay.setRoom(room);
        stay.setCheckIn(request.checkIn);
        stay.setCheckOut(checkOut);
        Stay savedStay = stayRepository.save(stay);
        Map<String, Object> dto = new java.util.HashMap<>();
        dto.put("id", savedStay.getId());
        dto.put("checkIn", savedStay.getCheckIn());
        dto.put("checkOut", savedStay.getCheckOut());
        if (savedStay.getUser() != null) {
            Map<String, Object> userInfo = new java.util.HashMap<>();
            userInfo.put("id", savedStay.getUser().getId());
            userInfo.put("name", savedStay.getUser().getName());
            dto.put("user", userInfo);
        }
        if (savedStay.getRoom() != null) {
            Map<String, Object> roomInfo = new java.util.HashMap<>();
            roomInfo.put("id", savedStay.getRoom().getId());
            roomInfo.put("descricao", savedStay.getRoom().getDescricao());
            roomInfo.put("valor", savedStay.getRoom().getValor());
            dto.put("room", roomInfo);
        }
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping("/report")
    public Object report(@RequestBody ReportRequest req) {
        if (req.userId == null || req.tipo == null) return null;
        switch (req.tipo) {
            case "maior":
                return java.util.Collections.singletonMap("maiorValor", stayRepository.findMaxStayValueByUserId(req.userId));
            case "menor":
                return java.util.Collections.singletonMap("menorValor", stayRepository.findMinStayValueByUserId(req.userId));
            case "total":
                return java.util.Collections.singletonMap("total", stayRepository.sumStayValueByUserId(req.userId));
            default:
                return java.util.Collections.singletonMap("erro", "Tipo de relatório inválido");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStay(@PathVariable Long id) {
        if (!stayRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        stayRepository.deleteById(id);
        return ResponseEntity.ok().body("Estadia deletada com sucesso!");
    }

    public static class StayRequest {
        public Long userId;
        public Long roomId;
        public LocalDate checkIn;
    }

    public static class ReportRequest {
        public Long userId;
        public String tipo;
    }
} 