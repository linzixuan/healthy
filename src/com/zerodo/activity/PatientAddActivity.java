package com.zerodo.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.adapter.PatientMedicalListApapter;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.util.StringUtil;
import com.zerodo.db.dao.MedicalCaseDao;
import com.zerodo.db.dao.PatientInfoDao;
import com.zerodo.db.model.MedicalCase;
import com.zerodo.db.model.PatientInfo;
import com.zerodo.db.model.Titles;

public class PatientAddActivity extends CommonActivity implements OnTouchListener,OnItemSelectedListener,OnClickListener,OnItemLongClickListener,OnItemClickListener{

	private Button btnBack,btnSave,addMedical,addAgain;
	private PatientInfo patientInfo;
	private Spinner patientSexySpinner;
	private TextView patientName,patientPhone,patientAge,patientBirth,patientHelthyNo,emergencyPersonh,emergencyPhone,patientAddress,patientRemark;
	private ArrayAdapter sexyAdapter;
	private String sexySelected;
	private String []sexyDatas={"男","女"};
	private ImageView headView;
	private Bitmap headBitmap;
	private byte[] imgContent;
	private int mYear,mMonth,mDay;
	private PatientMedicalListApapter patientMedicalListApapter;
	private ListView medicalListView;
	private MedicalCase medicalCase;
	private LinearLayout medicalListLinear;
	private final static int TRIALPATIENTCOUNT=10;//使用版本患者数量限制
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_patientadd);
		initUI();
		initDataOnCreate();
	}
	@Override
	protected void onResume() {
		super.onResume();
		initDataOnResume();
	}
	private void initUI(){
		btnBack=(Button) findViewById(R.id.patientBack);
		btnBack.setOnTouchListener(this);
		btnSave=(Button) findViewById(R.id.patientSave);
		btnSave.setOnTouchListener(this);
		addMedical=(Button) findViewById(R.id.addMedical);
		addMedical.setOnTouchListener(this);
		addAgain=(Button) findViewById(R.id.addAgain);
		addAgain.setOnTouchListener(this);
		patientSexySpinner=(Spinner) findViewById(R.id.patientSexySpinner);
		patientName=(TextView) findViewById(R.id.patientName);
		patientPhone=(TextView)findViewById(R.id.patientPhone);
		patientPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
		patientAge=(TextView) findViewById(R.id.patientAge);
		patientAge.setInputType(InputType.TYPE_CLASS_NUMBER);
		patientBirth=(TextView)findViewById(R.id.patientBirth);
		patientHelthyNo=(TextView)findViewById(R.id.patientHelthyNo);
		emergencyPersonh=(TextView)findViewById(R.id.emergencyPersonh);
		emergencyPhone=(TextView)findViewById(R.id.emergencyPhone);
		patientAddress=(TextView)findViewById(R.id.patientAddress);
		patientRemark=(TextView)findViewById(R.id.patientRemark);
		headView = (ImageView) findViewById(R.id.headPic);
		headView.setOnClickListener(this);
		medicalListView=(ListView) findViewById(R.id.medicalListView);
		medicalListView.setOnItemLongClickListener(this);
		medicalListView.setOnItemClickListener(this);
		medicalListLinear=(LinearLayout) findViewById(R.id.medicalListLinear);
		patientBirth.setOnClickListener(new OnClickListener(){  
			public void onClick(View v) { 
				hideIM(v); 
				showDialog(0);  
			}  
			});  
		patientBirth.setOnFocusChangeListener(new OnFocusChangeListener() {  
			public void onFocusChange(View v, boolean hasFocus) {  
				if (hasFocus == true) {  
					hideIM(v);  
					showDialog(0);    
				}  
			}  
		}); 

	}
	
	
	private void initDataOnCreate(){
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
		String nowDate=dateformat.format(new Date());
		//patientBirth.setText(nowDate);
		try{
			String[] dateArray=nowDate.split("-");
			mYear=Integer.valueOf(dateArray[0]);
			mMonth=Integer.valueOf(dateArray[1])-1;
			mDay=Integer.valueOf(dateArray[2]);			
		}catch(Exception e){
			e.printStackTrace();
		}

		Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        int sexyPosition=-1;
        if(null!=bundle){
        	patientInfo= (PatientInfo)bundle.get("model");
            if(null!=patientInfo){
            	convertModelToForm();
            	for(int i=0;i<sexyDatas.length;i++){
            		if(null==patientInfo.getFdSexy()){
            			sexyPosition=0;
            		}else{
                		if(sexyDatas[i].endsWith(patientInfo.getFdSexy()))
                			sexyPosition=i;          			
            		}

            	}
            }
            	      	
        }
		sexyAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sexyDatas); 
		sexyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		patientSexySpinner.setAdapter(sexyAdapter);  
		patientSexySpinner.setOnItemSelectedListener(this);  
		patientSexySpinner.setVisibility(View.VISIBLE);  
		if(sexyPosition>-1)
			patientSexySpinner.setSelection(sexyPosition);
		
		
	}
	
	private void initDataOnResume(){
		//病历列表
		if(null!=patientInfo){
			MedicalCaseDao medicalCaseDao =new MedicalCaseDao(this);
			String[]selectionArgs={String.valueOf(patientInfo.getFdId())};
			List datas=medicalCaseDao.find(null, "fd_patient_id=?", selectionArgs, null, null, "fd_date desc", null);
			patientMedicalListApapter=new PatientMedicalListApapter(this,datas);
			medicalListView.setAdapter(patientMedicalListApapter);	
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)medicalListLinear.getLayoutParams();
			linearParams.height=70*(datas.size());
			medicalListLinear.setLayoutParams(linearParams);
		}	
	}
	
	public boolean onItemLongClick(AdapterView adapterView, View view, int index,
			long arg3) {
			medicalCase=(MedicalCase)patientMedicalListApapter.getDatas().get(index);
			medicalListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {                                        
	        	public void onCreateContextMenu(ContextMenu menu, View v,                                                       
	        			ContextMenuInfo menuInfo) {                                               
	        		menu.add(0, 0, 0, "编辑");                                               
	        		menu.add(0, 1, 0, "删除");                                                
	        		menu.add(0, 2, 0, "导出");  
	        		menu.add(0, 3, 0, "收藏"); 
	        		}                               
	        	});	
			return false;
	}
	
	public void onItemClick(AdapterView adapterView, View view, int index, long arg3) {
		Bundle bundle = new Bundle();
		medicalCase=(MedicalCase)patientMedicalListApapter.getDatas().get(index);
		Intent it=new Intent(this,MedicalCaseAddActivity.class);
		bundle.putSerializable("medicalCase",medicalCase);
		it.putExtras(bundle);
		startActivity(it);
	}
	// 长按菜单响应函数        
	public boolean onContextItemSelected(MenuItem item) { 
		MedicalCaseDao dao=new MedicalCaseDao(this);
		switch (item.getItemId()) {               
			case 0:                       
				Bundle bundle = new Bundle();
				Intent it=new Intent(this,MedicalCaseAddActivity.class);
				bundle.putSerializable("medicalCase",medicalCase);
				it.putExtras(bundle);
				startActivity(it);
			break;                 
			case 1:  
				dialog(medicalCase);
				break;                 
			case 2:                        
				// 导出                     
				break; 
			case 3:                        
				medicalCase.setFdIsFavor(1);
				dao.update(medicalCase);
				Toast.makeText(this,"收藏成功", Toast.LENGTH_SHORT).show();
				break;                
			}                 
		return super.onContextItemSelected(item); 
	}
	
	private void dialog(final MedicalCase medicalCase) { 
		 final MedicalCaseDao dao=new MedicalCaseDao(this);
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("你确定删除该病历?"); 
		 builder.setTitle("提示"); 
		 builder.setPositiveButton("确定", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
					dao.delete(medicalCase.getFdId());  
					patientMedicalListApapter.delete(medicalCase);
					patientMedicalListApapter.notifyDataSetChanged();
					Toast.makeText(PatientAddActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
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
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                String mm;
                String dd;
                if(monthOfYear<=9)
                {
                	mMonth = monthOfYear+1;
                	mm="0"+mMonth;
                }
                else{
                	mMonth = monthOfYear+1;
                	mm=String.valueOf(mMonth);
                	}
                if(dayOfMonth<=9)
                {
                	mDay = dayOfMonth;
                	dd="0"+mDay;
                }
                else{
                	mDay = dayOfMonth;
                	dd=String.valueOf(mDay);
                	}
                mDay = dayOfMonth;
                patientBirth.setText(String.valueOf(mYear)+"-"+mm+"-"+dd);
                patientAge.setText(getAge(mYear, mMonth, mDay));
            }			
        };
        
    protected String getAge(int birthYear, int birthMonth,int birthDay ){
    	Calendar birthday = new GregorianCalendar(birthYear,birthMonth, birthDay);
        Calendar now = Calendar.getInstance();   
        int day = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);   
        int month = now.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);   
        int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);   
        //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。   
        if(day<0){   
            month -= 1;   
            now.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。   
            day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);   
        }   
        if(month<0){   
            month = (month+12)%12;   
            year--;   
        }   
        return String.valueOf(year);	
    }    
	protected Dialog onCreateDialog(int id) {
	    return new DatePickerDialog(this,
	    		mDateSetListener,
	            mYear, mMonth, mDay);
	}

	// 隐藏手机键盘
	private void hideIM(View edt){
	    try {
	         InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
	         IBinder  windowToken = edt.getWindowToken();
	         if(windowToken != null) {
	             im.hideSoftInputFromWindow(windowToken, 0);
	         }
	     } catch (Exception e) {
	       
	     }
	 }	
	public void onClick(View arg0) {
		final CharSequence[] items = { "相册", "拍照" };
		AlertDialog dlg = new AlertDialog.Builder(PatientAddActivity.this)
				.setTitle("选择图片")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 这里item是根据选择的方式，
						// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
						if (item == 1) {
							Intent getImageByCamera = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							startActivityForResult(getImageByCamera, 1);
						} else {
							Intent getImage = new Intent(
									Intent.ACTION_GET_CONTENT);
							getImage.addCategory(Intent.CATEGORY_OPENABLE);
							getImage.setType("image/jpeg");
							startActivityForResult(getImage, 0);
						}
					}
				}).create();
		dlg.show();
	}
	//修改图片大小
	public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
         intent.setDataAndType(uri, "image/*");
         // 设置裁剪
         intent.putExtra("crop", "true");
         // aspectX aspectY 是宽高的比例
         intent.putExtra("aspectX", 1);
         intent.putExtra("aspectY", 1);
         // outputX outputY 是裁剪图片宽高
         intent.putExtra("outputX", 320);
         intent.putExtra("outputY", 320);
         intent.putExtra("return-data", true);
         startActivityForResult(intent, 2);
 }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getContentResolver();
		/**
		 * 因为两种方式都用到了startActivityForResult方法， 这个方法执行完后都会执行onActivityResult方法，
		 * 所以为了区别到底选择了那个方式获取图片要进行判断，
		 * 这里的requestCode跟startActivityForResult里面第二个参数对应
		 */
		if (resultCode != RESULT_CANCELED) {
			if (requestCode == 0) {
				try {
					// 获得图片的uri
					Uri originalUri = data.getData();
					startPhotoZoom(originalUri);
//					// 将图片内容解析成字节数组
//					imgContent = readStream(resolver.openInputStream(Uri
//							.parse(originalUri.toString())));
//					// 将字节数组转换为ImageView可调用的Bitmap对象
//					headBitmap = getPicFromBytes(imgContent, null);
//					// //把得到的图片绑定在控件上显示
//					headView.setImageBitmap(headBitmap);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else if (requestCode == 1) {
				try {
					Uri originalUri = data.getData();
					if(originalUri!=null){
						startPhotoZoom(originalUri);
					}else{
						Bundle bundle = data.getExtras();  
						if (bundle != null) { 
							Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), (Bitmap) bundle.get("data"), null,null));
							bundle.remove("data");
						}  
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 把得到的图片绑定在控件上显示
				//headView.setImageBitmap(headBitmap);
			}else if(requestCode == 2){
				if (data != null) {
					headBitmap = (Bitmap) data.getExtras().get("data");
					headView.setImageBitmap(headBitmap);
				}
			}
		}
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}
	public Bitmap Bytes2Bimap(byte[] b) {
        if (null!=b&&b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
	
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.patientBack:
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
		case R.id.patientSave:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				PatientInfoDao patientInfoDao=new PatientInfoDao(this);
				if(null==patientInfo){
					if(!checkForm())break;
					if(!checkIsVIP())break;
					patientInfo=new PatientInfo();
					convertFormToModel();
					if(findExistsPatient())break;
					patientInfoDao.insert(patientInfo);
				}else{
					convertFormToModel();
					if(!checkForm())break;
					if(findExistsPatient())break;
					patientInfoDao.update(patientInfo);
				}
				Toast.makeText(this,"保存成功", Toast.LENGTH_SHORT).show();
				this.finish();
				break;
			}
			break;
		case R.id.addMedical:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				addMedical.setBackgroundResource(R.drawable.add_button_select);
				break;
			case MotionEvent.ACTION_UP:
				addMedical.setBackgroundResource(R.drawable.add_button_normal);
				PatientInfoDao patientInfoDao=new PatientInfoDao(this);
				if(null==patientInfo){
					if(!checkForm())break;
					if(!checkIsVIP())break;
					if(findExistsPatient())break;
					patientInfo=new PatientInfo();
					convertFormToModel();
					long row=patientInfoDao.insert(patientInfo);
					int id=new Long(row).intValue();
					patientInfo=patientInfoDao.get(id);
					
				}else{
					convertFormToModel();
					if(!checkForm())break;
					patientInfoDao.update(patientInfo);
				}
				Toast.makeText(this,"保存成功", Toast.LENGTH_SHORT).show();
				Bundle bundle = new Bundle();
				Intent it=new Intent(this,MedicalCaseAddActivity.class);
				bundle.putSerializable("patientInfo",patientInfo);
				it.putExtras(bundle);
				startActivity(it);
				break;
			}
			break;
		case R.id.addAgain:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				addAgain.setBackgroundResource(R.drawable.add_button_select);
				break;
			case MotionEvent.ACTION_UP:
				addAgain.setBackgroundResource(R.drawable.add_button_normal);
				PatientInfoDao patientInfoDao=new PatientInfoDao(this);
				if(null==patientInfo){
					if(!checkForm())break;
					if(!checkIsVIP())break;
					if(findExistsPatient())break;
					patientInfo=new PatientInfo();
					convertFormToModel();
					patientInfoDao.insert(patientInfo);
				}else{
					convertFormToModel();
					patientInfoDao.update(patientInfo);
				}
				Toast.makeText(this,"保存成功", Toast.LENGTH_SHORT).show();
				resetForm();
				break;
			}
			break;
		}
		return true;
	}
	
	private void convertFormToModel(){
		patientInfo.setFdName(patientName.getText().toString());
		patientInfo.setFdPhone(patientPhone.getText().toString());
		if(StringUtil.isNotNull(patientAge.getText().toString())&&patientAge.getText().toString().matches("[0-9]*"))
			patientInfo.setFdAge(Integer.valueOf(patientAge.getText().toString()));
		patientInfo.setFdBirthDay(patientBirth.getText().toString());
		patientInfo.setFdHealthyNo(patientHelthyNo.getText().toString());
		patientInfo.setFdEmergencyPerson(emergencyPersonh.getText().toString());
		patientInfo.setFdEmergencyPhone(emergencyPhone.getText().toString());
		patientInfo.setFdAddress(patientAddress.getText().toString());
		patientInfo.setFdRemark(patientRemark.getText().toString());
		patientInfo.setFdSexy(sexySelected);
		headBitmap=((BitmapDrawable)headView.getDrawable()).getBitmap();   
		//int size=headBitmap.getWidth()*headBitmap.getHeight()*4; 
		ByteArrayOutputStream baos=new ByteArrayOutputStream(); 
		headBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		imgContent = baos.toByteArray();
		patientInfo.setFdHeadPic(imgContent);
	}
	private void convertModelToForm(){
		patientName.setText(patientInfo.getFdName());
		patientPhone.setText(patientInfo.getFdPhone());
		patientAge.setText(String.valueOf(patientInfo.getFdAge()));
		patientBirth.setText(patientInfo.getFdBirthDay());
		patientHelthyNo.setText(patientInfo.getFdHealthyNo());
		emergencyPersonh.setText(patientInfo.getFdEmergencyPerson());
		emergencyPhone.setText(patientInfo.getFdEmergencyPhone());
		patientAddress.setText(patientInfo.getFdAddress());
		patientRemark.setText(patientInfo.getFdRemark());
		patientName.setText(patientInfo.getFdName());
		headBitmap=Bytes2Bimap(patientInfo.getFdHeadPic());
		if(null!=headBitmap)
			headView.setImageBitmap(headBitmap);
		
	}
	private void resetForm(){
		patientInfo=null;
		patientName.setText(null);
		patientPhone.setText(null);
		patientAge.setText(null);
		patientBirth.setText(null);
		patientHelthyNo.setText(null);
		emergencyPersonh.setText(null);
		emergencyPhone.setText(null);
		patientAddress.setText(null);
		patientRemark.setText(null);
		patientName.setText(null);
		patientSexySpinner.setSelection(0);
		Bitmap bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.default_head_pic);
		headView.setImageBitmap(bmp);
		if(null!=patientMedicalListApapter){
			patientMedicalListApapter.setDatas(new ArrayList());
			patientMedicalListApapter.notifyDataSetChanged();			
		}
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)medicalListLinear.getLayoutParams();
		linearParams.height=0;
		medicalListLinear.setLayoutParams(linearParams);
		
	}
	private boolean checkForm(){
		if(StringUtil.isNull(patientName.getText().toString())){
			Toast.makeText(this,"患者姓名不能为空！请您输入患者姓名。", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(StringUtil.isNull(patientPhone.getText().toString())){
			Toast.makeText(this,"联系电话不能为空！请您输入联系电话。", Toast.LENGTH_SHORT).show();
			return false;
		}
//		if(StringUtil.isNull(patientInfo.getFdBirthDay())){
//			Toast.makeText(this,"出生日期不能为空！请您输入出生日期。", Toast.LENGTH_SHORT).show();
//			rtnValue=false;
//		}
		if(!patientAge.getText().toString().matches("[0-9]*")){
			Toast.makeText(this,"年龄必须为整数！请您重新输入年龄。", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private boolean findExistsPatient(){
		boolean rtnValue=false;
		PatientInfoDao patientInfoDao=new PatientInfoDao(this);
		String whereBlock;
		if(null!=patientInfo){
			whereBlock="fd_name='"+patientName.getText()+"' and fd_phone='"+patientPhone.getText()+"' and fd_id!='"+patientInfo.getFdId()+"'";
		}else{
			whereBlock="fd_name='"+patientName.getText()+"' and fd_phone='"+patientPhone.getText()+"'";
		}
		List existsPatient=patientInfoDao.find(null,whereBlock , null, null, null, null, null);
		if(!existsPatient.isEmpty()){
			Toast.makeText(this,"患者信息已存在，请勿重复录入！", Toast.LENGTH_SHORT).show();
			rtnValue=true;
		}
		return rtnValue;
	}

	public void onItemSelected(AdapterView adapter, View view, int arg2n,
			long arg3) {
		int index=adapter.getSelectedItemPosition();
		sexySelected=sexyDatas[index];
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean checkIsVIP(){
		boolean rtnValue=true;
		if(!HealthyApplication.getInstance().isVIP()){
			PatientInfoDao dao=new PatientInfoDao(this);
			List patientList=dao.find();
			if(patientList.size()>=TRIALPATIENTCOUNT){
				Toast.makeText(this,"抱歉！患者数量已经超出试用版限制，如需继续使用请您通过“设置->授权”取得捷度官方正式授权。", Toast.LENGTH_SHORT).show();
				rtnValue=false;
			}
		}
		return rtnValue;
	}
}
