package com.zinum.model;

import com.zinum.enums.HomeCategorySection;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "addresses")
public class HomeCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @Column( name = "name", nullable = false)
    String name;

    @Column( name = "image", nullable = false)
    String image;

    @Column(name = "category_id", nullable = false)
    Long categoryId;

    @Column(name = "home_category_section", nullable = false)
    HomeCategorySection homeCategorySection;
}
