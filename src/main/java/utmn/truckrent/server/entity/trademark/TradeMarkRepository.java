package utmn.truckrent.server.entity.trademark;

import org.hibernate.Session;
import utmn.truckrent.server.repository.GenericRepository;
import utmn.truckrent.server.repository.GenericRepositoryImpl;
import utmn.truckrent.server.utils.HibernateUtil;

import java.util.List;

public interface TradeMarkRepository extends GenericRepository<TradeMark, Integer> {
    TradeMarkRepositoryImpl instance = new TradeMarkRepositoryImpl();

    List<TradeMark> findAllByTitle(String title);

    static TradeMarkRepositoryImpl getInstance(){
        return instance;
    }

    class TradeMarkRepositoryImpl extends GenericRepositoryImpl<TradeMark, Integer> implements TradeMarkRepository {
        public TradeMarkRepositoryImpl() {
            super(TradeMark.class);
        }

        @Override
        public List<TradeMark> findAllByTitle(String title) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM TradeMark d WHERE d.title = :title", TradeMark.class)
                        .setParameter("title", title)
                        .getResultList();
            }
        }
    }
}
