package ru.iamserj.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.parse.*;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTabFragment extends Fragment {
	
	private static final String TAG = "asdf";
	private static final int STORAGE_REQUEST_CODE = 1000;
	private static final int PICK_IMAGE_REQUEST_CODE = 2000;
	
	private ImageView iv_sharepic_placeholder;
	private ImageView iv_iconAddPhoto;
	private EditText et_sharepic_description;
	private Button bt_sharepic_share;
	
	private Bitmap receivedImageBitmap;
	
	public SharePictureTabFragment() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);
		
		iv_sharepic_placeholder = view.findViewById(R.id.iv_sharepic_placeholder);
		iv_iconAddPhoto = view.findViewById(R.id.iv_iconAddPhoto);
		et_sharepic_description = view.findViewById(R.id.et_sharepic_description);
		bt_sharepic_share = view.findViewById(R.id.bt_sharepic_share);
		
		iv_sharepic_placeholder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// from SDK version 23 (Android 6.0 Marshmallow) we must get dangerous permission granted. ActivityCompat is used because this is Fragment
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					// 1000 is a request code, unique for this fragment
					requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
				} else {
					chooseImage();
				}
				
			}
		});
		
		bt_sharepic_share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (receivedImageBitmap != null) {
					/*if (et_sharepic_description.getText().toString().equals("")) {  // no description
					
					} else {*/
					
					final ProgressDialog progressDialog = new ProgressDialog(getContext());
					progressDialog.setMessage("Uploading image...");
					progressDialog.show();
					
					// convert to ByteArrayStream in order to send to server
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
					byte[] bytes = byteArrayOutputStream.toByteArray();
					
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
					Date date = new Date(System.currentTimeMillis());
					
					ParseFile parseFile = new ParseFile(formatter.format(date) + ".png", bytes);// 20200605_100030.png
					ParseObject parseObject = new ParseObject("Photo");                         // Parse class name is Photo
					parseObject.put("picture", parseFile);                                      // Parse column name is picture
					parseObject.put("pic_desc", et_sharepic_description.getText().toString());  // description column
					parseObject.put("username", ParseUser.getCurrentUser().getUsername());      // username column
					
					parseObject.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							if (e == null) {
								FancyToast.makeText(getContext(), "Image uploaded", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
							} else {
								FancyToast.makeText(getContext(), "Error uploading image " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
							}
							progressDialog.dismiss();
						}
					});
					
					//}
				} else {
					FancyToast.makeText(getContext(), "Please, select an image", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
				}
				
			}
		});
		
		return view;
	}
	
	// User's response to permission request
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		if (requestCode == STORAGE_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				chooseImage();
			}
		}
	}
	
	private void chooseImage() {
		//FancyToast.makeText(getContext(), "Storage permission is given", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);    // 2000
	}
	
	// Result of getting image
	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {       // 2000, -1, not null
			// Image received                           // data => Intent { dat=content://media/external/images/media/99622 flg=0x1 (has extras) }
			try {
				Uri selectedImage = data.getData();                             // content://media/external/images/media/99622
				String[] filePathColumn = {MediaStore.Images.Media.DATA};     // {"_data"}
				Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);     // 0
				String picturePath = cursor.getString(columnIndex);             // path of selected image. /storage/emulated/0/Snapseed/IMG_20200402_152548-01.jpeg
				cursor.close();
				
				receivedImageBitmap = BitmapFactory.decodeFile(picturePath);    // convert image to Bitmap
				iv_sharepic_placeholder.setImageBitmap(receivedImageBitmap);
				removeOpacity(iv_sharepic_placeholder);
				iv_iconAddPhoto.setVisibility(View.GONE);
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
		
	}
	
	private void removeOpacity(View view) {
		view.setAlpha(1);
	}
}
