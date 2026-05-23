package utmn.truckrent.server.entity.driver;

import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;

public class DriverService {
    private static final DriverRepository.DriverRepositoryImpl repository = DriverRepository.getInstance();

    //create
    public static Driver register(
            String surname,
            String name,
            String lastname,
            Account account
    ) throws ServiceExecutionException {
        Driver driver = new Driver();
        if(surname == null || name == null)
            throw new ServiceExecutionException("One of not-nullable provided parameters is null!");

        driver.setSurname(surname);
        driver.setName(name);
        driver.setLastname(lastname);
        driver.setAccount(account);

        return repository.save(driver);
    }

    //get
    public static Driver get(int id) throws ServiceExecutionException{
        return repository.findById(id).orElseThrow(() -> new ServiceExecutionException("Can't find object with provided id (#%d)"
                .formatted(id)));
    }

    //update
    public static Driver update(Driver mergeObj){
        return repository.merge(mergeObj);
    }

    //delete
    public static void delete(int id){
        repository.deleteById(id);
    }

    public static void delete(Driver deleteObj) throws ServiceExecutionException{
        if(deleteObj == null) throw new ServiceExecutionException("Provided null object");
        delete(deleteObj.getDriverId());
    }
}
