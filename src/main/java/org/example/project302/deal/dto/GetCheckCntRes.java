package org.example.project302.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCheckCntRes {
    private Integer headCount;
    private Integer checkedCount;
}
