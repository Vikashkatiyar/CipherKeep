package com.encription.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.encription.model.Secret;
import com.encription.model.User;
import com.encription.dto.SecretPageResponseDTO;
import com.encription.dto.SecretRequestDTO;
import com.encription.dto.SecretResponseDTO;
import com.encription.exception.SecretNotFoundException;
import com.encription.exception.UserNotFoundException;
import com.encription.mapper.SecretMapper;
import com.encription.repository.SecretRepository;
import com.encription.repository.SecretVersionRepository;
import com.encription.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class SecretServiceImplTest {

    @Mock
    private SecretRepository secretRepository;

    @Mock
    private SecretMapper secretMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecretVersionRepository secretVersionRepository;

    @InjectMocks
    private SecretServiceImpl secretService;

    private Secret secret;
    private SecretRequestDTO secretRequestDTO;
    private SecretResponseDTO secretResponseDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        secret = new Secret();
        secret.setId(1L);
        secret.setName("Test Secret");
        secret.setDescription("This is a test secret");
        secret.setEncryptedData("encryptedData");
        secret.setUser(user);

        secretRequestDTO = new SecretRequestDTO();
        secretRequestDTO.setName("Test Secret");
        secretRequestDTO.setDescription("This is a test secret");
        secretRequestDTO.setEncryptedData("encryptedData");
        secretRequestDTO.setUser_id(1L);

        secretResponseDTO = new SecretResponseDTO();
        secretResponseDTO.setName("Test Secret");
        secretResponseDTO.setDescription("This is a test secret");
    }

    @Test
    void testCreateSecret() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(secretMapper.mapToSecret(any(SecretRequestDTO.class))).thenReturn(secret);
        when(secretRepository.save(any(Secret.class))).thenReturn(secret);
        when(secretMapper.mapToSecretResponseDto(any(Secret.class))).thenReturn(secretResponseDTO);

        SecretResponseDTO createdSecret = secretService.createSecret(secretRequestDTO);

        assertNotNull(createdSecret);
        assertEquals("Test Secret", createdSecret.getName());
        verify(secretRepository,times(1)).save(any(Secret.class));
    }

    @Test
    void testCreateSecret_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(SecretNotFoundException.class, () -> secretService.createSecret(secretRequestDTO));
        verify(secretRepository,never()).save(any(Secret.class));
    }

    @Test
    void testGetSecretById_SecretNotFound() {
        when(secretRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SecretNotFoundException.class, () -> secretService.getSecretById(1L));
    }

    @Test
    void testUpdateSecret() throws Exception {
        when(secretRepository.findById(1L)).thenReturn(Optional.of(secret));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(secretRepository.save(secret)).thenReturn(secret);
        when(secretMapper.mapToSecretResponseDto(secret)).thenReturn(secretResponseDTO);

        SecretResponseDTO updatedSecret = secretService.updateSecret(1L, secretRequestDTO);

        assertNotNull(updatedSecret);
        assertEquals("Test Secret", updatedSecret.getName());
        verify(secretRepository, times(1)).save(secret);
    }

    @Test
    void testUpdateSecret_SecretNotFound() {
        when(secretRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SecretNotFoundException.class, () -> secretService.updateSecret(1L, secretRequestDTO));
    }

    @Test
    void testUpdateSecret_UserNotFound() {
        when(secretRepository.findById(1L)).thenReturn(Optional.of(secret));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> secretService.updateSecret(1L, secretRequestDTO));
    }

    @Test
    void testDeleteSecret() {
        when(secretRepository.existsById(1L)).thenReturn(true);

        boolean isDeleted = secretService.deleteSecret(1L);

        assertTrue(isDeleted);
        verify(secretRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteSecret_SecretNotFound() {
        when(secretRepository.existsById(1L)).thenReturn(false);

        assertThrows(SecretNotFoundException.class, () -> secretService.deleteSecret(1L));
    }

    @Test
    void testGetAllSecrets() {
        List<Secret> secrets = new ArrayList<>();
        secrets.add(secret);

        when(secretRepository.findAll()).thenReturn(secrets);
        when(secretMapper.mapToSecretResponseDto(secret)).thenReturn(secretResponseDTO);

        List<SecretResponseDTO> allSecrets = secretService.getAllSecret();

        assertNotNull(allSecrets);
        assertEquals(1, allSecrets.size());
        assertEquals("Test Secret", allSecrets.get(0).getName());
    }

    @Test
    void testGetAllSecretWithPagination() {
        List<Secret> secrets = new ArrayList<>();
        secrets.add(secret);
        Page<Secret> secretPage = new PageImpl<>(secrets);
        Pageable pageable = PageRequest.of(0, 10);

        when(secretRepository.findAll(pageable)).thenReturn(secretPage);
        when(secretMapper.mapToSecretResponseDto(secret)).thenReturn(secretResponseDTO);

        SecretPageResponseDTO pagedSecrets = secretService.getAllSecretWithPagination(0, 10);

        assertNotNull(pagedSecrets);
        assertEquals(1, pagedSecrets.getSecretResponseDTOList().size());
        assertEquals("Test Secret", pagedSecrets.getSecretResponseDTOList().get(0).getName());
    }

    @Test
    void testGetAllSecretWithPaginationAndSorting() {
        List<Secret> secrets = new ArrayList<>();
        secrets.add(secret);
        Page<Secret> secretPage = new PageImpl<>(secrets);
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        when(secretRepository.findAll(pageable)).thenReturn(secretPage);
        when(secretMapper.mapToSecretResponseDto(secret)).thenReturn(secretResponseDTO);

        SecretPageResponseDTO pagedSecrets = secretService.getAllSecretWithPaginationAndSorting(0, 10, "name", "asc");

        assertNotNull(pagedSecrets);
        assertEquals(1, pagedSecrets.getSecretResponseDTOList().size());
        assertEquals("Test Secret", pagedSecrets.getSecretResponseDTOList().get(0).getName());
    }
}
