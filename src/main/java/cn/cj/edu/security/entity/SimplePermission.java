package cn.cj.edu.security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplePermission {
    private Integer id;
    private String name;
    private String description;
}
