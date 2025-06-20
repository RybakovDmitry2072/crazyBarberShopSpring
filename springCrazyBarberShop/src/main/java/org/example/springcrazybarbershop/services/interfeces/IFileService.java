package org.example.springcrazybarbershop.services.interfeces;

import org.example.springcrazybarbershop.dto.form.FileForm;
import org.example.springcrazybarbershop.models.File;

import java.io.IOException;

public interface IFileService {

    File uploadFile(FileForm fileData, String directory) throws IOException;
    void deleteImage(Long imageId);
}
