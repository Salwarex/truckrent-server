package utmn.truckrent.server.entity.finance;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.driver.Driver;

import java.math.BigDecimal;
import java.util.Objects;

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

    public Finance(int financeId, Driver driver, BigDecimal income, BigDecimal outcome) {
        this.financeId = financeId;
        this.driver = driver;
        this.income = income;
        this.outcome = outcome;
    }

    public Finance() {
    }

    public int getFinanceId() {
        return financeId;
    }

    public void setFinanceId(int financeId) {
        this.financeId = financeId;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOutcome() {
        return outcome;
    }

    public void setOutcome(BigDecimal outcome) {
        this.outcome = outcome;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Finance finance = (Finance) object;
        return financeId == finance.financeId && Objects.equals(driver, finance.driver) && Objects.equals(income, finance.income) && Objects.equals(outcome, finance.outcome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(financeId, driver, income, outcome);
    }
}
