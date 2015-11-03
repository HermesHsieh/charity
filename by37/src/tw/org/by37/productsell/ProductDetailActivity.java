package tw.org.by37.productsell;

import org.w3c.dom.Text;

import tw.org.by37.BackActivity;
import tw.org.by37.R;
import tw.org.by37.config.SysConfig;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ProductDetailActivity extends BackActivity {
	
	
	private ImageView mProductImageView;
	private TextView mProductNameTextView;
	private TextView mProductCashTextView;
	private TextView mProductDescriptionTextView;
	private ProductData mProductData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		setView();
		init();
	}
	
	private void setView(){
		mProductImageView = (ImageView)findViewById(R.id.product_detail_imageview);
		mProductNameTextView = (TextView)findViewById(R.id.product_detail_name_textview);
		mProductCashTextView = (TextView)findViewById(R.id.product_detail_cash_textview);
		mProductDescriptionTextView = (TextView)findViewById(R.id.product_detail_description_textview);
	}
	
	private void init(){
		mProductData = SysConfig.productMap.get(getIntent().getExtras().get("PRODUCT_KEY"));
		
		String name = mProductData.getName();
		String imageURL = mProductData.getImageURL();
		String cash = mProductData.getCash();
		String description = mProductData.getDescription();
		
		ImageLoader.getInstance().displayImage(imageURL, mProductImageView);
		mProductNameTextView.setText(name);
		mProductCashTextView.setText(cash);
		mProductDescriptionTextView.setText(description);
		
		
	}

}
