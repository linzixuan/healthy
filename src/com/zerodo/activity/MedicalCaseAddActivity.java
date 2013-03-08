package com.zerodo.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.util.SDFileUtil;
import com.zerodo.base.util.StringUtil;
import com.zerodo.db.dao.AttachmentDao;
import com.zerodo.db.dao.MedicalCaseDao;
import com.zerodo.db.dao.PatientInfoDao;
import com.zerodo.db.model.Attachment;
import com.zerodo.db.model.MedicalCase;
import com.zerodo.db.model.PatientInfo;

public class MedicalCaseAddActivity extends CommonActivity implements OnTouchListener,OnItemSelectedListener {
	
	
	private Button btnBack,btnSave,addAgain,addAttachment;
	private Spinner patientSpinner;
	private ArrayAdapter patientAdapter;
	private PatientInfo patientInfoSelected;
	private String[] patientDatas,diseaseDatas;
	private List patientList,diseaseList;
	private MedicalCase medicalCase;
	private TextView patientPhone,fdDate,fdFeatures,fdAdvice,fdRemark,fdArea,fdWard,fdBed,otherRemark,fdDisease;
	private int mYear,mMonth,mDay;
	private ImageView headPic,patientAddBtn,selectDisease;
	private boolean newCreate=true;
	//��̬�������
	private final static String MODELNAME="com.zerodo.db.model.MedicalCase";
	private LinearLayout attachmentLayouts;
	private int attachmentCount=0;
	private ImageView attachmentPicSelected;
	private final static int TRIALCASETCOUNT=5;//ʹ�ð汾������������
	
	
	private Builder builder;
	private Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_medicalcaseadd);
		initUI();
		initData();
		displayAttachment();
		try {                         
			Class.forName("android.os.AsyncTask");                 
		}catch (ClassNotFoundException e) {
			e.printStackTrace();                 
		}
	}

	private void initUI(){
		btnBack=(Button) findViewById(R.id.medicalCaseBack);
		btnBack.setOnTouchListener(this);
		btnSave=(Button) findViewById(R.id.medicalCaseSave);
		btnSave.setOnTouchListener(this);
		patientAddBtn=(ImageView) findViewById(R.id.patientAddBtn);
		patientAddBtn.setOnTouchListener(this);
		addAgain=(Button) findViewById(R.id.addAgain);
		addAgain.setOnTouchListener(this);
		addAttachment=(Button) findViewById(R.id.addAttachment);
		addAttachment.setOnTouchListener(this);
		patientSpinner=(Spinner) findViewById(R.id.patientSpinner);
		patientPhone=(TextView) findViewById(R.id.patientPhone);
		fdDate=(TextView) findViewById(R.id.fdDate);
		fdFeatures=(TextView) findViewById(R.id.fdFeatures);
		fdAdvice=(TextView) findViewById(R.id.fdAdvice);
		fdRemark=(TextView) findViewById(R.id.fdRemark);
		fdArea=(TextView) findViewById(R.id.fdArea);
		fdWard=(TextView) findViewById(R.id.fdWard);
		fdBed=(TextView) findViewById(R.id.fdBed);
		fdDisease=(TextView) findViewById(R.id.fdDisease);
		otherRemark=(TextView) findViewById(R.id.otherRemark);
		headPic=(ImageView) findViewById(R.id.headPic);
		selectDisease=(ImageView) findViewById(R.id.selectDisease);
		selectDisease.setOnTouchListener(this);
		attachmentLayouts=(LinearLayout) findViewById(R.id.attachment);
		fdDate.setOnClickListener(new OnClickListener(){  
			public void onClick(View v) { 
				hideIM(v); 
				showDialog(0);  
			}  
			});  
		fdDate.setOnFocusChangeListener(new OnFocusChangeListener() {  
			public void onFocusChange(View v, boolean hasFocus) {  
				if (hasFocus == true) {  
					hideIM(v);  
					showDialog(0);    
				}  
			}  
		});  

		fdFeatures.setOnClickListener(new OnClickListener(){  
			public void onClick(View v) { 
				hideIM(v); 
				Bundle bundle=new Bundle();
				bundle.putString("fdDisease",fdDisease.getText().toString());
				bundle.putString("fdFeatures",fdFeatures.getText().toString());
				Intent it=new Intent(MedicalCaseAddActivity.this,FeaturesInputActivity.class);
				it.putExtras(bundle);
				startActivityForResult(it, 1);
			}  
			});
		fdFeatures.setOnFocusChangeListener(new OnFocusChangeListener() {  
			public void onFocusChange(View v, boolean hasFocus) { 
				if (hasFocus == true) {  
					hideIM(v); 
					Bundle bundle=new Bundle();
					String diseaseID;
					bundle.putString("fdDisease",fdDisease.getText().toString());
					bundle.putString("fdFeatures",fdFeatures.getText().toString());
					Intent it=new Intent(MedicalCaseAddActivity.this,FeaturesInputActivity.class);
					it.putExtras(bundle);
					startActivityForResult(it, 1);  
				}  
			}  
		});
		fdAdvice.setOnClickListener(new OnClickListener(){  
			public void onClick(View v) { 
				hideIM(v); 
				Bundle bundle=new Bundle();
				bundle.putString("fdDisease",fdDisease.getText().toString());
				bundle.putString("fdAdvice",fdAdvice.getText().toString());
				Intent it=new Intent(MedicalCaseAddActivity.this,AdviceInputActivity.class);
				it.putExtras(bundle);
				startActivityForResult(it, 2);
			}  
			});
		fdAdvice.setOnFocusChangeListener(new OnFocusChangeListener() {  
			public void onFocusChange(View v, boolean hasFocus) {  
				if (hasFocus == true) {  
					hideIM(v); 
					Bundle bundle=new Bundle();
					bundle.putString("fdDisease",fdDisease.getText().toString());
					bundle.putString("fdAdvice",fdAdvice.getText().toString());
					Intent it=new Intent(MedicalCaseAddActivity.this,AdviceInputActivity.class);
					it.putExtras(bundle);
					startActivityForResult(it, 2);  
				}  
			}  
		});
	}
	
	
	private void initData(){
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
		String nowDate=dateformat.format(new Date());
		fdDate.setText(nowDate);
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
        PatientInfo patientExtras = null;
        if(null!=bundle){
        	medicalCase= (MedicalCase)bundle.get("medicalCase");
        	patientExtras=(PatientInfo)bundle.get("patientInfo");
            if(null!=medicalCase){
            	convertModelToForm();
            	newCreate=false;
            }else
            	medicalCase=new MedicalCase();
        }else{
        	medicalCase=new MedicalCase();
        }
        //����
        int defaultPosition=-1;
        PatientInfoDao patientInfoDao=new PatientInfoDao(this);
		patientList=patientInfoDao.find();
		patientDatas=new String[patientList.size()];
		for(int i=0;i<patientList.size();i++){
			PatientInfo patientInfo=(PatientInfo)patientList.get(i);
			patientDatas[i]=patientInfo.getFdName();
			if(null!=medicalCase&&patientInfo.getFdId()==medicalCase.getFdPaitentId())
				defaultPosition=i;
			else if(null!=patientExtras&&patientInfo.getFdId()==patientExtras.getFdId())
				defaultPosition=i;
		}
        patientAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, patientDatas); 
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        patientSpinner.setAdapter(patientAdapter);  
        patientSpinner.setOnItemSelectedListener(this);  
        patientSpinner.setVisibility(View.VISIBLE);  
		if(defaultPosition>-1)
			patientSpinner.setSelection(defaultPosition);	
	}
	private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                String mm;
                String dd;
                if(monthOfYear<=9){
                	mMonth = monthOfYear+1;
                	mm="0"+mMonth;
                }else{
                	mMonth = monthOfYear+1;
                	mm=String.valueOf(mMonth);
                	}
                if(dayOfMonth<=9){
                	mDay = dayOfMonth;
                	dd="0"+mDay;
                }else{
                	mDay = dayOfMonth;
                	dd=String.valueOf(mDay);
                	}
                mDay = dayOfMonth;
                fdDate.setText(String.valueOf(mYear)+"-"+mm+"-"+dd);
            }			
        };

	protected Dialog onCreateDialog(int id) {
	    return new DatePickerDialog(this,
	    		mDateSetListener,
	            mYear, mMonth, mDay);
	}

	// �����ֻ�����
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getContentResolver();
		if(null==data)
			return;
		if (requestCode == 1) {
			try {
				Bundle bundle=data.getExtras();
				if(null!=bundle){
					fdFeatures.setText(bundle.getString("features"));				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		if (requestCode == 2) {
			try {
				Bundle bundle=data.getExtras();
				if(null!=bundle){
					fdAdvice.setText(bundle.getString("advice"));				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		if (requestCode == 6) {
			try {
				Bundle bundle=data.getExtras();
				if(null!=bundle){
					fdDisease.setText(bundle.getString("fdDidease"));				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		if (resultCode != RESULT_CANCELED) {
			if (requestCode == 3) {
				try {
					Uri originalUri = data.getData();
	                //��ͼƬ���ݽ������ֽ����� 
	                byte[] mContent=readStream(resolver.openInputStream(Uri.parse(originalUri.toString()))); 
	                //���ֽ�����ת��ΪImageView�ɵ��õ�Bitmap���� 
	                Bitmap myBitmap = getPicFromBytes(mContent, null); 
	                myBitmap=compress(myBitmap);
	                attachmentPicSelected.setImageBitmap(myBitmap);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else if (requestCode == 4) {
				try {
					Bitmap myBitmap=null;
					Uri originalUri = data.getData();
					if(originalUri!=null){
						myBitmap=compressByPath(originalUri.getPath());
					}
					if(myBitmap==null){
						Bundle bundle = data.getExtras();  
						if (bundle != null) {  
							myBitmap = (Bitmap) bundle.get("data"); 
							myBitmap=compress(myBitmap);
						}  
					}
//					=BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
//	                byte[] mContent=readStream(resolver.openInputStream(Uri.parse(originalUri.toString()))); 
//	                Bitmap myBitmap = getPicFromBytes(mContent, null); 
	               // myBitmap=compress(myBitmap);
	                attachmentPicSelected.setImageBitmap(myBitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(requestCode == 5){
				if (data != null) {
					attachmentPicSelected.setImageBitmap((Bitmap) data.getExtras().get("data"));
				}
			}
		}
	}
	private Bitmap compressByPath(String path){
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // ��ȡ���ͼƬ�Ŀ�͸�
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);  //��ʱ��bitmapΪnull
        options.inJustDecodeBounds = false;
         //�������ű�
        int be = (int)(options.outHeight / (float)800);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        //���¶���ͼƬ��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false
        bitmap=BitmapFactory.decodeFile(path,options);     //��ʱ��bitmap��Ϊnull
        return bitmap;
	} 
	
	
	
	private Bitmap compress(Bitmap image) throws IOException {  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();          
	    image.compress(Bitmap.CompressFormat.PNG, 100, baos);
	    int be=(int)baos.toByteArray().length / (1024*1024);
	    if(be<1){
	    	return image;
	    }else{
	    	float a =(float)(1024*1024)/baos.toByteArray().length ;
			int b=(int) (a*100);
			baos.reset();//����baos�����baos
	    	image.compress(Bitmap.CompressFormat.PNG, b, baos);//����ѹ��50%����ѹ��������ݴ�ŵ�baos�� 
	    }
	    byte[] baosArray=baos.toByteArray();
	    ByteArrayInputStream isBm = new ByteArrayInputStream(baosArray);   
	    BitmapFactory.Options newOpts = new BitmapFactory.Options();   
	    //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��   
	    
	    int i = 0; 
        // ���������ʾ �����ɵ�ͼƬΪԭʼͼƬ�ļ���֮һ��    
    	newOpts.inSampleSize = be+1;  
        // ����֮ǰ����Ϊ��true������Ҫ��Ϊfalse������ʹ�������ͼƬ    
    	newOpts.inJustDecodeBounds = false;   
    	image = BitmapFactory.decodeStream(isBm, null, newOpts); 
	    return image;
	}   
	   public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) { 
	        if (bytes != null) 
	            if (opts != null) 
	                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts); 
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

	
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.medicalCaseBack:
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
		case R.id.medicalCaseSave:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				if(!checkForm())break;
				if(!checkIsVIP())break;
				new SaveTask(this).execute("save");
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
				if(!checkForm())break;
				if(!checkIsVIP())break;
				new SaveTask(this).execute("saveAdd");
				break;
			}
			break;
		case R.id.patientAddBtn:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				patientAddBtn.setBackgroundResource(R.drawable.icon_patient_add_select);
				break;
			case MotionEvent.ACTION_UP:
				patientAddBtn.setBackgroundResource(R.drawable.icon_patient_add_normal);
				Intent it=new Intent(this, PatientAddActivity.class);
				startActivity(it);
				break;
			}
			break;
		case R.id.addAttachment:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				addAttachment.setBackgroundResource(R.drawable.add_button_select);
				break;
			case MotionEvent.ACTION_UP:
				addAttachment.setBackgroundResource(R.drawable.add_button_normal);
				LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View convertView = (LinearLayout) layoutInflater.inflate(R.layout.attachment_item, null);
				ImageView picView=(ImageView) convertView.findViewById(R.id.attachmentPic);
				picView.setOnClickListener(new onAttachmentPicClickListner());
				ImageView bigView=(ImageView) convertView.findViewById(R.id.attachment2Big);
				bigView.setOnClickListener(new onAttachmentBigClickListner());
				ImageView deleteView=(ImageView) convertView.findViewById(R.id.attachmentDelete);
				deleteView.setOnClickListener(new onAttachmentDelClickListner());
				attachmentLayouts.addView(convertView);
				break;
			}
			break;
		case R.id.selectDisease:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				Intent it=new Intent(this, DiseaseSelectActivity.class);
				startActivityForResult(it, 6);
				break;
			}
		}
		return true;
	}
	
    class SaveTask extends AsyncTask<String, Void, Integer>{
    	private AlertDialog.Builder builder;
    	private Dialog dialog;
    	private Context myContext;
    	
    	public SaveTask(Context myContext) {
    		this.myContext = myContext;
    		builder = new Builder(myContext);
    		builder.setMessage("�����С��������Ժ�");
    		dialog = builder.show();
		}
    	
		@Override
		protected Integer doInBackground(String... params) {
			MedicalCaseDao medicalCaseDao=new MedicalCaseDao(MedicalCaseAddActivity.this);
			boolean sdValible=true;
			if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
				sdValible=false;
			}
			String param=params[0];
			if(param.equals("save")){
				if(newCreate){
					convertFormToModel();
					long fdId=medicalCaseDao.insert(medicalCase);
					medicalCase.setFdId(new Long(fdId).intValue());
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String updateTime=sdf.format(new Date());
					patientInfoSelected.setFdUpdateTime(updateTime);
					PatientInfoDao patientInfoDao=new PatientInfoDao(MedicalCaseAddActivity.this);
					patientInfoDao.update(patientInfoSelected);
				}else{
					convertFormToModel();
					medicalCaseDao.update(medicalCase);
				}
				saveAttachment();
				dialog.dismiss();
				if(sdValible){
					return 1;
				}else{
					return 11;
				}
			}else if(param.equals("saveAdd")){
				if(newCreate){
					convertFormToModel();
					long fdId=medicalCaseDao.insert(medicalCase);
					medicalCase.setFdId(new Long(fdId).intValue());
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String updateTime=sdf.format(new Date());
					patientInfoSelected.setFdUpdateTime(updateTime);
					PatientInfoDao patientInfoDao=new PatientInfoDao(MedicalCaseAddActivity.this);
					patientInfoDao.update(patientInfoSelected);
				}else{
					convertFormToModel();
					medicalCaseDao.update(medicalCase);
				}
				saveAttachment();
				dialog.dismiss();
				if(sdValible){
					return 2;
				}else{
					return 22;
				}
			}
			return null;
		}
		
		protected void onPostExecute(Integer result) {
			switch (result) {
			case 1:
				Toast.makeText(myContext,"����ɹ�", Toast.LENGTH_SHORT).show();
				MedicalCaseAddActivity.this.finish();
				break;
			case 11:
				Toast.makeText(myContext,"����SD�������ã�����ͼƬ��������ʧ�ܣ�����ȷ��SD����������ʹ�á�", Toast.LENGTH_SHORT).show();
				Toast.makeText(myContext,"����ɹ�", Toast.LENGTH_SHORT).show();
				MedicalCaseAddActivity.this.finish();
				break;
			case 2:
				resetForm();
				Toast.makeText(myContext,"����ɹ�", Toast.LENGTH_SHORT).show();
				break;
			case 22:
				resetForm();
				Toast.makeText(myContext,"����SD�������ã�����ͼƬ��������ʧ�ܣ�����ȷ��SD����������ʹ�á�", Toast.LENGTH_SHORT).show();
				Toast.makeText(myContext,"����ɹ�", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
    }
	private void convertFormToModel(){
		medicalCase.setFdDate(fdDate.getText().toString());
		medicalCase.setFdAdvice(fdAdvice.getText().toString());
		medicalCase.setFdArea(fdArea.getText().toString());
		medicalCase.setFdBed(fdBed.getText().toString());
		medicalCase.setFdFeatures(fdFeatures.getText().toString());
		medicalCase.setFdPaitentId(patientInfoSelected.getFdId());
		medicalCase.setFdPaitentName(patientInfoSelected.getFdName());
		medicalCase.setFdRemark(fdRemark.getText().toString());
		medicalCase.setFdWard(fdWard.getText().toString());
		medicalCase.setFdDisease(fdDisease.getText().toString());
	}
	private void convertModelToForm(){
		fdDate.setText(medicalCase.getFdDate());
		fdAdvice.setText(medicalCase.getFdAdvice());
		fdArea.setText(medicalCase.getFdArea());
		fdBed.setText(medicalCase.getFdBed());
		fdFeatures.setText(medicalCase.getFdFeatures());
		fdRemark.setText(medicalCase.getFdRemark());
		fdWard.setText(medicalCase.getFdWard());
		fdDisease.setText(medicalCase.getFdDisease());
		
	}
	private void resetForm(){
		newCreate=true;
		medicalCase=new MedicalCase();
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
		String nowDate=dateformat.format(new Date());
		fdDate.setText(nowDate);
		fdAdvice.setText(null);
		fdArea.setText(null);
		fdBed.setText(null);
		fdFeatures.setText(null);
		fdRemark.setText(null);
		fdWard.setText(null);
		attachmentLayouts.removeAllViews();
		fdDisease.setText(null);
	}
	
	private boolean checkForm(){
		if(null==patientInfoSelected){
			Toast.makeText(this,"���߲���Ϊ�գ�����ѡ��¼�뻼����Ϣ��", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(StringUtil.isNull(fdDisease.getText().toString())){
			Toast.makeText(this,"��ϲ���Ϊ�գ�����¼�������Ϣ��", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	public void onItemSelected(AdapterView adapterView, View view, int arg2,
			long arg3) {
		int index=adapterView.getSelectedItemPosition();
		switch (((View) view.getParent()).getId()) {
		case R.id.patientSpinner:
			patientInfoSelected=(PatientInfo)patientList.get(index);
			otherRemark.setText(patientInfoSelected.getFdRemark());
			Bitmap headBitmap=Bytes2Bimap(patientInfoSelected.getFdHeadPic());
			if(null!=headBitmap)
				headPic.setImageBitmap(headBitmap);
			String htmlTelText="<a style=\"color:blue;\" href=\"\">"+patientInfoSelected.getFdPhone()+"</a>";
			patientPhone.setText(Html.fromHtml(htmlTelText));
			patientPhone.setClickable(true);
			patientPhone.setOnClickListener(new OnClickListener() {
			    public void onClick(View v) {
			    	TextView view=(TextView)v;
			    	String phoneNum=view.getText().toString();
			    	dialog(phoneNum);
			    }
			});
			break;
		}
	}
	public Bitmap Bytes2Bimap(byte[] b) {
        if (null!=b&&b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
	private void dialog(final String phoneNum) { 
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("��ȷ������:"+phoneNum); 
		 builder.setTitle("��ʾ"); 
		 builder.setPositiveButton("ȷ��", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
					Intent dialIntent =new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNum));
					dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(dialIntent);
			 	} 
		 }); 
		 builder.setNegativeButton("ȡ��", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
			 	} 
		 }); 
		 builder.create().show(); 
	 } 

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	//������ز����ࡢ����
	
	/**
	 *  ɾ������
	 */
	class onAttachmentDelClickListner implements OnClickListener{
		public void onClick(View view) {
			//attachmentLayouts.removeView((View) view.getParent().getParent());
			dialog(view);
		}
	}
	
	private void dialog(final View view) { 
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("��ȷ��ɾ���ø���?"); 
		 builder.setTitle("��ʾ"); 
		 builder.setPositiveButton("ȷ��", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		attachmentLayouts.removeView((View) view.getParent().getParent());
			 		dialog.dismiss(); 
					Toast.makeText(MedicalCaseAddActivity.this,"ɾ���ɹ�", Toast.LENGTH_SHORT).show();
			 	} 
		 }); 
		 builder.setNegativeButton("ȡ��", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
			 	} 
		 }); 
		 builder.create().show(); 
	 } 
	/**
	 *  �Ŵ󸽼�
	 */
	class onAttachmentBigClickListner implements OnClickListener{
		public void onClick(View view) {
			ImageView attachmentPic=(ImageView) ((View)view.getParent().getParent()).findViewById(R.id.attachmentPic);
			Intent it=new Intent(MedicalCaseAddActivity.this, BigImageActivity.class);
//			Bundle bundle = new Bundle();
//			Bitmap attachmentBitmap=((BitmapDrawable)attachmentPic.getDrawable()).getBitmap();    
//			ByteArrayOutputStream baos=new ByteArrayOutputStream(); 
//			attachmentBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//			bundle.putByteArray("bitmapByteArray", baos.toByteArray());
//			it.putExtras(bundle);
			Bitmap attachmentBitmap=((BitmapDrawable)attachmentPic.getDrawable()).getBitmap();
			HealthyApplication.getInstance().setBigBitmap(attachmentBitmap);
			startActivity(it);
		}
	}
	/**
	 * �༭ͼƬ
	 */
	class onAttachmentPicClickListner implements OnClickListener{
		public void onClick(View view) {
			attachmentPicSelected=(ImageView) view;
			final CharSequence[] items = { "���", "����" };
			AlertDialog dlg = new AlertDialog.Builder(MedicalCaseAddActivity.this)
					.setTitle("ѡ��ͼƬ")
					.setItems(items, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							// ����item�Ǹ���ѡ��ķ�ʽ��
							// ��items�������涨�������ַ�ʽ�����յ��±�Ϊ1���Ծ͵������շ���
							if (item == 1) {
								Intent getImageByCamera = new Intent(
										"android.media.action.IMAGE_CAPTURE");
								startActivityForResult(getImageByCamera, 4);
							} else {
								Intent getImage = new Intent(
										Intent.ACTION_GET_CONTENT);
								getImage.addCategory(Intent.CATEGORY_OPENABLE);
								getImage.setType("image/jpeg");
								startActivityForResult(getImage, 3);
							}
						}
					}).create();
			dlg.show();
		}
	}
	/**
	 * ���渽��
	 */	
	private void displayAttachment(){
		if(newCreate)
			return;
		AttachmentDao attachmentDao=new AttachmentDao(this);
		List<Attachment> attachmentList=attachmentDao.find(null, "fd_main_id='"+medicalCase.getFdId()+"' and fd_model_name='"+MODELNAME+"'" , null, null, null, null, null);
		Bitmap bitmap=null;
		for(Attachment attachment:attachmentList){
			LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View convertView = (LinearLayout) layoutInflater.inflate(R.layout.attachment_item, null);
			//ͼƬ��ʼ��
			ImageView picView=(ImageView) convertView.findViewById(R.id.attachmentPic);
			picView.setOnClickListener(new onAttachmentPicClickListner());
			if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
				SDFileUtil sdFileUtil=new SDFileUtil();
				bitmap=sdFileUtil.readFromSD2Bitmap(attachment.getFdFilePath());
				if(null!=bitmap){
					picView.setImageBitmap(bitmap);	
				}
						
			}

			
			//��ʼ����������
			EditText attName=(EditText) convertView.findViewById(R.id.attachmentName);
			attName.setText(attachment.getFdName());
			ImageView bigView=(ImageView) convertView.findViewById(R.id.attachment2Big);
			bigView.setOnClickListener(new onAttachmentBigClickListner());
			//ɾ����ť��ʼ��
			ImageView deleteView=(ImageView) convertView.findViewById(R.id.attachmentDelete);
			deleteView.setOnClickListener(new onAttachmentDelClickListner());
			
			attachmentLayouts.addView(convertView);		
		}

	}
	/**
	 * ���渽��
	 */
	private void saveAttachment(){
		//�ж�sd���Ƿ����
		boolean sdValible=true;
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			sdValible=false;
		//�༭����ǰ��ɾ��ԭ����
		AttachmentDao attachmentDao=new AttachmentDao(this);
		if(!newCreate){
			List<Attachment> oldAttachmentList=attachmentDao.find(null, "fd_main_id='"+medicalCase.getFdId()+"' and fd_model_name='"+MODELNAME+"'" , null, null, null, null, null);
			for(Attachment attachment:oldAttachmentList){
				String filePath=attachment.getFdFilePath();
				if(StringUtil.isNotNull(filePath)){
					SDFileUtil sdFileUtil=new SDFileUtil();
					sdFileUtil.deleteFile(attachment.getFdFilePath());
				}
				attachmentDao.delete(attachment.getFdId());
			}
		}
		//������ϸ���
		for(int i=0,size=attachmentLayouts.getChildCount();i<size;i++){
			View attLayout=attachmentLayouts.getChildAt(i);
			EditText attName=(EditText)attLayout.findViewById(R.id.attachmentName);
			ImageView attPic=(ImageView)attLayout.findViewById(R.id.attachmentPic);
			Attachment attachment=new Attachment();
			String path="healthy/"+patientInfoSelected.getFdName()+"/"+medicalCase.getFdDate()+"/";
			String fileName=Long.toHexString(System.currentTimeMillis())+".png";
			if(sdValible){
				Bitmap headBitmap=((BitmapDrawable)attPic.getDrawable()).getBitmap();   
				SDFileUtil sdFileUtil=new SDFileUtil();
				sdFileUtil.write2SDFromBitmap(path, fileName, headBitmap);
				attachment.setFdFilePath(path+fileName);
			}
			attachment.setFdMainId(medicalCase.getFdId());
			attachment.setFdModelName(MODELNAME);
			attachment.setFdName(attName.getText().toString());
			attachmentDao.insert(attachment);
		}			
	}
	//�޸�ͼƬ��С
	public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
         intent.setDataAndType(uri, "image/*");
         // ���òü�
         intent.putExtra("crop", "true");
         // aspectX aspectY �ǿ�ߵı���
         intent.putExtra("aspectX", 1);
         intent.putExtra("aspectY", 1);
         // outputX outputY �ǲü�ͼƬ���
         intent.putExtra("outputX", 320);
         intent.putExtra("outputY", 320);
         intent.putExtra("return-data", true);
         startActivityForResult(intent, 5);
	}


	
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	private boolean checkIsVIP(){
		boolean rtnValue=true;
		if(!HealthyApplication.getInstance().isVIP()){
			MedicalCaseDao dao=new MedicalCaseDao(this);
			List patientList=dao.find(null, "fd_patient_id='"+patientInfoSelected.getFdId()+"'", null, null, null, null, null);
			if(patientList.size()>=TRIALCASETCOUNT){
				Toast.makeText(this,"��Ǹ�����������Ѿ��������ð����ƣ��������ʹ������ͨ��������->��Ȩ��ȡ�ýݶȹٷ���ʽ��Ȩ��", Toast.LENGTH_SHORT).show();
				rtnValue=false;
			}
		}
		return rtnValue;
	}

}
