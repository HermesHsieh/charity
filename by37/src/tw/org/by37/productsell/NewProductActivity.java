package tw.org.by37.productsell;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.BackActivity;
import tw.org.by37.R;
import tw.org.by37.data.GoodsTypesText;
import tw.org.by37.data.SelectingData;
import tw.org.by37.data.TypesData;
import tw.org.by37.data.UserData2;
import tw.org.by37.member.UserData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewProductActivity extends BackActivity {

	private static final String TAG = NewProductActivity.class.getName();

	private ImageView mPictureImageView0;
	private ImageView mPictureImageView1;
	private ImageView mPictureImageView2;
	private ImageView mCurrectImageView;
	private Button mUploadProductBtn;

	private int serverResponseCode = 0;
	private ProgressDialog dialog = null;

	private String upLoadServerUri = null;

	private EditText mProductNameEdittext;
	private EditText mProductAmountEdittext;
	private EditText mProductLovePointEdittext;
	private EditText mProductDonateEdittext;
	private EditText mProductDiscriptionEdittext;
	private EditText mProductLocationEdittext;
	private EditText mProductKeyWordEdittext;

	// Type
	private Spinner mProductTypeSpinner;

	// Amount
	private SeekBar mProductAmountSeekBar;
	private TextView mProductAmountTextView;

	private String productName = "";
	private String productType = "";
	private String productAmount = "";
	private String productLovePoint = "";
	private String productDescription = "";
	private String productTranspot = "";
	private String productLocation = "";
	private String productKeyWord = "";

	private ArrayList<String> imageList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_product);
		setView();
		init();
	}

	private void init() {
		imageList = new ArrayList<String>();
	}

	private void setView() {

		mProductTypeSpinner = (Spinner) findViewById(R.id.new_product_type_spinner);
		getGoodsTypes();

		mProductAmountTextView = (TextView) findViewById(R.id.new_product_amount_text);
		mProductAmountSeekBar = (SeekBar) findViewById(R.id.new_product_amount_seekbar);
		mProductAmountSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						mProductAmountTextView.setText("" + progress);
					}
				});

		mProductNameEdittext = (EditText) findViewById(R.id.new_product_name_edittext);
		mProductDiscriptionEdittext = (EditText) findViewById(R.id.new_product_discription_edittext);
		mProductLocationEdittext = (EditText) findViewById(R.id.new_product_location_edittext);
		mProductKeyWordEdittext = (EditText) findViewById(R.id.new_product_keyword_edittext);


		mPictureImageView0 = (ImageView) findViewById(R.id.new_product_picture_imageview0);
		mPictureImageView0.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCurrectImageView = mPictureImageView0;
				showGetPictureView();
			}
		});
		
		mPictureImageView1 = (ImageView) findViewById(R.id.new_product_picture_imageview1);
		mPictureImageView1.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCurrectImageView = mPictureImageView1;
				showGetPictureView();
			}
		});
		
		mPictureImageView2 = (ImageView) findViewById(R.id.new_product_picture_imageview2);
		mPictureImageView2.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCurrectImageView = mPictureImageView2;
				showGetPictureView();
			}
		});

		mUploadProductBtn = (Button) findViewById(R.id.new_product_upload_btn);
		mUploadProductBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				productName = mProductNameEdittext.getText().toString();
				productAmount = mProductAmountTextView.getText().toString();
				productDescription = mProductDiscriptionEdittext.getText()
						.toString();

				new UpdateProductTask().execute();
			}
		});

		upLoadServerUri = "http://charity.gopagoda.io/upload";
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		Log.d(TAG, "path:" + cursor.getString(column_index));
		return cursor.getString(column_index);
	}

	class UpdateProductTask extends AsyncTask<Void, Boolean, Boolean> {

		private ProgressDialog progDailog;

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result){
				Toast.makeText(NewProductActivity.this, "上傳成功", Toast.LENGTH_SHORT).show();
				finish();
			}else{
				Toast.makeText(NewProductActivity.this, "上傳失敗", Toast.LENGTH_SHORT).show();
			}
			progDailog.dismiss();
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progDailog = new ProgressDialog(NewProductActivity.this);
			progDailog.setMessage("上傳中...");
			progDailog.setIndeterminate(false);
			progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDailog.setCancelable(true);
			progDailog.show();

		}

		@Override
		protected Boolean doInBackground(Void... paramsss) {
			// TODO Auto-generated method stub
			boolean flag =false;
			String result = null;
			
			HttpClient client = new DefaultHttpClient();

			HttpPost post = new HttpPost("http://charity.gopagoda.io/addGoods");

			/*
			 * Post運作傳送變數必須用NameValuePair[]陣列儲存
			 */
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", productName));
			params.add(new BasicNameValuePair("description", productDescription));
			params.add(new BasicNameValuePair("quantity", productAmount));
			params.add(new BasicNameValuePair("user_id", UserData2.user_id.toString()));
//			 params.add(new BasicNameValuePair("user_id", "60"));
			params.add(new BasicNameValuePair("type", productType));
			for (int i = 0; i < imageList.size(); i++) {
				params.add(new BasicNameValuePair("images[" + i + "]",imageList.get(i)));
			}

			try {
				// setup multipart entity
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

				for (int i = 0; i < params.size(); i++) {
					// identify param type by Key
					if (params.get(i).getName().contains("images")) {
						entity.addPart(params.get(i).getName(), new FileBody(new File(params.get(i).getValue())));
					} else {
						entity.addPart(params.get(i).getName(),new StringBody(params.get(i).getValue(),Charset.forName("UTF-8")));
					}
				}

				post.setEntity(entity);

				try {
					// execute and get response
					result = new String(client.execute(post,
							new BasicResponseHandler()).getBytes(), HTTP.UTF_8);
					Log.d(TAG, "result = "+result);
					flag = true;

				} catch (Exception e) {
					e.printStackTrace();
					Log.d(TAG, e.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return flag;
		}

	}

	/** 從txt檔取得物品的類別資料 **/
	private void getGoodsTypes() {
		Log.i(TAG, "getGoodsTypes");
		String result = null;

		result = GoodsTypesText.readText();

		if (result != null) {
			try {
				JSONArray mJsonArray = new JSONArray(result);
				Log.i(TAG, "mJsonArray Length : " + mJsonArray.length());

				/** 清空物資類別清單 **/
				SelectingData.clearGoodsTypesList();

				for (int i = 0; i < mJsonArray.length(); i++) {
					TypesData mData = new TypesData();
					JSONObject mJsonObject = mJsonArray.getJSONObject(i);
					try {
						mData.id = mJsonObject.getString("id");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					try {
						mData.name = mJsonObject.getString("name");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					try {
						mData.created_at = mJsonObject.getString("created_at");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					try {
						mData.updated_at = mJsonObject.getString("updated_at");
					} catch (JSONException e) {
						e.printStackTrace();
					}

					/** 加入到物資類別清單 **/
					SelectingData.addGoodsTypesListItem(mData);

				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e(TAG, "The GoodsTypesData from Server == null!!");
			}
		} else {
			Log.e(TAG, "getGoodsTypes Result == null !!");

		}

		setGoodsTypesInSpinner();

		Log.i(TAG, SelectingData.getGoodsTypesListItemString());
	}

	private void setGoodsTypesInSpinner() {
		String[] category = new String[SelectingData.mGoodsTypes.size()];

		for (int i = 0; i < SelectingData.mGoodsTypes.size(); i++) {
			category[i] = SelectingData.mGoodsTypes.get(i).name;
		}
		

		ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, category);
		mProductTypeSpinner.setAdapter(adapter_category);
		mProductTypeSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Log.d(TAG, "arg2 = " + arg2);
						productType = SelectingData.mGoodsTypes.get(arg2).id
								.toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {


		// 當使用者按下確定後
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				Bundle extras = data.getExtras();
				Bitmap bmp = (Bitmap) extras.get("data");
				mCurrectImageView.setImageBitmap(bmp);
				saveResizeImage(bmp, mCurrectImageView.getId());
				break;
			case 1:
				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				try {
					// 由抽象資料接口轉換圖檔路徑為Bitmap
					Bitmap bitmap = BitmapFactory.decodeStream(cr
							.openInputStream(uri));
					
					Log.d(TAG, "bitmap = "+bitmap);
					mCurrectImageView.setImageBitmap(bitmap);
//					mCurrectImageView.setImageBitmap(getResizedBitmap(bitmap,640, 480));
//					saveResizeImage(bitmap, mCurrectImageView.getId());
				} catch (FileNotFoundException e) {
					Log.e("Exception", e.getMessage(), e);
				}
				break;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showGetPictureView() {
		final String[] lunch = { "拍攝照片", "相簿" };
		new AlertDialog.Builder(NewProductActivity.this).setItems(lunch,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intent_camera = new Intent(
									android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent_camera, 0);
							break;
						case 1:
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, 1);
                            break;
						}
					}
				}).show();
	}

	private String filePath = Environment.getExternalStorageDirectory()
			+ File.separator + "BY37/";

	private void saveResizeImage(Bitmap photo, int name) {
		// photo = Bitmap.createScaledBitmap(photo, 480, 640, true);
		// photo = getResizedBitmap(photo,photo.getWidth(),photo.getHeight());
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
		String fileName = name + ".jpg";
		String path = filePath + fileName;

		checkFileExist(filePath);

		try {
			File f = new File(path);
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();
			imageList.add(path);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, e.toString());
		}

	}

	private void checkFileExist(String filePath) {
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		}
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// Create a matrix for the manipulation
		Matrix matrix = new Matrix();

		// Resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);

		// Recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;

	}

}
