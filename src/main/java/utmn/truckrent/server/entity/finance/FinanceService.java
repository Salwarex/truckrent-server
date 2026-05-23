package utmn.truckrent.server.entity.finance;

import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.driver.Driver;

import java.math.BigDecimal;

public class FinanceService {
    private static final FinanceRepository.FinanceRepositoryImpl repository = FinanceRepository.getInstance();

    //create
    public static Finance register(
            Driver driver,
            BigDecimal income,
            BigDecimal outcome
    ) throws ServiceExecutionException {
        Finance finance = new Finance();
        if(driver == null || income == null || outcome == null)
            throw new ServiceExecutionException("One of not-nullable provided parameters is null!");

        finance.setDriver(driver);
        finance.setIncome(income);
        finance.setOutcome(outcome);

        return repository.save(finance);
    }

    //get
    public static Finance get(int id) throws ServiceExecutionException{
        return repository.findById(id).orElseThrow(() -> new ServiceExecutionException("Can't find object with provided id (#%d)"
                .formatted(id)));
    }

    //update
    public static Finance update(Finance mergeObj){
        return repository.merge(mergeObj);
    }

    //delete
    public static void delete(int id){
        repository.deleteById(id);
    }

    public static void delete(Finance deleteObj) throws ServiceExecutionException{
        if(deleteObj == null) throw new ServiceExecutionException("Provided null object");
        delete(deleteObj.getFinanceId());
    }
}
