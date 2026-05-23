package utmn.truckrent.server.entity.container;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.delivery.Delivery;
import utmn.truckrent.server.entity.trademark.TradeMark;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "containers")
public class Container {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int containerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trademarkId", nullable = false)
    private TradeMark tradeMark;

    @OneToMany(mappedBy = "container", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Delivery> deliveries = new ArrayList<>();
}
