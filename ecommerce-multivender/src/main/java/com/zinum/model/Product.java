package com.zinum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "mrp_price", nullable = false)
    int mrpPrice;

    @Column(name = "selling_price", nullable = false)
    int sellingPrice;

    @Column(name = "discount_percent", nullable = false)
    int discountPercent;

    @Column(name = "quantity", nullable = false)
    int quantity;

    @Column(name = "color")
    String color;

    @ElementCollection
    List<String> images = new ArrayList<>();

    @Column(name = "num_of_ratings")
    int numOfRatings = 0;

    @Column(name = "sizes")
    String sizes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews = new ArrayList<>();

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    Set<Wishlist> wishlists = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonBackReference
    Seller seller;
}
