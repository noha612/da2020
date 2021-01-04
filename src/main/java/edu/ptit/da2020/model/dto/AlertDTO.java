package edu.ptit.da2020.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlertDTO implements Serializable {
    String mobileId;
    String roadId;
    Integer trafficLevel;
}
