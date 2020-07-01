package ru.iamserj.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.*;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {
	
	private LinearLayout ll_posts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_posts);
		
		ll_posts = findViewById(R.id.ll_posts);
		
		Intent receivedIntent = getIntent();
		final String receivedUsername = receivedIntent.getStringExtra("username");
		
		setTitle(receivedUsername + "'s posts");
		
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...");
		dialog.show();
		
		ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
		parseQuery.whereEqualTo("username", receivedUsername);
		parseQuery.orderByDescending("createdAt");
		
		parseQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (objects.size() > 0 && e == null) {
					for (ParseObject post : objects) {
						
						final TextView tv_postDescription = new TextView(UsersPosts.this);
						if (post.get("pic_desc") != null) tv_postDescription.setText(post.get("pic_desc") + "");
						
						ParseFile postPicture = (ParseFile) post.get("picture");    // cast Object to a ParseFile
						postPicture.getDataInBackground(new GetDataCallback() {
							@Override
							public void done(byte[] data, ParseException e) {
								
								if (data != null && e == null) {
									
									Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
									
									// ImageView params
									LinearLayout.LayoutParams paramsPicture = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
									paramsPicture.setMargins(5, 5, 5, 5);
									
									// ImageView
									ImageView iv_postPicture = new ImageView(UsersPosts.this);
									iv_postPicture.setLayoutParams(paramsPicture);
									iv_postPicture.setScaleType(ImageView.ScaleType.FIT_CENTER);
									iv_postPicture.setImageBitmap(bitmap);
									
									// TextView params
									// TODO: add date before description
									LinearLayout.LayoutParams paramsDescription = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
									paramsDescription.setMargins(5, 5, 5, 15);
									tv_postDescription.setLayoutParams(paramsDescription);
									tv_postDescription.setGravity(Gravity.CENTER);
									tv_postDescription.setBackgroundColor(Color.BLUE);
									tv_postDescription.setTextColor(Color.WHITE);
									tv_postDescription.setTextSize(30f);
									
									ll_posts.addView(iv_postPicture);
									ll_posts.addView(tv_postDescription);
								}
							}
						});
					}
				} else {
					//FancyToast.makeText(UsersPosts.this, receivedUsername + " doesn't have any posts", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
					//finish();
				}
				
				dialog.dismiss();
			}
		});
		
	}
}