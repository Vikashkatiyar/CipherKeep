package com.encription.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SecretVersion extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "secret_id", nullable = false)
    private Secret secret;

    private String encryptedData;
    private LocalDate lastModified;
    private String encryptionVersion;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

}
