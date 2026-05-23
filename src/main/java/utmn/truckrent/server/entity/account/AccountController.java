package utmn.truckrent.server.entity.account;

import io.javalin.Javalin;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.entity.ServiceExecutionException;

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
        post("{id}", ctx -> {
            try{
                int execId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("execId")));
                Account executor = AccountService.get(execId);
                if(executor == null || executor.getRole().getLevel() <= 0){
                    answerErr(ctx, 403, 0, "Доступ запрещён!");
                    return;
                }

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
        get("{id}", ctx -> {

        }); //аутентификация и авторизация
    }

    @Override
    protected String path() {
        return "account";
    }
}
