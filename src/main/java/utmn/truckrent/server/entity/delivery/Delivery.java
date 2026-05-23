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

    public Delivery(int deliveryId, Partner sender, Partner receiver, Driver driver, Container container, Truck truck, LocalDateTime loadDate, LocalDateTime unloadDate) {
        this.deliveryId = deliveryId;
        this.sender = sender;
        this.receiver = receiver;
        this.driver = driver;
        this.container = container;
        this.truck = truck;
        this.loadDate = loadDate;
        this.unloadDate = unloadDate;
    }

    public Delivery() {
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Partner getSender() {
        return sender;
    }

    public void setSender(Partner sender) {
        this.sender = sender;
    }

    public Partner getReceiver() {
        return receiver;
    }

    public void setReceiver(Partner receiver) {
        this.receiver = receiver;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public LocalDateTime getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(LocalDateTime loadDate) {
        this.loadDate = loadDate;
    }

    public LocalDateTime getUnloadDate() {
        return unloadDate;
    }

    public void setUnloadDate(LocalDateTime unloadDate) {
        this.unloadDate = unloadDate;
    }
}
