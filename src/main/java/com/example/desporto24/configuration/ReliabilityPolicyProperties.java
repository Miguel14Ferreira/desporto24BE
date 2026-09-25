package com.example.desporto24.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "desporto.reliability")
public class ReliabilityPolicyProperties {
    private int freeCancelHours = 2;
    private int lookbackDays = 90;
    private double lateCancellationWeight = 0.5;
    private double noShowWeight = 1.0;
    private double warningThreshold = 2.0;
    private double reducedPriorityThreshold = 3.0;
    private double restrictionThreshold = 4.0;
    private int restrictionDays = 7;
    private int attendanceMarkingDelayMinutes = 30;
}
