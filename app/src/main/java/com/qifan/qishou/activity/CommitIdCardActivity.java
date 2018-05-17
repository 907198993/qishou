package com.qifan.qishou.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.androidtools.SPUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.rx.IOCallBack;
import com.github.customview.MyLinearLayout;
import com.github.customview.MyTextView;
import com.qifan.qishou.Config;
import com.qifan.qishou.GetSign;
import com.qifan.qishou.MainActivity;
import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseActivity;
import com.qifan.qishou.base.MyCallBack;
import com.qifan.qishou.network.ApiRequest;
import com.qifan.qishou.network.response.GradObj;
import com.qifan.qishou.network.response.LoginObj;
import com.qifan.qishou.network.response.UploadImageObj;
import com.qifan.qishou.network.response.UploadImgItem;
import com.qifan.qishou.tools.BitmapUtils;
import com.qifan.qishou.tools.ImageUtils;
import com.qifan.qishou.tools.ImgUtil;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import top.zibin.luban.Luban;

/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class CommitIdCardActivity extends BaseActivity {
    @BindView(R.id.linearImage1)
    MyLinearLayout linearImage1;
    @BindView(R.id.linearImage2)
    MyLinearLayout linearImage2;
    @BindView(R.id.tv_register_commit)
    MyTextView tvRegisterCommit;
    @BindView(R.id.iv_id1)
    ImageView ivId1;
    @BindView(R.id.iv_id2)
    ImageView ivId2;
    private BottomSheetDialog selectPhotoDialog;

    private  int flag;//判断选择的第一个还是第二个。
    private int picOrCamera; //判断选择的是拍照还是选择图片
    Bitmap  bitmap;
    private  String ImagePath1;
    private  String ImagePath2;
    File file;
    @Override
    protected int getContentView() {
        return R.layout.activity_id_card;
    }

    @Override
    protected void initView() {
        setAppTitle("上传身份证");
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.linearImage1, R.id.linearImage2, R.id.tv_register_commit})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.linearImage1:
                flag =1;
                addPhoto();
                break;
            case R.id.linearImage2:
                flag = 2;
                addPhoto();
                break;
            case R.id.tv_register_commit:
                Map<String,String> map=new HashMap<String,String>();
                map.put("userId",getUserId());
                if (TextUtils.isEmpty(ImagePath1)) {
                    showMsg("正面照没有上传");
                    return;
                } else if (TextUtils.isEmpty(ImagePath2)) {
                    showMsg("背面照没有上传");
                    return;
                }
                map.put("cardFront",ImagePath1);
                map.put("cardReverse",ImagePath2);
                map.put("sign", GetSign.getSign(map));
                ApiRequest.UploadCard(map, new MyCallBack<GradObj>(mContext) {
                    @Override
                    public void onSuccess(GradObj obj) {
                     if(obj.getResult()==1){
                         showMsg("成功提交认证信息，请耐心等待审核！");
                         finish();
                     }
                    }
                });
                break;
        }
    }

    private void addPhoto() {
        showSelectPhotoDialog();
    }

    private void showSelectPhotoDialog() {
        if (selectPhotoDialog == null) {
            View sexView = LayoutInflater.from(mContext).inflate(R.layout.popu_select_photo, null);
            sexView.findViewById(R.id.tv_select_photo).setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    selectPhotoDialog.dismiss();
                    selectPhoto();

                }
            });
            sexView.findViewById(R.id.tv_take_photo).setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    selectPhotoDialog.dismiss();
                    takePhoto();
                }
            });
            sexView.findViewById(R.id.tv_photo_cancle).setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    selectPhotoDialog.dismiss();
                }
            });
            selectPhotoDialog = new BottomSheetDialog(mContext);
            selectPhotoDialog.setCanceledOnTouchOutside(true);
            selectPhotoDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            selectPhotoDialog.setContentView(sexView);
        }
        selectPhotoDialog.show();
    }

    //选择相册
    private void selectPhoto() {
        picOrCamera=1;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3000);
    }

    private String path = Environment.getExternalStorageDirectory() +
            File.separator + Environment.DIRECTORY_DCIM + File.separator;

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "IMG_" + dateFormat.format(date);
    }

    Uri photoUri;
    private String imgSaveName = "";

    private void takePhoto() {
        picOrCamera=2;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/test/" + System.currentTimeMillis() + ".jpg");
            file.getParentFile().mkdirs();
            //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
                Uri uri = FileProvider.getUriForFile(this, "com.xykj.customview.fileprovider", file);

                //添加权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                startActivityForResult(intent, REQUEST_CAMERA);
                startActivityForResult(intent, 2000);
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 2000:
                    imgSaveName = file.getAbsolutePath();
                    uploadImg();

                break;
            case 3000:
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    imgSaveName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                }
                uploadImg();
                if (resultCode == RESULT_OK) {
                     bitmap = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageOnKitKat(this, data);        //ImgUtil是自己实现的一个工具类
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageBeforeKitKat(this, data);
                    }
                }
                break;
        }
    }

    private void uploadImg() {
        showLoading();
        Log.i("========", "========" + imgSaveName);
        RXStart(new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String newPath = ImageUtils.filePath;
                ImageUtils.makeFolder(newPath);
                FileInputStream fis = null;
                try {
                    List<File> files = Luban.with(mContext).load(imgSaveName).get();
                    String imgStr = BitmapUtils.bitmapToString2(files.get(0));
                    subscriber.onNext(imgStr);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
            @Override
            public void onMyNext(String baseImg) {
                UploadImgItem item = new UploadImgItem();
                item.setFile(baseImg);
                Map<String, String> map = new HashMap<String, String>();
                map.put("sign", GetSign.getSign(map));
                ApiRequest.uploadImg(map, item, new MyCallBack<UploadImageObj>(mContext) {
                    @Override
                    public void onSuccess(UploadImageObj obj) {
                        if(flag ==1){ //如果选择的是第一个上传
                            ImagePath1 = obj.getImg();
                            if(picOrCamera ==1){//同时不是选择拍照。则执行
                                ivId1.setVisibility(View.VISIBLE);
                                ivId1.setImageBitmap(bitmap);
                                linearImage1.setVisibility(View.GONE);//隐藏界面
                            }else{
                                ivId1.setImageBitmap(BitmapFactory.decodeFile(imgSaveName));
                                ivId1.setVisibility(View.VISIBLE);
                                linearImage1.setVisibility(View.GONE);//隐藏界面
                            }
                        }else{
                            ImagePath2 = obj.getImg();
                            if(picOrCamera ==1){//同时不是选择拍照。则执行
                                ivId2.setVisibility(View.VISIBLE);
                                ivId2.setImageBitmap(bitmap);
                                linearImage2.setVisibility(View.GONE);//隐藏界面
                            }else{
                                ivId2.setImageBitmap(BitmapFactory.decodeFile(imgSaveName));
                                ivId2.setVisibility(View.VISIBLE);
                                linearImage2.setVisibility(View.GONE);//隐藏界面
                            }
                        }
                    }
                });
            }

            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                dismissLoading();
                showToastS("图片处理失败");
            }
        });
    }

}
