package utmn.truckrent.server.entity.account;

import io.javalin.Javalin;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.controller.rest.Response;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.entity.driver.DriverService;
import utmn.truckrent.server.entity.partner.Partner;
import utmn.truckrent.server.entity.partner.PartnerController;
import utmn.truckrent.server.entity.partner.PartnerService;
import utmn.truckrent.server.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountController extends Controller {
    public AccountController(Javalin app) {
        super(app);
    }

    @Override
    protected void initEndpoints() {
        post("", ctx -> {
            String login = ctx.formParam("login");
            String password = ctx.formParam("password");
            String roleStr = ctx.formParam("role");

            Role role = null;
            if(roleStr != null) role = Role.valueOf(roleStr);

            try{
                Account account = AccountService.register(login, password, role);
                answerMapping(ctx, 200, 1, account);
            }catch (ServiceExecutionException e){
                answerErr(ctx, 500, 0, "Возникла ошибка при регистрации пользователя: %s".formatted(e.getMessage()));
            }
        }); //создание нового аккаунта
        get("{id}", ctx -> {
            try{
                int id = Integer.parseInt(ctx.pathParam("id"));
                Account result = AccountService.get(id);
                answerMapping(ctx, 200, 1, result);
            }catch (ServiceExecutionException e){
                answerErr(ctx, 500, 0, "Ошибка сервиса: %s".formatted(e.getMessage()));
            }
            catch (NumberFormatException e){
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            }
            catch (Exception e){
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        }); //получение аккаунта
        put("{id}", ctx -> {
            try{
                int execId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("execId")));
                Account executor = AccountService.get(execId);
                if(executor == null || executor.getRole().getLevel() <= 0){
                    answerErr(ctx, 403, 0, "Доступ запрещён!");
                    return;
                }

                int id = Integer.parseInt(ctx.pathParam("id"));
                String login = ctx.formParam("login");
                String password = ctx.formParam("password");
                String roleStr = ctx.formParam("role");

                Account account = AccountService.get(id);
                if(login != null) account.setLogin(login);
                if(password != null) account.setPassword(password);
                if(roleStr != null){
                    Role role = Role.valueOf(roleStr);
                    account.setRole(role);
                }

                Account result = AccountService.update(account);

                answerMapping(ctx, 200, 1, result);
            }catch (ServiceExecutionException e){
                answerErr(ctx, 500, 0, "Ошибка сервиса: %s".formatted(e.getMessage()));
            }
            catch (NumberFormatException e){
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            }
            catch (Exception e){
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        }); //внесение изменений
        delete("{id}", ctx -> {
            try{
                int execId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("execId")));
                Account executor = AccountService.get(execId);
                if(executor == null || executor.getRole().getLevel() <= 0){
                    answerErr(ctx, 403, 0, "Доступ запрещён!");
                    return;
                }

                int id = Integer.parseInt(ctx.pathParam("id"));
                AccountService.delete(id);

                answerMapping(ctx, 200, 1, "");
            }
            catch (ServiceExecutionException e){
                answerErr(ctx, 500, 0, "Ошибка сервиса: %s".formatted(e.getMessage()));
            }
            catch (NumberFormatException e){
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            }
            catch (Exception e){
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        }); //удаление
        post("auth", ctx -> {
            try{
                String login = ctx.formParam("login");
                String password = ctx.formParam("password");

                if(login == null || password == null)
                    answerErr(ctx, 400, 0, "Неккоректные параметры запроса: Логин или пароль пусты!");

                Account account = AccountService.get(login);
                if(account.isPasswordMatch(password)){
                    answerMapping(ctx, 200, 1, account);
                }
                else{
                    answerErr(ctx, 403, 0, "Неверный логин или пароль!");
                }
            }catch (ServiceExecutionException e){
                answerErr(ctx, 403, 0, "Неверный логин или пароль!");
            }
            catch (NumberFormatException e){
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            }
            catch (Exception e){
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        }); //аутентификация и авторизация

        get("filter", ctx -> {
            try{
                List<List<Account>> lists = new ArrayList<>();

                String roleStr = ctx.queryParam("role");
                String driverIdStr = ctx.queryParam("driverId");
                String partnerIdStr = ctx.queryParam("partnerId");

                if(roleStr != null){
                    Role role = Role.valueOf(roleStr);
                    lists.add(AccountService.filterRole(role));
                }
                if(driverIdStr != null){
                    int id = Integer.parseInt(driverIdStr);
                    Driver driver = DriverService.get(id);

                    lists.add(AccountRepository.getInstance().findAllByDriver(driver));
                }
                if(partnerIdStr != null){
                    int id = Integer.parseInt(partnerIdStr);
                    Partner partner = PartnerService.get(id);

                    lists.add(AccountRepository.getInstance().findAllByPartner(partner));
                }

                List<Account> result = new ArrayList<>();

                int i = 0;
                for(List<Account> list: lists){
                    if(i == 0 && !lists.isEmpty()) result = lists.getFirst();
                    else result = ListUtils.and(result, list);
                    i++;
                }

                answerResponse(ctx, 200, new Response.ListResponse<>(1, result));

            }catch (ServiceExecutionException e){
                answerErr(ctx, 500, 0, "Ошибка сервиса: %s".formatted(e.getMessage()));
            }
            catch (NumberFormatException e){
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            }
            catch (Exception e){
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        });
    }

    @Override
    protected String path() {
        return "account";
    }
}
