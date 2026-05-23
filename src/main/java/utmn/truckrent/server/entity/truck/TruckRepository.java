package utmn.truckrent.server.entity.truck;

import org.hibernate.Session;
import utmn.truckrent.server.entity.truckmark.TruckMark;
import utmn.truckrent.server.repository.GenericRepository;
import utmn.truckrent.server.repository.GenericRepositoryImpl;
import utmn.truckrent.server.utils.HibernateUtil;

import java.util.List;

public interface TruckRepository extends GenericRepository<Truck, Integer> {
    TruckRepositoryImpl instance = new TruckRepositoryImpl();

    List<Truck> findAllByTruckMark(TruckMark truckMark);
    List<Truck> findAllByLoadCapacityLess(int loadCapacityKg);
    List<Truck> findAllByLoadCapacityMore(int loadCapacityKg);

    static TruckRepositoryImpl getInstance(){
        return instance;
    }

    class TruckRepositoryImpl extends GenericRepositoryImpl<Truck, Integer> implements TruckRepository {
        public TruckRepositoryImpl() {
            super(Truck.class);
        }

        @Override
        public List<Truck> findAllByTruckMark(TruckMark truckMark) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Truck d WHERE d.truckMark = :truckMark", Truck.class)
                        .setParameter("truckMark", truckMark)
                        .getResultList();
            }
        }

        @Override
        public List<Truck> findAllByLoadCapacityLess(int loadCapacityKg) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Truck d WHERE d.loadCapacityKg <= :loadCapacityKg", Truck.class)
                        .setParameter("loadCapacityKg", loadCapacityKg)
                        .getResultList();
            }
        }

        @Override
        public List<Truck> findAllByLoadCapacityMore(int loadCapacityKg) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Truck d WHERE d.loadCapacityKg >= :loadCapacityKg", Truck.class)
                        .setParameter("loadCapacityKg", loadCapacityKg)
                        .getResultList();
            }
        }
    }
}
