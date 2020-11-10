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
//@Data
public class EdgeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "from")
    private String intersactionIdFrom;

    @Column(name = "to")
    private String intersactionIdTo;

    @Column(name = "haversine_scorer")
    private Double haversineScorer;

    @Column(name = "estimate_speed")
    private Double estimateSpeed;
}