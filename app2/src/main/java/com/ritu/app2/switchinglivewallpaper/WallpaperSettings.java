package com.ritu.app2.switchinglivewallpaper;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ritu.app2.R;


public class WallpaperSettings extends PreferenceActivity {
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}	
}
