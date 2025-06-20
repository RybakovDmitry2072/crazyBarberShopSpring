package org.example.springcrazybarbershop.adapters;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.adapters.interfaces.FileData;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class MultipartFileAdapter implements FileData {

    private final MultipartFile file;

    @Override
    public String getOriginalFilename() {
        return file.getOriginalFilename();
    }

    @Override
    public long getSize() {
        return file.getSize();
    }

    @Override
    public String getContentType() {
        return file.getContentType();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return file.getInputStream();
    }

}
