package com.will.gps.layout;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezreal.timeselectview.CityPickerView;
import com.ezreal.timeselectview.TimePickerView;
import com.google.gson.Gson;
import com.will.gps.MainActivity;
import com.will.gps.MainActivity.MyOnTouchListener;
import com.will.gps.R;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.LocalAccountBean;
import com.will.gps.view.CircleImageView;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by MaiBenBen on 2019/4/13.
 */

public class UserFragment extends Fragment implements View.OnClickListener{

    private Gson gson = new Gson();
    private RMessage rMessage = new RMessage();
    private View view;
    /*@BindView(R.id.layout_head)
    RelativeLayout mLayoutHead;*/
    private CircleImageView mIvHead;
    private TextView mTvAccount;
    private EditText mEtNick,mEtRealName;
    private TextView mTvSex;
    private TextView mTvBirthDay;
    private TextView mTvLocation;
    private EditText mEtSignature;
    // 个人信息
    private LocalAccountBean mAccountBean;
    // 头像本地路径
    /*private String mHeadImgPath = "";
    // 获取图像请求码
    private static final int TAKE_PHOTO = 30001;*/
    private static final int SELECT_PHOTO = 30000;
    // 信息是否有被更新
    private boolean haveAccountChange = false;
    // 是否处于编辑状态
    private boolean isEditor;
    // 输入服务，用于显示键盘
    private InputMethodManager mInputMethodManager;
    private Context context;
    private Button mButton;
    private Button uCommit;

    private GestureDetector mGestureDetector;
    //private SVCGestureListener mGestureListener = new SVCGestureListener();
    MyOnTouchListener myOnTouchListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        view = inflater.inflate(R.layout.activity_fragment_user,container,false);

        mIvHead=(CircleImageView)view.findViewById(R.id.iv_head_picture);
        mIvHead.setImageResource(R.mipmap.zu);
        mTvAccount=(TextView)view.findViewById(R.id.tv_account);
        mEtNick=(EditText)view.findViewById(R.id.et_account_nick);
        mTvSex=(TextView)view.findViewById(R.id.tv_account_sex);
        mTvBirthDay=(TextView)view.findViewById(R.id.tv_account_birth);
        mTvLocation=(TextView)view.findViewById(R.id.tv_account_location);
        mEtSignature=(EditText)view.findViewById(R.id.et_account_signature);
        //mTextView = (TextView)getActivity().findViewById(R.id.txt_content);
        mIvHead=(CircleImageView)view.findViewById(R.id.iv_head_picture);
        mButton=(Button)view.findViewById(R.id.iv_menu_btn);
        uCommit= (Button)view.findViewById(R.id.iv_menu_com);
        mEtRealName=(EditText)view.findViewById(R.id.et_account_rlnm);

        /*mEtNick.setOnTouchListener(this);
        mEtSignature.setOnTouchListener(this);*/
        //mGestureDetector = new GestureDetector(getActivity(), mGestureListener);
        //mGestureDetector.setIsLongpressEnabled(true);
        //mGestureDetector.setOnDoubleTapListener(mGestureListener);
        //fragment具体代码：
        //过程：初始化监听器，注册和注销，同时监听器中监听手势动作
        myOnTouchListener = new MyOnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isEditor) {
                    if (v.getId() == R.id.et_account_nick) {
                        mEtNick.requestFocus();
                        mEtNick.setSelection(mEtNick.getText().length());
                        mInputMethodManager.showSoftInput(mEtNick, 0);
                    } else if (v.getId() == R.id.et_account_signature) {
                        mEtSignature.requestFocus();
                        mEtSignature.setSelection(mEtSignature.getText().length());
                        mInputMethodManager.showSoftInput(mEtSignature, 0);
                    }
                    return true;
                }
                return false;
            }
        };
        ((MainActivity)getActivity()).registerMyOnTouchListener(myOnTouchListener);
        showData();
        init();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        /*myOnTouchListener = new MainActivity.MyOnTouchListener() {
            // EditText 获取焦点并将光标移动到末尾
            @Override
            public boolean onTouch( View v,MotionEvent ev) {
                if (isEditor) {
                    if (v.getId() == R.id.et_account_nick) {
                        mEtNick.requestFocus();
                        mEtNick.setSelection(mEtNick.getText().length());
                        mInputMethodManager.showSoftInput(mEtNick, 0);
                    } else if (v.getId() == R.id.et_account_signature) {
                        mEtSignature.requestFocus();
                        mEtSignature.setSelection(mEtSignature.getText().length());
                        mInputMethodManager.showSoftInput(mEtSignature, 0);
                    }
                    return true;
                }
                return false;
            }
        };
        ((MainActivity) getActivity()).registerMyOnTouchListener(myOnTouchListener)*/;
    }


    // 显示数据
    public void showData() {
        mIvHead.setImageResource(R.mipmap.zu);
        mTvAccount.setText(MySocket.user.getPhonenum());
        mEtNick.setText(MySocket.user.getUserName());
        mTvSex.setText(MySocket.user.getSex());
        mTvBirthDay.setText(MySocket.user.getBirthday());
        mTvLocation.setText(MySocket.user.getLocate());
        mEtSignature.setText(MySocket.user.getSignature());
        mEtRealName.setText(MySocket.user.getRealName());

        //mAccountBean = NimUserHandler.getInstance().getLocalAccount();
//        if (mAccountBean != null) {
//            /*ImageUtils.setImageByFile(this, mIvHead,
//                    mAccountBean.getHeadImgUrl(), R.mipmap.bg_img_defalut);*/
//            mIvHead.setImageResource(R.mipmap.zu);
//            //mTvAccount.setText(mAccountBean.getAccount());
//            mTvAccount.setText(MySocket.user.getPhonenum());
//            //mEtNick.setText(mAccountBean.getNick());
//            mEtNick.setText(MySocket.user.getUserName());
//            if (mAccountBean.getGenderEnum() == GenderEnum.FEMALE) {
//                mTvSex.setText("女");
//            } else if (mAccountBean.getGenderEnum() == GenderEnum.MALE) {
//                mTvSex.setText("男");
//            } else {
//                mTvSex.setText("保密");
//            }
//            mEtSignature.setText(mAccountBean.getSignature());
//            String birthday = mAccountBean.getBirthDay();
//            if (TextUtils.isEmpty(birthday)) {
//                mTvBirthDay.setText("未设置");
//            } else {
//                mTvBirthDay.setText(birthday);
//            }
//            String location = mAccountBean.getLocation();
//            if (TextUtils.isEmpty(location)) {
//                mTvLocation.setText("未设置");
//            } else {
//                mTvLocation.setText(location);
//            }
//        }
    }
    private void init(){
        //mInputMethodManager = (InputMethodManager)getSystemService(context.INPUT_METHOD_SERVICE);
        // 文字
        //mLayoutHead.setOnClickListener(this);
        mTvSex.setOnClickListener(this);
        mTvBirthDay.setOnClickListener(this);
        mTvLocation.setOnClickListener(this);

        // 标题栏
       /* mIvBack.setOnClickListener(this);
        mIvMenu.setOnClickListener(this);*/

        // 输入框
        /*mEtNick.setOnTouchListener(this);
        mEtSignature.setOnTouchListener(this);*/

        //编辑按钮
        mButton.setOnClickListener(this);
        // 结束编辑，相当于初始化为非编辑状态
        uCommit.setOnClickListener(this);
        finishEdit();
    }

    // EditText 获取焦点并将光标移动到末尾
    /*@Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isEditor) {
            if (v.getId() == R.id.et_account_nick) {
                mEtNick.requestFocus();
                mEtNick.setSelection(mEtNick.getText().length());
                mInputMethodManager.showSoftInput(mEtNick, 0);
            } else if (v.getId() == R.id.et_account_signature) {
                mEtSignature.requestFocus();
                mEtSignature.setSelection(mEtSignature.getText().length());
                mInputMethodManager.showSoftInput(mEtSignature, 0);
            }
            return true;
        }
        return false;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.et_account_nick:
                mEtNick.requestFocus();
                mEtNick.setSelection(mEtNick.getText().length());
                mInputMethodManager.showSoftInput(mEtNick, 0);
                break;
            case R.id.et_account_signature:
                mEtSignature.requestFocus();
                mEtSignature.setSelection(mEtSignature.getText().length());
                mInputMethodManager.showSoftInput(mEtSignature, 0);*/
            case R.id.layout_head:
                setHeadImg();
                break;
            case R.id.tv_account_sex:
                setSex();
                break;
            case R.id.tv_account_location:
                setLocation();
                break;
            case R.id.tv_account_birth:
                setBirthday();
                break;
            /*case R.id.iv_back_btn:
                this.finish();
                break;*/
            case R.id.iv_menu_btn:
                if (isEditor) {
                    finishEdit();
                    mButton.setText("编辑个人信息");
                } else {
                    startEdit();
                    mButton.setText("完成");
                }
                break;

            case R.id.iv_menu_com:
                getData(new CallBack() {
                    @Override
                    public void getResult(String result) {
                        ((MySocket)getActivity().getApplication()).send(result);
                        Toast.makeText(getActivity(),"上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
    /**
     * 启动编辑
     */
    private void startEdit() {
        //mIvMenu.setImageResource(R.mipmap.done);
        // 可点击
        //mLayoutHead.setClickable(true);
        mTvSex.setClickable(true);
        mTvLocation.setClickable(true);
        mTvBirthDay.setClickable(true);
        // 可编辑
        mEtNick.setFocusable(true);
        mEtNick.setFocusableInTouchMode(true);
        mEtSignature.setFocusable(true);
        mEtSignature.setFocusableInTouchMode(true);
        mEtRealName.setFocusableInTouchMode(true);

        isEditor = true;
    }
    /**
     * 结束编辑，判断是否有修改，决定是否同步缓存数据
     */
    private void finishEdit() {
        /*if (!mEtNick.getText().toString()
                .equals(mAccountBean.getNick())) {
            mAccountBean.setNick(mEtNick.getText().toString());
            haveAccountChange = true;
        }

        if (!mEtSignature.getText().toString()
                .equals(mAccountBean.getSignature())) {
            mAccountBean.setSignature(mEtSignature.getText().toString());
            haveAccountChange = true;
        }

        if (haveAccountChange) {

            // 将数据更新到缓存
            //NimUserHandler.getInstance().setLocalAccount(mAccountBean);
            // 通知handler将数据更新到服务器
            //NimUserHandler.getInstance().syncChange2Service();

            haveAccountChange = false;
        }
*/



        //mIvMenu.setImageResource(R.mipmap.editor);
        // 不可点击
        //mLayoutHead.setClickable(false);
        mTvSex.setClickable(false);
        mTvLocation.setClickable(false);
        mTvBirthDay.setClickable(false);
        // 不可编辑
        mEtNick.setFocusable(false);
        mEtNick.setFocusableInTouchMode(false);
        mEtSignature.setFocusable(false);
        mEtSignature.setFocusableInTouchMode(false);
        mEtRealName.setFocusableInTouchMode(false);

        isEditor = false;
    }

    /*接口*/
    public interface CallBack{
        public void getResult(String result);
    }
    /*接口回调*/
    public void getData(CallBack callBack){
        MySocket.user.setUserName(mEtNick.getText().toString());
        MySocket.user.setSex(mTvSex.getText().toString());
        MySocket.user.setLocate(mTvLocation.getText().toString());
        MySocket.user.setSignature(mEtSignature.getText().toString());
        MySocket.user.setBirthday(mTvBirthDay.getText().toString());
        rMessage.setSendername(mEtNick.getText().toString());
        MySocket.user.setRealName(mEtRealName.getText().toString());
        rMessage.setSenderphone(MySocket.user.getPhonenum());
        rMessage.setType("更新");
        rMessage.setDate(new Date());
        rMessage.setContent(gson.toJson(MySocket.user));
        String msg = gson.toJson(rMessage);
        callBack.getResult(msg);
    }


   /* @Override//fragment中注册button点击事件
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
            }
        });
    }*/
    /**
     * 设置性别
     */
    private void setSex(){
        final int[] selected = new int[1];
        /*if (mAccountBean.getGenderEnum() == GenderEnum.MALE) {
            selected[0] = 0;
        } else if (mAccountBean.getGenderEnum() == GenderEnum.FEMALE) {
            selected[0] = 1;
        } else {
            selected[0] = 2;
        }*/
        final String[] items = new String[]{"男", "女", "保密"};
        new AlertDialog.Builder(context)
                .setTitle("性别")
                .setSingleChoiceItems(items, selected[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which != selected[0]) {
                            if (which == 0) {
                                //mAccountBean.setGenderEnum(GenderEnum.MALE);
                                mTvSex.setText("男");
                            } else if (which == 1) {
                                //mAccountBean.setGenderEnum(GenderEnum.FEMALE);
                                mTvSex.setText("女");
                            } else {
                                //mAccountBean.setGenderEnum(GenderEnum.UNKNOWN);
                                mTvSex.setText("保密");
                            }
                            haveAccountChange = true;
                        }else
                            mTvSex.setText("男");
                        dialog.dismiss();
                    }
                }).create().show();
    }

    /**
     * 设置生日
     */
    private void setBirthday() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_birthday, null);
        final AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
        TimePickerView timePickerView = (TimePickerView) view.findViewById(R.id.date_picker);
        timePickerView.setSelectedListener(new TimePickerView.OnDateSelectedListener() {
            @Override
            public void selectedDate(int year, int month, int day) {
                String yearString = String.valueOf(year);
                String monthString = String.valueOf(month);
                String dayString = String.valueOf(day);
                if (monthString.length() == 1){
                    monthString = "0" + monthString;
                }
                if (dayString.length() == 1){
                    dayString = "0" + dayString;
                }
                String birthday = String.format("%s-%s-%s", yearString, monthString, dayString);
                if (!birthday.equals(mTvBirthDay.getText().toString())) {
                    //mAccountBean.setBirthDay(birthday);
                    mTvBirthDay.setText(birthday);
                    haveAccountChange = true;
                }
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    /**
     * 设置地区
     */
    private void setLocation(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_location, null);
        final AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
        CityPickerView cityPickerView = (CityPickerView) view.findViewById(R.id.city_picker);
        cityPickerView.setCitySelectedListener(new CityPickerView.OnCitySelectedListener() {
            @Override
            public void citySelected(String province, String city) {
                String location = province + "/" + city;
                if (!location.equals(mTvLocation.getText().toString())) {
                    //mAccountBean.setLocation(location);
                    mTvLocation.setText(location);
                    haveAccountChange = true;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 设置头像，拍照或选择照片
     */
    private void setHeadImg() {
        //View view = LayoutInflater.from(context).inflate(R.layout.dialog_set_head_img, null);
        //final AlertDialog alertDialog = new AlertDialog.Builder(context).setView(view).create();
        //TextView take = (TextView) view.findViewById(R.id.tv_take_photo);
        //TextView select = (TextView) view.findViewById(R.id.tv_select_img);
        /*take.setOnClickListener(new View.OnClickListener() {//调用相机照头像
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mHeadImgPath = Constant.APP_CACHE_PATH + File.separator + "image"
                            + File.separator + mAccountBean.getAccount() + ".jpg";
                    Uri uri = Uri.fromFile(new File(mHeadImgPath));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, TAKE_PHOTO);
                } catch (Exception e) {
                    ToastUtils.showMessage(AccountInfoActivity.this, "启动相机出错！请重试");
                    e.printStackTrace();
                }

            }
        });*/
        /*select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/
                //alertDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择头像图片"), SELECT_PHOTO);
       /*     }
        });*/
        //alertDialog.show();
    }
    @Override
    public void onDestroy() {//注销自己的触摸事件
        super.onDestroy();
        ((MainActivity) getActivity()).unregisterMyOnTouchListener(myOnTouchListener);
    }
}
