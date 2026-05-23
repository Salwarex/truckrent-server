package utmn.truckrent.server.entity.delivery;

import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.container.Container;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.entity.partner.Partner;
import utmn.truckrent.server.entity.truck.Truck;

public class DeliveryService {
    private static final DeliveryRepository.DeliveryRepositoryImpl repository = DeliveryRepository.getInstance();

    //create
    public static Delivery register(
            Partner sender,
            Partner receiver,
            Driver driver,
            Container container,
            Truck truck) throws ServiceExecutionException {
        Delivery delivery = new Delivery();
        if(sender == null
                || receiver == null || driver == null || container == null || truck == null)
            throw new ServiceExecutionException("One of not-nullable provided parameters is null!");

        delivery.setSender(sender);
        delivery.setReceiver(receiver);
        delivery.setDriver(driver);
        delivery.setContainer(container);
        delivery.setTruck(truck);

        return repository.save(delivery);
    }

    //get
    public static Delivery get(int id) throws ServiceExecutionException{
        return repository.findById(id).orElseThrow(() -> new ServiceExecutionException("Can't find object with provided id (#%d)"
                .formatted(id)));
    }

    //update
    public static Delivery update(Delivery mergeObj){
        return repository.merge(mergeObj);
    }

    //delete
    public static void delete(int id){
        repository.deleteById(id);
    }

    public static void delete(Delivery deleteObj) throws ServiceExecutionException{
        if(deleteObj == null) throw new ServiceExecutionException("Provided null object");
        delete(deleteObj.getDeliveryId());
    }
}
