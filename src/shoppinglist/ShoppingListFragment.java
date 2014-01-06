package shoppinglist;

import java.util.ArrayList;

import shoppinglist.model.Information;
import shoppinglist.model.ShoppingItem;
import shoppinglist.model.ShoppingList;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class ShoppingListFragment extends Fragment {
	private Context mContext;
	private ListView shoppingListView;
	private ArrayList<String> shoppingArrayList;
	private ArrayAdapter<String> shoppingListAdapter;
	private OnListItemClickListener shoppingListener;
		
	public ShoppingListFragment(Context context) {
		mContext = context;
		
		shoppingArrayList = new ArrayList<String>();
		shoppingListAdapter = new ArrayAdapter<String>(mContext, R.layout.list_item, shoppingArrayList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shopping_list, null);
		
		shoppingListView = (ListView)view.findViewById(R.id.shopping_list);
		shoppingListView.setAdapter(shoppingListAdapter);
		shoppingListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		shoppingListView.setOnItemClickListener(onItemClickListener);
		
		return view;
	}
	
	public interface OnListItemClickListener {
		public void onListItemClick(AdapterView<?> adapterView, View clickedView, int position, long id);
	}
	
	AdapterView.OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id) {
			shoppingListener.onListItemClick(adapterView, clickedView, position, id);
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		shoppingListener = (OnListItemClickListener)activity;
	}
	
	public void refreshList() {
		shoppingArrayList.clear();
		for (ShoppingItem item : Information.getShoppingList().getItems()) {
			shoppingArrayList.add(item.getName());
		}
		shoppingListAdapter.notifyDataSetChanged();
	}
}
