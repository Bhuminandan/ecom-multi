package com.zinum.model;

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
@Table(name = "deals")
public class Deal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "discount")
    Integer discount;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "category_id")
    HomeCategory category;
}
