package utmn.truckrent.server.entity.account;

import io.javalin.Javalin;
import ru.vit4liy.jwt.JwtBuildException;
import ru.vit4liy.jwt.JwtManager;
import utmn.truckrent.server.Application;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.controller.rest.Response;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountController extends Controller {
    private long ACCESS_TOKEN_TTL = 60 * 60 * 1000L; // 60 минут

    public AccountController(Javalin app) {
        super(app);
    }

    @Override
    protected void initEndpoints() {
        post("create", ctx -> {
            String login = ctx.formParam("login");
            String password = ctx.formParam("password");
            String roleStr = ctx.formParam("role");

            Role role = null;
            if(roleStr != null) role = Role.valueOf(roleStr);

            try{
                Account account = AccountService.register(login, password, role);
                answerResponse(ctx, 200,
                        new Response.SuccessAccessResponse(1,
                                account,
                                account.getRefreshToken(),
                                Application.getJwtManager().getToken(account, account, ACCESS_TOKEN_TTL)
                        ));
            }catch (ServiceExecutionException e){
                answerErr(ctx, 500, 0, "Возникла ошибка при регистрации пользователя: %s".formatted(e.getMessage()));
            }
        }); //создание нового аккаунта
        get("read/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.USER.getLevel())) return;

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
        put("update/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

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
        delete("delete/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

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
                    answerResponse(ctx, 200,
                            new Response.SuccessAccessResponse(1,
                                    account,
                                    account.getRefreshToken(),
                                    Application.getJwtManager().getToken(account, account, ACCESS_TOKEN_TTL)
                            ));
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

                if(roleStr != null){
                    Role role = Role.valueOf(roleStr);
                    lists.add(AccountService.filterRole(role));
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

        get("refresh", ctx -> {
            try {
                String refreshToken = ctx.header("Refresh-Token");
                if (refreshToken == null || refreshToken.isBlank()) {
                    answerErr(ctx, 400, 0, "Отсутствует refresh-token в заголовке 'Refresh-Token'");
                    return;
                }

                Account account = AccountRepository.AccountRepositoryImpl.instance.findByRefreshToken(refreshToken)
                        .orElseThrow(() -> new ServiceExecutionException("Аккаунт с указанным refresh-token не найден!"));

                String newAccessToken = Application.getJwtManager().getToken(account, account, ACCESS_TOKEN_TTL);

                ctx.json(Map.of(
                        "status", 1,
                        "access_token", newAccessToken,
                        "token_type", "Bearer",
                        "expires_in", ACCESS_TOKEN_TTL / 1000 // в секундах
                ));
                ctx.status(200);

            } catch (JwtBuildException e) {
                answerErr(ctx, 500, 0, "Ошибка генерации токена: %s".formatted(e.getMessage()));
            } catch (ServiceExecutionException e) {
                answerErr(ctx, 500, 0, "Ошибка сервиса: %s".formatted(e.getMessage()));
            } catch (Exception e) {
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        });
    }

    @Override
    protected String path() {
        return "account";
    }
}
