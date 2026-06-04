package utmn.truckrent.server.entity.delivery;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.entity.partner.Partner;
import utmn.truckrent.server.entity.truck.Truck;
import utmn.truckrent.server.entity.container.Container;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private LocalDateTime loadedDate;

    private LocalDateTime unloadedDate;

    public Delivery(int deliveryId, Partner sender, Partner receiver, Driver driver, Container container, Truck truck, LocalDateTime loadedDate, LocalDateTime unloadedDate) {
        this.deliveryId = deliveryId;
        this.sender = sender;
        this.receiver = receiver;
        this.driver = driver;
        this.container = container;
        this.truck = truck;
        this.loadedDate = loadedDate;
        this.unloadedDate = unloadedDate;
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

    public LocalDateTime getLoadedDate() {
        return loadedDate;
    }

    public void setLoadedDate(LocalDateTime loadedDate) {
        this.loadedDate = loadedDate;
    }

    public LocalDateTime getUnloadedDate() {
        return unloadedDate;
    }

    public void setUnloadedDate(LocalDateTime unloadedDate) {
        this.unloadedDate = unloadedDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Delivery delivery = (Delivery) object;
        return deliveryId == delivery.deliveryId && Objects.equals(sender, delivery.sender) && Objects.equals(receiver, delivery.receiver) && Objects.equals(driver, delivery.driver) && Objects.equals(container, delivery.container) && Objects.equals(truck, delivery.truck) && Objects.equals(loadedDate, delivery.loadedDate) && Objects.equals(unloadedDate, delivery.unloadedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryId, sender, receiver, driver, container, truck, loadedDate, unloadedDate);
    }
}
