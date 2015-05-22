package tw.org.by37.organization;

import android.provider.BaseColumns;

public interface DBConstantsOrganization extends BaseColumns {
        public static final String TABLE_NAME = "organization";
        public static final String ORG_ID = "org_id";
        public static final String ORG_NAME = "org_name";
        public static final String ORG_EMAIL = "org_email";
        public static final String ORG_FAX = "org_fax";
        public static final String ORG_ADDRESS = "org_address";
        public static final String ORG_PHONE = "org_phone";
        public static final String ORG_DIV = "org_div";
        public static final String ORG_CITY = "org_city";
        public static final String ORG_TYPE = "org_type";
        public static final String ORG_LNG = "org_lng";
        public static final String ORG_LAT = "org_lat";
        public static final String ORG_CREATED_AT = "org_created_at";
        public static final String ORG_UPDATED_AT = "org_updated_at";

}
