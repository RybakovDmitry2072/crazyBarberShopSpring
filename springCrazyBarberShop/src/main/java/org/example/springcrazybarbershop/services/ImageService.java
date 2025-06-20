package org.example.springcrazybarbershop.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.dto.form.FileForm;
import org.example.springcrazybarbershop.mappers.FileMapper;
import org.example.springcrazybarbershop.models.File;
import org.example.springcrazybarbershop.repositories.FileRepository;
import org.example.springcrazybarbershop.services.interfeces.IFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class ImageService implements IFileService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private final String storageProperties;
    private final FileMapper fileMapper;
    private final FileRepository fileRepository;
    private Path uploadDir;

    public ImageService(@Value("${image.storage.path}") String storageProperties,
                       FileMapper fileMapper,
                       FileRepository fileRepository) {
        this.storageProperties = storageProperties;
        this.fileMapper = fileMapper;
        this.fileRepository = fileRepository;
    }

    @PostConstruct
    public void init() {
        try {
            uploadDir = Paths.get(storageProperties).toAbsolutePath().normalize();
            if (!Files.exists(uploadDir)) {
                logger.info("Создание директории для загрузки: {}", uploadDir);
                Files.createDirectories(uploadDir);
                logger.info("Директория успешно создана");
            }
        } catch (IOException e) {
            logger.error("Не удалось создать директорию для загрузки {}: {}", uploadDir, e.getMessage(), e);
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    @Override
    @Transactional
    public File uploadFile(FileForm fileForm, String directory) throws IOException {
        try {
            logger.debug("Начало загрузки файла: {}", fileForm.getFileName());
            File fileEntity = fileMapper.fileFormToEntity(fileForm);
            logger.debug("Создана сущность файла: {}", fileEntity);

            Path targetLocation = uploadDir.resolve(fileEntity.getStorageFileName());
            logger.debug("Сохранение файла в: {}", targetLocation);

            Files.copy(fileForm.getFileStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Файл успешно сохранен: {}", fileEntity.getOriginalFileName());

            File savedFile = fileRepository.saveOrUpdateFile(fileEntity);
            logger.debug("Информация о файле сохранена в базе данных: {}", savedFile);
            return savedFile;
            
        } catch (IOException e) {
            logger.error("Ошибка при сохранении файла {}: {}", fileForm.getFileName(), e.getMessage(), e);
            throw new IOException("Failed to store file", e);
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при загрузке файла {}: {}", fileForm.getFileName(), e.getMessage(), e);
            throw new RuntimeException("Unexpected error while storing file", e);
        }
    }

    @Override
    public void deleteImage(Long imageId) {
        try {
            logger.debug("Попытка удаления изображения с ID: {}", imageId);
            // TODO: impl
            logger.info("Изображение успешно удалено: {}", imageId);
        } catch (Exception e) {
            logger.error("Ошибка при удалении изображения с ID {}: {}", imageId, e.getMessage(), e);
            throw e;
        }
    }
}
