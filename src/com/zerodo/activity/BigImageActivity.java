package com.zerodo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ZoomControls;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.ui.ImageZoomView;
import com.zerodo.base.util.ZoomState;
import com.zerodo.listener.SimpleZoomListener;

public class BigImageActivity extends CommonActivity implements OnTouchListener {
	private ImageZoomView mZoomView;
	private ZoomState mZoomState;
	private Bitmap mBitmap;
	private SimpleZoomListener mZoomListener;
	private Button btnBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_bigimage);
		initUI();
	}
	private void initUI(){
		btnBack=(Button) findViewById(R.id.btnBack);
		btnBack.setOnTouchListener(this);
		mZoomView = (ImageZoomView) findViewById(R.id.zoomView);
		mZoomView.setImage(HealthyApplication.getInstance().getBigBitmap());
		
//		Intent intent = this.getIntent();
//		Bundle bundle = intent.getExtras();
//		byte[] bitmapByteArray=bundle.getByteArray("bitmapByteArray");
//		mZoomView.setImage(Bytes2Bimap(bitmapByteArray));
//		mBitmap = BitmapFactory.decodeResource(
//				this.getResources(), R.drawable.about_icon);
//		mZoomView.setImage(mBitmap);
		mZoomState = new ZoomState();
		mZoomView.setZoomState(mZoomState);
		mZoomListener = new SimpleZoomListener();
		mZoomListener.setZoomState(mZoomState);
		mZoomView.setOnTouchListener(mZoomListener);
		resetZoomState();
		
		ZoomControls zoomCtrl = (ZoomControls) findViewById(R.id.zoomCtrl);
		zoomCtrl.setOnZoomInClickListener(new OnClickListener() {
			public void onClick(View v) {
				float z = mZoomState.getZoom() + 0.25f;
				mZoomState.setZoom(z);
				mZoomState.notifyObservers();
			}
		});
		zoomCtrl.setOnZoomOutClickListener(new OnClickListener() {

			public void onClick(View v) {
				float z = mZoomState.getZoom() - 0.25f;
				mZoomState.setZoom(z);
				mZoomState.notifyObservers();
			}
		});
	}
	
	public Bitmap Bytes2Bimap(byte[] b) {
        if (null!=b&&b.length != 0) {
        	return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		HealthyApplication.getInstance().setBigBitmap(null);
		if (mBitmap != null)
			mBitmap.recycle();
	}

	private void resetZoomState() {
		mZoomState.setPanX(0.5f);
		mZoomState.setPanY(0.5f);
		mZoomState.setZoom(1f);
		mZoomState.notifyObservers();
	}
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.btnBack:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				this.finish();
				break;
			}
			break;
		}
		return true;
	}
}
