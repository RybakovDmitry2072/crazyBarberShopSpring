package org.example.springcrazybarbershop.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileForm {

    private Long size;

    private Long userId;

    private String fileName;

    private String contentType;

    private InputStream fileStream;

}
