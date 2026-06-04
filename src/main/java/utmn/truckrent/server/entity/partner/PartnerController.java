package utmn.truckrent.server.entity.partner;

import io.javalin.Javalin;
import io.javalin.http.Context;
import utmn.truckrent.server.Role;
import utmn.truckrent.server.controller.Controller;
import utmn.truckrent.server.controller.rest.Response;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.account.AccountService;
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

                Partner partner = createObject(ctx, title, contactPhone, contactEmail, contactName, account);
                if(partner == null) return;
                answerMapping(ctx, 200, 1, partner);
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
                String phone = ctx.queryParam("contactPhone");
                if(phone != null){
                    lists.add(PartnerRepository.PartnerRepositoryImpl.instance.findAllByPhone(phone));
                }
                String email = ctx.queryParam("contactEmail");
                if(email != null){
                    lists.add(PartnerRepository.PartnerRepositoryImpl.instance.findAllByEmail(email));
                }
                String contactName = ctx.queryParam("contactName");
                if(contactName != null){
                    lists.add(PartnerRepository.PartnerRepositoryImpl.instance.findAllByContactName(contactName));
                }
                String accountIdStr = ctx.queryParam("accountId");
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

        get("all", ctx -> {
            try{
                List<Partner> result = new ArrayList<>();
                result = PartnerRepository.getInstance().findAll();
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
                List<RawPartner> rawPartners = ctx.bodyAsClass(
                        new com.fasterxml.jackson.core.type.TypeReference<List<RawPartner>>() {}.getType()
                );

                List<Partner> createdPartners = new ArrayList<>();

                for (RawPartner raw : rawPartners) {
                    Account account = AccountService.get(raw.getAccountId());
                    Partner partner = createObject(ctx, raw.getTitle(), raw.getContactPhone(), raw.getContactEmail(), raw.getContactName(), account);
                    if (partner != null) createdPartners.add(partner);
                    else return;
                }

                answerResponse(ctx, 200, new Response.ListResponse<>(1, createdPartners));

            } catch (NumberFormatException e) {
                answerErr(ctx, 400, 0, "Неккоректные параметры запроса: %s".formatted(e.getMessage()));
            } catch (ServiceExecutionException e) {
                answerErr(ctx, 500, 0, "Ошибка сервиса: %s".formatted(e.getMessage()));
            } catch (Exception e) {
                answerErr(ctx, 500, 0, "Внутренняя ошибка сервера: %s".formatted(e.getMessage()));
            }
        });
    }

    private Partner createObject(Context ctx, String title, String contactPhone, String contactEmail, String contactName, Account account) {
        try{
            return PartnerService.register(title, contactPhone, contactEmail, contactName, account);
        }catch (ServiceExecutionException e){
            answerErr(ctx, 500, 0, "Возникла ошибка при регистрации объекта: %s".formatted(e.getMessage()));
            return null;
        }
    }

    @Override
    protected String path() {
        return "partner";
    }

    public static class RawPartner {
        private String title;
        private String contactPhone;
        private String contactEmail;
        private String contactName;
        private Integer accountId;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContactPhone() { return contactPhone; }
        public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
        public String getContactEmail() { return contactEmail; }
        public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
        public String getContactName() { return contactName; }
        public void setContactName(String contactName) { this.contactName = contactName; }
        public Integer getAccountId() { return accountId; }
        public void setAccountId(Integer accountId) { this.accountId = accountId; }
    }
}
