package utmn.truckrent.server.entity.truckmark;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.truck.Truck;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "truckmarks")
public class TruckMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int truckmarkId;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "truckmark", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Truck> trucks = new ArrayList<>();


}
