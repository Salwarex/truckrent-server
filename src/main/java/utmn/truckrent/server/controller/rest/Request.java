package utmn.truckrent.server.controller.rest;

public interface Request {
    String getToken();

    class Default implements Request{
        private String token;

        public Default(String token) {
            this.token = token;
        }

        public Default() {
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    class CheckMatchingPassRequest implements Request{
        private String token;
        private String username;
        private String passwordEncoded;


        public CheckMatchingPassRequest() {
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPasswordEncoded() {
            return passwordEncoded;
        }

        public void setPasswordEncoded(String passwordEncoded) {
            this.passwordEncoded = passwordEncoded;
        }
    }

    class RefreshAccessToken implements Request{
        private String providedRefresh;
        private String username;
        private String clientKey;

        public RefreshAccessToken(String providedRefresh, String username) {
            this.providedRefresh = providedRefresh;
            this.username = username;
        }

        public RefreshAccessToken() {
        }

        public String getProvidedRefresh() {
            return providedRefresh;
        }

        public void setProvidedRefresh(String providedRefresh) {
            this.providedRefresh = providedRefresh;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getClientKey() {
            return clientKey;
        }

        public void setClientKey(String clientKey) {
            this.clientKey = clientKey;
        }

        @Override
        public String getToken() {
            return "";
        }
    }
}
