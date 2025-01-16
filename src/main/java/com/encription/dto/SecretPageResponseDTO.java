package com.encription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecretPageResponseDTO {
    List<SecretResponseDTO > secretResponseDTOList;
    Integer pageNumber;
    Integer pageSize;
    Long totalElements;
    int totalPages;
    boolean isLast;
}
