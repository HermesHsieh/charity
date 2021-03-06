package tw.org.by37.menu;

import java.util.ArrayList;

import tw.org.by37.R;
import tw.org.by37.R.id;
import tw.org.by37.R.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlidingMenuAdapter extends BaseAdapter {

        private final static String TAG = SlidingMenuAdapter.class.getName();

        private Context mContext;

        private LayoutInflater infalter;

        private ArrayList<SlidingMenuDetail> data = new ArrayList<SlidingMenuDetail>();

        public SlidingMenuAdapter(Context context) {
                this.infalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.mContext = context;
        }

        @Override
        public int getCount() {
                return data.size();
        }

        @Override
        public SlidingMenuDetail getItem(int position) {
                return data.get(position);
        }

        @Override
        public long getItemId(int position) {
                return position;
        }

        public void addAllDatas(ArrayList<SlidingMenuDetail> files) {

                try {
                        this.data.clear();
                        this.data.addAll(files);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                notifyDataSetChanged();
        }

        public ArrayList<SlidingMenuDetail> getMyListData() {
                return this.data;
        }

        public String getItemName(int position) {
                return data.get(position).name;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

                final ViewHolder holder;

                if (convertView == null) {

                        convertView = infalter.inflate(R.layout.item_menu_item, null);
                        holder = new ViewHolder();
                        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                        holder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
                        holder.img_hint = (ImageView) convertView.findViewById(R.id.img_hint);
                        holder.rl_layout = (RelativeLayout) convertView.findViewById(R.id.rl_layout);
                        holder.item_click = (RelativeLayout) convertView.findViewById(R.id.item_click);
                        convertView.setTag(holder);

                } else {
                        holder = (ViewHolder) convertView.getTag();
                }

                if (position == 0) {
                        holder.rl_layout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                        holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.sliding_menu_background_2));
                }

                holder.tv_name.setText(data.get(position).name);

                // 自訂按鈕顯示刪除按鈕(是自訂且為編輯模式時才顯示)
                if (data.get(position).isHint) {
                        holder.img_hint.setVisibility(View.VISIBLE);
                } else {
                        holder.img_hint.setVisibility(View.GONE);
                }

                // 設定項目icon,若沒有設定icon,則用預設的icon
                Drawable drawable = null;
                if (data.get(position).icon >= 0) {
                        drawable = mContext.getResources().getDrawable(data.get(position).icon);
                } else {
                        drawable = mContext.getResources().getDrawable(R.drawable.ic_person);
                }
                // 這一步必須要做,否則不會顯示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tv_name.setCompoundDrawables(drawable, null, null, null);

                return convertView;
        }

        public class ViewHolder {
                ImageView img_icon;
                TextView tv_name;
                ImageView img_hint;
                RelativeLayout rl_layout;
                RelativeLayout item_click;
        }
}
