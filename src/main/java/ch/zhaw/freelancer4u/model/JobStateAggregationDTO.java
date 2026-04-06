package ch.zhaw.freelancer4u.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class JobStateAggregationDTO {
    private String id;
    private String [] jobIds;
    private String count;
}