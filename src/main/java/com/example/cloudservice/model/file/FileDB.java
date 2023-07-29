package com.example.cloudservice.model.file;

import com.example.cloudservice.model.authentication.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Filename is mandatory")
    private String name;
    @NotBlank(message = "Filetype is mandatory")
    private String type;
    @NotBlank(message = "Filesize is mandatory")
    private Long size;
    @Lob
    @NotBlank(message = "Filedata is mandatory")
    private byte[] data;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
