package com.zinum.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class CreateProductRequest {
    String title;
    String description;
    int mrpPrice;
    int sellingPrice;
    String color;
    List<String> images;
    String category;
    String category2;
    String category3;
    String sizes;
}
