package utmn.truckrent.server.entity.truckmark;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.truck.Truck;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "truckmarks")
public class TruckMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int truckmarkId;

    @Column(nullable = false)
    private String title;

    public TruckMark(int truckmarkId, String title) {
        this.truckmarkId = truckmarkId;
        this.title = title;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TruckMark truckMark = (TruckMark) object;
        return truckmarkId == truckMark.truckmarkId && Objects.equals(title, truckMark.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(truckmarkId, title);
    }
}
