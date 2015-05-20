package tw.org.by37.fragment.productsell;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

import tw.org.by37.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewProductActivity extends Activity {
	
	private static final String TAG = NewProductActivity.class.getName();
	
    private static final int PHOTO_PICKED_WITH_DATA = 3021;  
	private Button mSelectPictureBtn;
	private ImageView mPictureImageView;
	private Button mUploadProductBtn;
	
	private TextView messageText;
    private ImageView imageview;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;
        
    private String upLoadServerUri = null;
    private String imagepath=null;
    
    private EditText mProductNameEdittext;
    private EditText mProductAmountEdittext;
    private EditText mProductLovePointEdittext;
    private EditText mProductDonateEdittext;
    private EditText mProductDiscriptionEdittext;
    private EditText mProductLocationEdittext;
    private EditText mProductKeyWordEdittext;
    
    //Type
    private Spinner mProductTypeSpinner;
    private String[] types = {"3C電子", "家電", "衣服"};
    private ArrayAdapter<String> mTypeAdapter;
    
    //Amount
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
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_product);
		setView();
	}
	
	private void setView(){
		
		
		
		mProductTypeSpinner = (Spinner)findViewById(R.id.new_product_type_spinner);
		mTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
		mProductTypeSpinner.setAdapter(mTypeAdapter);
//		mProductTypeSpinner.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				productType = types[arg2];
//				
//			}
//		});
		
		mProductAmountTextView = (TextView)findViewById(R.id.new_product_amount_text);
		mProductAmountSeekBar = (SeekBar)findViewById(R.id.new_product_amount_seekbar);
		mProductAmountSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mProductAmountTextView.setText(""+progress);
			}
		});
		
		mProductNameEdittext = (EditText)findViewById(R.id.new_product_name_edittext);
		mProductDiscriptionEdittext = (EditText)findViewById(R.id.new_product_discription_edittext);
		mProductLocationEdittext = (EditText)findViewById(R.id.new_product_location_edittext);
		mProductKeyWordEdittext = (EditText)findViewById(R.id.new_product_keyword_edittext);
		
		mSelectPictureBtn = (Button)findViewById(R.id.new_product_select_picture_btn);
		mPictureImageView = (ImageView)findViewById(R.id.new_product_picture_imageview);
		mSelectPictureBtn.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				doPickPhotoFromGallery();
				Intent intent = new Intent();
	            intent.setType("image/*");
	            intent.setAction(Intent.ACTION_GET_CONTENT);
	            startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
			}
		});
		
		mUploadProductBtn = (Button)findViewById(R.id.new_product_upload_btn);
		mUploadProductBtn.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				productName = mProductNameEdittext.getText().toString();
				productAmount = mProductAmountTextView.getText().toString();
				productDescription = mProductDiscriptionEdittext.getText().toString();
				
				new UpdateProductTask().execute();
				
//				dialog = ProgressDialog.show(NewProductActivity.this, "", "Uploading file...", true);
//	             messageText.setText("uploading started.....");
//	             new Thread(new Runnable() {
//	                 public void run() {
//	                      uploadFile(imagepath);
//	                 }
//	               }).start();     
			}
		});
		
		messageText  = (TextView)findViewById(R.id.messageText);
		upLoadServerUri = "http://charity.gopagoda.io/upload";
	}
	
	 // 请求Gallery程序  
    protected void doPickPhotoFromGallery() {  
        try {  
            // Launch picker to choose photo for selected contact  
            final Intent intent = getPhotoPickIntent();  
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);  
        } catch (ActivityNotFoundException e) {  
        }  
    }  
    
 // 封装请求Gallery的intent  
    public static Intent getPhotoPickIntent() {  
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);  
        intent.setType("image/*");  
        intent.putExtra("crop", "true");  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        intent.putExtra("outputX", 80);  
        intent.putExtra("outputY", 80);  
        intent.putExtra("return-data", true);  
        return intent;  
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 當使用者按下確定後
		if (resultCode == RESULT_OK) {
			// 取得圖檔的路徑位置
			Uri uri = data.getData();
			// 寫log
			Log.e("uri", uri.toString());
			// 抽象資料的接口
			ContentResolver cr = this.getContentResolver();
			try {
				// 由抽象資料接口轉換圖檔路徑為Bitmap
				Bitmap bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(uri));
				// 將Bitmap設定到ImageView
				imagepath = getRealPathFromURI(uri);
				mPictureImageView.setImageBitmap(bitmap);
				mPictureImageView.setVisibility(View.VISIBLE);
				messageText.setText("Uploading file path:" +imagepath);
			} catch (FileNotFoundException e) {
				Log.e("Exception", e.getMessage(), e);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		Log.d(TAG, "path:"+cursor.getString(column_index));
		return cursor.getString(column_index);
	}
	
	public String getRealPathFromURI(Uri contentUri) {
	    String res = null;
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
	    if(cursor.moveToFirst()){;
	       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       res = cursor.getString(column_index);
	    }
	    cursor.close();
	    return res;
	}
	
	public int uploadFile(String sourceFileUri) {

		Log.d(TAG, "sourceFileUri = "+sourceFileUri);
		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			Log.e("uploadFile", "Source File not exist :" + imagepath);

			runOnUiThread(new Runnable() {
				public void run() {
					messageText.setText("Source File not exist :" + imagepath);
				}
			});

			return 0;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						public void run() {
							String msg = "File Upload Completed.\n\n See uploaded file your server. \n\n";
							messageText.setText(msg);
							Toast.makeText(NewProductActivity.this,
									"File Upload Complete.", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText
								.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(NewProductActivity.this,
								"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText.setText("Got Exception : see logcat ");
						Toast.makeText(NewProductActivity.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();
			return serverResponseCode;

		} // End else block
	}
	
	class UpdateProductTask extends AsyncTask<Void, Void, Void>{
		
		
		private ProgressDialog progDailog;
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
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
		protected Void doInBackground(Void... paramsss) {
			// TODO Auto-generated method stub
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
	        params.add(new BasicNameValuePair("image", imagepath));
	        params.add(new BasicNameValuePair("user_id", "1"));
	        params.add(new BasicNameValuePair("type", "1"));
	       

	        try {
	                // setup multipart entity
	                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

	                for (int i = 0; i < params.size(); i++) {
	                        // identify param type by Key
	                        if (params.get(i).getName().equals("image")) {
	                                entity.addPart(params.get(i).getName(), new FileBody(new File(params.get(i).getValue())));
	                        } else {
	                                entity.addPart(params.get(i).getName(), new StringBody(params.get(i).getValue(), Charset.forName("UTF-8")));
	                        }
	                }

	                post.setEntity(entity);

	                try {
	                        // execute and get response
	                        result = new String(client.execute(post, new BasicResponseHandler()).getBytes(), HTTP.UTF_8);

	                        Log.i(TAG, "postUsers Result : " + result);
	                } catch (Exception e) {
	                        e.printStackTrace();
	                        Log.d(TAG, e.toString());
	                        Log.e(TAG, "postUsers Result Exception!");
	                }
	        } catch (Exception e) {
	                e.printStackTrace();
	                Log.d(TAG, e.toString());
	                Log.e(TAG, "postUsers Exception");
	        }
			return null;
		}
		
	}

	
	

}
