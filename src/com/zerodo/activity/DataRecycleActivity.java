package com.zerodo.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.adapter.BackupListApapter;
import com.zerodo.backup.BackupTask;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.db.dao.MedicalCaseDao;
import com.zerodo.db.dao.PatientInfoDao;

public class DataRecycleActivity extends CommonActivity implements
		OnTouchListener, OnItemClickListener, OnItemLongClickListener {
	private Button btnBack, backup;
	private ListView backupListView;
	private BackupListApapter backupListApapter;
	private List<File> datas;
	private File fileSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_datarecycle);
		initUI();
		initData();
		registerBoradcastReceiver();
	}

	private void initUI() {
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnTouchListener(this);
		backup = (Button) findViewById(R.id.backup);
		backup.setOnTouchListener(this);
		backupListView = (ListView) findViewById(R.id.backupListView);
		backupListView.setOnItemClickListener(this);
		backupListView.setOnItemLongClickListener(this);

	}

	private void initData() {
		datas = backupFiles();
		backupListApapter = new BackupListApapter(this, datas);
		backupListView.setAdapter(backupListApapter);
	}

    public void registerBoradcastReceiver(){ 
        IntentFilter myIntentFilter = new IntentFilter(); 
        myIntentFilter.addAction("backup"); 
        //注册广播       
        registerReceiver(mBroadcastReceiver, myIntentFilter); 
    } 
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){ 
        @Override 
        public void onReceive(Context context, Intent intent) { 
            String action = intent.getAction(); 
            if(action.equals("backup")){ 
				datas = backupFiles();
				backupListApapter.setDatas(datas);
				backupListApapter.notifyDataSetChanged();
            } 
        }

         
    };
	private List backupFiles() {
		List fileList =new ArrayList();
		File zipDir = new File(Environment.getExternalStorageDirectory(),
				"dohealthy");
		if (zipDir.exists()) {
			File[] files = zipDir.listFiles();
			for (File file : files) {
				if (file.getName().indexOf(".backup") >= 0) {
					fileList.add(file);
				}
			}
		}
		fileList = fileSort(fileList);
		return fileList;
	}

	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.btnBack:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				unregisterReceiver(mBroadcastReceiver);
				this.finish();
				break;
			}
			break;
		case R.id.backup:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				backup.setBackgroundResource(R.drawable.add_button_select);
				break;
			case MotionEvent.ACTION_UP:
				backup.setBackgroundResource(R.drawable.add_button_normal);
				dialog("backup", null);
				break;
			}
			break;
		}
		return true;
	}

	private void dialog(final String param, final String fileName) {
		AlertDialog.Builder builder = new Builder(this);
		if ("backup".equals(param))
			builder.setMessage("你确定进行数据备份?");
		else
			builder.setMessage("你确定进行数据还原?");
		builder.setTitle("提示");
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						new BackupTask(DataRecycleActivity.this, fileName)
								.execute(param);
						dialog.dismiss();
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

	public boolean onItemLongClick(AdapterView<?> arg0, View view, int index,
			long arg3) {
		fileSelected = backupListApapter.getDatas().get(index);
		backupListView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.add(0, 0, 0, "还原");
						menu.add(0, 1, 0, "删除");
					}

					public boolean onContextItemSelected(MenuItem item) {
						return false;

					}
				});
		return false;
	}

	public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
		fileSelected = backupListApapter.getDatas().get(index);
		backupListView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.add(0, 0, 0, "还原");
						menu.add(0, 1, 0, "删除");
					}

					public boolean onContextItemSelected(MenuItem item) {
						return false;
					}
				});
	}

	// 长按菜单响应函数
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			dialog("restroe", fileSelected.getName());
			break;
		case 1:
			backupListApapter.delete(fileSelected);
			backupListApapter.notifyDataSetChanged();
			fileSelected.delete();
			Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);
	}

	private List fileSort(List files) {
		ComparatorFile comparator = new ComparatorFile();
		Collections.sort(files, comparator);
		return files;
	}

	class ComparatorFile implements Comparator {

		public int compare(Object arg0, Object arg1) {
			File file1 = (File) arg0;
			File file2 = (File) arg1;
			int flag;
			if (file1.lastModified() > file2.lastModified()) {
				flag = -1;
			} else {
				flag = 1;
			}
			return flag;
		}

	}
}
