package tw.org.by37.data;

import java.util.ArrayList;

import tw.org.by37.MainActivity;

import android.R;

public class SelectingData {

        public static String suppliesId = "";

        public static String organizationId = "";

        public static SupplyData mSupplyData = null;

        public static OrganizationData mOrganizationData = null;

        public static ArrayList<TypesData> mGoodsTypes = new ArrayList<TypesData>();

        public static ArrayList<TypesData> mOrganizationTypes = new ArrayList<TypesData>();

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
