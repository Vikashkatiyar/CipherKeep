package com.encription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretResponseDTO {

    private String name;
    private String description;
    private String encryptedData;
    private LocalDate createdOn;
    private LocalDate lastModified;
    private String encryptionVersion;


}
