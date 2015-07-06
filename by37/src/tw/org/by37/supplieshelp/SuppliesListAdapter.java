package tw.org.by37.supplieshelp;

import java.util.ArrayList;

import tw.org.by37.MainActivity;
import tw.org.by37.R;
import tw.org.by37.data.SelectingData;
import tw.org.by37.service.LocationService;
import tw.org.by37.util.FunctionUtil;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SuppliesListAdapter extends BaseAdapter {

        private final static String TAG = SuppliesListAdapter.class.getName();

        private Context mContext;

        private LayoutInflater infalter;

        private ArrayList<SupportData> data = new ArrayList<SupportData>();

        /** 動態設定機構TextView的參數 **/
        private boolean isMeasured = false;

        private boolean index_view = false;

        public SuppliesListAdapter(Context context) {
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
        public SupportData getItem(int position) {
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

                        /** 動態設定名稱欄位長度 **/
                        LayoutParams mNameText = (LayoutParams) holder.tv_name.getLayoutParams();
                        mNameText.width = (MainActivity.myScreenWidth) * 2 / 7;
                        holder.tv_name.setLayoutParams(mNameText);

                        /** 動態設定機構名稱長度 **/
                        LayoutParams mText = (LayoutParams) holder.tv_organization_name.getLayoutParams();
                        mText.width = (MainActivity.myScreenWidth) * 4 / 10;
                        holder.tv_organization_name.setLayoutParams(mText);

                        /** 動態設定機構名稱的Margin **/
                        MarginLayoutParams marginParams = (MarginLayoutParams) holder.tv_organization_name.getLayoutParams();
                        if (index_view) {
                                marginParams.leftMargin = ((MainActivity.myScreenWidth) / 2) * 75 / 100;
                        } else {
                                marginParams.leftMargin = ((MainActivity.myScreenWidth) / 2) * 4 / 5;
                        }
                        holder.tv_organization_name.setLayoutParams(marginParams);

                        view.setTag(holder);
                } else {
                        holder = (ViewHolder) view.getTag();
                }

                holder.tv_name.setText(data.get(position).getName());
                if (data.get(position).getOrganizationData().length > 0 && data.get(position).getOrganizationData()[0].getName() != null) {
                        holder.tv_organization_name.setText(data.get(position).getOrganizationData()[0].getName());
                }
                double lat = Double.valueOf(data.get(position).getOrganizationData()[0].getLatitude());
                double lng = Double.valueOf(data.get(position).getOrganizationData()[0].getLongitude());

                if (LocationService.checkLocationStatus()) {
                        int dst = (int) FunctionUtil.Distance(LocationService.longitude, LocationService.latitude, lng, lat);

                        String sDst = "";

                        if (dst < 1000)
                                sDst = (dst / 1) + " M";
                        else
                                sDst = (dst / 1000) + " KM";

                        holder.tv_distance.setText(sDst);
                } else {
                        holder.tv_distance.setText("");
                }

                // holder.tv_category.setText("Category : " +
                // data.get(position).getGoodsTypeId() + ", Name : " +
                // SelectingData.getGoodsTypesNameForId(data.get(position).getGoodsTypeId()));

                return view;
        }

        public void setListData(ArrayList<SupportData> data) {
                try {
                        this.data.clear();
                        this.data.addAll(data);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                notifyDataSetChanged();
        }

        public void addListData(ArrayList<SupportData> data) {
                for (int i = 0; i < data.size(); i++) {
                        this.data.add(data.get(i));
                }
                notifyDataSetChanged();
        }

        /** 取得所有ListData資料 **/
        public ArrayList<SupportData> getSuppliesListData() {
                return this.data;
        }

        /** 取得物資需求ListView 對應 Position 的資料ID **/
        public String getSuppliesDataId(int position) {
                return data.get(position).getId();
        }

        /** 取得物資需求ListView 對應 Position 的資料OrganizationId **/
        public String getSuppliesDataOrganizationId(int position) {
                return data.get(position).getOrganizationId();
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
