package edu.ptit.da2020.model.dto;

import lombok.Data;

@Data
public class Transport extends Moving {
    public Transport() {
        type = "TRANSPORT";
    }
}
