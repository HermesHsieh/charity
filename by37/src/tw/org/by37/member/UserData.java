package tw.org.by37.member;

public class UserData {

        /** 使用者登入狀態 **/
        private boolean userStatus = false;
        /** 回傳的資料結果 **/
        private String status;
        /** 回傳的訊息 **/
        private String message;
        /** 使用者的JSONOjbect資料 **/
        private User user;

        /** Constructor **/
        public UserData() {
        }

        /** 使用者登入狀態 **/
        public void setUserStatus(boolean status) {
                this.userStatus = status;
        }

        public boolean getUserStatus() {
                return userStatus;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public String getStatus() {
                return status;
        }

        public void setMessage(String message) {
                this.message = message;
        }

        public String getMessage() {
                return message;
        }

        public void setUser(User user) {
                this.user = user;
        }

        public User getUser() {
                return user;
        }

        @Override
        public String toString() {
                StringBuffer sb = new StringBuffer();
                sb.append("UserData : ");
                try {
                        sb.append("userStatus = " + userStatus).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("status = " + status).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("message = " + message).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("user = " + user.toString());
                } catch (Exception e) {

                }
                return sb.toString();
        }
}
