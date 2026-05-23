package utmn.truckrent.server.controller;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public abstract class Controller {
    protected final Javalin app;

    public Controller(Javalin app) {
        this.app = app;
        initEndpoints();
    }

    protected abstract void initEndpoints();
    //ctx.status(200).json(new Response.Default(1, Application.getObjectMapper().writeValueAsString(user)));

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
}
