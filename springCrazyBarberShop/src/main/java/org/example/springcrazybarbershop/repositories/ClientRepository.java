package org.example.springcrazybarbershop.repositories;

import org.example.springcrazybarbershop.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Object> findUserByEmail(String email);

    Optional<Client> findClientById(Long id);

    List<Client> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        String firstName, String lastName, String email);
}
