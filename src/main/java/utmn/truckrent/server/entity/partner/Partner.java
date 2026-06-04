package utmn.truckrent.server.entity.partner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import utmn.truckrent.server.entity.account.Account;

import java.util.Objects;

@Entity
@Table(name = "partners")
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int partnerId;

    @Column(nullable = false)
    private String title;

    private String contactPhone;
    private String contactEmail;
    private String contactName;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    public Partner(int partnerId, String title, String contactPhone, String contactEmail, String contactName, Account account) {
        this.partnerId = partnerId;
        this.title = title;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.contactName = contactName;
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
        Partner partner = (Partner) object;
        return partnerId == partner.partnerId && Objects.equals(title, partner.title) && Objects.equals(contactPhone, partner.contactPhone) && Objects.equals(contactEmail, partner.contactEmail) && Objects.equals(contactName, partner.contactName) && Objects.equals(account, partner.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partnerId, title, contactPhone, contactEmail, contactName, account);
    }
}
