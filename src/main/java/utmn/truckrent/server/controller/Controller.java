package utmn.truckrent.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import utmn.truckrent.server.Application;
import utmn.truckrent.server.controller.rest.Response;

public abstract class Controller {
    protected final Javalin app;

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
}
