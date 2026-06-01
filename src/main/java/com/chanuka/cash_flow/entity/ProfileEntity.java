package com.chanuka.cash_flow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity                                 // marks this class as a JPA entity
@Table(name="tbl_profiles")             // specifies table name
@Data                                   // lombok annotation that automatically generates setters, getters, etc...
@AllArgsConstructor                     // generates constructors
@NoArgsConstructor
@Builder                                // allows object creation using builder patterns
public class ProfileEntity {

    @Id                                 // marks this as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)              // database auto generates IDs
    private Long id;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    private String profileImageUrl;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String activationToken;

    // runs automatically before inserts a new record
    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            isActive = false;
        }
    }
}
