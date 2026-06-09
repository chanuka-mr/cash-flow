package com.chanuka.cash_flow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="tbl_categories")
@Setter                              // auto generate getters, setters...
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryEntity {

    @Id                             // marks this as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)             // generate automatically
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String type;

    private String icon;

    @ManyToOne(fetch=FetchType.LAZY)                                // load it only when needed
    @JoinColumn(name="profile_id", nullable = false)
    private ProfileEntity profile;
}
