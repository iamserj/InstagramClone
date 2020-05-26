package ru.iamserj.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


public class MainActivity extends AppCompatActivity {
	
	private static final String TAG = "MY_LOG_TAG";
	
	private EditText et_username, et_email, et_password;
	private Button bt_login, bt_signup;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle("Sign Up");
		
		et_username = findViewById(R.id.et_username);
		et_email = findViewById(R.id.et_email);
		et_password = findViewById(R.id.et_password);
		bt_login = findViewById(R.id.bt_login);
		bt_signup = findViewById(R.id.bt_signup);
		
		if (ParseUser.getCurrentUser() != null) {
			ParseUser.logOut();
		}
		
		bt_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		
		bt_signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final ParseUser appUser = new ParseUser();
				appUser.setEmail(et_email.getText().toString());
				appUser.setUsername(et_username.getText().toString());
				appUser.setPassword(et_password.getText().toString());
				
				ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setMessage("Signing up " + et_username.getText().toString());
				progressDialog.show();
				
				appUser.signUpInBackground(new SignUpCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
							FancyToast.makeText(MainActivity.this, appUser.getUsername() + " is signed up", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
						} else {
							FancyToast.makeText(MainActivity.this, "Sign up error: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
						}
					}
				});
				
				progressDialog.dismiss();
			}
		});
		
	}
}
