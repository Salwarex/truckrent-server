package utmn.truckrent.server.entity.truckmark;

import io.javalin.Javalin;
import io.javalin.http.Context;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.controller.rest.Response;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class TruckMarkController extends Controller {
    public TruckMarkController(Javalin app) {
        super(app);
    }

    @Override
    protected void initEndpoints() {
        post("create", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

                String title = ctx.formParam("title");

                TruckMark truckMark = createObject(ctx, title);
                if(truckMark == null) return;
                answerMapping(ctx, 200, 1, truckMark);
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
                TruckMark result = TruckMarkService.get(id);
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

                String title = ctx.formParam("title");

                TruckMark object = TruckMarkService.get(id);

                if(title != null) object.setTitle(title);

                TruckMark result = TruckMarkService.update(object);

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
                TruckMarkService.delete(id);

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

                List<List<TruckMark>> lists = new ArrayList<>();

                String title = ctx.queryParam("title");

                if(title != null){
                    lists.add(TruckMarkRepository.TruckMarkRepositoryImpl.instance.findAllByTitle(title));
                }

                List<TruckMark> result = new ArrayList<>();

                int i = 0;
                for(List<TruckMark> list: lists){
                    if(i == 0 && !lists.isEmpty()) result = lists.getFirst();
                    else result = ListUtils.and(result, list);
                    i++;
                }

                answerResponse(ctx, 200, new Response.ListResponse<>(1, result));

            }
            catch (NumberFormatException e){
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            }
            catch (Exception e){
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        });

        get("all", ctx -> {
            try{
                List<TruckMark> result = new ArrayList<>();
                result = TruckMarkRepository.getInstance().findAll();
                answerResponse(ctx, 200, new Response.ListResponse<>(1, result));
            }
            catch (NumberFormatException e){
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            }
            catch (Exception e){
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        });

        post("createAll", ctx -> {
            try {
                List<RawTruckMark> rawTruckMarks = ctx.bodyAsClass(
                        new com.fasterxml.jackson.core.type.TypeReference<List<RawTruckMark>>() {}.getType()
                );

                List<TruckMark> createdTruckMarks = new ArrayList<>();

                for (RawTruckMark raw : rawTruckMarks) {
                    TruckMark truckMark = createObject(ctx, raw.getTitle());
                    if (truckMark != null) createdTruckMarks.add(truckMark);
                    else return;
                }

                answerResponse(ctx, 200, new Response.ListResponse<>(1, createdTruckMarks));

            } catch (NumberFormatException e) {
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            } catch (Exception e) {
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        });
    }

    private TruckMark createObject(Context ctx, String title) {
        try{
            return TruckMarkService.register(title);
        }catch (ServiceExecutionException e){
            answerErr(ctx, 500, 0, "Возникла ошибка при регистрации объекта: %s".formatted(e.getMessage()));
            return null;
        }
    }

    @Override
    protected String path() {
        return "truckmark";
    }

    public static class RawTruckMark {
        private String title;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
    }
}
