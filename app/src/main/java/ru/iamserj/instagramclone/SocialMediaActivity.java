package ru.iamserj.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.parse.*;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SocialMediaActivity extends AppCompatActivity {
	
	private static final int STORAGE_REQUEST_CODE = 3000;
	private static final int PICK_IMAGE_REQUEST_CODE = 4000;
	
	private Toolbar toolbar;
	private ViewPager viewPager;
	private TabLayout tabLayout;
	private TabAdapter tabAdapter;
	private Bitmap capturedImageBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_media);
		
		setTitle("Social Media");
		
		toolbar = findViewById(R.id.tabsToolbar);
		setSupportActionBar(toolbar);
		
		viewPager = findViewById(R.id.viewPager);
		tabAdapter = new TabAdapter(getSupportFragmentManager());
		viewPager.setAdapter(tabAdapter);   // set tabs as assigned in TabAdapter.java
		
		tabLayout = findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(viewPager, false);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.my_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.item_postImage) {
			// from SDK version 23 (Android 6.0 Marshmallow) we must get dangerous permission granted. ActivityCompat is used because this is Fragment
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				// 1000 is a request code, unique for this fragment
				requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
			} else {
				captureImage();
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		if (requestCode == STORAGE_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				captureImage();
			}
		}
	}
	
	private void captureImage() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
	}
	
	
	// Result of getting image
	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {       // 4000, -1, not null
			
			// Image captured                           //
			try {
				Uri capturedImage = data.getData();                             //
				capturedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), capturedImage);
				
				
				// convert to ByteArrayStream in order to send to server
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				capturedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
				byte[] bytes = byteArrayOutputStream.toByteArray();
				
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
				Date date = new Date(System.currentTimeMillis());
				
				ParseFile parseFile = new ParseFile(formatter.format(date) + ".png", bytes);// 20200605_100030.png
				ParseObject parseObject = new ParseObject("Photo");                         // Parse class name is Photo
				parseObject.put("picture", parseFile);                                      // Parse column name is picture
				//parseObject.put("pic_desc", et_sharepic_description.getText().toString());  // description column
				parseObject.put("username", ParseUser.getCurrentUser().getUsername());      // username column
				final ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("Uploading image...");
				progressDialog.show();
				parseObject.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
							FancyToast.makeText(getApplicationContext(), "Image uploaded", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
						} else {
							FancyToast.makeText(getApplicationContext(), "Error uploading image " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
						}
						progressDialog.dismiss();
					}
				});
				
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
		
	}
}

























