package utmn.truckrent.server.entity.delivery;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.entity.partner.Partner;
import utmn.truckrent.server.entity.truck.Truck;
import utmn.truckrent.server.entity.container.Container;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int deliveryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "senderId", nullable = false)
    private Partner sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiverId", nullable = false)
    private Partner receiver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driverId", nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "containerId", nullable = false)
    private Container container;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "truckId", nullable = false)
    private Truck truck;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime loadDate;

    private LocalDateTime unloadDate;
}
