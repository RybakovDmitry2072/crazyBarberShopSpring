package org.example.springcrazybarbershop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.dto.ClientDto;
import org.example.springcrazybarbershop.dto.form.EditClientForm;
import org.example.springcrazybarbershop.dto.form.FileForm;
import org.example.springcrazybarbershop.dto.form.auth.SignUpForm;
import org.example.springcrazybarbershop.exceptions.NotFountUserExceptions;
import org.example.springcrazybarbershop.exceptions.UserAlreadyExistException;
import org.example.springcrazybarbershop.mappers.ClientMapper;
import org.example.springcrazybarbershop.mappers.SignUpMapper;
import org.example.springcrazybarbershop.mappers.UserMapper;
import org.example.springcrazybarbershop.models.Client;
import org.example.springcrazybarbershop.models.File;
import org.example.springcrazybarbershop.models.PasswordResetToken;
import org.example.springcrazybarbershop.models.Role;
import org.example.springcrazybarbershop.repositories.*;
import org.example.springcrazybarbershop.services.interfeces.IClientService;
import org.example.springcrazybarbershop.services.interfeces.IFileService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService implements IClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final SignUpMapper signUpMapper;
    private final PasswordEncoder passwordEncoder;
    private final ClientMapper clientMapper;
    private final PasswordTokenRepository passwordTokenRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRoleRepository userRoleRepository;
    private final FileRepository fileRepository;
    private final IFileService fileService;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ClientDto> getAllClients() {
        try {
            logger.debug("Получение списка всех клиентов");
            List<Client> clients = clientRepository.findAll();
            logger.debug("Найдено {} клиентов", clients.size());
            return clients.stream()
                    .map(clientMapper::toClientDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Ошибка при получении списка клиентов: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void createPasswordResetTokenForUser(Client client, String token) {
        try {
            logger.debug("Создание токена сброса пароля для клиента с ID: {}", client.getId());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            PasswordResetToken passwordToken = PasswordResetToken.builder()
                    .token(token)
                    .client(client)
                    .expiryDate(calendar.getTime())
                    .build();
            
            passwordTokenRepository.save(passwordToken);
            logger.info("Токен сброса пароля успешно создан для клиента с ID: {}", client.getId());
        } catch (Exception e) {
            logger.error("Ошибка при создании токена сброса пароля для клиента с ID {}: {}", 
                client.getId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        try {
            log.info("Попытка удаления клиента с ID: {}", id);
            
            Client client = clientRepository.findClientById(id)
                    .orElseThrow(() -> {
                        log.error("Клиент не найден с ID: {}", id);
                        return new NotFountUserExceptions();
                    });
            
            try {
                log.debug("Удаление записей для клиента с ID: {}", id);
                appointmentRepository.deleteAllByClientId(id);
                entityManager.flush();
                
                log.debug("Проверка и удаление файла пользователя с ID: {}", id);
                fileRepository.findFileByUser_Id(id).ifPresent(file -> {
                    fileRepository.delete(file);
                    entityManager.flush();
                });
                
                log.debug("Удаление ролей для клиента с ID: {}", id);
                userRoleRepository.deleteAllByUserId(id);
                entityManager.flush();
                
                log.debug("Удаление клиента с ID: {}", id);
                clientRepository.delete(client);
                entityManager.flush();
                
                log.info("Клиент успешно удален, ID: {}", id);
            } catch (Exception e) {
                log.error("Ошибка при удалении клиента с ID: {}", id, e);
                throw new RuntimeException("Не удалось удалить клиента: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении клиента с id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Long getClientCount() {
        try {
            logger.debug("Подсчет общего количества клиентов");
            Long count = clientRepository.count();
            logger.debug("Общее количество клиентов: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Ошибка при подсчете количества клиентов: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<ClientDto> searchClients(String query) {
        try {
            logger.debug("Поиск клиентов по запросу: {}", query);
            List<ClientDto> results = clientRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    query, query, query)
                .stream()
                .map(clientMapper::toClientDto)
                .collect(Collectors.toList());
            logger.debug("Найдено {} клиентов по запросу: {}", results.size(), query);
            return results;
        } catch (Exception e) {
            logger.error("Ошибка при поиске клиентов по запросу {}: {}", query, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ClientDto updateClient(Long clientId, EditClientForm editClientForm) {
        try {
            Client client = clientRepository.findClientById(clientId)
                    .orElseThrow(NotFountUserExceptions::new);

            client.setFirstName(editClientForm.getFirstName());
            client.setLastName(editClientForm.getLastName());
            client.setPhone(editClientForm.getPhone());
            client.setEmail(editClientForm.getEmail());
            
            if (editClientForm.getRoles() != null && !editClientForm.getRoles().isEmpty()) {
                userRoleRepository.deleteAllByUserId(client.getId());
                entityManager.flush();
                
                client.setRoles(editClientForm.getRoles());
            }

            try {
                if (editClientForm.isRemoveImage()) {
                    if (client.getImage() != null) {
                        fileService.deleteImage(client.getImage().getId());
                        client.setImage(null);
                    }
                }
                else if (editClientForm.getImageFile() != null && !editClientForm.getImageFile().isEmpty()) {
                    MultipartFile imageFile = editClientForm.getImageFile();
                    FileForm fileForm = FileForm.builder()
                            .fileName(imageFile.getOriginalFilename())
                            .contentType(imageFile.getContentType())
                            .size(imageFile.getSize())
                            .userId(clientId)
                            .fileStream(imageFile.getInputStream())
                            .build();
                    
                    File newImage = fileService.uploadFile(fileForm, "profile_images");
                    client.setImage(newImage);
                }
            } catch (IOException e) {
                log.error("Ошибка при обработке изображения для клиента {}: {}", clientId, e.getMessage());
                throw new RuntimeException("Ошибка при обработке изображения", e);
            }

            return clientMapper.toClientDto(clientRepository.save(client));
        } catch (Exception e) {
            logger.error("Ошибка при обновлении данных клиента с id {}: {}", clientId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ClientDto getClientById(Long id) {
        try {
            return clientMapper.toClientDto(
                    clientRepository.findClientById(id).orElseThrow(NotFountUserExceptions::new));
        } catch (Exception e) {
            logger.error("Ошибка при получении данных клиента с id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Client findByEmail(String email) {
        try {
            logger.debug("Поиск клиента по email: {}", email);
            Client client = (Client) userRepository.findUserByEmail(email)
                .orElseThrow(NotFountUserExceptions::new);
            logger.debug("Клиент найден по email: {}", email);
            return client;
        } catch (NotFountUserExceptions e) {
            logger.warn("Клиент с email {} не найден", email);
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при поиске клиента по email {}: {}", email, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Client registerNewClientAccount(SignUpForm signUpForm) throws UserAlreadyExistException {
        try {
            logger.debug("Регистрация нового клиента с email: {}", signUpForm.getEmail());
            
            if (emailExists(signUpForm.getEmail())) {
                logger.warn("Попытка регистрации с существующим email: {}", signUpForm.getEmail());
                throw new UserAlreadyExistException("There is an account with that email address: "
                        + signUpForm.getEmail());
            }
            
            Client client = signUpMapper.toClient(signUpForm, passwordEncoder);
            client.setRoles(Set.of(Role.CLIENT, Role.USER));
            Client savedClient = clientRepository.save(client);
            logger.info("Успешно зарегистрирован новый клиент с ID: {}", savedClient.getId());
            return savedClient;
        } catch (UserAlreadyExistException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при регистрации нового клиента с email {}: {}", 
                signUpForm.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    private boolean emailExists(String email) {
        try {
            logger.debug("Проверка существования email: {}", email);
            boolean exists = userRepository.findUserByEmail(email).isPresent();
            logger.debug("Email {} {}", email, exists ? "существует" : "не существует");
            return exists;
        } catch (Exception e) {
            logger.error("Ошибка при проверке существования email {}: {}", email, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Client getUserByPasswordResetToken(String token) {
        try {
            logger.debug("Поиск клиента по токену сброса пароля: {}", token);
            PasswordResetToken resetToken = passwordTokenRepository.findByToken(token);
            if (resetToken != null) {
                logger.debug("Найден клиент по токену сброса пароля");
                return resetToken.getClient();
            }
            logger.warn("Токен сброса пароля не найден: {}", token);
            return null;
        } catch (Exception e) {
            logger.error("Ошибка при поиске клиента по токену сброса пароля {}: {}", 
                token, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void changeUserPassword(Client client, String newPassword) {
        try {
            logger.debug("Изменение пароля для клиента с ID: {}", client.getId());
            client.setPassword(passwordEncoder.encode(newPassword));
            clientRepository.save(client);
            logger.info("Пароль успешно изменен для клиента с ID: {}", client.getId());
        } catch (Exception e) {
            logger.error("Ошибка при изменении пароля для клиента с ID {}: {}", 
                client.getId(), e.getMessage(), e);
            throw e;
        }
    }
}
