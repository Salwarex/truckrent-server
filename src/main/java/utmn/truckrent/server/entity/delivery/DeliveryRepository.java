package utmn.truckrent.server.entity.delivery;

import org.hibernate.Session;
import utmn.truckrent.server.entity.container.Container;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.entity.partner.Partner;
import utmn.truckrent.server.entity.truck.Truck;
import utmn.truckrent.server.repository.GenericRepository;
import utmn.truckrent.server.repository.GenericRepositoryImpl;
import utmn.truckrent.server.utils.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryRepository  extends GenericRepository<Delivery, Integer> {
    DeliveryRepositoryImpl instance = new DeliveryRepositoryImpl();

    List<Delivery> findAllBySender(Partner sender);
    List<Delivery> findAllByReceiver(Partner receiver);
    List<Delivery> findAllByDriver(Driver driver);
    List<Delivery> findAllByContainer(Container container);
    List<Delivery> findAllByTruck(Truck truck);

    List<Delivery> findAllLoadedUntil(LocalDateTime time);
    List<Delivery> findAllLoadedAfter(LocalDateTime time);
    List<Delivery> findAllUnloadedUntil(LocalDateTime time);
    List<Delivery> findAllUnloadedAfter(LocalDateTime time);

    static DeliveryRepositoryImpl getInstance(){
        return instance;
    }

    class DeliveryRepositoryImpl extends GenericRepositoryImpl<Delivery, Integer> implements DeliveryRepository {
        public DeliveryRepositoryImpl() {
            super(Delivery.class);
        }

        @Override
        public List<Delivery> findAllBySender(Partner sender) {
            try(Session session = HibernateUtil.getSessionFactory().openSession()){
                return session.createQuery(
                                "SELECT a FROM Delivery a WHERE a.sender = :sender", Delivery.class)
                        .setParameter("sender", sender)
                        .getResultList();
            }
        }

        @Override
        public List<Delivery> findAllByReceiver(Partner receiver) {
            try(Session session = HibernateUtil.getSessionFactory().openSession()){
                return session.createQuery(
                                "SELECT a FROM Delivery a WHERE a.receiver = :receiver", Delivery.class)
                        .setParameter("receiver", receiver)
                        .getResultList();
            }
        }

        @Override
        public List<Delivery> findAllByDriver(Driver driver) {
            try(Session session = HibernateUtil.getSessionFactory().openSession()){
                return session.createQuery(
                                "SELECT a FROM Delivery a WHERE a.driver = :driver", Delivery.class)
                        .setParameter("driver", driver)
                        .getResultList();
            }
        }

        @Override
        public List<Delivery> findAllByContainer(Container container) {
            try(Session session = HibernateUtil.getSessionFactory().openSession()){
                return session.createQuery(
                                "SELECT a FROM Delivery a WHERE a.container = :container", Delivery.class)
                        .setParameter("container", container)
                        .getResultList();
            }
        }

        @Override
        public List<Delivery> findAllByTruck(Truck truck) {
            try(Session session = HibernateUtil.getSessionFactory().openSession()){
                return session.createQuery(
                                "SELECT a FROM Delivery a WHERE a.truck = :truck", Delivery.class)
                        .setParameter("truck", truck)
                        .getResultList();
            }
        }

        @Override
        public List<Delivery> findAllLoadedUntil(LocalDateTime time) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Delivery d WHERE d.loadedUntil <= :time", Delivery.class)
                        .setParameter("time", time)
                        .getResultList();
            }
        }

        @Override
        public List<Delivery> findAllLoadedAfter(LocalDateTime time) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Delivery d WHERE d.loadedAfter >= :time", Delivery.class)
                        .setParameter("time", time)
                        .getResultList();
            }
        }

        @Override
        public List<Delivery> findAllUnloadedUntil(LocalDateTime time) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Delivery d WHERE d.unloadedUntil <= :time", Delivery.class)
                        .setParameter("time", time)
                        .getResultList();
            }
        }

        @Override
        public List<Delivery> findAllUnloadedAfter(LocalDateTime time) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery(
                                "SELECT d FROM Delivery d WHERE d.unloadedAfter >= :time", Delivery.class)
                        .setParameter("time", time)
                        .getResultList();
            }
        }
    }
}
