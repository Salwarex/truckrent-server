package utmn.truckrent.server.entity.container;

import io.javalin.Javalin;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.account.AccountService;
import utmn.truckrent.server.entity.trademark.TradeMark;
import utmn.truckrent.server.entity.trademark.TradeMarkService;

import java.util.Objects;

public class ContainerController extends Controller {
    public ContainerController(Javalin app) {
        super(app);
    }

    @Override
    protected void initEndpoints() {
        post("", ctx -> {
            try{
                int execId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("execId")));
                Account executor = AccountService.get(execId);
                if(executor == null || executor.getRole().getLevel() <= 2){
                    answerErr(ctx, 403, 0, "Доступ запрещён!");
                    return;
                }

                Integer trademarkId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("trademarkId")));
                TradeMark tradeMark = TradeMarkService.get(trademarkId);

                try{
                    Container account = ContainerService.register(tradeMark);
                    answerMapping(ctx, 200, 1, account);
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
        post("{id}", ctx -> {
            try{
                int execId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("execId")));
                Account executor = AccountService.get(execId);
                if(executor == null || executor.getRole().getLevel() <= 0){
                    answerErr(ctx, 403, 0, "Доступ запрещён!");
                    return;
                }

                int id = Integer.parseInt(ctx.pathParam("id"));
                Container result = ContainerService.get(id);
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
        put("{id}", ctx -> {
            try{
                int execId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("execId")));
                Account executor = AccountService.get(execId);
                if(executor == null || executor.getRole().getLevel() <= 2){
                    answerErr(ctx, 403, 0, "Доступ запрещён!");
                    return;
                }

                int id = Integer.parseInt(ctx.pathParam("id"));
                String valueTradeMarkId = ctx.formParam("trademarkId");

                Container object = ContainerService.get(id);

                if(valueTradeMarkId != null){
                    int trademarkId = Integer.parseInt(valueTradeMarkId);
                    TradeMark tradeMark = TradeMarkService.get(trademarkId);
                    object.setTradeMark(tradeMark);
                }

                Container result = ContainerService.update(object);

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
                if(executor == null || executor.getRole().getLevel() <= 2){
                    answerErr(ctx, 403, 0, "Доступ запрещён!");
                    return;
                }

                int id = Integer.parseInt(ctx.pathParam("id"));
                ContainerService.delete(id);

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
    }

    @Override
    protected String path() {
        return "container";
    }
}
