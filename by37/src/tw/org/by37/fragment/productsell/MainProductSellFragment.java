package tw.org.by37.fragment.productsell;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.R;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainProductSellFragment extends Fragment {
	
	private static final String TAG = MainProductSellFragment.class.getName();

	private HorizontalScrollView mTabScrollView;;
	private String[] tabTitles = {"推薦","慈善專區","促銷品","一元起標","全部商品"};
	private ArrayList<TextView> mTextViewAry;
	
	private TextView mProductAmountTextView;
	private ArrayList<ProductData> productList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_main_productsell, container,false);
		setView(v);
		init();
		initTabBar();
		return v;
	}
	
	private void setView(View v){
		mTabScrollView = (HorizontalScrollView)v.findViewById(R.id.productsell_tab_scrollview);
		mProductAmountTextView = (TextView)v.findViewById(R.id.main_product_count_tv);
	}
	
	private void init(){
		mTextViewAry = new ArrayList<TextView>();
		productList = new ArrayList<ProductData>();
		
		new GetAllProducts().execute();
	}
	
	private void initTabBar(){
		LinearLayout mTabLinerLayout = new LinearLayout(getActivity());
		for(int i =0;i<tabTitles.length;i++){
			String title = tabTitles[i];
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			p.width=getScreenWidth()/3;
			p.height=130;
			TextView tv = new TextView(getActivity());
			tv.setText(title);
			tv.setTextSize(18);
			tv.setGravity(Gravity.CENTER);
			tv.setLayoutParams(p);
			tv.setTag(title);
			mTextViewAry.add(tv);
			mTabLinerLayout.addView(tv);
			if(i==0){
				tv.setBackgroundColor(Color.YELLOW);
			}
			tv.setOnClickListener(new TextView.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(TextView tv : mTextViewAry){
						String tag = (String) tv.getTag();
						if(tag.equals(v.getTag())){
							tv.setBackgroundColor(Color.YELLOW);
						}else{
							tv.setBackgroundColor(Color.GRAY);
						}
					}
				}
			});
		}
		mTabScrollView.addView(mTabLinerLayout);
	}
	
	private int getScreenWidth(){
		DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
	}
	
	private class GetAllProducts extends AsyncTask<Void, Boolean, Boolean>{

		private String strResult = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub

			boolean flag = false;

			// 透過HTTP連線取得回應
			try {
				// for port 80 requests!
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet("http://charity.gopagoda.io/getAllGoods");
				HttpResponse response = httpclient.execute(httpget);

				/* 取出回應字串 */
				strResult = EntityUtils.toString(response.getEntity(), "UTF-8");

				Log.i(TAG, "getAllProducts Result : " + strResult);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return flag;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			String amount = "";
			if(result){
				try {
					JSONArray newArray = new JSONArray(strResult);
					amount = ""+newArray.length();
					for(int i=0;i<newArray.length();i++){
						JSONObject obj = newArray.getJSONObject(i);
						ProductData mProductData = new ProductData();
						mProductData.setName(obj.getString("name"));
						mProductData.setImage(obj.getString("image"));
					}
					Log.d(TAG, "count = "+newArray.length());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mProductAmountTextView.setText(amount);
				mProductAmountTextView.setVisibility(View.VISIBLE);
			}
		}
		
	}

}
