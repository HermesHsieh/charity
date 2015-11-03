package tw.org.by37.productsell;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.BackActivity;
import tw.org.by37.R;
import tw.org.by37.config.SysConfig;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

public class AllProductActivity extends BackActivity {

	private static final String TAG = AllProductActivity.class.getName();
	private ArrayList<String> imageList;

	/**
	 * 用來展示圖片的Gallery
	 */
	private GridView mAllProductGridview;

	/**
	 * GridView所使用的Adapter
	 */
	private AllProductAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_product);
		new GetAllProducts().execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 結束Activity時同時也取消所有圖片下載的工作

		this.adapter.cancelAllTasks();
	}

	private class GetAllProducts extends AsyncTask<Void, Boolean, Boolean> {

		private String strResult = null;
		private ArrayList<ProductData> dataList;
		private ProgressDialog progDailog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			imageList = new ArrayList<String>();
			dataList = new ArrayList<ProductData>();
			progDailog = new ProgressDialog(AllProductActivity.this);
			progDailog.setMessage("更新中...");
			progDailog.setIndeterminate(false);
			progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDailog.setCancelable(true);
			progDailog.show();
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
				Log.d(TAG, "api=" + SysConfig.getAllgoods);

				HttpResponse response = httpclient.execute(httpget);

				/* 取出回應字串 */
				strResult = EntityUtils.toString(response.getEntity(), "UTF-8");

				Log.i(TAG, "getAllProducts Result : " + strResult);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (flag) {
				String id = "";
				String name = "";
				String description = "";
				String quantity = "";
				String merchandise_type_id = "";
				String cash = "";
				String imageUrl = "";
				try {
					JSONArray newArray = new JSONArray(strResult);
					for (int i = 0; i < newArray.length(); i++) {
						JSONObject obj = newArray.getJSONObject(i);
						id = obj.getString("id");
						name = obj.getString("name");
						description = obj.getString("description");
						quantity = obj.getString("quantity");
						merchandise_type_id = obj.getString("merchandise_type_id");
						cash = obj.getString("cash");
						imageUrl = obj.getString("image");
						ProductData mProductData = new ProductData();
						mProductData.setId(id);
						mProductData.setName(name);
						mProductData.setDescription(description);
						mProductData.setQuantity(quantity);
						mProductData.setType(merchandise_type_id);
						mProductData.setCash(cash);
						mProductData.setImageURL(SysConfig.imagePathApi(imageUrl));
						SysConfig.productMap.put(id, mProductData);
						dataList.add(mProductData);
						imageList.add(SysConfig.imagePathApi(imageUrl));
						Log.d(TAG, "count = " + imageList.size());
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
				mAllProductGridview = (GridView) findViewById(R.id.all_product_gridview);
				adapter = new AllProductAdapter(AllProductActivity.this,
						imageList, mAllProductGridview, dataList);
				
				mAllProductGridview.setAdapter(adapter);
			}
			progDailog.dismiss();
		}

	}

}
