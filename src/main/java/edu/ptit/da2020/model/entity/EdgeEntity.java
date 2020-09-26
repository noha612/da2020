package edu.ptit.da2020.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "edge")
@Data
public class EdgeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "intersection_id_1")
    private String intersactionId1;

    @Column(name = "intersection_id_2")
    private String intersactionId2;

    @Column(name = "haversine_scorer")
    private Double haversineScorer;
}