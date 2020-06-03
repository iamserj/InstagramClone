package ru.iamserj.instagramclone;

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
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {
	
	private ConstraintLayout cl_login_root;
	private EditText et_login_name, et_login_password;
	private Button bt_login_login;
	private TextView tv_login_signup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		setTitle("Login");
		
		cl_login_root = findViewById(R.id.cl_login_root);
		et_login_name = findViewById(R.id.et_login_name);
		et_login_password = findViewById(R.id.et_login_password);
		bt_login_login = findViewById(R.id.bt_login_login);
		tv_login_signup = findViewById(R.id.tv_login_signup);
		
		if (ParseUser.getCurrentUser() != null) {
			ParseUser.logOut();
		}
		
		cl_login_root.setOnClickListener(new View.OnClickListener() {
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
		
		et_login_password.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					loginUser();
				}
				return false;
			}
		});
		
		bt_login_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loginUser();
			}
		});
		
		tv_login_signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void loginUser() {
		if (et_login_name.getText().toString().equals("")
			|| et_login_password.getText().toString().equals("")) {
			FancyToast.makeText(LoginActivity.this, "All fields are required", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
			return;
		}
		
		ParseUser.logInInBackground(et_login_name.getText().toString(), et_login_password.getText().toString(),
				new LogInCallback() {
					@Override
					public void done(ParseUser user, ParseException e) {
						if (user != null && e == null) {
							FancyToast.makeText(LoginActivity.this, user.getUsername() + " is logged in", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
							goSocialMediaActivity();
						} else {
							FancyToast.makeText(LoginActivity.this, "Login error: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
						}
					}
				});
	}
	
	private void goSocialMediaActivity() {
		Intent intent = new Intent(LoginActivity.this, SocialMediaActivity.class);
		startActivity(intent);
	}
}
