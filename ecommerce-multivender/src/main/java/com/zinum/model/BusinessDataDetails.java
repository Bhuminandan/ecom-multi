package com.zinum.model;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class BusinessDataDetails {

    String businessName;
    String businessEmail;
    String businessMobile;
    String businessAddress;
    String logo;
    String banner;
}
