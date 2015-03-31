package tw.org.by37.fragment.main;

import tw.org.by37.R;

import tw.org.by37.fragment.member.LoginFragment;
import tw.org.by37.emergency.EmergencyFragment;
import tw.org.by37.fragment.member.MemberLoginFragment;
import tw.org.by37.fragment.organization.OrganizationFragment;
import tw.org.by37.fragment.productsell.MainProductSellFragment;
import tw.org.by37.fragment.search.SearchFragment;
import tw.org.by37.fragment.supplieshelp.SuppliesHelpFragment;
import tw.org.by37.fragment.test.TestFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MainFragment extends Fragment {

        private final static String TAG = "MainFragment";

        private Context mContext;

        /** MemberLogin Fragment **/
        private LoginFragment mMemberLoginFragment;

        /** Search Fragment **/
        private SearchFragment mSearchFragment;

        /** SuppliesHelp Fragment **/
        private SuppliesHelpFragment mSuppliesHelpFragment;

        /** Organization Fragment **/
        private OrganizationFragment mOrganizationFragment;
        
        /** Emergency Fragment **/
        private EmergencyFragment mEmergencyFragment;

        
        /** ProductSell Fragment**/
        private MainProductSellFragment mMainProductSellFragment;
        private TestFragment mTestFragment;
        
        

        private ListView mListView;
        
        
    	
    	

        public MainFragment() {
                super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_main, container, false);

                findView(view);
                
//                switchTestFragment();
                
                switchEmergencyFragment();
                
                switchProductSellFragment();

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {
        }

        /**
         * switchMemberLoginFragment 介面
         */
        public void switchLoginFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);
                FragmentTransaction ft = manager.beginTransaction();

                if (mMemberLoginFragment == null)
                        mMemberLoginFragment = new LoginFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mMemberLoginFragment);
                } else {
                        ft.replace(R.id.fragment_content, mMemberLoginFragment);
                }
                ft.commit();

                getActivity().setTitle(getString(R.string.fragment_title_member_login));
        }

        /**
         * switchSearchFragment 介面
         */
        public void switchSearchFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mSearchFragment == null)
                        mSearchFragment = new SearchFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mSearchFragment);
                } else {
                        ft.replace(R.id.fragment_content, mSearchFragment);
                }
                ft.commit();

                getActivity().setTitle(getString(R.string.fragment_title_search));
        }

        /**
         * switchSearchFragment 介面
         */
        public void switchSuppliesHelpFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mSuppliesHelpFragment == null)
                        mSuppliesHelpFragment = new SuppliesHelpFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mSuppliesHelpFragment);
                } else {
                        ft.replace(R.id.fragment_content, mSuppliesHelpFragment);
                }
                ft.commit();

                getActivity().setTitle(getString(R.string.fragment_title_supplieshelp));
        }

        /**
         * switchOrganizationFragment 介面
         */
        public void switchOrganizationFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mOrganizationFragment == null)
                        mOrganizationFragment = new OrganizationFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mOrganizationFragment);
                } else {
                        ft.replace(R.id.fragment_content, mOrganizationFragment);
                }
                ft.commit();

                getActivity().setTitle(getString(R.string.fragment_title_organization));
        }

        /**
         * switchTestFragment 介面
         */
        public void switchTestFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mTestFragment == null)
                        mTestFragment = new TestFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mTestFragment);
                } else {
                        ft.replace(R.id.fragment_content, mTestFragment);
                }
                ft.commit();

                getActivity().setTitle(getString(R.string.fragment_title_test));
        }
        
        /**
         * switchEmergencyFragment 介面
         */
        public void switchEmergencyFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_emergency);

                FragmentTransaction ft = manager.beginTransaction();

                if (mEmergencyFragment == null)
                	mEmergencyFragment = new EmergencyFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_emergency, mEmergencyFragment);
                } else {
                        ft.replace(R.id.fragment_emergency, mEmergencyFragment);
                }
                ft.commit();

        }
        
        
        /**
         * switchProductSellFragment 介面
         */
        public void switchProductSellFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_productsell);

                FragmentTransaction ft = manager.beginTransaction();

                if (mMainProductSellFragment == null)
                	mMainProductSellFragment = new MainProductSellFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_productsell, mMainProductSellFragment);
                } else {
                        ft.replace(R.id.fragment_productsell, mMainProductSellFragment);
                }
                ft.commit();

        }
        
        
        
        
        
        

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, "Request Code : " + requestCode);
                Log.i(TAG, "Result Code : " + resultCode);
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
        public void onStart() {
                super.onStart();
        }

        @Override
        public void onStop() {
                super.onStop();
        }

        @Override
        public void onPause() {
                super.onPause();
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
        }
        
        
}
