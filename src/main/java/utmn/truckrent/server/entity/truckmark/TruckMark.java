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

//    @OneToMany(mappedBy = "truckmark", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<Truck> trucks = new ArrayList<>();

    public TruckMark(int truckmarkId, String title) {
        this.truckmarkId = truckmarkId;
        this.title = title;
//        this.trucks = trucks;
    }

    public TruckMark() {
    }

    public int getTruckmarkId() {
        return truckmarkId;
    }

    public void setTruckmarkId(int truckmarkId) {
        this.truckmarkId = truckmarkId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public List<Truck> getTrucks() {
//        return trucks;
//    }
//
//    public void setTrucks(List<Truck> trucks) {
//        this.trucks = trucks;
//    }
}
