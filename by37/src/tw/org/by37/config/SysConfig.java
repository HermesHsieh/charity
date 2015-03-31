package tw.org.by37.config;

/** 專案的全域共同參數設定 **/
public class SysConfig {

        /** Project Path Name **/
        public final static String packageName = "tw.org.by37";

        /** Database Name Define **/
        /** SQLite 資料庫名稱-1 **/
        public final static String dbSupplies = "supplies.db";
        public final static String dbOrganization = "organization.db";
        /** End of Database Name Define **/

        /**
         * Preferences Key Values : 儲存於手機內部 Preferences 的鍵值名稱
         **/
        /** Preferences Key Value : 使用者帳號資料 **/
        public final static String k_UserData_1 = "UserAccount";
        /** Preferences Key Value : 物資需求排序的值 **/
        public final static String k_Supplies_Order = "SuppliesOrder";
        /** Preferences Key Value : 物資需求類別篩選的值 **/
        public final static String k_Supplies_Category = "SuppliesCategory";
        /** Preferences Key Value : 物資需求更新時間 **/
        public final static String k_Supplies_Data_UpdateTime = "SuppliesDataUpdateTime";
        /** Preferences Key Value : 基購資料更新時間 **/
        public final static String k_Organization_Data_UpdateTime = "OrganizationDataUpdateTime";
        /** End of Preferences Key Values **/

        /** External Storage Path - SD Card **/
        public final static String SD_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        /** System Path **/
        public final static String ROOT_PATH = android.os.Environment.getDataDirectory().getPath();
        /** 手機內存database路徑 **/
        public final static String DB_PATH = ROOT_PATH + "/data/" + packageName + "/databases/";
        /** SD的database路徑 **/
        public final static String SD_DB_PATH = SD_PATH + "/by37/";

        static final String ApiServer = "http://charity.gopagoda.io/";

        /** User 註冊, 登入, 更新, 推播的DeviceID **/
        public static final String usersApi = ApiServer + "users";

        /** 急難救助資料 **/
        public static final String emergencyApi = ApiServer + "emergencies";

        /** 基構類別 **/
        public static final String organizationsTypeApi = ApiServer + "organizationsType";

        /** 基構資料 **/
        public static final String organizationsApi = ApiServer + "organizations";
        
        /** 基構資料 for Id **/
        public static final String organizationsIdApi = ApiServer + "getSupportsByOrganizationId/";

        /** 物資需求資料, 包含物品資料及所在基構資料 **/
        public static final String suppliesApi = ApiServer + "getOrganizationsBySupport";

        /** 物資類別資料 **/
        public static final String goodsTypesApi = ApiServer + "goodsTypes";

}
