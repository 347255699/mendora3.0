package org.mendora.java8;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Employee {
    private String name;
    private String city;
}
