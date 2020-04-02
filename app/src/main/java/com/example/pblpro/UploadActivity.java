package com.example.pblpro;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import co.junwei.cpabe.*;   //CPABE import


public class UploadActivity extends AppCompatActivity {

    private Button result;

    public ArrayList<ListItem> listViewItemList = new ArrayList<ListItem>(); //리스트뷰
    private ArrayList<ListItem> filteㅔredItemList = listViewItemList; //리스트뷰 임시저장소

    //암호화에 필요한 변수
    static String dir;  //절대경로
    static String pubfile; //경로에 생성
    static String mskfile; //경로에 생성
    static String prvfile;  //경로에 생성

    static String inputfile;  //평문
    static String encfile;
    static String decfile; //복호화 파일

    static String policy; //정책


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Intent intent = getIntent(); //데이터 수신
        inputfile = intent.getStringExtra("filePath");//string형
        System.out.println("====================" + inputfile);


        result = (Button) findViewById(R.id.res); //입력버튼저장

        result.setOnClickListener(new View.OnClickListener() {  //완료 버튼 누를 때
            @Override
            public void onClick(View v) {
                Toast.makeText(UploadActivity.this, "입력되었습니다.", Toast.LENGTH_SHORT).show();


                //문자가져오기
                EditText edittext = (EditText) findViewById(R.id.poText);
                policy = edittext.getText().toString(); //정책저장
                System.out.println("===============" + policy);


                //속성기반암호화

                //******변한부분 : sd카드 경로를 구해서 거기에 저장 ********
                String ess = Environment.getExternalStorageState();
                String dirPath = null;
                if (ess.equals(Environment.MEDIA_MOUNTED)) {
                    dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    System.out.println("SD Card stored in " + dirPath);
                } else {
                    System.out.println("SD Card stored");
                }
                //****************************************************


                System.out.println("===================" + dirPath);
                String demo = dirPath + "/demo";

                System.out.println("============" + dirPath);

                File file = new File(demo);
                file.mkdir();

                //일치하는 폴더가 없으면 폴더 생성
                if (!file.exists()) {
                    file.mkdir();
                    System.out.println("make dir success");
                }


                dir = demo;
                System.out.println("=======================" + dir);
                pubfile = dir + "/pub_key";  //경로에 생성
                System.out.println("============pubfile : " + pubfile);
                mskfile = dir + "/master_key"; //경로에 생성
                prvfile = dir + "/prv_key"; //경로에 생성
                encfile = dir + "/input.cpabe"; //암호화 파일 생성 //sdcard/demo아래에 생기게 함
                decfile = dir + "/input.new"; //복호화된 파일 생성


                Cpabe enc = new Cpabe(); //클래스 생성
                System.out.println("//start to setup");
                try {
                    enc.setup(pubfile, mskfile);  //파일 생성
                    System.out.println("//end to setup");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


                System.out.println("//start to enc");
                try {
                    enc.enc(pubfile, policy, inputfile, encfile);
                    System.out.println("//end to enc");
                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });
    }


}
