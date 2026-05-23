package utmn.truckrent.server.entity.driver;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.finance.Finance;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.delivery.Delivery;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drivers")
@PrimaryKeyJoinColumn(name = "accountId")
public class Driver extends Account {
    @Column(nullable = false, unique = true)
    private String driverCode;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Delivery> deliveries = new ArrayList<>();

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Finance> finances = new ArrayList<>();

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String name;

    private String lastname;

}
