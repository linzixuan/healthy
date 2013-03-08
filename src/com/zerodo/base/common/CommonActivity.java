package com.zerodo.base.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class CommonActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);	
	}
	
}
