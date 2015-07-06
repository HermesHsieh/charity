package tw.org.by37.config;

import android.os.Environment;

/** 專案的全域共同參數設定 **/
public class SysConfig {

        /** Project Path Name **/
        public final static String packageName = "tw.org.by37";

        /** Database Name Define **/
        /** SQLite 資料庫名稱-1 **/
        public final static String dbSupplies = "supplies.db";
        public final static String dbOrganization = "organization.db";
        /** End of Database Name Define **/

        /** 相機照片暫存檔案名稱 **/
        public final static String CMA_TEMP_PIC_NAME = "tempImage.png";

        /**
         * Preferences Key Values : 儲存於手機內部 Preferences 的鍵值名稱
         **/
        /** Preferences Key Value : 使用者資料 **/
        public final static String k_UserData_1 = "UserData";
        /** Preferences Key Value : 使用者帳號 **/
        public final static String k_UserData_2 = "UserAccount";
        /** Preferences Key Value : 使用者密碼 **/
        public final static String k_UserData_3 = "UserPassword";
        /** Preferences Key Value : 使用者來源 **/
        public final static String k_UserData_4 = "UserSource";
        /** Preferences Key Value : 使用者帳號記住我 **/
        public final static String k_UserData_5 = "UserRememberMe";
        /** Preferences Key Value : 使用者帳號記住我的帳號 **/
        public final static String k_UserData_6 = "UserRMAccount";
        /** Preferences Key Value : 物資需求排序的值 **/
        public final static String k_Supplies_Order = "SuppliesOrder";
        /** Preferences Key Value : 物資需求類別篩選的值 **/
        public final static String k_Supplies_Category = "SuppliesCategory";
        /** Preferences Key Value : 物資需求更新時間 **/
        public final static String k_Supplies_Data_UpdateTime = "SuppliesDataUpdateTime";
        /** Preferences Key Value : 物資需求類別更新時間 **/
        public final static String k_Supplies_Types_Data_UpdateTime = "SuppliesTypesDataUpdateTime";
        /** Preferences Key Value : 基購資料更新時間 **/
        public final static String k_Organization_Data_UpdateTime = "OrganizationDataUpdateTime";
        /** Preferences Key Value : 確認我的位置的提示訊息 **/
        public final static String k_My_Position_Hint = "MyPositionHint";
        /** End of Preferences Key Values **/

        /** External Storage Path - SD Card **/
        public final static String SD_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        /** System Path **/
        public final static String ROOT_PATH = android.os.Environment.getDataDirectory().getPath();
        /** 手機內存database路徑 **/
        public final static String DB_PATH = ROOT_PATH + "/data/" + packageName + "/databases/";
        /** 手機內存pictures路徑 **/
        public final static String PIC_PATH = ROOT_PATH + "/data/" + packageName + "/pictures/";
        /** SD的database路徑 **/
        public final static String SD_DB_PATH = SD_PATH + "/by37/";
        /** SD的Pictures路徑 **/
        public final static String SD_PIC_PATH = android.os.Environment.getExternalStorageDirectory() + "/Pictures/by37";
        /** SD的Temp Pictures路徑 **/
        public final static String SD_TEMP_PIC_PATH = SD_PIC_PATH + "/temp";

        static final String ApiServer = "http://charity.gopagoda.io/";
        /** User 註冊 **/
        public static final String usersApi = ApiServer + "addUser";
        /** User 登入 **/
        public static final String usersLoginApi = ApiServer + "checkUser";
        /** 急難救助資料 **/
        public static final String emergenciesApi = ApiServer + "emergencies";
        public static final String emergencyAPI = ApiServer + "emergency/";
        /** 基構類別 **/
        public static final String organizationsTypeApi = ApiServer + "organizationsType";
        /** 基構資料 **/
        public static final String organizationsApi = ApiServer + "organizations";
        /** 基構資料 for Id **/
        public static final String organizationsIdApi = ApiServer + "getSupportsByOrganizationId/";
        /** 鄰近基構資料 **/
        public static final String organizationsNearApi = ApiServer + "test/";
        /** 物資需求資料, 包含物品資料及所在基構資料 **/
        public static final String suppliesApi = ApiServer + "getOrganizationsBySupport";
        /** 物資需求資料 for Id **/
        public static final String suppliesIdApi = ApiServer + "getSupportBySupportId/";
        /** 物資類別資料 **/
        public static final String goodsTypesApi = ApiServer + "goodsTypes";
        
        /** 取的所有商品 **/
        public static final String getAllgoods = ApiServer+"getAllGoods";

        /** 廣播參數 **/
        public static final String MAIN_BROADCAST_STRING = "MainBroadcast";
        public static final String GET_EMERGENCY_BROADCAST = "GetEmergencyBroadCast";
        public static final String GET_EMERGENCY_CONTENT_BROADCAST = "GetEmergencyContentBroadCast";
        public static final String GET_NEAR_ORGANIZATION_CONTENT_BROADCAST = "GetNearOrganizationContentBroadCast";

}
