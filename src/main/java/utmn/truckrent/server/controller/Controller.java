package utmn.truckrent.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import ru.vit4liy.jwt.JwtExtractException;
import ru.vit4liy.jwt.JwtManager;
import utmn.truckrent.server.Application;
import utmn.truckrent.server.controller.rest.Response;
import utmn.truckrent.server.entity.ServiceExecutionException;
import utmn.truckrent.server.entity.account.Account;
import utmn.truckrent.server.entity.account.AccountService;
import utmn.truckrent.server.utils.TokenMaster;

public abstract class Controller {
    protected final Javalin app;
    protected final static JwtManager jwt = Application.getJwtManager();
    protected final static TokenMaster tokenMaster = Application.getTokenMaster64();

    public Controller(Javalin app) {
        this.app = app;
        initEndpoints();
    }

    protected abstract void initEndpoints();

    protected abstract String path();

    protected void post(String endpoint, Handler handler){
        app.post(getKeyName(endpoint), handler);
    }

    protected void get(String endpoint, Handler handler){
        app.get(getKeyName(endpoint), handler);
    }

    protected void put(String endpoint, Handler handler){
        app.put(getKeyName(endpoint), handler);
    }

    protected void patch(String endpoint, Handler handler){
        app.patch(getKeyName(endpoint), handler);
    }

    protected void delete(String endpoint, Handler handler){
        app.delete(getKeyName(endpoint), handler);
    }

    protected void head(String endpoint, Handler handler){
        app.head(getKeyName(endpoint), handler);
    }

    protected void options(String endpoint, Handler handler){app.options(getKeyName(endpoint), handler);}

    private String getKeyName(String endPoint){
        return "/" + path() + (endPoint == null || endPoint.isBlank() || endPoint.isEmpty() ? "" : "/" + endPoint);
    }

    protected void answerMapping(Context ctx, int code, int localCode, Object obj) {
        try{
            answerJsonDefault(ctx, code, localCode, Application.getObjectMapper().writeValueAsString(obj));
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    protected void answerJsonDefault(Context ctx, int code, int localCode, String json){
        answerResponse(ctx, code, new Response.Default(localCode, json));
    }

    protected void answerResponse(Context ctx, int code, Response response){
        ctx.status(code).json(response);
    }

    protected void answerErr(Context ctx, int httpCode, int apiCode, String description){
        ctx.status(httpCode).json(new Response.Default(apiCode, description));
    }


    private String extractBearerToken(Context ctx) {
        String header = ctx.header("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7).trim();
    }

    protected boolean checkAccess(Context ctx, String token, int requiredLevel) throws ServiceExecutionException {
        String accessToken = extractBearerToken(ctx);

        if (accessToken == null || accessToken.isBlank()) {
            answerErr(ctx, 401, 0, "Отсутствует access-token в заголовке 'Access-Token'");
            return false;
        }

        JwtManager.JwtExtractResult jwtCheckResult;
        try{
            jwtCheckResult = Application.getJwtManager().extractToken(accessToken);
        } catch (JwtExtractException e) {
            throw new RuntimeException(e);
        }

        if(!jwtCheckResult.check()){
            answerErr(ctx, 401, 0, "Токен устарел");
            return false;
        }

        String username = jwtCheckResult.username();
        Account executor = AccountService.get(username);
        if(executor == null || executor.getRole().getLevel() <= requiredLevel){
            answerErr(ctx, 403, 0, "Доступ запрещён!");
            return false;
        }

        return true;
    }
}
