package utmn.truckrent.server.entity.driver;

import org.hibernate.Session;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.repository.GenericRepository;
import utmn.truckrent.server.repository.GenericRepositoryImpl;
import utmn.truckrent.server.utils.HibernateUtil;

import java.util.List;

public interface DriverRepository extends GenericRepository<Driver, Integer> {
    DriverRepositoryImpl instance = new DriverRepositoryImpl();

    List<Driver> findAllByAccount(Account account);
    List<Driver> findAllBySurname(String surname);
    List<Driver> findAllByName(String name);
    List<Driver> findAllByLastname(String lastname);

    static DriverRepositoryImpl getInstance(){
        return instance;
    }

    class DriverRepositoryImpl extends GenericRepositoryImpl<Driver, Integer> implements DriverRepository {
        public DriverRepositoryImpl() {
            super(Driver.class);
        }

        @Override
        public List<Driver> findAllByAccount(Account account) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Driver d WHERE d.account = :account", Driver.class)
                        .setParameter("account", account)
                        .getResultList();
            }
        }

        @Override
        public List<Driver> findAllBySurname(String surname) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Driver d WHERE d.surname = :surname", Driver.class)
                        .setParameter("surname", surname)
                        .getResultList();
            }
        }

        @Override
        public List<Driver> findAllByName(String name) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Driver d WHERE d.name = :name", Driver.class)
                        .setParameter("name", name)
                        .getResultList();
            }
        }

        @Override
        public List<Driver> findAllByLastname(String lastname) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Driver d WHERE d.lastname = :lastname", Driver.class)
                        .setParameter("lastname", lastname)
                        .getResultList();
            }
        }
    }
}
