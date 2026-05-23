package utmn.truckrent.server.entity.driver;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.account.Account;

@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int driverId;

//    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<Delivery> deliveries = new ArrayList<>();
//
//    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<Finance> finances = new ArrayList<>();

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
//        this.deliveries = deliveries;
//        this.finances = finances;
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

//    public List<Delivery> getDeliveries() {
//        return deliveries;
//    }
//
//    public void setDeliveries(List<Delivery> deliveries) {
//        this.deliveries = deliveries;
//    }
//
//    public List<Finance> getFinances() {
//        return finances;
//    }
//
//    public void setFinances(List<Finance> finances) {
//        this.finances = finances;
//    }

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
}
