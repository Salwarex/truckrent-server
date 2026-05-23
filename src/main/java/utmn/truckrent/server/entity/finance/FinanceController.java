package utmn.truckrent.server.entity.finance;

import io.javalin.Javalin;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.account.AccountService;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.entity.driver.DriverService;

import java.math.BigDecimal;
import java.util.Objects;

public class FinanceController extends Controller {
    public FinanceController(Javalin app) {
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

                Integer driverId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("accountId")));
                BigDecimal income = BigDecimal.valueOf(Long.parseLong(Objects.requireNonNull(ctx.formParam("income"))));
                BigDecimal outcome = BigDecimal.valueOf(Long.parseLong(Objects.requireNonNull(ctx.formParam("outcome"))));

                Driver driver = DriverService.get(driverId);

                try{
                    Finance result = FinanceService.register(driver, income, outcome);
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
                Finance result = FinanceService.get(id);
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

                String valueDriverId = ctx.formParam("driverId");
                String valueIncome = ctx.formParam("income");
                String valueOutcome = ctx.formParam("outcome");

                Finance object = FinanceService.get(id);

                if(valueDriverId != null){
                    Integer driverId = Integer.parseInt(valueDriverId);
                    Driver driver = DriverService.get(driverId);
                    object.setDriver(driver);
                }
                if(valueIncome != null){
                    BigDecimal income = BigDecimal.valueOf(Long.parseLong(valueIncome));
                    object.setIncome(income);
                }
                if(valueOutcome != null){
                    BigDecimal outcome = BigDecimal.valueOf(Long.parseLong(valueOutcome));
                    object.setOutcome(outcome);
                }

                Finance result = FinanceService.update(object);

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
                FinanceService.delete(id);

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
        return "finance";
    }
}
