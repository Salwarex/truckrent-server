package utmn.truckrent.server.entity.partner;

import org.hibernate.Session;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.repository.GenericRepository;
import utmn.truckrent.server.repository.GenericRepositoryImpl;
import utmn.truckrent.server.utils.HibernateUtil;

import java.util.List;

public interface PartnerRepository extends GenericRepository<Partner, Integer> {
    PartnerRepositoryImpl instance = new PartnerRepositoryImpl();

    List<Partner> findAllByTitle(String title);
    List<Partner> findAllByPhone(String phone);
    List<Partner> findAllByEmail(String email);
    List<Partner> findAllByContactName(String contactName);
    List<Partner> findAllByAccount(Account account);

    static PartnerRepositoryImpl getInstance(){
        return instance;
    }

    class PartnerRepositoryImpl extends GenericRepositoryImpl<Partner, Integer> implements PartnerRepository {
        public PartnerRepositoryImpl() {
            super(Partner.class);
        }

        @Override
        public List<Partner> findAllByTitle(String title) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Partner d WHERE d.title = :title", Partner.class)
                        .setParameter("title", title)
                        .getResultList();
            }
        }

        @Override
        public List<Partner> findAllByPhone(String phone) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Partner d WHERE d.contactPhone = :contactPhone", Partner.class)
                        .setParameter("contactPhone", phone)
                        .getResultList();
            }
        }

        @Override
        public List<Partner> findAllByEmail(String email) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Partner d WHERE d.contactEmail = :contactEmail", Partner.class)
                        .setParameter("contactEmail", email)
                        .getResultList();
            }
        }

        @Override
        public List<Partner> findAllByContactName(String contactName) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Partner d WHERE d.contactName = :contactName", Partner.class)
                        .setParameter("contactName", contactName)
                        .getResultList();
            }
        }

        @Override
        public List<Partner> findAllByAccount(Account account) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Partner d WHERE d.account = :account", Partner.class)
                        .setParameter("account", account)
                        .getResultList();
            }
        }
    }
}
