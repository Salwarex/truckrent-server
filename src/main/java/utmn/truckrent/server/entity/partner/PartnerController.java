package utmn.truckrent.server.entity.partner;

import io.javalin.Javalin;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.controller.rest.Response;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.account.AccountService;
import utmn.truckrent.server.entity.driver.Driver;
import utmn.truckrent.server.entity.driver.DriverService;
import utmn.truckrent.server.entity.finance.Finance;
import utmn.truckrent.server.entity.finance.FinanceRepository;
import utmn.truckrent.server.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PartnerController extends Controller {
    public PartnerController(Javalin app) {
        super(app);
    }

    @Override
    protected void initEndpoints() {
        post("create", ctx -> {
            try{
                if(!checkAccess(ctx, ctx.header("Access-Token"), Role.ADMIN.getLevel())) return;

                String title = ctx.formParam("title");
                String contactPhone = ctx.formParam("contactPhone");
                String contactEmail = ctx.formParam("contactEmail");
                String contactName = ctx.formParam("contactName");
                String accountIdValue = ctx.formParam("accountId");

                Integer accountId = Integer.parseInt(Objects.requireNonNull(accountIdValue));

                Account account = AccountService.get(accountId);

                try{
                    Partner result = PartnerService.register(title, contactPhone, contactEmail, contactName, account);
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
                Partner result = PartnerService.get(id);
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
                String contactPhone = ctx.formParam("contactPhone");
                String contactEmail = ctx.formParam("contactEmail");
                String contactName = ctx.formParam("contactName");
                String accountIdValue = ctx.formParam("accountId");

                Partner object = PartnerService.get(id);

                if(accountIdValue != null){
                    Integer accountId = Integer.parseInt(Objects.requireNonNull(accountIdValue));
                    Account account = AccountService.get(accountId);
                    object.setAccount(account);
                }
                if(title != null) object.setTitle(title);
                if(contactPhone != null) object.setContactPhone(contactPhone);
                if(contactEmail != null) object.setContactEmail(contactEmail);
                if(contactName != null) object.setContactName(contactName);

                Partner result = PartnerService.update(object);

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
                PartnerService.delete(id);

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

                List<List<Partner>> lists = new ArrayList<>();

                String title = ctx.queryParam("title");
                if(title != null){
                    lists.add(PartnerRepository.PartnerRepositoryImpl.instance.findAllByTitle(title));
                }
                String phone = ctx.queryParam("phone");
                if(phone != null){
                    lists.add(PartnerRepository.PartnerRepositoryImpl.instance.findAllByPhone(phone));
                }
                String email = ctx.queryParam("email");
                if(email != null){
                    lists.add(PartnerRepository.PartnerRepositoryImpl.instance.findAllByEmail(email));
                }
                String contactName = ctx.queryParam("contact_name");
                if(contactName != null){
                    lists.add(PartnerRepository.PartnerRepositoryImpl.instance.findAllByContactName(contactName));
                }
                String accountIdStr = ctx.queryParam("account");
                if(accountIdStr != null){
                    Account account = AccountService.get(Integer.parseInt(accountIdStr));
                    lists.add(PartnerRepository.PartnerRepositoryImpl.instance.findAllByAccount(account));
                }

                List<Partner> result = new ArrayList<>();

                int i = 0;
                for(List<Partner> list: lists){
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
        return "partner";
    }
}
