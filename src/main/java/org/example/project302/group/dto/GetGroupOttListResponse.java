package org.example.project302.group.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetGroupOttListResponse {
    private Long univ_id;
    private Long pdId;
    private String pdName;
    private String pdContent;
    private Integer views;
    private Integer pdPrice;
    private LocalDateTime creatDate;
    private String pullUpDate;
    private String detailAddr;
    private String ctgrName;
    private Integer minPeople;
    private Integer maxPeople;
    private Boolean roundYn;
    private LocalDateTime closDate;
    private String dDay;
    private Integer chatCount;
}
