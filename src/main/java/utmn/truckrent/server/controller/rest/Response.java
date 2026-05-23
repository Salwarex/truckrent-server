package utmn.truckrent.server.controller.rest;

import io.javalin.http.Context;

public interface Response {
    int getCode();

    String getDescription();

    /*
     * -10 - Не выполнено: Неизвестная ошибка<br>
     * -9 - Зарезервировано
     * -8 - Не выполнено: Ошибка стороннего ресурса
     * -7 - Не выполнено: Ошибка базы данных
     * -6 - Не выполнено: Доступ запрещен
     * -5 - Не выполнено: Ошибка окружения
     * -4 - Не выполнено: Плохие параметры (ошибка типизации или неверное количество)
     * -3 - Не выполнено: Плохие параметры (ошибка формата)
     * -2 - Не выполнено: Ошибка кода
     * -1 - Выполнено, но неудачно (условные запросы)
     * 0 - Выполнено
     * 1 - Удачно выполнено (условные запросы)
     * 2 - Выполнено частично (условные запросы)
     * 3-10 - Зарезервировано
     * 11 - Задача в процессе
     * 12 - Обрабатывается
     */
    static void err(Context ctx, int httpCode, int apiCode, String description){
        ctx.status(httpCode).json(new Default(apiCode, description));
    }

    class Default implements Response{
        private int code;
        private String description;

        public Default(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public Default() {
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
