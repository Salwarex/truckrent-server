package utmn.truckrent.server.entity.account;

import org.hibernate.Session;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.entity.partner.Partner;
import utmn.truckrent.server.repository.GenericRepository;
import utmn.truckrent.server.repository.GenericRepositoryImpl;
import utmn.truckrent.server.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends GenericRepository<Account, Integer> {
    AccountRepositoryImpl instance = new AccountRepositoryImpl();

    static AccountRepositoryImpl getInstance(){
        return instance;
    }

    Optional<Account> findByLogin(String login);
    List<Account> findAllByRole(Role role);
    List<Account> findAllByDriver(Driver driver);
    List<Account> findAllByPartner(Partner partner);

    class AccountRepositoryImpl extends GenericRepositoryImpl<Account, Integer> implements AccountRepository{

        public AccountRepositoryImpl() {
            super(Account.class);
        }

        @Override
        public Optional<Account> findByLogin(String login) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return Optional.ofNullable(session.createQuery(
                                "SELECT u FROM Account u WHERE u.login = :login", Account.class)
                        .setParameter("login", login).uniqueResult());
            }
        }

        @Override
        public List<Account> findAllByRole(Role role) {
            try(Session session = HibernateUtil.getSessionFactory().openSession()){
                return session.createQuery(
                        "SELECT a FROM Account a WHERE a.role = :role", Account.class)
                        .setParameter("role", role)
                        .getResultList();
            }
        }

        @Override
        public List<Account> findAllByDriver(Driver driver) {
            try(Session session = HibernateUtil.getSessionFactory().openSession()){
                return session.createQuery(
                                "SELECT a FROM Account a WHERE a.driver = :driver", Account.class)
                        .setParameter("driver", driver)
                        .getResultList();
            }
        }

        @Override
        public List<Account> findAllByPartner(Partner partner) {
            try(Session session = HibernateUtil.getSessionFactory().openSession()){
                return session.createQuery(
                                "SELECT a FROM Account a WHERE a.partner = :partner", Account.class)
                        .setParameter("partner", partner)
                        .getResultList();
            }
        }
    }
}

