package utmn.truckrent.server.entity.truck;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.truckmark.TruckMark;

@Entity
@Table(name = "trucks")
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int truckId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "truckmarkId", nullable = false)
    private TruckMark truckMark;

    @Column(nullable = false)
    private int loadCapacityKg;
}
