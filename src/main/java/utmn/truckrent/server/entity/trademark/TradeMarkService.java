package utmn.truckrent.server.entity.trademark;

import utmn.truckrent.server.entity.ServiceExecutionException;

public class TradeMarkService {
    private static final TradeMarkRepository.TradeMarkRepositoryImpl repository = TradeMarkRepository.getInstance();

    //create
    public static TradeMark register(
            String title
    ) throws ServiceExecutionException {
        TradeMark tradeMark = new TradeMark();
        if(title == null)
            throw new ServiceExecutionException("One of not-nullable provided parameters is null!");

        tradeMark.setTitle(title);

        return repository.save(tradeMark);
    }

    //get
    public static TradeMark get(int id) throws ServiceExecutionException{
        return repository.findById(id).orElseThrow(() -> new ServiceExecutionException("Can't find object with provided id (#%d)"
                .formatted(id)));
    }

    //update
    public static TradeMark update(TradeMark mergeObj){
        return repository.merge(mergeObj);
    }

    //delete
    public static void delete(int id){
        repository.deleteById(id);
    }

    public static void delete(TradeMark deleteObj) throws ServiceExecutionException{
        if(deleteObj == null) throw new ServiceExecutionException("Provided null object");
        delete(deleteObj.getTrademarkId());
    }
}
