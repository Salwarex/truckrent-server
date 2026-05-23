package utmn.truckrent.server.entity.partner;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.delivery.Delivery;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drivers")
@PrimaryKeyJoinColumn(name = "accountId")
public class Partner {
    @Column(nullable = false, unique = true)
    private String partnerCode;

    @Column(nullable = false)
    private String title;

    private String contactPhone;
    private String contactEmail;
    private String contactName;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Delivery> deliveriesAsSender = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Delivery> deliveriesAsReceiver = new ArrayList<>();
}
