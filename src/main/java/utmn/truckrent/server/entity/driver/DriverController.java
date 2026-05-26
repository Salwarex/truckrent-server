package utmn.truckrent.server.entity.driver;

import io.javalin.Javalin;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.controller.rest.Response;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.account.AccountService;
import utmn.truckrent.server.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DriverController extends Controller {
    public DriverController(Javalin app) {
        super(app);
    }

    @Override
    protected void initEndpoints() {
        post("create", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

                String surname = ctx.formParam("surname");
                String name = ctx.formParam("name");
                String lastname = ctx.formParam("lastname");
                Integer accountId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("accountId")));

                Account account = AccountService.get(accountId);

                try{
                    Driver result = DriverService.register(surname, name, lastname, account);
                    answerMapping(ctx, 200, 1, result);
                }catch (ServiceExecutionException e){
                    answerErr(ctx, 500, 0, "Возникла ошибка при регистрации объекта: %s".formatted(e.getMessage()));
                }
            }catch (ServiceExecutionException e){
                answerErr(ctx, 500, 0, "Ошибка сервиса: %s".formatted(e.getMessage()));
            }
            catch (NumberFormatException e){
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            }
            catch (Exception e){
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        }); //создание нового
        get("read/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.USER.getLevel())) return;

                int id = Integer.parseInt(ctx.pathParam("id"));
                Driver result = DriverService.get(id);
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
        }); //получение
        put("update/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

                int id = Integer.parseInt(ctx.pathParam("id"));

                String surname = ctx.formParam("surname");
                String name = ctx.formParam("name");
                String lastname = ctx.formParam("lastname");
                String valueAccountId = ctx.formParam("accountId");

                Driver object = DriverService.get(id);

                if(surname != null) object.setSurname(surname);
                if(name != null) object.setName(name);
                if(lastname != null) object.setLastname(lastname);
                if(valueAccountId != null) {
                    int accountId = Integer.parseInt(valueAccountId);
                    Account account = AccountService.get(accountId);
                    object.setAccount(account);
                }

                Driver result = DriverService.update(object);

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
        delete("delete/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

                int id = Integer.parseInt(ctx.pathParam("id"));
                DriverService.delete(id);

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


        get("filter", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.USER.getLevel())) return;

                List<List<Driver>> lists = new ArrayList<>();

                String accountIdStr = ctx.queryParam("account");
                String surname = ctx.queryParam("surname");
                String name = ctx.queryParam("name");
                String lastname = ctx.queryParam("lastname");


                if(accountIdStr != null){
                    Account account = AccountService.get(Integer.parseInt(accountIdStr));
                    lists.add(DriverRepository.DriverRepositoryImpl.instance.findAllByAccount(account));
                }
                if(surname != null){
                    lists.add(DriverRepository.DriverRepositoryImpl.instance.findAllBySurname(surname));
                }
                if(name != null){
                    lists.add(DriverRepository.DriverRepositoryImpl.instance.findAllByName(name));
                }
                if(lastname != null){
                    lists.add(DriverRepository.DriverRepositoryImpl.instance.findAllByLastname(lastname));
                }

                List<Driver> result = new ArrayList<>();

                int i = 0;
                for(List<Driver> list: lists){
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
        return "driver";
    }
}
