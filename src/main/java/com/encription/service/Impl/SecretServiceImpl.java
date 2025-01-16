package com.encription.service.Impl;

import com.encription.CipherKeepApplication;
import com.encription.constants.ApplicationConstants;
import com.encription.exception.EncryptionDecryptionException;
import com.encription.model.ActionType;
import com.encription.model.Secret;
import com.encription.model.SecretVersion;
import com.encription.model.User;
import com.encription.repository.SecretRepository;
import com.encription.repository.SecretVersionRepository;
import com.encription.repository.UserRepository;
import com.encription.dto.SecretPageResponseDTO;
import com.encription.dto.SecretRequestDTO;
import com.encription.dto.SecretResponseDTO;
import com.encription.exception.SecretNotFoundException;
import com.encription.exception.UserNotFoundException;
import com.encription.mapper.SecretMapper;
import com.encription.service.SecretService;
import com.encription.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecretServiceImpl implements SecretService {

    @Autowired
    private SecretRepository secretRepository;

    @Autowired
    private SecretMapper secretMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecretVersionRepository secretVersionRepository;


    @Override
    public SecretResponseDTO createSecret(SecretRequestDTO secretRequestDTO) {

        Long userId = secretRequestDTO.getUser_id();
        if (!userRepository.existsById(userId)) {
            throw new SecretNotFoundException("User Not Found with user_id: " + userId);
        }
        Secret secret = secretMapper.mapToSecret(secretRequestDTO);
        try {
            String encryptedData = EncryptionUtil.encrypt(secretRequestDTO.getEncryptedData(), ApplicationConstants.SECRET_KEY);
            secret.setEncryptedData(encryptedData);

            SecretVersion initialVersion = createInitialVersion(secret, secret.getEncryptedData());
            Secret savedSecret = secretRepository.save(secret);
            secretVersionRepository.save(initialVersion);


            String decryptedData = EncryptionUtil.decrypt(savedSecret.getEncryptedData(), ApplicationConstants.SECRET_KEY);
            savedSecret.setEncryptedData(decryptedData);

            CipherKeepApplication.logger.info("Secret created successfully for userId: {}", userId);
            return secretMapper.mapToSecretResponseDto(savedSecret);
        } catch (Exception e) {
            throw new EncryptionDecryptionException("Error during encryption/decryption process",e);
        }

    }

    @Override
    public SecretResponseDTO getSecretById(Long id) {
        CipherKeepApplication.logger.info("Fetching secret with ID: {}", id);
        Optional<Secret> secretOptional = secretRepository.findById(id);

        if (secretOptional.isEmpty()) {
            CipherKeepApplication.logger.error("Secret Not Found with id: {}", id);
            throw new SecretNotFoundException("Secret Not Found with id: " + id);
        }
        Secret secret = secretOptional.get();
        secret.setEncryptedData(EncryptionUtil.decrypt(secret.getEncryptedData(), ApplicationConstants.SECRET_KEY));
        CipherKeepApplication.logger.info("Secret fetched successfully with ID: {}", id);
        return secretMapper.mapToSecretResponseDto(secret);
    }

    @Override
    public SecretResponseDTO updateSecret(Long id, SecretRequestDTO secretRequestDTO) {
        CipherKeepApplication.logger.info("Updating secret with ID: {}", id);
        Optional<Secret> optionalSecret = secretRepository.findById(id);
        if (optionalSecret.isEmpty()) {
            CipherKeepApplication.logger.error("Secret not found with ID: {}", id);
            throw new SecretNotFoundException("Secret not found with id: " + id);
        }

        Secret secret = optionalSecret.get();

        this.archiveCurrVersion(secret);

        // Assuming SecretRequestDTO has the necessary fields to update Secret
        secret.setName(secretRequestDTO.getName());
        secret.setDescription(secretRequestDTO.getDescription());
        secret.setEncryptedData(secretRequestDTO.getEncryptedData());
        secret.setCreatedOn(secretRequestDTO.getCreatedOn());
        secret.setLastModified(secretRequestDTO.getLastModified());
        secret.setEncryptionVersion(secretRequestDTO.getEncryptionVersion());

        Long userId = secretRequestDTO.getUser_id();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            CipherKeepApplication.logger.error("User not found with ID: {}", userId);

            throw new UserNotFoundException("User not found with id: " + userId);
        }
        secret.setUser(optionalUser.get());

        // Update other fields as necessary
        Secret updatedSecret = secretRepository.save(secret);
        CipherKeepApplication.logger.info("Secret updated successfully with ID: {}", updatedSecret.getId());
        return secretMapper.mapToSecretResponseDto(updatedSecret);
    }

    @Override
    public boolean deleteSecret(Long id) {
        CipherKeepApplication.logger.info("Attempting to delete secret with ID: {}", id);
        if (!secretRepository.existsById(id)) {
            throw new SecretNotFoundException("Secret not found with id " + id);
        }
        secretRepository.deleteById(id);
        CipherKeepApplication.logger.info("Secret deleted successfully with ID: {}", id);
        return true;
    }

    @Override
    public List<SecretResponseDTO> getAllSecret() {
        CipherKeepApplication.logger.info("Fetching all secrets");
        List<Secret> allSecrets= secretRepository.findAll();
        CipherKeepApplication.logger.debug("Retrieved {} secrets from the database", allSecrets.size());
        return allSecrets.stream()
               .map(secret-> secretMapper.mapToSecretResponseDto(secret))
               .collect(Collectors.toList());

    }

    public SecretPageResponseDTO getAllSecretWithPagination(Integer pageNumber, Integer pageSize){
        CipherKeepApplication.logger.info("Fetching secrets with pagination: pageNumber {}, pageSize {}", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Secret> secretPages = secretRepository.findAll(pageable);
        List<Secret> allSecrets = secretPages.getContent();
        List<SecretResponseDTO> allSecretResponseDto= allSecrets.stream()
                .map(secret-> secretMapper.mapToSecretResponseDto(secret))
                .collect(Collectors.toList());


        CipherKeepApplication.logger.info("Fetched {} secrets on page {} of size {}", allSecretResponseDto.size(), pageNumber, pageSize);
        return SecretPageResponseDTO.builder()
                .secretResponseDTOList(allSecretResponseDto)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(secretPages.getTotalElements())
                .totalPages(secretPages.getTotalPages())
                .isLast(secretPages.isLast())
        .build();
    }


    public SecretPageResponseDTO getAllSecretWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String direction){
        CipherKeepApplication.logger.info("Fetching secrets with pagination and sorting: pageNumber {}, pageSize {}, sortBy {}, direction {}", pageNumber, pageSize, sortBy, direction);
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Secret> secretPages = secretRepository.findAll(pageable);
        List<Secret> allSecrets = secretPages.getContent();

        List<SecretResponseDTO> allSecretResponseDto= allSecrets.stream()
                .map(secret-> secretMapper.mapToSecretResponseDto(secret))
                .collect(Collectors.toList());


        CipherKeepApplication.logger.info("Fetched {} secrets on page {} of size {} with sorting", allSecretResponseDto.size(), pageNumber, pageSize);
        return SecretPageResponseDTO.builder()
                .secretResponseDTOList(allSecretResponseDto)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(secretPages.getTotalElements())
                .totalPages(secretPages.getTotalPages())
                .isLast(secretPages.isLast())
                .build();
    }



    private SecretVersion createInitialVersion(Secret secret, String encryptedData) {
        SecretVersion version = new SecretVersion();
        version.setSecret(secret);
        version.setEncryptedData(encryptedData);
        version.setLastModified(LocalDate.now());
        version.setEncryptionVersion("initial version");
        version.setActionType(ActionType.CREATED);
        return version;
    }

    private void archiveCurrVersion(Secret secret) {
        if (secret.getVersions().size() >= 10) {
            SecretVersion oldestVersion = secret.getVersions().get(0);
            secretVersionRepository.delete(oldestVersion);
            secret.getVersions().remove(0);
        }

        SecretVersion newVersion = new SecretVersion();
        newVersion.setSecret(secret);
        newVersion.setEncryptedData(secret.getEncryptedData());
        newVersion.setLastModified(secret.getLastModified());
        newVersion.setEncryptionVersion(secret.getEncryptionVersion());
        newVersion.setActionType(ActionType.UPDATED);
        secret.getVersions().add(newVersion);
    }


}
