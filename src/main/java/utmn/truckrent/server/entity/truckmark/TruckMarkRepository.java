package utmn.truckrent.server.entity.truckmark;

import org.hibernate.Session;
import utmn.truckrent.server.repository.GenericRepository;
import utmn.truckrent.server.repository.GenericRepositoryImpl;
import utmn.truckrent.server.utils.HibernateUtil;

import java.util.List;

public interface TruckMarkRepository extends GenericRepository<TruckMark, Integer> {
    TruckMarkRepositoryImpl instance = new TruckMarkRepositoryImpl();

    List<TruckMark> findAllByTitle(String title);

    static TruckMarkRepositoryImpl getInstance(){
        return instance;
    }

    class TruckMarkRepositoryImpl extends GenericRepositoryImpl<TruckMark, Integer> implements TruckMarkRepository {
        public TruckMarkRepositoryImpl() {
            super(TruckMark.class);
        }

        @Override
        public List<TruckMark> findAllByTitle(String title) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM TruckMark d WHERE d.title = :title", TruckMark.class)
                        .setParameter("title", title)
                        .getResultList();
            }
        }
    }
}
