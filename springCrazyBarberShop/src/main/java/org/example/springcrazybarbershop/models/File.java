package org.example.springcrazybarbershop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String storageFileName;
    private String originalFileName;
    private String contentType;
    private Long size;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}