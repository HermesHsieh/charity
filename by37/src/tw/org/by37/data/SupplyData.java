package tw.org.by37.data;

/** 物資需求 - 物資資料物件 **/
public class SupplyData {
        public String _id = "";
        public String id = "";
        public String name = "";
        public String description = "";
        public String organizationId = "";
        public String category = "";
        public String total = "";
        public String created_at = "";
        public String updated_at = "";
        public String organization_id = "";
        public String organization_name = "";
        public String organization_email = "";
        public String organization_fax = "";
        public String organization_address = "";
        public String organization_phone = "";
        public String organization_division = "";
        public String organization_city = "";
        public String organization_type = "";
        public double organization_longitude = 0;
        public double organization_latitude = 0;
        public String organization_created_at = "";
        public String organization_updated_at = "";
        public String upload = "";
        public boolean selected = false;

        public String getDataString() {
                StringBuffer sb = new StringBuffer();
                sb.append("_id : ").append(_id).append("\n");
                sb.append("id : ").append(id).append("\n");
                sb.append("name : ").append(name).append("\n");
                sb.append("description : ").append(description).append("\n");
                sb.append("organizationId : ").append(organizationId).append("\n");
                sb.append("category : ").append(category).append("\n");
                sb.append("total : ").append(total).append("\n");
                sb.append("created_at : ").append(created_at).append("\n");
                sb.append("updated_at : ").append(updated_at).append("\n");
                return sb.toString();
        }
}
