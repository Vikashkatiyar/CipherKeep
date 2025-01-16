package com.encription.mapper;

import com.encription.model.Secret;
import com.encription.repository.UserRepository;
import com.encription.dto.SecretRequestDTO;
import com.encription.dto.SecretResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SecretMapper {

      @Autowired
      private UserRepository userRepository;

       public Secret mapToSecret(SecretRequestDTO secretRequestDTO){
           if(Objects.isNull(secretRequestDTO)) {
               return new Secret();
           }

            return Secret.builder()
                    .name(secretRequestDTO.getName())
                    .description(secretRequestDTO.getDescription())
                    .encryptedData(secretRequestDTO.getEncryptedData())
                    .encryptionVersion(secretRequestDTO.getEncryptionVersion())
                    .createdOn(secretRequestDTO.getCreatedOn())
                    .lastModified(secretRequestDTO.getLastModified())
                    .user(userRepository.findById(secretRequestDTO.getUser_id()).get())
           .build();
        }

    public SecretResponseDTO mapToSecretResponseDto(Secret savedSecret) {
        if(Objects.isNull(savedSecret)) {
            return new SecretResponseDTO();
        }

        return SecretResponseDTO.builder()
                .name(savedSecret.getName())
                .description(savedSecret.getDescription())
                .encryptedData(savedSecret.getEncryptedData())
                .encryptionVersion(savedSecret.getEncryptionVersion())
                .createdOn(savedSecret.getCreatedOn())
                .lastModified(savedSecret.getLastModified())
                .build();
    }
}
