package utmn.truckrent.server.entity.partner;

import jakarta.persistence.*;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.delivery.Delivery;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drivers")
@PrimaryKeyJoinColumn(name = "accountId")
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partner_seq")
    @SequenceGenerator(name = "partner_seq", sequenceName = "partner_id_seq", allocationSize = 50)
    private int partnerId;

    @Column(nullable = false)
    private String title;

    private String contactPhone;
    private String contactEmail;
    private String contactName;

//    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<Delivery> deliveriesAsSender = new ArrayList<>();
//
//    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<Delivery> deliveriesAsReceiver = new ArrayList<>();

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    public Partner(int partnerId, String title, String contactPhone, String contactEmail, String contactName, Account account) {
        this.partnerId = partnerId;
        this.title = title;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.contactName = contactName;
//        this.deliveriesAsSender = deliveriesAsSender;
//        this.deliveriesAsReceiver = deliveriesAsReceiver;
        this.account = account;
    }

    public Partner() {
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

//    public List<Delivery> getDeliveriesAsSender() {
//        return deliveriesAsSender;
//    }
//
//    public void setDeliveriesAsSender(List<Delivery> deliveriesAsSender) {
//        this.deliveriesAsSender = deliveriesAsSender;
//    }
//
//    public List<Delivery> getDeliveriesAsReceiver() {
//        return deliveriesAsReceiver;
//    }
//
//    public void setDeliveriesAsReceiver(List<Delivery> deliveriesAsReceiver) {
//        this.deliveriesAsReceiver = deliveriesAsReceiver;
//    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
