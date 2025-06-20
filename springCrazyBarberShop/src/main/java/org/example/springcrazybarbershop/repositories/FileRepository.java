package org.example.springcrazybarbershop.repositories;

import jakarta.transaction.Transactional;
import org.example.springcrazybarbershop.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findFileByUser_Id(Long userId);

    default File saveOrUpdateFile(File file) {
        Optional<File> existingFile = findFileByUser_Id(file.getUser().getId());
        existingFile.ifPresent(value -> file.setId(value.getId()));
        return save(file);
    }
}