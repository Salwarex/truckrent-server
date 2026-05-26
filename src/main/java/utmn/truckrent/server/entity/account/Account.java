package utmn.truckrent.server.entity.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ru.vit4liy.jwt.JwtClient;
import ru.vit4liy.jwt.JwtUser;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.utils.ServiceHash;

@Entity
@Table(name = "accounts")
public class Account implements JwtUser, JwtClient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_id_seq", allocationSize = 50)
    private int accountId;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    @JsonIgnore
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @JsonIgnore
    private String refreshToken;

    public Account(int accountId, String login, String passwordHash, Role role, String refreshToken) {
        this.accountId = accountId;
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public Account() {
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isPasswordMatch(String password){
        return ServiceHash.isMatch(password, passwordHash);
    }


    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPassword(String password) {
        this.passwordHash = ServiceHash.hash(password, ServiceHash.createSalt());
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public String getTitle() {
        return login;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    //    public Driver getDriver() {
//        return driver;
//    }
//
//    public void setDriver(Driver driver) {
//        this.driver = driver;
//    }
//
//    public Partner getPartner() {
//        return partner;
//    }
//
//    public void setPartner(Partner partner) {
//        this.partner = partner;
//    }
}
