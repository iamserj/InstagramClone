package ru.iamserj.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.parse.*;
import com.shashank.sony.fancytoastlib.FancyToast;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTabFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
	
	private ListView lv_users_users;
	private ArrayList<String> arrayListUsernames;
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
		arrayListUsernames = new ArrayList();
		arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayListUsernames);
		
		lv_users_users.setOnItemClickListener(UsersTabFragment.this);
		lv_users_users.setOnItemLongClickListener(UsersTabFragment.this);
		
		ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
		parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
		parseQuery.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> usersList, ParseException e) {
				if (e == null) {
					if (usersList.size() > 0) {
						for (ParseUser user : usersList) {
							arrayListUsernames.add(user.getUsername());
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
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		
		final ProgressDialog dialog = new ProgressDialog(getContext());
		
		ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
		parseQuery.whereEqualTo("username", arrayListUsernames.get(position));
		parseQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (objects.size() > 0 && e == null) {
					dialog.show();
					Intent intent = new Intent(getContext(), UsersPosts.class);
					intent.putExtra("username", arrayListUsernames.get(position));
					startActivity(intent);
				} else {
					FancyToast.makeText(getContext(), arrayListUsernames.get(position) + " doesn't have any posts", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
				}
				dialog.dismiss();
			}
		});
		
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
		parseQuery.whereEqualTo("username", arrayListUsernames.get(position));
		parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (user != null && e == null) {
					
					String userData = "\n\n"
							+ "Name: " + user.get("profile_name") + "\n\n"
							+ "Bio: " + user.get("profile_bio") + "\n\n"
							+ "Profession: " + user.get("profile_profession") + "\n\n"
							+ "Hobby: " + user.get("profile_hobby") + "\n\n"
							+ "Sport: " + user.get("profile_sport") + "\n\n";
					userData = userData.replace("null", "-");
							
					final PrettyDialog prettyDialog = new PrettyDialog(getContext());
					prettyDialog.setTitle(user.getUsername() + "'s info")
							.setMessage(userData)
							.setIcon(R.drawable.ic_baseline_person_24)
							.addButton("OK", R.color.pdlg_color_white, R.color.pdlg_color_green, new PrettyDialogCallback() {
								@Override
								public void onClick() {
									prettyDialog.dismiss();
								}
							})
							.show();
				}
			}
		});
		
		return true;
	}
}
