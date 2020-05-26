package ru.iamserj.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {
	
	private EditText et_username, et_password;
	private Button bt_login, bt_signup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		setTitle("Login");
		
		et_username = findViewById(R.id.et_username);
		et_password = findViewById(R.id.et_password);
		bt_login = findViewById(R.id.bt_login);
		bt_signup = findViewById(R.id.bt_signup);
		
		if (ParseUser.getCurrentUser() != null) {
			ParseUser.logOut();
		}
		
		bt_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseUser.logInInBackground(et_username.getText().toString(), et_password.getText().toString(),
						new LogInCallback() {
							@Override
							public void done(ParseUser user, ParseException e) {
								if (user != null && e == null) {
									FancyToast.makeText(LoginActivity.this, user.getUsername() + " is logged in", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
								} else {
									FancyToast.makeText(LoginActivity.this, "Login error: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
								}
							}
						});
			}
		});
		
		bt_signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}
}
