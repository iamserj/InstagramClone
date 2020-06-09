package ru.iamserj.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


public class MainActivity extends AppCompatActivity {
	
	private static final String TAG = "MY_LOG_TAG";
	
	private ConstraintLayout cl_signup_root;
	private EditText et_signup_name, et_signup_email, et_signup_password;
	private Button bt_signup_signup;
	private TextView tv_signup_login;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle("Sign Up");
		
		cl_signup_root = findViewById(R.id.cl_signup_root);
		et_signup_name = findViewById(R.id.et_signup_name);
		et_signup_email = findViewById(R.id.et_signup_email);
		et_signup_password = findViewById(R.id.et_signup_password);
		bt_signup_signup = findViewById(R.id.bt_signup_signup);
		tv_signup_login = findViewById(R.id.tv_signup_login);
		
		if (ParseUser.getCurrentUser() != null) {
			FancyToast.makeText(MainActivity.this, "Signed in as " + ParseUser.getCurrentUser().getUsername(), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
			goSocialMediaActivity();
		}
		
		tv_signup_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();   // kill current activity
			}
		});
		
		et_signup_password.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					signUpUser();
				}
				return false;
			}
		});
		
		cl_signup_root.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				
			}
		});
		
		
		bt_signup_signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signUpUser();
			}
		});
		
	}
	
	private void signUpUser() {
		if (et_signup_email.getText().toString().equals("")
			|| et_signup_name.getText().toString().equals("")
			|| et_signup_password.getText().toString().equals("")) {
			FancyToast.makeText(MainActivity.this, "All fields are required", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
			return;
		}
		
		final ParseUser appUser = new ParseUser();
		appUser.setEmail(et_signup_email.getText().toString());
		appUser.setUsername(et_signup_name.getText().toString());
		appUser.setPassword(et_signup_password.getText().toString());
		
		ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
		progressDialog.setMessage("Signing up " + et_signup_name.getText().toString());
		progressDialog.show();
		
		appUser.signUpInBackground(new SignUpCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					FancyToast.makeText(MainActivity.this, appUser.getUsername() + " is signed up", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
					goSocialMediaActivity();
				} else {
					FancyToast.makeText(MainActivity.this, "Sign up error: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
				}
			}
		});
		
		progressDialog.dismiss();
	}
	
	private void goSocialMediaActivity() {
		Intent intent = new Intent(MainActivity.this, SocialMediaActivity.class);
		startActivity(intent);
		
		finish();   // kill current activity
	}
}
