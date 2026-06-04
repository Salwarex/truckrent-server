package utmn.truckrent.server.entity.container;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.delivery.Delivery;
import utmn.truckrent.server.entity.trademark.TradeMark;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "containers")
public class Container {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int containerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trademarkId", nullable = false)
    private TradeMark tradeMark;
    public Container(int containerId, TradeMark tradeMark) {
        this.containerId = containerId;
        this.tradeMark = tradeMark;
    }

    public Container() {
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public TradeMark getTradeMark() {
        return tradeMark;
    }

    public void setTradeMark(TradeMark tradeMark) {
        this.tradeMark = tradeMark;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Container container = (Container) object;
        return containerId == container.containerId && Objects.equals(tradeMark, container.tradeMark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerId, tradeMark);
    }
}
