package utmn.truckrent.server.entity.container;

import org.hibernate.Session;
import utmn.truckrent.server.entity.trademark.TradeMark;
import utmn.truckrent.server.repository.GenericRepository;
import utmn.truckrent.server.repository.GenericRepositoryImpl;
import utmn.truckrent.server.utils.HibernateUtil;

import java.util.List;

public interface ContainerRepository  extends GenericRepository<Container, Integer> {
    ContainerRepositoryImpl instance = new ContainerRepositoryImpl();

    List<Container> findAllByTradeMark(TradeMark tradeMark);

    static ContainerRepositoryImpl getInstance(){
        return instance;
    }

    class ContainerRepositoryImpl extends GenericRepositoryImpl<Container, Integer> implements ContainerRepository{
        public ContainerRepositoryImpl() {
            super(Container.class);
        }

        @Override
        public List<Container> findAllByTradeMark(TradeMark tradeMark) {
            try(Session session = HibernateUtil.getSessionFactory().openSession()){
                return session.createQuery(
                                "SELECT a FROM Container a WHERE a.tradeMark = :tradeMark", Container.class)
                        .setParameter("tradeMark", tradeMark)
                        .getResultList();
            }
        }
    }
}
