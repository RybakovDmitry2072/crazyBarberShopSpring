package org.example.springcrazybarbershop.services.interfeces;

import org.example.springcrazybarbershop.dto.ClientDto;
import org.example.springcrazybarbershop.dto.form.EditClientForm;
import org.example.springcrazybarbershop.dto.form.auth.SignUpForm;
import org.example.springcrazybarbershop.exceptions.UserAlreadyExistException;
import org.example.springcrazybarbershop.models.Client;

import java.util.List;

public interface IClientService {

    Client registerNewClientAccount(SignUpForm signUpForm) throws UserAlreadyExistException;

    ClientDto updateClient(Long clientId, EditClientForm editClientForm);

    Client findByEmail(String email);

    ClientDto getClientById(Long id);
    
    List<ClientDto> getAllClients();
    
    void deleteClient(Long id);
    
    Long getClientCount();
    
    List<ClientDto> searchClients(String query);

    void createPasswordResetTokenForUser(Client client, String token);
    
    Client getUserByPasswordResetToken(String token);
    
    void changeUserPassword(Client client, String newPassword);
}
