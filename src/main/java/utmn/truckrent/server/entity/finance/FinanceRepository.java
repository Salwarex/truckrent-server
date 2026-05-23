package utmn.truckrent.server.entity.finance;

import org.hibernate.Session;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.repository.GenericRepository;
import utmn.truckrent.server.repository.GenericRepositoryImpl;
import utmn.truckrent.server.utils.HibernateUtil;

import java.util.List;

public interface FinanceRepository extends GenericRepository<Finance, Integer> {
    FinanceRepositoryImpl instance = new FinanceRepositoryImpl();

    List<Finance> findAllByDriver(Driver driver);

    static FinanceRepositoryImpl getInstance(){
        return instance;
    }

    class FinanceRepositoryImpl extends GenericRepositoryImpl<Finance, Integer> implements FinanceRepository {
        public FinanceRepositoryImpl() {
            super(Finance.class);
        }

        @Override
        public List<Finance> findAllByDriver(Driver driver) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Finance d WHERE d.driver = :driver", Finance.class)
                        .setParameter("driver", driver)
                        .getResultList();
            }
        }
    }
}
