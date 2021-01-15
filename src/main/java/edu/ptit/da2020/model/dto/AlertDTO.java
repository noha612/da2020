package edu.ptit.da2020.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlertDTO implements Serializable {

  String mobileId;
  String roadId;
  String trafficLevel;
}
