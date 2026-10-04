package com.example.desporto24.model.reference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "reference_catalog_item",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_reference_catalog_category_code",
                columnNames = {"category", "code"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceCatalogItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String category;

    @Column(nullable = false, length = 120)
    private String code;

    @Column(nullable = false, length = 160)
    private String label;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private int sortOrder = 100;
}
