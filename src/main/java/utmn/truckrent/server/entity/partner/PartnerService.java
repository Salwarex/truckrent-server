package utmn.truckrent.server.entity.partner;

import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;

public class PartnerService {
    private static final PartnerRepository.PartnerRepositoryImpl repository = PartnerRepository.getInstance();

    //create
    public static Partner register(
            String title,
            String contactPhone,
            String contactEmail,
            String contactName,
            Account account
    ) throws ServiceExecutionException {
        Partner partner = new Partner();
        if(title == null || account == null)
            throw new ServiceExecutionException("One of not-nullable provided parameters is null!");

        partner.setTitle(title);
        partner.setContactPhone(contactPhone);
        partner.setContactEmail(contactEmail);
        partner.setContactName(contactName);
        partner.setAccount(account);

        return repository.save(partner);
    }

    //get
    public static Partner get(int id) throws ServiceExecutionException{
        return repository.findById(id).orElseThrow(() -> new ServiceExecutionException("Can't find object with provided id (#%d)"
                .formatted(id)));
    }

    //update
    public static Partner update(Partner mergeObj){
        return repository.merge(mergeObj);
    }

    //delete
    public static void delete(int id){
        repository.deleteById(id);
    }

    public static void delete(Partner deleteObj) throws ServiceExecutionException{
        if(deleteObj == null) throw new ServiceExecutionException("Provided null object");
        delete(deleteObj.getPartnerId());
    }
}
