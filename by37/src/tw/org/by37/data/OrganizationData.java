package tw.org.by37.data;

public class OrganizationData {

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
