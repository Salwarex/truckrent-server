package utmn.truckrent.server.entity.container;

import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.trademark.TradeMark;

public class ContainerService {
    private static final ContainerRepository.ContainerRepositoryImpl repository = ContainerRepository.getInstance();

    //create
    public static Container register(TradeMark tradeMark) throws ServiceExecutionException {
        Container container = new Container();
        if(tradeMark == null) throw new ServiceExecutionException("Provided trademark can't be null!");

        container.setTradeMark(tradeMark);

        return repository.save(container);
    }

    //get
    public static Container get(int id) throws ServiceExecutionException{
        return repository.findById(id).orElseThrow(() -> new ServiceExecutionException("Can't find container with provided id (#%d)"
                .formatted(id)));
    }

    //update
    public static Container update(Container mergeObj){
        return repository.merge(mergeObj);
    }

    //delete
    public static void delete(int id){
        repository.deleteById(id);
    }

    public static void delete(Container deleteObj) throws ServiceExecutionException{
        if(deleteObj == null) throw new ServiceExecutionException("Provided null object");
        delete(deleteObj.getContainerId());
    }
}
