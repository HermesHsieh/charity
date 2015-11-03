package tw.org.by37.productsell;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;

import tw.org.by37.MainActivity;
import tw.org.by37.R;
import tw.org.by37.config.SysConfig;
import tw.org.by37.main.MainFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainProductSellFragment extends Fragment {

        private static final String TAG = MainProductSellFragment.class.getName();

        private HorizontalScrollView mTabScrollView;;
        private String[] tabTitles = { "推薦", "慈善專區", "促銷品", "一元起標", "全部商品" };
        private ArrayList<TextView> mTextViewAry;

        private TextView mProductAmountTextView;
        private ArrayList<ProductData> productList;

        private ImageView mLeftImageView;
        private ImageView mRightImageView;
        private TextView mLeftTextView;
        private TextView mRightTextView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                // TODO Auto-generated method stub
        		Log.d(TAG, "onCreateView");
                View v = inflater.inflate(R.layout.fragment_main_productsell, container, false);
                setView(v);
                init();
                initTabBar();
                return v;
        }

        private void setView(View v) {
        		Log.d(TAG, "setView");
                mTabScrollView = (HorizontalScrollView) v.findViewById(R.id.productsell_tab_scrollview);
                mProductAmountTextView = (TextView) v.findViewById(R.id.main_product_count_tv);

                mLeftImageView = (ImageView) v.findViewById(R.id.fragment_main_product_left_imageview);
                mRightImageView = (ImageView) v.findViewById(R.id.fragment_main_product_right_imageview);

                mLeftTextView = (TextView) v.findViewById(R.id.fragment_main_product_left_textview);
                mRightTextView = (TextView) v.findViewById(R.id.fragment_main_product_right_textview);
        }

        private void init() {
                mTextViewAry = new ArrayList<TextView>();
                

                
        }

        private void initTabBar() {
                LinearLayout mTabLinerLayout = new LinearLayout(getActivity());
                for (int i = 0; i < tabTitles.length; i++) {
                        String title = tabTitles[i];
                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        p.width = MainActivity.myScreenWidth / 3;
                        p.height = MainActivity.myScreenHeight / 15;
                        TextView tv = new TextView(getActivity());
                        tv.setText(title);
                        tv.setTextSize(18);
                        tv.setGravity(Gravity.CENTER);
                        tv.setLayoutParams(p);
                        tv.setTag(title);
                        mTextViewAry.add(tv);
                        mTabLinerLayout.addView(tv);
                        if (i == 0) {
                                tv.setBackgroundColor(Color.YELLOW);
                        }
                        tv.setOnClickListener(new TextView.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        for (TextView tv : mTextViewAry) {
                                                String tag = (String) tv.getTag();
                                                if (tag.equals(v.getTag())) {
                                                        tv.setBackgroundColor(Color.YELLOW);
                                                } else {
                                                        tv.setBackgroundColor(0x00000000);
                                                }
                                        }
                                }
                        });
                }
                mTabScrollView.addView(mTabLinerLayout);
        }

        private int getScreenWidth() {
                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                return metrics.widthPixels;
        }

        private class GetAllProducts extends AsyncTask<Void, Boolean, Boolean> {

                private String strResult = null;
                private String amount = "";

                @Override
                protected void onPreExecute() {
                        // TODO Auto-generated method stub
                        super.onPreExecute();
                        productList = new ArrayList<ProductData>();
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                        // TODO Auto-generated method stub

                        boolean flag = false;

                        // 透過HTTP連線取得回應
                        try {
                                // for port 80 requests!
                                HttpClient httpclient = new DefaultHttpClient();
                                HttpGet httpget = new HttpGet(SysConfig.getAllgoods);
                                HttpResponse response = httpclient.execute(httpget);

                                /* 取出回應字串 */
                                strResult = EntityUtils.toString(response.getEntity(), "UTF-8");

                                flag = true;
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        if (flag) {
                                String name = "";
                                String imageUrl = "";
                                Bitmap bmp = null;
                                try {
                                        JSONArray newArray = new JSONArray(strResult);
                                        amount = "" + newArray.length();
                                        
                                        int i = newArray.length();
                                        while(i>=0){
                                        	
                                        	JSONObject obj = newArray.getJSONObject(i-1);
                                            name = obj.getString("name");
                                            imageUrl = obj.getString("image");
                                            ProductData mProductData = new ProductData();
                                            mProductData.setName(name);
                                            mProductData.setImageURL(SysConfig.imagePathApi(imageUrl));
                                            
                                            if (!imageUrl.equals("null") && productList.size() < 2) {
                                                productList.add(mProductData);
                                        }
                                          
                                            i--;
                                        }
                                        
                                       
                                        Log.d(TAG, "count = " + newArray.length());
                                } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                	Log.e(TAG, e.toString());
                                        e.printStackTrace();
                                }
                        }

                        return flag;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);

                        if (result) {

                                mProductAmountTextView.setText(amount);
                                mProductAmountTextView.setVisibility(View.VISIBLE);
                                
                                int count = productList.size();
                                ImageView[] IMGS = {mLeftImageView,mRightImageView};
                                TextView[] TEXTVIEWS = {mLeftTextView,mRightTextView};
                                
                                if(count == 0 ){
                                	
                                }else{
                                	for(int i = 0 ; i< count ; i++){
                                		String imageURL = productList.get(i).getImageURL();
                                		Log.d(TAG, "imageURL = "+imageURL);
                                		ImageView imageview = IMGS[i];
                                		String imageName = productList.get(i).getName();
                                		TextView textview = TEXTVIEWS[i];
                                		
                                		ImageLoader.getInstance().displayImage(imageURL, imageview);
                                		textview.setText(imageName);
                                		
                                	}
                                }
                                
                        }

                        MainFragment.getInstance().closeProgressDialog();
                }

        }


		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			Log.d(TAG, "onResume");
			new GetAllProducts().execute();
		}
        
        

}
