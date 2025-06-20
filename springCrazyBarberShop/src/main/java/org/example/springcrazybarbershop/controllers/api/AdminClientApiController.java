package org.example.springcrazybarbershop.controllers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.dto.ClientDto;
import org.example.springcrazybarbershop.dto.ErrorResponse;
import org.example.springcrazybarbershop.dto.UserDto;
import org.example.springcrazybarbershop.dto.form.EditClientForm;
import org.example.springcrazybarbershop.services.interfeces.IClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/clients")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class AdminClientApiController {

    private final IClientService clientService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ClientDto> clients = clientService.getAllClients();
        
        int start = page * size;
        int end = Math.min(start + size, clients.size());
        List<ClientDto> pagedClients = clients.subList(start, end);
        
        Page<ClientDto> clientPage = new PageImpl<>(
            pagedClients, 
            PageRequest.of(page, size), 
            clients.size()
        );
        
        return ResponseEntity.ok(Map.of(
            "content", clientPage.getContent(),
            "totalElements", clientPage.getTotalElements(),
            "totalPages", clientPage.getTotalPages(),
            "currentPage", clientPage.getNumber()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, 
                                                @RequestBody EditClientForm form) {
        return ResponseEntity.ok(clientService.updateClient(id, form));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        log.info("Received DELETE request for client ID: {}", id);
        try {
            clientService.deleteClient(id);
            log.info("Successfully deleted client with ID: {}", id);
            return ResponseEntity.ok().body(Map.of("message", "Client successfully deleted"));
        } catch (Exception e) {
            log.error("Error deleting client with ID: {}", id, e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Ошибка при удалении клиента: " + e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getClientCount() {
        return ResponseEntity.ok(clientService.getClientCount());
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchClients(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ClientDto> clients = clientService.searchClients(query);
        
        int start = page * size;
        int end = Math.min(start + size, clients.size());
        List<ClientDto> pagedClients = clients.subList(start, end);
        
        Page<ClientDto> clientPage = new PageImpl<>(
            pagedClients, 
            PageRequest.of(page, size), 
            clients.size()
        );
        
        return ResponseEntity.ok(Map.of(
            "content", clientPage.getContent(),
            "totalElements", clientPage.getTotalElements(),
            "totalPages", clientPage.getTotalPages(),
            "currentPage", clientPage.getNumber()
        ));
    }
} 