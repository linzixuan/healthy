package com.zerodo.activity;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.adapter.ExpandableAdapter;
import com.zerodo.db.dao.MedicalCaseDao;
import com.zerodo.db.dao.PatientInfoDao;
import com.zerodo.db.dao.UserInfoDao;
import com.zerodo.db.model.MedicalCase;
import com.zerodo.db.model.PatientInfo;
import com.zerodo.db.model.UserInfo;

public class HomeActivity extends TabActivity implements OnClickListener,
		OnTouchListener, OnItemLongClickListener, OnChildClickListener {

	private ExpandableListView expandListView;
	private ExpandableAdapter adapter;
	private Button homeAddBtn;
	private ImageView homeHeadPic;
	private TextView patientCount, medicalCount;
	private Button addMedicalCase;
	private PatientInfo patientSelected;

	// 更多、分页
	private View moreView; // ListView底部View
	private Handler handler;
	private ProgressBar pg;
	private Button morebutton;
	private final static int rowsize = 10;
	private static int pageno = 0;
	private boolean removeFoot = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_home);
		initUI();
		initListFoot();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initUI() {
		homeAddBtn = (Button) findViewById(R.id.homeAddBtn);
		homeAddBtn.setOnTouchListener(this);
		expandListView = (ExpandableListView) findViewById(R.id.expandableListView);
		expandListView.setCacheColorHint(0);
		expandListView.setOnItemLongClickListener(this);
		expandListView.setOnChildClickListener(this);
		// expandListView.setOnTouchListener(this);
		homeHeadPic = (ImageView) findViewById(R.id.homeHeadPic);
		homeHeadPic.setOnClickListener(this);
		patientCount = (TextView) findViewById(R.id.patientCount);
		patientCount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		// TextPaint tp = patientCount.getPaint();
		// tp.setFakeBoldText(true);
		patientCount.setOnClickListener(this);
		medicalCount = (TextView) findViewById(R.id.medicalCount);
		medicalCount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		// tp = medicalCount.getPaint();
		// tp.setFakeBoldText(true);
		medicalCount.setOnClickListener(this);
		addMedicalCase = (Button) findViewById(R.id.addMedicalCase);
		addMedicalCase.setOnTouchListener(this);
		// tp.setFakeBoldText(true);
	}

	private void initListFoot() {
		// 实例化底部布局
		moreView = getLayoutInflater().inflate(R.layout.medicallist_foot, null);
		pg = (ProgressBar) moreView.findViewById(R.id.pg);
		morebutton = (Button) moreView.findViewById(R.id.morebutton);
		expandListView.addFooterView(moreView);
		handler = new Handler();
		pg.setVisibility(View.GONE);
		morebutton.setOnClickListener(this);
	}

	private void initData() {
		pageno=0;
		String limit=pageno*rowsize+","+rowsize;
		PatientInfoDao dao = new PatientInfoDao(this);
		List expandDatas = dao.find(null, null, null, null, null,
				" fd_update_time desc", limit);
		adapter = new ExpandableAdapter(this, expandDatas);
		pageno++;
		if(expandDatas.size()<rowsize){
			expandListView.setAdapter(adapter);
			if(expandListView.findViewById(R.id.morebutton)!=null&&expandListView.findViewById(R.id.morebutton).getVisibility()==View.VISIBLE){
				expandListView.removeFooterView(moreView);
			}
		}else{
			if(expandListView.findViewById(R.id.morebutton)==null){
				expandListView.addFooterView(moreView);
				morebutton.setVisibility(View.VISIBLE);
			}	
			expandListView.setAdapter(adapter);
		}
		
		List totalDatas = dao.find(null, null, null, null, null, null, null);
		String patientCountStr = totalDatas.size() + "个";
		patientCount.setText(patientCountStr);

		MedicalCaseDao medicalCaseDao = new MedicalCaseDao(this);
		List medicals = medicalCaseDao.find();
		String medicalCountStr = medicals.size() + "份";
		medicalCount.setText(medicalCountStr);

		UserInfoDao userInfoDao = new UserInfoDao(this);
		List userInfoList = userInfoDao.find();
		if (!userInfoList.isEmpty()) {
			UserInfo userInfo = (UserInfo) userInfoList.get(0);
			Bitmap headBitmap = Bytes2Bimap(userInfo.getFdHeadPic());
			if (null != headBitmap)
				homeHeadPic.setImageBitmap(headBitmap);
		}
		if (!HealthyApplication.getInstance().isVIP()) {
			TextView title = (TextView) findViewById(R.id.title);
			title.setText("健康簿（试用版）");
		}
	}

	public Bitmap Bytes2Bimap(byte[] b) {
		if (null != b && b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.addMedicalCase:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				addMedicalCase
						.setBackgroundResource(R.drawable.add_button_select);
				break;
			case MotionEvent.ACTION_UP:
				addMedicalCase
						.setBackgroundResource(R.drawable.add_button_normal);
				Intent it = new Intent(this, MedicalCaseAddActivity.class);
				startActivity(it);
				break;
			}
			break;
		case R.id.homeAddBtn:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				homeAddBtn.setBackgroundResource(R.drawable.add_button_select);
				break;
			case MotionEvent.ACTION_UP:
				homeAddBtn.setBackgroundResource(R.drawable.add_button_normal);
				Intent patientAddActivity = new Intent(this,
						PatientAddActivity.class);
				startActivity(patientAddActivity);
				break;
			}
			break;
		}

		return true;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.homeHeadPic:
			Intent userInfo = new Intent(this, UserInfoActivity.class);
			startActivity(userInfo);
			break;
		case R.id.patientCount:
			// 发送广播
			Intent patiendIntent = new Intent("patient");
			sendBroadcast(patiendIntent);
			break;
		case R.id.medicalCount:
			// 发送广播
			Intent medicalIntent = new Intent("medical");
			sendBroadcast(medicalIntent);
			break;
		case R.id.morebutton:
			morebutton.setVisibility(View.GONE);
			pg.setVisibility(View.VISIBLE);
			handler.postDelayed(new Runnable() {
				public void run() {
					int newDataNum = loadMoreDate();
					if (newDataNum == rowsize) {
						morebutton.setVisibility(View.VISIBLE);
					}
					pg.setVisibility(View.GONE);

				}
			}, 1000); // 延迟1秒执行runnable
			break;
		}
	}

	private int loadMoreDate() {
		PatientInfoDao patientInfoDao = new PatientInfoDao(this);
		String limit = pageno * rowsize + "," + rowsize;
		List moreDatas;
		moreDatas = patientInfoDao.find(null, null, null, null, null,
					"fd_update_time desc", limit);
		adapter.getDatas().addAll(moreDatas);
		adapter.notifyDataSetChanged();
		pageno++;
		if (moreDatas.size() < rowsize)
			expandListView.removeFooterView(moreView);
		return moreDatas.size();
	}

	public boolean onItemLongClick(AdapterView adapterView, final View view,
			final int index, long arg3) {

		expandListView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
						int type = ExpandableListView
								.getPackedPositionType(info.packedPosition);
						if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
							patientSelected = (PatientInfo) adapter
									.getGroup((Integer) view
											.getTag(R.id.expand_view_tagId));
							menu.add(0, 0, 0, "编辑");
							menu.add(0, 1, 0, "删除");
							menu.add(0, 2, 0, "添加病历");
							menu.add(0, 3, 0, "导出");
							menu.add(0, 4, 0, "收藏");
						}
					}
				});
		return false;
	}

	// 长按菜单响应函数
	public boolean onContextItemSelected(MenuItem item) {
		PatientInfoDao dao = new PatientInfoDao(this);
		Bundle bundle = null;
		Intent it = null;
		switch (item.getItemId()) {
		case 0:
			bundle = new Bundle();
			it = new Intent(this, PatientAddActivity.class);
			bundle.putSerializable("model", patientSelected);
			it.putExtras(bundle);
			startActivity(it);
			break;
		case 1:
			dialog(patientSelected);
			break;
		case 2:
			bundle = new Bundle();
			it = new Intent(this, MedicalCaseAddActivity.class);
			bundle.putSerializable("patientInfo", patientSelected);
			it.putExtras(bundle);
			startActivity(it);
			break;
		case 3:
			// 导出
			break;
		case 4:
			patientSelected.setFdIsFavor(1);
			dao.update(patientSelected);
			Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);
	}

	public boolean onChildClick(ExpandableListView expandableListView,
			View view, int groupPosition, int childPosition, long id) {
		MedicalCase medicalCase = (MedicalCase) adapter.getChild(groupPosition,
				childPosition);
		Bundle bundle = new Bundle();
		Intent it = new Intent(this, MedicalCaseAddActivity.class);
		bundle.putSerializable("medicalCase", medicalCase);
		it.putExtras(bundle);
		startActivity(it);
		return false;
	}

	private void dialog(final PatientInfo patientInfo) {
		final PatientInfoDao dao = new PatientInfoDao(this);
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("你确定删除该患者信息?");
		builder.setTitle("提示");
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						dao.execSQL("delete from table_medical_case where fd_patient_id='"
								+ patientInfo.getFdId() + "'");
						dao.delete(patientInfo.getFdId());
						adapter.delete(patientInfo);
						adapter.notifyDataSetChanged();
						Toast.makeText(HomeActivity.this, "删除成功",
								Toast.LENGTH_SHORT).show();
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
}
