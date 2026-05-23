package utmn.truckrent.server.entity.truckmark;

import utmn.truckrent.server.entity.ServiceExecutionException;

public class TruckMarkService {
    private static final TruckMarkRepository.TruckMarkRepositoryImpl repository = TruckMarkRepository.getInstance();

    //create
    public static TruckMark register(
            String title
    ) throws ServiceExecutionException {
        TruckMark truckMark = new TruckMark();
        if(title == null)
            throw new ServiceExecutionException("One of not-nullable provided parameters is null!");

        truckMark.setTitle(title);

        return repository.save(truckMark);
    }

    //get
    public static TruckMark get(int id) throws ServiceExecutionException{
        return repository.findById(id).orElseThrow(() -> new ServiceExecutionException("Can't find object with provided id (#%d)"
                .formatted(id)));
    }

    //update
    public static TruckMark update(TruckMark mergeObj){
        return repository.merge(mergeObj);
    }

    //delete
    public static void delete(int id){
        repository.deleteById(id);
    }

    public static void delete(TruckMark deleteObj) throws ServiceExecutionException{
        if(deleteObj == null) throw new ServiceExecutionException("Provided null object");
        delete(deleteObj.getTruckmarkId());
    }
}
