package utmn.truckrent.server.entity.delivery;

import io.javalin.Javalin;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.account.AccountService;
import utmn.truckrent.server.entity.container.Container;
import utmn.truckrent.server.entity.container.ContainerService;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.entity.driver.DriverService;
import utmn.truckrent.server.entity.partner.Partner;
import utmn.truckrent.server.entity.partner.PartnerService;
import utmn.truckrent.server.entity.truck.Truck;
import utmn.truckrent.server.entity.truck.TruckService;

import java.util.Objects;

public class DeliveryController extends Controller {
    public DeliveryController(Javalin app) {
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

                Integer senderId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("senderId")));
                Integer receiverId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("receiverId")));
                Integer driverId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("driverId")));
                Integer containerId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("containerId")));
                Integer truckId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("truckId")));

                Partner sender = PartnerService.get(senderId);
                Partner receiver = PartnerService.get(receiverId);
                Driver driver = DriverService.get(driverId);
                Container container = ContainerService.get(containerId);
                Truck truck = TruckService.get(truckId);

                try{
                    Delivery result = DeliveryService.register(sender, receiver, driver, container, truck);
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
        get("{id}", ctx -> {
            try{
                int id = Integer.parseInt(ctx.pathParam("id"));
                Delivery result = DeliveryService.get(id);
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
                Delivery object = DeliveryService.get(id);

                String valueSenderId = ctx.formParam("senderId");
                String valueReceiverId = ctx.formParam("receiverId");
                String valueDriverId = ctx.formParam("driverId");
                String valueContainerId = ctx.formParam("containerId");
                String valueTruckId = ctx.formParam("truckId");

                if(valueSenderId != null){
                    Integer senderId = Integer.valueOf(valueSenderId);
                    Partner sender = PartnerService.get(senderId);
                    object.setSender(sender);
                }
                if(valueReceiverId != null){
                    Integer receiverId = Integer.valueOf(valueReceiverId);
                    Partner receiver = PartnerService.get(receiverId);
                    object.setReceiver(receiver);
                }
                if(valueDriverId != null){
                    Integer driverId = Integer.valueOf(valueDriverId);
                    Driver driver = DriverService.get(driverId);
                    object.setDriver(driver);
                }
                if(valueContainerId != null){
                    Integer containerId = Integer.valueOf(valueContainerId);
                    Container container = ContainerService.get(containerId);
                    object.setContainer(container);
                }
                if(valueTruckId != null){
                    Integer truckId = Integer.valueOf(valueTruckId);
                    Truck truck = TruckService.get(truckId);
                    object.setTruck(truck);
                }
                //todo: ДАТЫ

                Delivery result = DeliveryService.update(object);

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
                DeliveryService.delete(id);

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
        return "delivery";
    }
}
