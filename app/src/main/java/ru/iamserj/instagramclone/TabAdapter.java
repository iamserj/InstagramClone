package ru.iamserj.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * @author iamserj
 * 01.06.2020 21:57
 */

public class TabAdapter extends FragmentPagerAdapter {
	public TabAdapter(@NonNull FragmentManager fm) {
		super(fm);
	}
	
	@NonNull
	@Override
	public Fragment getItem(int tabPosition) {
		
		switch (tabPosition) {
			case 0:
				return new ProfileTabFragment();
			case 1:
				return new UsersTabFragment();
			case 2:
				return new SharePictureTabFragment();
			default:
				return null;
		}
	}
	
	@Override
	public int getCount() {
		return 3;
	}
	
	@Nullable
	@Override
	public CharSequence getPageTitle(int tabPosition) {
		//return super.getPageTitle(position);
		switch (tabPosition) {
			case 0:
				return "My Profile";
			case 1:
				return "Browse Users";
			case 2:
				return "Share Pic";
			default:
				return null;
		}
	}
}
