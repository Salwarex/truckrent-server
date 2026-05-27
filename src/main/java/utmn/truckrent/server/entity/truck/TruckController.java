package utmn.truckrent.server.entity.truck;

import io.javalin.Javalin;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.controller.rest.Response;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.account.AccountService;
import utmn.truckrent.server.entity.truckmark.TruckMark;
import utmn.truckrent.server.entity.truckmark.TruckMarkRepository;
import utmn.truckrent.server.entity.truckmark.TruckMarkService;
import utmn.truckrent.server.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TruckController extends Controller {
    public TruckController(Javalin app) {
        super(app);
    }

    @Override
    protected void initEndpoints() {
        post("create", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

                String truckMarkIdValue = ctx.formParam("truckmarkId");
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
        get("read/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.USER.getLevel())) return;

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
        put("update/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

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
        delete("delete/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

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

        get("filter", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.USER.getLevel())) return;

                List<List<Truck>> lists = new ArrayList<>();

                String truckmarkIdStr = ctx.queryParam("truckmarkId");
                String loadLessStr = ctx.queryParam("capacity_less");
                String loadMoreStr = ctx.queryParam("capacity_more");

                if(truckmarkIdStr != null){
                    TruckMark truckMark = TruckMarkService.get(Integer.parseInt(truckmarkIdStr));
                    lists.add(TruckRepository.TruckRepositoryImpl.instance.findAllByTruckMark(truckMark));
                }
                if(loadLessStr != null){
                    int loadLess = Integer.parseInt(loadLessStr);
                    lists.add(TruckRepository.TruckRepositoryImpl.instance.findAllByLoadCapacityLess(loadLess));
                }
                if(loadMoreStr != null){
                    int loadMore = Integer.parseInt(loadMoreStr);
                    lists.add(TruckRepository.TruckRepositoryImpl.instance.findAllByLoadCapacityLess(loadMore));
                }

                List<Truck> result = new ArrayList<>();

                int i = 0;
                for(List<Truck> list: lists){
                    if(i == 0 && !lists.isEmpty()) result = lists.getFirst();
                    else result = ListUtils.and(result, list);
                    i++;
                }

                answerResponse(ctx, 200, new Response.ListResponse<>(1, result));

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
        });
    }

    @Override
    protected String path() {
        return "truck";
    }
}
