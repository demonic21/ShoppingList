package shoppinglist;

import java.util.ArrayList;

import shoppinglist.model.Information;
import shoppinglist.model.ShoppingItem;

import com.example.shoppinglist.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class ShoppedListFragment extends Fragment {
	private Context mContext;
	private ListView shoppedList;
	private ArrayList<String> shoppedArrayList;
	private ArrayAdapter<String> shoppedListAdapter;
	private OnListItemClickListener shoppedListener;
	
	public ShoppedListFragment(Context context) {
		mContext = context;
		
		shoppedArrayList = new ArrayList<String>();
		
		shoppedListAdapter = new ArrayAdapter<String>(mContext, R.layout.list_item, shoppedArrayList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shopped_list, null);
		
		shoppedList = (ListView)view.findViewById(R.id.shopped_list);
		shoppedList.setAdapter(shoppedListAdapter);
		shoppedList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		shoppedList.setOnItemClickListener(onItemClickListener);
		
		return view;
	}
	
	public interface OnListItemClickListener {
		public void onListItemClick(AdapterView<?> adapterView, View clickedView, int position, long id);
	}
	
	AdapterView.OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id) {
			shoppedListener.onListItemClick(adapterView, clickedView, position, id);
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		shoppedListener = (OnListItemClickListener)activity;
	}
	
	public void refreshList() {
		shoppedArrayList.clear();
		for (ShoppingItem item : Information.getShoppedList().getItems()) {
			shoppedArrayList.add(item.getName());
		}
		shoppedListAdapter.notifyDataSetChanged();
	}
}
