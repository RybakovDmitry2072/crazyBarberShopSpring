package org.example.springcrazybarbershop.controllers.api;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.dto.ClientDto;
import org.example.springcrazybarbershop.dto.form.EditClientForm;
import org.example.springcrazybarbershop.models.Client;
import org.example.springcrazybarbershop.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class ClientController {

    private final ClientService clientService;

    @PatchMapping("/{id}")
    private ResponseEntity<ClientDto> updateClient(@PathVariable Long id,
                                                   @RequestBody EditClientForm editClientForm){

        ClientDto clientDto = clientService.updateClient(id, editClientForm);
        return ResponseEntity.ok(clientDto);
    }


}
