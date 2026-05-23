package utmn.truckrent.server.entity.account;

import utmn.truckrent.server.Role;
import utmn.truckrent.server.entity.ServiceExecutionException;

import java.util.List;

public class AccountService {
    private static final AccountRepository.AccountRepositoryImpl repository = AccountRepository.getInstance();

    //create
    public static Account register(String login, String password, Role role) throws ServiceExecutionException {
        Account account = new Account();
        if(login == null || password == null) throw new ServiceExecutionException("Login and password can't be null!");

        account.setLogin(login);
        account.setPassword(password);
        account.setRole(role == null ? Role.USER : role);

        return repository.save(account);
    }

    //get
    public static Account get(String login) throws ServiceExecutionException{
        return repository.findByLogin(login).orElseThrow(() -> new ServiceExecutionException("Can't find account with provided login (%s)"
                .formatted(login)));
    }

    public static Account get(int id) throws ServiceExecutionException{
        return repository.findById(id).orElseThrow(() -> new ServiceExecutionException("Can't find account with provided id (#%d)"
                .formatted(id)));
    }

    //update
    public static Account update(Account account){
        return repository.merge(account);
    }

    //delete
    public static void delete(int id){
        repository.deleteById(id);
    }

    public static void delete(Account account) throws ServiceExecutionException{
        if(account == null) throw new ServiceExecutionException("Provided null account object");
        delete(account.getAccountId());
    }


    //filters
    public static List<Account> filterRole(Role role) throws ServiceExecutionException{
        if(role == null) throw new ServiceExecutionException("Provided null role object");
        return repository.findAllByRole(role);
    }
}
