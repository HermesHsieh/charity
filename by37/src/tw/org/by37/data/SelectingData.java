package tw.org.by37.data;

import java.util.ArrayList;
import java.util.List;

import tw.org.by37.MainActivity;
import tw.org.by37.organization.OrganizationData;

import android.R;
import android.util.Log;

public class SelectingData {
        private final static String TAG = "SelectingData";

        public static String suppliesId = null;

        public static String organizationId = null;

        public static SupplyData mSupplyData = null;

        public static OrganizationData mOrganizationData = null;

        public static ArrayList<TypesData> mGoodsTypes = new ArrayList<TypesData>();

        public static ArrayList<TypesData> mOrganizationTypes = new ArrayList<TypesData>();

        public static ArrayList<OrganizationData> mNearOrganizationData = null;

        public static void setNearOrganizationDataList(ArrayList<OrganizationData> data) {
                mNearOrganizationData = data;
        }

        /** 確認是否有物資資料 **/
        public static boolean checkSupplyData() {

                if (mSupplyData != null) {
                        return true;
                }
                return false;
        }

        /** 確認是否有基構資料 **/
        public static boolean checkOrganizationData() {
                if (mOrganizationData != null) {
                        return true;
                }
                return false;
        }

        /** 清除基構資料 **/
        public static void clearOrganizationData() {
                Log.i(TAG, "clearOrganizationData");
                mOrganizationData = null;
        }

        /** 清空物資類別資料 **/
        public static void clearGoodsTypesList() {
                mGoodsTypes.clear();

                addInitGoodsTypesItem();
        }

        /** 取得全部類別資料字串 **/
        public static String getGoodsTypesListItemString() {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < mGoodsTypes.size(); i++) {
                        sb.append("Number : ").append(i).append(", Id : ").append(mGoodsTypes.get(i).id).append(", Name : ").append(mGoodsTypes.get(i).name).append("\n");
                }
                return sb.toString();
        }

        /** 加入類別資料 **/
        public static void addGoodsTypesListItem(TypesData mData) {
                mGoodsTypes.add(mData);
        }

        public static void addInitGoodsTypesItem() {
                TypesData mData = new TypesData();
                mData.id = "0";
                mData.name = "全部";
                mGoodsTypes.add(mData);
        }

        /** 用ID查詢物資類別名稱 **/
        public static String getGoodsTypesNameForId(String id) {
                String name = null;
                for (int i = 0; i < mGoodsTypes.size(); i++) {
                        if (mGoodsTypes.get(i).id.equals(id)) {
                                name = mGoodsTypes.get(i).name;
                        }
                }
                return name;
        }

        /** 清空物資類別資料 **/
        public static void clearOrganizationTypesList() {
                mOrganizationTypes.clear();
        }

        /** 取得全部類別資料字串 **/
        public static String getOrganizationTypesListItemString() {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < mOrganizationTypes.size(); i++) {
                        sb.append("Number : ").append(i).append(", Id : ").append(mOrganizationTypes.get(i).id).append(", Name : ").append(mOrganizationTypes.get(i).name).append("\n");
                }
                return sb.toString();
        }

        /** 加入類別資料 **/
        public static void addOrganizationTypesListItem(TypesData mData) {
                mOrganizationTypes.add(mData);
        }
}
