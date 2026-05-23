package utmn.truckrent.server.entity.finance;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.driver.Driver;

import java.math.BigDecimal;

@Entity
@Table(name = "finances")
public class Finance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int financeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driverId", nullable = false)
    private Driver driver;

    @Column(nullable = false)
    private BigDecimal income;

    @Column(nullable = false)
    private BigDecimal outcome;
}
