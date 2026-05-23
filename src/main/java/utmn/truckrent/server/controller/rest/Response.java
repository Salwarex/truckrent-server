package utmn.truckrent.server.controller.rest;

import java.util.List;

public interface Response {
    int getCode();

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


    class Default implements Response{
        private int code;
        private String content;

        public Default(int code, String content) {
            this.code = code;
            this.content = content;
        }

        public Default() {
        }

        @Override
        public int getCode() {
            return code;
        }

        public String getContent() {
            return content;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    class ListResponse<T> implements Response{
        private int code;
        private List<T> list;

        public ListResponse(int code, List<T> list) {
            this.code = code;
            this.list = list;
        }

        public ListResponse() {
        }

        @Override
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }
    }
}
