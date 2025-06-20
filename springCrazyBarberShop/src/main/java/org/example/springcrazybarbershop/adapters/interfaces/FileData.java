package org.example.springcrazybarbershop.adapters.interfaces;

import java.io.IOException;
import java.io.InputStream;

public interface FileData {
    String getOriginalFilename();
    long getSize();
    String getContentType();
    InputStream getInputStream() throws IOException;
}
