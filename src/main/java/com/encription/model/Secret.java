package com.encription.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name = "secrets")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Secret extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "secret_id")
    private Long id;

    private String name;
    private String description;
    private String encryptedData;
    private LocalDate createdOn;
    private LocalDate lastModified;
    private String encryptionVersion;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "secret", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SecretVersion> versions = new ArrayList<>();

}
