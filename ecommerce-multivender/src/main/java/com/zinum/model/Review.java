package com.zinum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "reviews")
public class Review  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "rating", nullable = false)
    double rating;

    @Column(name = "comment", nullable = false)
    String comment;

    @ElementCollection
    List<String> images = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

}
