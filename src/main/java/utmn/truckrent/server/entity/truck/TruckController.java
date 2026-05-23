package utmn.truckrent.server.entity.truck;

import io.javalin.Javalin;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.account.AccountService;
import utmn.truckrent.server.entity.truckmark.TruckMark;
import utmn.truckrent.server.entity.truckmark.TruckMarkService;

import java.util.Objects;

public class TruckController extends Controller {
    public TruckController(Javalin app) {
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

                String truckMarkIdValue = ctx.formParam("truckMarkId");
                String loadCapacityKgValue = ctx.formParam("loadCapacityKg");

                int truckMarkId = Integer.parseInt(Objects.requireNonNull(truckMarkIdValue));
                int loadCapacityKg = Integer.parseInt(Objects.requireNonNull(loadCapacityKgValue));

                TruckMark truckMark = TruckMarkService.get(truckMarkId);

                try{
                    Truck result = TruckService.register(truckMark, loadCapacityKg);
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
        post("{id}", ctx -> {
            try{
                int execId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("execId")));
                Account executor = AccountService.get(execId);
                if(executor == null || executor.getRole().getLevel() <= 0){
                    answerErr(ctx, 403, 0, "Доступ запрещён!");
                    return;
                }

                int id = Integer.parseInt(ctx.pathParam("id"));
                Truck result = TruckService.get(id);
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

                String truckMarkIdValue = ctx.formParam("truckMarkId");
                String loadCapacityKgValue = ctx.formParam("loadCapacityKg");

                Truck object = TruckService.get(id);

                if(truckMarkIdValue != null) {
                    int truckMarkId = Integer.parseInt(truckMarkIdValue);
                    TruckMark truckMark = TruckMarkService.get(truckMarkId);
                    object.setTruckMark(truckMark);
                }
                if(loadCapacityKgValue != null){
                    int loadCapacityKg = Integer.parseInt(Objects.requireNonNull(loadCapacityKgValue));
                    object.setLoadCapacityKg(loadCapacityKg);
                }

                Truck result = TruckService.update(object);

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
                TruckService.delete(id);

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
        return "truck";
    }
}
