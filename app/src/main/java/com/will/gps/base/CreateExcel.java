package com.will.gps.base;

import android.os.Environment;

import com.will.gps.bean.SignTableBean;
import com.will.gps.bean.Signin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by MaiBenBen on 2019/6/4.
 */

public class CreateExcel {
    // 准备设置excel工作表的标题
    private WritableSheet sheet;	/**创建Excel工作薄*/
    private WritableWorkbook wwb;
    public static File file;
    //private String[] title = {"日期", "食物支出", "大保健费", "交通话费", "旅游出行", "穿着支出", "医疗保健", "人情客往", "宝宝专项", "房租水电", "其它支出", "备注说明"};
    private String[] title={"签到ID","群ID","发起人ID","发起时间","发起人经度","发起人纬度","签到范围","签到人ID","签到人经度","签到人纬度","签到是否结束","签到人是否签到","签到结果"};

    public CreateExcel(List<Signin> signins, List<SignTableBean> signTableBeans) {
        excelCreate(signins,signTableBeans);
    }

    public void excelCreate(List<Signin> signins,List<SignTableBean> signTableBeans) {
        try {            /**输出的excel文件的路径*/
            String filePath = Environment.getExternalStorageDirectory() + "/documents";
            file = new File(filePath, "群"+signins.get(0).getGroupid()+"签到情况.xls");
            int counter=0;
            if (!file.exists()) {
                file.createNewFile();
            }
            wwb = Workbook.createWorkbook(file);            /**添加第一个工作表并设置第一个Sheet的名字*/
            for(int j=0;j<signTableBeans.size();j++){
                sheet=wwb.createSheet(String.valueOf(signTableBeans.get(j).getId()),j);
                Label label;//第一行栏目
                for (int i = 0; i < title.length; i++) {
                    label = new Label(i, 0, title[i]);
                    sheet.addCell(label);
                }
                /*Class<?> classs = Class.forName("com.will.gps.bean.signin");
                Field[] filed = classs.getDeclaredFields();
                for (Field field : filed) {
                    System.out.println(field.get(signin));
                }*/
                System.out.println("###################################################");
                System.out.println("signins.get(k).getId()="+signins.get(j).getId()+",signTableBeans.get(j).getId()="+signTableBeans.get(j).getId()+"j="+j);
                for(int k=counter;signins.get(k).getId()==signTableBeans.get(j).getId();k++) {
                    /*for (int i = 0; i < title.length; i++) {
                        Label labeli = new Label(i, index, content[i]);
                        sheet.addCell(labeli);
                    }*/
                    /*counter=0;
                    for(Field field:Signin.class.getDeclaredFields()) {
                        field.setAccessible(true);
                        try {
                            System.out.println(field.get(signins.get(k)));
                            Label labeli = new Label(counter++, k+1, String.valueOf(field.get(signins.get(k))));
                            sheet.addCell(labeli);
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    counter=k;
                    System.out.println("############################################");
                    System.out.println("k="+k+"counter="+counter);*/
                    Label label1=new Label(0,k+1,String.valueOf(signins.get(counter).getId()));
                    sheet.addCell(label1);
                    label1=new Label(1,k+1,String.valueOf(signins.get(counter).getGroupid()));
                    sheet.addCell(label1);
                    label1=new Label(2,k+1,signins.get(counter).getOriginator());
                    sheet.addCell(label1);
                    label1=new Label(3,k+1,signins.get(counter).getTime());
                    sheet.addCell(label1);
                    label1=new Label(4,k+1,signins.get(counter).getLongitude());
                    sheet.addCell(label1);
                    label1=new Label(5,k+1,signins.get(counter).getLatitude());
                    sheet.addCell(label1);
                    label1=new Label(6,k+1,signins.get(counter).getRegion());
                    sheet.addCell(label1);
                    label1=new Label(7,k+1,signins.get(counter).getReceiver());
                    sheet.addCell(label1);
                    label1=new Label(8,k+1,signins.get(counter).getRlongitude());
                    sheet.addCell(label1);
                    label1=new Label(9,k+1,signins.get(counter).getRlatitude());
                    sheet.addCell(label1);
                    label1=new Label(10,k+1,String.valueOf(signins.get(counter).getState()));
                    sheet.addCell(label1);
                    label1=new Label(11,k+1,String.valueOf(signins.get(counter).getDone()));
                    sheet.addCell(label1);
                    label1=new Label(9,k+1,signins.get(counter).getResult());
                    sheet.addCell(label1);
                }
            }
            wwb.write();
            wwb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDataToExcel(int index, String[] content) throws Exception {
        Label label;
        for (int i = 0; i < title.length; i++) {            /**Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z			 * 在Label对象的子对象中指明单元格的位置和内容			 * */
            label = new Label(i, 0, title[i]);            /**将定义好的单元格添加到工作表中* */
            sheet.addCell(label);
        }
        /*
         * 把数据填充到单元格中
         * 需要使用jxl.write.Number
         * 路径必须使用其完整路径，否则会出现错误
         */
        for (int i = 0; i < title.length; i++) {
            Label labeli = new Label(i, index, content[i]);
            sheet.addCell(labeli);
        }
        // 写入数据
        wwb.write();
        // 关闭文件
        wwb.close();
    }

}
