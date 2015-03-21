package tw.org.by37.fragment.supplieshelp;

import android.provider.BaseColumns;

public interface DBConstantsSupplies extends BaseColumns {
        public static final String TABLE_NAME = "supplies";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String ORGANIZATIONID = "organizationId";
        public static final String CATEGORY = "category";
        public static final String TOTAL = "total";
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
        public static final String ORGANIZATION_NAME = "organization_name";
        public static final String ORGANIZATION_LONGITUDE = "organization_longitude";
        public static final String ORGANIZATION_LATITUDE = "organization_latitude";
}
