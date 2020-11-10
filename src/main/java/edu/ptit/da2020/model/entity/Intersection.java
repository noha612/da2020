package edu.ptit.da2020.model.entity;

import edu.ptit.da2020.model.graph.GraphNode;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "intersection")
//@Data
public class Intersection implements GraphNode {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;
}