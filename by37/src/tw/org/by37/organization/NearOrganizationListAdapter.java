package tw.org.by37.organization;

import java.util.ArrayList;

import tw.org.by37.MainActivity;
import tw.org.by37.R;
import tw.org.by37.data.SelectingData;
import tw.org.by37.data.SupplyData;
import tw.org.by37.supplieshelp.SuppliesHelpFragment;
import tw.org.by37.util.FunctionUtil;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NearOrganizationListAdapter extends BaseAdapter {

        private final static String TAG = "NearOrganizationListAdapter";

        private Context mContext;

        private LayoutInflater infalter;

        private ArrayList<OrganizationData> data = new ArrayList<OrganizationData>();

        private double mLongitude = SuppliesHelpFragment.wifi_longitude;

        private double mLatitude = SuppliesHelpFragment.wifi_latitude;

        /** 動態設定機構TextView的參數 **/
        private boolean isMeasured = false;

        private boolean index_view = false;

        public NearOrganizationListAdapter(Context context) {
                this.infalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.mContext = context;
        }

        /** 是否為首頁顯示 **/
        public void setIndexView(boolean status) {
                index_view = status;
        }

        @Override
        public int getCount() {
                return data.size();
        }

        @Override
        public OrganizationData getItem(int position) {
                return data.get(position);
        }

        @Override
        public long getItemId(int position) {
                return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

                ViewHolder holder;

                if (view == null) {
                        view = infalter.inflate(R.layout.item_organization, null);

                        holder = new ViewHolder();

                        holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                        holder.tv_type = (TextView) view.findViewById(R.id.tv_type);
                        holder.tv_distance = (TextView) view.findViewById(R.id.tv_distance);
                        holder.tv_category = (TextView) view.findViewById(R.id.tv_category);

                        /** 動態設定名稱欄位長度 **/
                        LayoutParams mNameText = (LayoutParams) holder.tv_name.getLayoutParams();
                        mNameText.width = (MainActivity.myScreenWidth) * 3 / 8;
                        holder.tv_name.setLayoutParams(mNameText);

                        /** 動態設定類別欄位長度 **/
                        LayoutParams mText = (LayoutParams) holder.tv_type.getLayoutParams();
                        mText.width = (MainActivity.myScreenWidth) * 4 / 10;
                        holder.tv_type.setLayoutParams(mText);

                        /** 動態設定類別欄位的Margin **/
                        MarginLayoutParams marginParams = (MarginLayoutParams) holder.tv_type.getLayoutParams();
                        if (index_view) {
                                marginParams.leftMargin = ((MainActivity.myScreenWidth) / 2) * 75 / 100;
                        } else {
                                marginParams.leftMargin = ((MainActivity.myScreenWidth) / 2) * 4 / 5;
                        }
                        holder.tv_type.setLayoutParams(marginParams);

                        view.setTag(holder);
                } else {
                        holder = (ViewHolder) view.getTag();
                }

                holder.tv_name.setText(data.get(position).getName());
                holder.tv_type.setText(data.get(position).getTitle());

                double lat = Double.valueOf(data.get(position).getLatitude());
                double lng = Double.valueOf(data.get(position).getLongitude());

                int dst = (int) FunctionUtil.Distance(mLongitude, mLatitude, lng, lat);

                String sDst = "";

                if (dst < 1000)
                        sDst = (dst / 1) + " M";
                else
                        sDst = (dst / 1000) + " KM";

                holder.tv_distance.setText(sDst);

                return view;
        }

        private String DistanceToString(int dst) {
                int meter = dst % 1000;
                int km = dst / 1000;
                return km + "." + meter;
        }

        public void setListData(ArrayList<OrganizationData> data) {
                try {
                        this.data.clear();
                        this.data.addAll(data);
                } catch (Exception e) {
                        e.printStackTrace();
                }

                notifyDataSetChanged();
        }

        public void addListData(ArrayList<OrganizationData> data) {
                for (int i = 0; i < data.size(); i++) {
                        this.data.add(data.get(i));
                }
                notifyDataSetChanged();
        }

        /** 取得所有ListData資料 **/
        public ArrayList<OrganizationData> getSuppliesListData() {
                return this.data;
        }

        /** 取得篩選的ListData資料 **/
        public ArrayList<OrganizationData> getSelectedListData() {

                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();

                for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).selected)
                                mList.add(data.get(i));
                }

                return mList;
        }

        /** 取得物資需求ListView 對應 Position 的資料ID **/
        public String getOrganizationDataId(int position) {
                return data.get(position).getId();
        }

        /** 取得鄰近機構ListView 對應 Position 的資料OrganizationId **/
        public String getOrganizationId(int position) {
                return data.get(position).getOrganization_Id();
        }

        public class ViewHolder {
                ImageView img_icon;
                TextView tv_name;
                TextView tv_type;
                TextView tv_distance;
                TextView tv_category;
        }

        public void clear() {
                data.clear();
                notifyDataSetChanged();
        }

}
