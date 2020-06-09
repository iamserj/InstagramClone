package ru.iamserj.instagramclone;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTabFragment extends Fragment implements AdapterView.OnItemClickListener {
	
	private ListView lv_users_users;
	private ArrayList arrayList;
	private ArrayAdapter arrayAdapter;
	
	public UsersTabFragment() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_users_tab, container, false);
		
		final TextView tv_users_loading = view.findViewById(R.id.tv_users_loading);
		
		lv_users_users = view.findViewById(R.id.lv_users_users);
		arrayList = new ArrayList();
		arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);
		
		lv_users_users.setOnItemClickListener(UsersTabFragment.this);
		
		ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
		parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
		parseQuery.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> usersList, ParseException e) {
				if (e == null) {
					if (usersList.size() > 0) {
						for (ParseUser user : usersList) {
							arrayList.add(user.getUsername());
						}
						lv_users_users.setAdapter(arrayAdapter);
						tv_users_loading.animate().alpha(0).setDuration(1000);
						lv_users_users.setVisibility(View.VISIBLE);
					}
				} else {
				
				}
			}
		});
		
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	
	
	
	}
}
