package tw.org.by37.fragment.supplieshelp;

import java.util.ArrayList;

import tw.org.by37.R;
import tw.org.by37.data.SelectingData;
import tw.org.by37.data.SupplyData;
import tw.org.by37.util.FunctionUtil;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SuppliesListAdapter extends BaseAdapter {

        private final static String TAG = "SuppliesListAdapter";

        private Context mContext;

        private LayoutInflater infalter;

        private ArrayList<SupplyData> data = new ArrayList<SupplyData>();

        private double mLongitude = 0.0;

        private double mLatitude = 0.0;

        public SuppliesListAdapter(Context context) {
                this.infalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.mContext = context;
        }

        @Override
        public int getCount() {
                return data.size();
        }

        @Override
        public SupplyData getItem(int position) {
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
                        view = infalter.inflate(R.layout.item_supply, null);

                        holder = new ViewHolder();

                        holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                        holder.tv_organization_name = (TextView) view.findViewById(R.id.tv_organization_name);
                        holder.tv_distance = (TextView) view.findViewById(R.id.tv_distance);
                        holder.tv_category = (TextView) view.findViewById(R.id.tv_category);

                        view.setTag(holder);
                } else {
                        holder = (ViewHolder) view.getTag();
                }

                holder.tv_name.setText("NAME : " + data.get(position).name);
                holder.tv_organization_name.setText("OrganizationID,Name : " + data.get(position).organizationId + ", " + data.get(position).organization_name);

                double lat = data.get(position).organization_latitude;
                double lng = data.get(position).organization_longitude;

                int dst = (int) FunctionUtil.Distance(mLongitude, mLatitude, lng, lat);

                String sDst = "";

                if (dst < 1000)
                        sDst = (dst / 1) + " M";
                else
                        sDst = (dst / 1000) + " KM";

                holder.tv_distance.setText("Distance : " + sDst);

                holder.tv_category.setText("Category : " + data.get(position).category + ", Name : " + SelectingData.getGoodsTypesNameForId(data.get(position).category));

                return view;
        }

        public void setListData(ArrayList<SupplyData> data, Double lng, Double lat) {
                try {
                        this.data.clear();
                        this.data.addAll(data);
                } catch (Exception e) {
                        e.printStackTrace();
                }

                if (lng > 0 && lat > 0) {
                        this.mLongitude = lng;
                        this.mLatitude = lat;
                } else {
                        Log.e(TAG, "set Lng and Lat error!!");
                }
                notifyDataSetChanged();
        }

        public void addListData(ArrayList<SupplyData> data) {
                for (int i = 0; i < data.size(); i++) {
                        this.data.add(data.get(i));
                }
                notifyDataSetChanged();
        }

        /** 取得所有ListData資料 **/
        public ArrayList<SupplyData> getSuppliesListData() {
                return this.data;
        }

        /** 取得篩選的ListData資料 **/
        public ArrayList<SupplyData> getSelectedListData() {

                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();

                for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).selected)
                                mList.add(data.get(i));
                }

                return mList;
        }

        /** 取得物資需求ListView 對應 Position 的資料ID **/
        public String getSuppliesDataId(int position) {
                return data.get(position).id;
        }

        /** 取得物資需求ListView 對應 Position 的資料OrganizationId **/
        public String getSuppliesDataOrganizationId(int position) {
                return data.get(position).organization_id;
        }

        public class ViewHolder {
                ImageView img_icon;
                TextView tv_name;
                TextView tv_organization_name;
                TextView tv_distance;
                TextView tv_category;
        }

        public void clear() {
                data.clear();
                notifyDataSetChanged();
        }

}
