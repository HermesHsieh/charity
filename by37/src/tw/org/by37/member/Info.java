package tw.org.by37.member;

/**
 * 使用者的細部資料 user_id,address,only,organization_ids,birthday,image,about_me
 * 
 * @author Tellasy
 * 
 */
public class Info {
        /** 會員編號 **/
        private String user_id;
        /** 設定居住城市 **/
        private String address;
        private String only;
        /** 偏好機構 **/
        private String organization_ids;
        /** 生日 **/
        private String birthday;
        /** 大頭照路徑 **/
        private String image;
        /** 關於我 **/
        private String about_me;

        public void setUser_Id(String user_id) {
                this.user_id = user_id;
        }

        public String getUser_id() {
                return this.user_id;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public String getAddress() {
                return address;
        }

        public void setOnly(String only) {
                this.only = only;
        }

        public String getOnly() {
                return only;
        }

        public void setBirthday(String birthday) {
                this.birthday = birthday;
        }

        public String getBirthday() {
                return birthday;
        }

        public void setImage(String image) {
                this.image = image;
        }

        public String getImage() {
                return image;
        }

        public void setAbout_me(String about_me) {
                this.about_me = about_me;
        }

        public String getAbout_me() {
                return about_me;
        }

        public void setOrganization_ids(String organization_ids) {
                this.organization_ids = organization_ids;
        }

        public String getOrganization_ids() {
                return organization_ids;
        }

        @Override
        public String toString() {
                StringBuffer sb = new StringBuffer();
                sb.append("Info : ");
                try {
                        sb.append("user_id = " + user_id).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("address = " + address).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("birthday = " + birthday).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("image = " + image).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("about_me = " + about_me).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("only = " + only).append(", ");
                } catch (Exception e) {

                }
                try {
                        sb.append("organization_ids = " + organization_ids);
                } catch (Exception e) {

                }
                return sb.toString();
        }

}
