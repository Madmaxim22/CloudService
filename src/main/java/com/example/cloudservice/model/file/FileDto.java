package com.example.cloudservice.model.file;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    @NotBlank(message = "Filename is mandatory")
    private String filename;
    @NotBlank(message = "Size is mandatory")
    private Long size;
}
