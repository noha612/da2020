package edu.ptit.da2020.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Transport extends Moving {
    public Transport() {
        type = "TRANSPORT";
    }
}
