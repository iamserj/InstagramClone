package ru.iamserj.instagramclone;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTabFragment extends Fragment {
	
	private static final String PROFILE_NAME = "profile_name";
	private static final String PROFILE_BIO = "profile_bio";
	private static final String PROFILE_PROFESSION = "profile_profession";
	private static final String PROFILE_HOBBY = "profile_hobby";
	private static final String PROFILE_SPORT = "profile_sport";
	
	
	private EditText et_profile_name, et_profile_bio, et_profile_profession, et_profile_hobby, et_profile_sport;
	private Button bt_profile_update;
	
	public ProfileTabFragment() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
		
		et_profile_name = view.findViewById(R.id.et_profile_name);
		et_profile_bio = view.findViewById(R.id.et_profile_bio);
		et_profile_profession = view.findViewById(R.id.et_profile_profession);
		et_profile_hobby = view.findViewById(R.id.et_profile_hobby);
		et_profile_sport = view.findViewById(R.id.et_profile_sport);
		
		bt_profile_update = view.findViewById(R.id.bt_profile_update);
		
		final ParseUser parseUser = ParseUser.getCurrentUser();
		if (parseUser.get(PROFILE_NAME) != null) et_profile_name.setText(parseUser.get(PROFILE_NAME).toString());
		if (parseUser.get(PROFILE_BIO) != null) et_profile_bio.setText(parseUser.get(PROFILE_BIO).toString());
		if (parseUser.get(PROFILE_PROFESSION) != null) et_profile_profession.setText(parseUser.get(PROFILE_PROFESSION).toString());
		if (parseUser.get(PROFILE_HOBBY) != null) et_profile_hobby.setText(parseUser.get(PROFILE_HOBBY).toString());
		if (parseUser.get(PROFILE_SPORT) != null) et_profile_sport.setText(parseUser.get(PROFILE_SPORT).toString());
		
		
		bt_profile_update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				parseUser.put(PROFILE_NAME, et_profile_name.getText().toString());
				parseUser.put(PROFILE_BIO, et_profile_bio.getText().toString());
				parseUser.put(PROFILE_PROFESSION, et_profile_profession.getText().toString());
				parseUser.put(PROFILE_HOBBY, et_profile_hobby.getText().toString());
				parseUser.put(PROFILE_SPORT, et_profile_sport.getText().toString());
				parseUser.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
							FancyToast.makeText(getContext(), "Profile data saved", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
						} else {
							FancyToast.makeText(getContext(), "Error: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
						}
					}
				});
			}
		});
		return view;
	}
}
