package tw.org.by37.organization;

import tw.org.by37.supplieshelp.SupportData;

public class OrganizationData {

        /** 機構編號ID **/
        private String id;
        /** 機構名字 **/
        private String name;
        /** 機構電子郵件地址 **/
        private String email;
        /** 機構住址 **/
        private String address;
        /** 機構電話 **/
        private String phone;
        /** 機構經緯度 **/
        private String longitude;
        /** 機構經緯度 **/
        private String latitude;
        /** 資料創立時間 */
        private String created_at;
        /** 資料更新時間 */
        private String updated_at;
        /** 機構標題 */
        private String title;
        /** 機構簡介內容 */
        private String content;
        /** 機構關鍵字 **/
        private String keyword;
        /** 機構網址 **/
        private String url;
        /** 機構聯絡人 **/
        private String contact;
        /** 機構編號 **/
        private String number;
        /** 機構的物資需求資料 **/
        private SupportData[] supports;

        private String organization_id;

        private String organizationType_id;

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

        public void setEmail(String email) {
                this.email = email;
        }

        public String getEmail() {
                return email;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public String getAddress() {
                return address;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public String getPhone() {
                return phone;
        }

        public void setLongitude(String longitude) {
                this.longitude = longitude;
        }

        public String getLongitude() {
                return longitude;
        }

        public void setLatitude(String latitude) {
                this.latitude = latitude;
        }

        public String getLatitude() {
                return latitude;
        }

        public void setCreated_At(String created_at) {
                this.created_at = created_at;
        }

        public String getCreated_At() {
                return created_at;
        }

        public void setUpdated_At(String updated_at) {
                this.updated_at = updated_at;
        }

        public String getUpdated_At() {
                return updated_at;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getTitle() {
                return title;
        }

        public void setContent(String content) {
                this.content = content;
        }

        public String getContent() {
                return content;
        }

        public void setKeyword(String keyword) {
                this.keyword = keyword;
        }

        public String getKeyword() {
                return keyword;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        public String getUrl() {
                return url;
        }

        public void setContact(String contact) {
                this.contact = contact;
        }

        public String getContact() {
                return contact;
        }

        public void setNumber(String number) {
                this.number = number;
        }

        public String getNumber() {
                return number;
        }

        public void setOrganization_Id(String organization_id) {
                this.organization_id = organization_id;
        }

        public String getOrganization_Id() {
                return organization_id;
        }

        public void setOrganizationType_Id(String organizationType_id) {
                this.organizationType_id = organizationType_id;
        }

        public String getOrganizationType_Id() {
                return organizationType_id;
        }

        public void setSupportData(SupportData[] supports) {
                this.supports = supports;
        }

        public SupportData[] getSupportData() {
                return this.supports;
        }

        @Override
        public String toString() {
                return "OrganizationData = id : " + id + ", name : " + name + ", email : " + email + ", address : " + address + ", phone : " + phone + ", longitude : " + longitude + ", latitude : " + latitude + ", created_at : " + created_at + ", updated_at : " + updated_at + ", title : " + title + ", content : " + content + ", keyword : " + keyword + ", url : " + url + ", contact : " + contact + ", number : " + number + ", supports : [ " + supports.toString() + " ], organization_id : " + organization_id + ", organizationType_id : " + organizationType_id;
        }

        // private Supports[] supports;

        public String _id = "";
        public String org_id = "";
        public String org_name = "";
        public String org_title = "";
        public String org_content = "";
        public String org_keyword = "";
        public String org_email = "";
        public String org_fax = "";
        public String org_address = "";
        public String org_phone = "";
        public String org_div = "";
        public String org_city = "";
        public String org_type = "";
        public double org_lng = 0;
        public double org_lat = 0;
        public String org_url = "";
        public String org_contact = "";
        public String org_number = "";
        public String org_created_at = "";
        public String org_updated_at = "";
        public String upload = "";
        public boolean selected = false;

        public String getDataString() {
                StringBuffer sb = new StringBuffer();
                sb.append("_id : ").append(_id).append("\n");
                sb.append("org_id : ").append(org_id).append("\n");
                sb.append("org_name : ").append(org_name).append("\n");
                sb.append("org_title : ").append(org_title).append("\n");
                sb.append("org_content : ").append(org_content).append("\n");
                sb.append("org_keyword : ").append(org_keyword).append("\n");
                sb.append("org_type : ").append(org_type).append("\n");
                sb.append("org_email : ").append(org_email).append("\n");
                sb.append("org_phone : ").append(org_phone).append("\n");
                sb.append("org_fax : ").append(org_fax).append("\n");
                sb.append("org_address : ").append(org_address).append("\n");
                sb.append("org_city : ").append(org_city).append("\n");
                sb.append("org_div : ").append(org_div).append("\n");
                sb.append("org_lng : ").append(org_lng).append("\n");
                sb.append("org_lat : ").append(org_lat).append("\n");
                sb.append("org_url : ").append(org_url).append("\n");
                sb.append("org_contact : ").append(org_contact).append("\n");
                sb.append("org_number : ").append(org_number).append("\n");
                sb.append("org_created_at : ").append(org_created_at).append("\n");
                sb.append("org_updated_at : ").append(org_updated_at).append("\n");
                return sb.toString();
        }
}
