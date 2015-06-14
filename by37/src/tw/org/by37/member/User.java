package tw.org.by37.member;

/**
 * 使用者的基本資料 id,name,email,password,source
 * 
 * @author Tellasy
 * 
 */
public class User {
        private String id;
        /** 姓名 **/
        private String name;
        /** 帳號(電子郵件地址) **/
        private String email;
        /** 使用者密碼 **/
        private String password;
        /** by37=一般註冊, facebook=Facebook, google=Google+ **/
        private String source;
        private String enable;
        private String role;

        private Info info;

        public void setId(String id) {
                this.id = id;
        }

        public String getId() {
                return id;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getName() {
                return name;
        }

        public void setMail(String mail) {
                this.email = mail;
        }

        public String getMail() {
                return email;
        }

        public void setPassowrd(String password) {
                this.password = password;
        }

        public String getPassword() {
                return password;
        }

        public void setSource(String source) {
                this.source = source;
        }

        public String getSource() {
                return source;
        }

        public void setEnable(String enable) {
                this.enable = enable;
        }

        public String getEnable() {
                return enable;
        }

        public void setRole(String role) {
                this.role = role;
        }

        public String getRole() {
                return role;
        }

        public void setInfo(Info info) {
                this.info = info;
        }

        public Info getInfo() {
                return info;
        }

        @Override
        public String toString() {
                StringBuffer sb = new StringBuffer();
                sb.append("User : ");
                try {
                        sb.append("email = " + email).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("source = " + source).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("enable = " + enable).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("role = " + role).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("name = " + name).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("password = " + password).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("info = " + info.toString());
                } catch (Exception e) {

                }
                return sb.toString();
        }

}
