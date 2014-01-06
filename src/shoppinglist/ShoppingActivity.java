package shoppinglist;

import java.util.Locale;

import shoppinglist.model.*;

import com.example.shoppinglist.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class ShoppingActivity extends FragmentActivity implements ShoppingListFragment.OnListItemClickListener, ShoppedListFragment.OnListItemClickListener, AddItemFragment.CustomOnClickListener, HttpGetListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_menu, menu);
        return true;
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			if(requestCode == 1) {
				ShoppingListFragment shoppingListFragment = (ShoppingListFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPager.getId() + ":0");
				ShoppedListFragment shoppedListFragment = (ShoppedListFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPager.getId() + ":1");
				shoppingListFragment.refreshList();
				shoppedListFragment.refreshList();
			}
		}
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ShoppingActivity.this);
    	switch(item.getItemId()) {
    	case R.id.load_list :
			alert_confirm.setMessage("새로운 쇼핑 목록을 불러오시겠습니까? 기존의 쇼핑 데이터는 삭제됩니다.");
			alert_confirm.setCancelable(false);
			alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(ShoppingActivity.this,LoadShoppingListActivity.class);
					startActivityForResult(intent, 1);
				}
			});
			alert_confirm.setNegativeButton("취소", null);
    		break;
    	case R.id.end_shopping :
			alert_confirm.setMessage("쇼핑을 완료하시겠습니까?");
			alert_confirm.setCancelable(false);
			alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.e("test3", Information.getShoppedList().getXML());
					new DBAsyncTask((HttpGetListener) ShoppingActivity.this).execute("2", Information.getShoppedList().getXML());
				}
			});
			alert_confirm.setNegativeButton("취소", null);
    		break;
    	}
    	
    	AlertDialog alert = alert_confirm.create();
		alert.show();
    	
		return super.onOptionsItemSelected(item);
	}



	/**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
    	Context mContext;

        public SectionsPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
            case 0:
            	return new ShoppingListFragment(mContext);
            case 1:
            	return new ShoppedListFragment(mContext);
            case 2:
            	return new AddItemFragment(mContext);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

	@Override
	public void onListItemClick(AdapterView<?> adapterView, View clickedView, int position, long id) {
		ShoppingListFragment shoppingList = (ShoppingListFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPager.getId() + ":0");
		ShoppedListFragment shoppedgList = (ShoppedListFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPager.getId() + ":1");
		String item;
		
		switch (adapterView.getId()) {
		case R.id.shopping_list:
			Information.getShoppedList().addItems(Information.getShoppingList().removeItems(position));
			break;
		case R.id.shopped_list:
			Information.getShoppingList().addItems(Information.getShoppedList().removeItems(position));
			break;
		}
		
		shoppingList.refreshList();
		shoppedgList.refreshList();
	}

	@Override
	public void onClicked() {
		AddItemFragment addItem = (AddItemFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPager.getId() + ":2");
		ShoppingListFragment shoppingList = (ShoppingListFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPager.getId() + ":0");
		ShoppingItem item = new ShoppingItem(null, addItem.getItemName());
		Information.getShoppingList().addItems(item);
		
		shoppingList.refreshList();
	}

	@Override
	public void doStuff(String result) {
		if(result.equals("0"))
			Toast.makeText(this, "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(this, "저장이 실패하였습니다.", Toast.LENGTH_SHORT).show();
	}
}
