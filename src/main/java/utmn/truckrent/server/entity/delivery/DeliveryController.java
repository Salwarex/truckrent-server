package utmn.truckrent.server.entity.delivery;

import io.javalin.Javalin;
import io.javalin.http.Context;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.controller.rest.Response;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.container.Container;
import utmn.truckrent.server.entity.container.ContainerService;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.entity.driver.DriverService;
import utmn.truckrent.server.entity.partner.Partner;
import utmn.truckrent.server.entity.partner.PartnerService;
import utmn.truckrent.server.entity.truck.Truck;
import utmn.truckrent.server.entity.truck.TruckService;
import utmn.truckrent.server.utils.ListUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeliveryController extends Controller {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

    public DeliveryController(Javalin app) {
        super(app);
    }

    @Override
    protected void initEndpoints() {
        post("create", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

                Integer senderId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("senderId")));
                Integer receiverId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("receiverId")));
                Integer driverId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("driverId")));
                Integer containerId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("containerId")));
                Integer truckId = Integer.valueOf(Objects.requireNonNull(ctx.formParam("truckId")));

                String loadedDateStr = ctx.queryParam("loadedDate");
                String unloadedDateStr = ctx.queryParam("unloadedDate");

                Partner sender = PartnerService.get(senderId);
                Partner receiver = PartnerService.get(receiverId);
                Driver driver = DriverService.get(driverId);
                Container container = ContainerService.get(containerId);
                Truck truck = TruckService.get(truckId);
                LocalDateTime loadedDate = null;
                LocalDateTime unloadedDate = null;

                if(loadedDateStr != null){
                    loadedDate = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(loadedDateStr)),
                            ZoneId.systemDefault()
                    );
                }
                if(unloadedDateStr != null){
                    unloadedDate = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(unloadedDateStr)),
                            ZoneId.systemDefault()
                    );
                }

                Delivery delivery = createObject(ctx, sender, receiver, driver, container, truck, loadedDate, unloadedDate);
                if(delivery == null) return;
                answerMapping(ctx, 200, 1, delivery);
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
        put("update/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

                int id = Integer.parseInt(ctx.pathParam("id"));
                Delivery object = DeliveryService.get(id);

                String valueSenderId = ctx.formParam("senderId");
                String valueReceiverId = ctx.formParam("receiverId");
                String valueDriverId = ctx.formParam("driverId");
                String valueContainerId = ctx.formParam("containerId");
                String valueTruckId = ctx.formParam("truckId");
                String loadedDateStr = ctx.queryParam("loadedDate");
                String unloadedDateStr = ctx.queryParam("unloadedDate");

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
                if(loadedDateStr != null){
                    LocalDateTime loadedDate = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(loadedDateStr)),
                            ZoneId.systemDefault()
                    );
                    object.setLoadedDate(loadedDate);
                }
                if(unloadedDateStr != null){
                    LocalDateTime unloadedDate = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(unloadedDateStr)),
                            ZoneId.systemDefault()
                    );
                    object.setUnloadedDate(unloadedDate);
                }

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
        delete("delete/{id}", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

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

        get("filter", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.USER.getLevel())) return;

                List<List<Delivery>> lists = new ArrayList<>();

                String driverIdStr = ctx.queryParam("driverId");
                String senderIdStr = ctx.queryParam("senderId");
                String receiverIdStr = ctx.queryParam("receiverId");
                String containerIdStr = ctx.queryParam("containerId");
                String truckIdStr = ctx.queryParam("truckId");
                String loadedUntilTimestampStr = ctx.queryParam("loadedUntil");
                String loadedAfterTimestampStr = ctx.queryParam("loadedAfter");
                String unloadedUntilTimestampStr = ctx.queryParam("unloadedUntil");
                String unloadedAfterTimestampStr = ctx.queryParam("unloadedAfter"); //кол-во миллисекунд с 1970

                if(driverIdStr != null){
                    Driver driver = DriverService.get(Integer.parseInt(driverIdStr));
                    lists.add(DeliveryRepository.DeliveryRepositoryImpl.instance.findAllByDriver(driver));
                }
                if(senderIdStr != null){
                    Partner sender = PartnerService.get(Integer.parseInt(senderIdStr));
                    lists.add(DeliveryRepository.DeliveryRepositoryImpl.instance.findAllBySender(sender));
                }
                if(receiverIdStr != null){
                    Partner receiver = PartnerService.get(Integer.parseInt(receiverIdStr));
                    lists.add(DeliveryRepository.DeliveryRepositoryImpl.instance.findAllByReceiver(receiver));
                }
                if(containerIdStr != null){
                    Container container = ContainerService.get(Integer.parseInt(containerIdStr));
                    lists.add(DeliveryRepository.DeliveryRepositoryImpl.instance.findAllByContainer(container));
                }
                if(truckIdStr != null){
                    Truck truck = TruckService.get(Integer.parseInt(truckIdStr));
                    lists.add(DeliveryRepository.DeliveryRepositoryImpl.instance.findAllByTruck(truck));
                }
                if(loadedUntilTimestampStr != null){
                    System.out.printf("Provided Timestamp: %s%n", loadedUntilTimestampStr);

                    LocalDateTime loadedUntil = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(loadedUntilTimestampStr)),
                            ZoneId.systemDefault()
                    );

                    System.out.println(FORMATTER.format(loadedUntil));
                    lists.add(DeliveryRepository.DeliveryRepositoryImpl.instance.findAllLoadedUntil(loadedUntil));
                }
                if(loadedAfterTimestampStr != null){
                    System.out.printf("Provided Timestamp: %s%n", loadedAfterTimestampStr);

                    LocalDateTime loadedAfter = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(loadedAfterTimestampStr)),
                            ZoneId.systemDefault()
                    );

                    System.out.println(FORMATTER.format(loadedAfter));
                    lists.add(DeliveryRepository.DeliveryRepositoryImpl.instance.findAllLoadedAfter(loadedAfter));
                }
                if(unloadedUntilTimestampStr != null){
                    System.out.printf("Provided Timestamp: %s%n", unloadedUntilTimestampStr);

                    LocalDateTime unloadedUntil = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(unloadedUntilTimestampStr)),
                            ZoneId.systemDefault()
                    );

                    System.out.println(FORMATTER.format(unloadedUntil));
                    lists.add(DeliveryRepository.DeliveryRepositoryImpl.instance.findAllUnloadedUntil(unloadedUntil));
                }
                if(unloadedAfterTimestampStr != null){
                    System.out.printf("Provided Timestamp: %s%n", unloadedAfterTimestampStr);

                    LocalDateTime unloadedAfter = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(unloadedAfterTimestampStr)),
                            ZoneId.systemDefault()
                    );

                    System.out.println(FORMATTER.format(unloadedAfter));
                    lists.add(DeliveryRepository.DeliveryRepositoryImpl.instance.findAllUnloadedAfter(unloadedAfter));
                }

                List<Delivery> result = new ArrayList<>();

                int i = 0;
                for(List<Delivery> list: lists){
                    if(i == 0 && !lists.isEmpty()) result = lists.getFirst();
                    else result = ListUtils.and(result, list);
                    i++;
                }

                answerResponse(ctx, 200, new Response.ListResponse<>(1, result));

            }catch (ServiceExecutionException e){
                e.printStackTrace();
                answerErr(ctx, 500, 0, "Ошибка сервиса: %s".formatted(e.getMessage()));
            }
            catch (NumberFormatException e){
                e.printStackTrace();
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            }
            catch (Exception e){
                e.printStackTrace();
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        });

        get("all", ctx -> {
            try{
                List<Delivery> result = new ArrayList<>();
                result = DeliveryRepository.getInstance().findAll();
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
                List<RawDelivery> rawDeliveries = ctx.bodyAsClass(
                        new com.fasterxml.jackson.core.type.TypeReference<List<RawDelivery>>() {}.getType()
                );

                List<Delivery> createdDeliveries = new ArrayList<>();

                for (RawDelivery raw : rawDeliveries) {
                    Partner sender = PartnerService.get(raw.getSenderId());
                    Partner receiver = PartnerService.get(raw.getReceiverId());
                    Driver driver = DriverService.get(raw.getDriverId());
                    Container container = ContainerService.get(raw.getContainerId());
                    Truck truck = TruckService.get(raw.getTruckId());

                    LocalDateTime loadedDate = null;
                    if (raw.getLoadedDate() != null) {
                        loadedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(raw.getLoadedDate()), ZoneId.systemDefault());
                    }

                    LocalDateTime unloadedDate = null;
                    if (raw.getUnloadedDate() != null) {
                        unloadedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(raw.getUnloadedDate()), ZoneId.systemDefault());
                    }

                    Delivery delivery = createObject(ctx, sender, receiver, driver, container, truck, loadedDate, unloadedDate);
                    if (delivery != null) createdDeliveries.add(delivery);
                    else return;
                }

                answerResponse(ctx, 200, new Response.ListResponse<>(1, createdDeliveries));

            } catch (NumberFormatException e) {
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            } catch (ServiceExecutionException e) {
                answerErr(ctx, 500, 0, "Ошибка сервиса: %s".formatted(e.getMessage()));
            } catch (Exception e) {
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        });
    }

    private Delivery createObject(Context ctx, Partner sender, Partner receiver, Driver driver, Container container, Truck truck, LocalDateTime loadedDate, LocalDateTime unloadedDate) {
        try{
            return DeliveryService.register(sender, receiver, driver, container, truck, loadedDate, unloadedDate);
        }catch (ServiceExecutionException e){
            answerErr(ctx, 500, 0, "Возникла ошибка при регистрации объекта: %s".formatted(e.getMessage()));
            return null;
        }
    }

    @Override
    protected String path() {
        return "delivery";
    }

    public static class RawDelivery {
        private Integer senderId;
        private Integer receiverId;
        private Integer driverId;
        private Integer containerId;
        private Integer truckId;
        private Long loadedDate;
        private Long unloadedDate;

        public Integer getSenderId() { return senderId; }
        public void setSenderId(Integer senderId) { this.senderId = senderId; }
        public Integer getReceiverId() { return receiverId; }
        public void setReceiverId(Integer receiverId) { this.receiverId = receiverId; }
        public Integer getDriverId() { return driverId; }
        public void setDriverId(Integer driverId) { this.driverId = driverId; }
        public Integer getContainerId() { return containerId; }
        public void setContainerId(Integer containerId) { this.containerId = containerId; }
        public Integer getTruckId() { return truckId; }
        public void setTruckId(Integer truckId) { this.truckId = truckId; }
        public Long getLoadedDate() { return loadedDate; }
        public void setLoadedDate(Long loadedDate) { this.loadedDate = loadedDate; }
        public Long getUnloadedDate() { return unloadedDate; }
        public void setUnloadedDate(Long unloadedDate) { this.unloadedDate = unloadedDate; }
    }
}
