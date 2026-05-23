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

    public Truck(int truckId, TruckMark truckMark, int loadCapacityKg) {
        this.truckId = truckId;
        this.truckMark = truckMark;
        this.loadCapacityKg = loadCapacityKg;
    }

    public Truck() {
    }

    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }

    public TruckMark getTruckMark() {
        return truckMark;
    }

    public void setTruckMark(TruckMark truckMark) {
        this.truckMark = truckMark;
    }

    public int getLoadCapacityKg() {
        return loadCapacityKg;
    }

    public void setLoadCapacityKg(int loadCapacityKg) {
        this.loadCapacityKg = loadCapacityKg;
    }
}
