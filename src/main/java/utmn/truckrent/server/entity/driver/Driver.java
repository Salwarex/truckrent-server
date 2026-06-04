package utmn.truckrent.server.entity.driver;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.account.Account;

import java.util.Objects;

@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int driverId;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String name;

    private String lastname;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    public Driver(int driverId, String surname, String name, String lastname, Account account) {
        this.driverId = driverId;
        this.surname = surname;
        this.name = name;
        this.lastname = lastname;
        this.account = account;
    }

    public Driver() {
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Driver driver = (Driver) object;
        return driverId == driver.driverId && Objects.equals(surname, driver.surname) && Objects.equals(name, driver.name) && Objects.equals(lastname, driver.lastname) && Objects.equals(account, driver.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverId, surname, name, lastname, account);
    }
}
