package com.zerodo.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.util.StringUtil;
import com.zerodo.db.dao.DepartmentDao;
import com.zerodo.db.dao.EducationalDao;
import com.zerodo.db.dao.TitlesDao;
import com.zerodo.db.dao.UserInfoDao;
import com.zerodo.db.model.Department;
import com.zerodo.db.model.Educational;
import com.zerodo.db.model.Titles;
import com.zerodo.db.model.UserInfo;

public class UserInfoActivity extends CommonActivity implements OnClickListener,OnItemSelectedListener,OnTouchListener{
	
	private ImageView headView;
	private Bitmap headBitmap;
	private byte[] imgContent;
	private Spinner titlesSpinner,departmentSpinner,educationalSpinner;
	private ArrayAdapter titlesAdapter,departmentAdapter,educationalAdapter;
	private String[] titlesDatas,departmentDatas,educationalDatas;
	private List titlesList,departmentList,educationalList;
	private Titles titlesSelected;
	private Department departmentSelected;
	private Educational educationalSelected;
	private UserInfo userInfo;
	private Button btnBack,btnAdd;
	private TextView userName,nikeName,intorducation,hospital,specialty;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_userinfo);
		initUI();
		initDatas();
	}
	
	private void initUI(){
		headView = (ImageView) findViewById(R.id.headPic);
		headView.setOnClickListener(this);
		titlesSpinner=(Spinner) findViewById(R.id.titlesSpinner);
		departmentSpinner=(Spinner) findViewById(R.id.departmentSpinner);
		educationalSpinner=(Spinner) findViewById(R.id.educationalSpinner);
		btnBack=(Button) findViewById(R.id.userInfoBack);
		btnBack.setOnTouchListener(this);
		btnAdd=(Button) findViewById(R.id.userInfoAdd);
		btnAdd.setOnTouchListener(this);
		userName=(TextView) findViewById(R.id.userName);
		nikeName=(TextView) findViewById(R.id.nikeName);
		intorducation=(TextView) findViewById(R.id.intorducation);
		hospital=(TextView) findViewById(R.id.hospital);
		specialty=(TextView) findViewById(R.id.specialty);
	}
	
	private void initDatas(){
		int titlesPosition=-1;
		int deparmentPosition=-1;
		int educationalPosition=-1;
		
		UserInfoDao userInfoDao=new UserInfoDao(this);
		List userInfoList=userInfoDao.find();
		if(!userInfoList.isEmpty()){
			userInfo=(UserInfo) userInfoList.get(0);
			convertModelToForm();
		}
			
        TitlesDao titlesDao=new TitlesDao(this);
        titlesList=titlesDao.find();
        titlesDatas=new String[titlesList.size()];
		for(int i=0;i<titlesList.size();i++){
			Titles titles=(Titles)titlesList.get(i);
			titlesDatas[i]=titles.getFdName();
			if(null!=userInfo&&titles.getFdId()==userInfo.getFdTitlesId())
				titlesPosition=i;
		}
		initSpinner(titlesSpinner, titlesAdapter, titlesDatas, titlesPosition);
		
        DepartmentDao departmentDao=new DepartmentDao(this);
        departmentList=departmentDao.find();
        departmentDatas=new String[ departmentList.size()];
		for(int i=0;i< departmentList.size();i++){
			Department department=(Department)departmentList.get(i);
			departmentDatas[i]=department.getFdName();
			if(null!=userInfo&&department.getFdId()==userInfo.getFdDepartmentId())
				deparmentPosition=i;
		}
		initSpinner( departmentSpinner, departmentAdapter,  departmentDatas, deparmentPosition);
		
        EducationalDao educationalDao=new EducationalDao(this);
        educationalList=educationalDao.find();
        educationalDatas=new String[educationalList.size()];
		for(int i=0;i<educationalList.size();i++){
			Educational educational=(Educational)educationalList.get(i);
			educationalDatas[i]=educational.getFdName();
			if(null!=userInfo&&educational.getFdId()==userInfo.getFdEducationalId())
				educationalPosition=i;
		}
		initSpinner(educationalSpinner, educationalAdapter, educationalDatas, educationalPosition);
		
	}
	
	private void initSpinner(Spinner spinner,ArrayAdapter adapter,String [] datas,int position){
		adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, datas); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		spinner.setAdapter(adapter);  
		spinner.setOnItemSelectedListener(this);  
		spinner.setVisibility(View.VISIBLE);  
		if(position>-1)
			spinner.setSelection(position);
		
	}
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.userInfoBack:
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
		case R.id.userInfoAdd:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnAdd.setBackgroundResource(R.drawable.add_button_select);
				break;
			case MotionEvent.ACTION_UP:
				btnAdd.setBackgroundResource(R.drawable.add_button_normal);
				UserInfoDao userInfoDao=new UserInfoDao(this);
				if(null==userInfo){
					userInfo=new UserInfo();
					convertFormToModel();
					if(!checkForm())break;
					userInfoDao.insert(userInfo);
				}else{
					convertFormToModel();
					if(!checkForm())break;
					userInfoDao.update(userInfo);
				}
				Toast.makeText(this,"保存成功", Toast.LENGTH_SHORT).show();
				break;
			}
			break;
		}
		return true;
	}
	public void onClick(View arg0) {
		final CharSequence[] items = { "相册", "拍照" };
		AlertDialog dlg = new AlertDialog.Builder(UserInfoActivity.this)
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
					
					
//					super.onActivityResult(requestCode, resultCode, data);
//					Bundle extras = data.getExtras();
//					headBitmap = (Bitmap) extras.get("data");
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					headBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//					imgContent = baos.toByteArray();
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

	public void onItemSelected(AdapterView<?> adapter, View view, int arg2,
			long arg3) {
		int index=adapter.getSelectedItemPosition();
		switch (((View) view.getParent()).getId()) {
		case R.id.titlesSpinner:
			titlesSelected=(Titles)titlesList.get(index);
			break;
		case R.id.departmentSpinner:
			departmentSelected=(Department)departmentList.get(index);	
			break;
		case R.id.educationalSpinner:
			educationalSelected=(Educational)educationalList.get(index);	
			break;
		}

		
	}
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	private void convertFormToModel(){
		if(null!=departmentSelected)
			userInfo.setFdDepartmentId(departmentSelected.getFdId());
		if(null!=educationalSelected)
			userInfo.setFdEducationalId(educationalSelected.getFdId());
		if(null!=titlesSelected)
			userInfo.setFdTitlesId(titlesSelected.getFdId());
		userInfo.setFdHospital(hospital.getText().toString());
//		userInfo.setFdspecialty(specialty.getText().toString());
		userInfo.setFdIntroduction(intorducation.getText().toString());
		userInfo.setFdNikeName(nikeName.getText().toString());
		userInfo.setFdUserName(userName.getText().toString());
		headBitmap=((BitmapDrawable)headView.getDrawable()).getBitmap();   

		//int size=headBitmap.getWidth()*headBitmap.getHeight()*4; 
		ByteArrayOutputStream baos=new ByteArrayOutputStream(); 
		headBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		imgContent = baos.toByteArray();
		userInfo.setFdHeadPic(imgContent);
	}
	
	private void convertModelToForm(){
		hospital.setText(userInfo.getFdHospital());
		intorducation.setText(userInfo.getFdIntroduction());
		nikeName.setText(userInfo.getFdNikeName());
		userName.setText(userInfo.getFdUserName());
//		specialty.setText(userInfo.getFdspecialty());
		headBitmap=Bytes2Bimap(userInfo.getFdHeadPic());
		if(null!=headBitmap)
			headView.setImageBitmap(headBitmap);
	}
	
	private boolean checkForm(){
		boolean rtnValue=true;
		if(StringUtil.isNull(userInfo.getFdUserName())){
			Toast.makeText(this,"名称不能为空！请您重新输入名称。", Toast.LENGTH_SHORT).show();
			rtnValue=false;
		}
		return rtnValue;
	}

}
