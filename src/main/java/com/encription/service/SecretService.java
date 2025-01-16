package com.encription.service;

import com.encription.dto.SecretPageResponseDTO;
import com.encription.dto.SecretRequestDTO;
import com.encription.dto.SecretResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SecretService {

    SecretResponseDTO createSecret(SecretRequestDTO secretRequestDTO);

    SecretResponseDTO getSecretById(Long id);

    SecretResponseDTO updateSecret(Long id, SecretRequestDTO secretRequestDTO);

    boolean deleteSecret(Long id);

    List<SecretResponseDTO> getAllSecret();

    SecretPageResponseDTO getAllSecretWithPagination(Integer pageNumber, Integer pageSize);

    SecretPageResponseDTO getAllSecretWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String direction);
}
