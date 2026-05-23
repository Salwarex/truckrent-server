package utmn.truckrent.server.entity.truck;

import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.truckmark.TruckMark;

public class TruckService {
    private static final TruckRepository.TruckRepositoryImpl repository = TruckRepository.getInstance();

    //create
    public static Truck register(
            TruckMark truckMark,
            int loadCapacityKg
    ) throws ServiceExecutionException {
        Truck truck = new Truck();
        if(truckMark == null || loadCapacityKg < 0)
            throw new ServiceExecutionException("One of not-nullable provided parameters is null!");

        truck.setTruckMark(truckMark);
        truck.setLoadCapacityKg(loadCapacityKg);

        return repository.save(truck);
    }

    //get
    public static Truck get(int id) throws ServiceExecutionException{
        return repository.findById(id).orElseThrow(() -> new ServiceExecutionException("Can't find object with provided id (#%d)"
                .formatted(id)));
    }

    //update
    public static Truck update(Truck mergeObj){
        return repository.merge(mergeObj);
    }

    //delete
    public static void delete(int id){
        repository.deleteById(id);
    }

    public static void delete(Truck deleteObj) throws ServiceExecutionException{
        if(deleteObj == null) throw new ServiceExecutionException("Provided null object");
        delete(deleteObj.getTruckId());
    }
}
