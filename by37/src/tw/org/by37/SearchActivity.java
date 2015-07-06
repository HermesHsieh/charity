package tw.org.by37;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchActivity extends Activity {

        private final static String TAG = SearchActivity.class.getName();

        private Context mContext;

        static final LatLng NKUT = new LatLng(23.979548, 120.696745);
        private GoogleMap map;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Log.v(TAG, "onCreate");
                setContentView(R.layout.activity_search);
                mContext = this;

                map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                Marker nkut = map.addMarker(new MarkerOptions().position(NKUT).title("南開科技大學").snippet("數位生活創意系"));

                // Move the camera instantly to NKUT with a zoom of 16.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(NKUT, 16));
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.v(TAG, "Request Code : " + requestCode);
                Log.v(TAG, "Result Code : " + resultCode);
        }

        /** Preferences **/
        /** End of Preferences **/

        /** GotoActivity **/
        /** End of GotoActivity **/

        /** Activity Bundle **/
        /** End of Activity Bundle **/

        @Override
        public void onResume() {
                super.onResume();
        }

        @Override
        protected void onStart() {
                super.onStart();
        }

        @Override
        public void onStop() {
                super.onStop();
        }

        @Override
        protected void onPause() {
                super.onPause();
        }

        @Override
        protected void onDestroy() {
                super.onDestroy();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it
                // is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                default:
                        return false;
                }
        }
}
