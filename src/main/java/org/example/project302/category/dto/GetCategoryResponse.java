package org.example.project302.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryResponse {
    private Long ctgrId;
    private String ctgrName;
}
