package com.zinum.model;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Home extends BaseEntity {

    List<HomeCategory> grid;

    List<HomeCategory> shopByCategories;

    List<HomeCategory> electricCategories;

    List<HomeCategory> dealCategories;

    List<Deal> deals;
}
