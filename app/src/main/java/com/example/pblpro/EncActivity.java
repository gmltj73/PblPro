package com.example.pblpro;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import co.junwei.cpabe.Cpabe;


public class EncActivity extends AppCompatActivity {

    private Button result;

    //암호화에 필요한 변수
    static Uri uri;  //경로 변수
    static String pubfile;
    static String inputfile;
    static String encfile;
    static String policy; //정책


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enc);


        Button pksel = (Button) findViewById(R.id.pksel); //pksel 버튼 생성
        Button msel = (Button) findViewById(R.id.msel); //msel 버튼 생성


        // pksel 버튼 선택시 파일 선택 요청
        pksel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent 생성 //이미지 요청
                intent.setType("*/*"); //파일포맷을 파일 전체로 정함
                intent = Intent.createChooser(intent, "choose a file");
                startActivityForResult(intent, 1000); // 이미지 요청 받음

            }
        });

        // msel 버튼 선택시 파일 선택 요청
        msel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent 생성 //이미지 요청
                intent.setType("*/*"); //파일포맷을 파일 전체로 정함
                intent = Intent.createChooser(intent, "choose a file");
                startActivityForResult(intent, 1001); // 이미지 요청 받음
            }
        });


        result = (Button) findViewById(R.id.res); //입력버튼저장

        result.setOnClickListener(new View.OnClickListener() {  //완료 버튼 누를 때
            @Override
            public void onClick(View v) {
                Toast.makeText(EncActivity.this, "입력되었습니다.", Toast.LENGTH_SHORT).show();


                //문자가져오기
                EditText edittext = (EditText) findViewById(R.id.poText);
                policy = edittext.getText().toString(); //정책저장
                System.out.println("===============" + policy);


                // sd카드 경로(dirpath)를 구함
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
                String demo = dirPath + "/demo"; //demo 폴더 생성

                System.out.println("============" + dirPath);

                File file = new File(demo);
                file.mkdir();

                //일치하는 폴더가 없으면 폴더 생성
                if (!file.exists()) {
                    file.mkdir();
                    System.out.println("make dir success");
                }

                encfile = dirPath + "/demo/input.cpabe"; //암호화된 파일 생성

                //암호화 수행
                Cpabe enc = new Cpabe();
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


    //파일 선택
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String filePath = null;
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) { // publicfile = 1000
            //요청 코드와 결과코드 그리고 데이터(사진)이 정상적으로 선택된 상태

            uri = data.getData();
            String src = uri.getPath(); //경로 얻기
            System.out.println("==================" + src);

            Context context = getApplicationContext();

            filePath = RealPathUtil.getRealPathFromURI_API19(context, uri);
            System.out.println("==================" + filePath);

            try {
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(filePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    fos = new FileOutputStream("/data/user/0/com.example.pblpro/files/demo/input");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int datt = 0;
                while ((datt = fis.read()) != -1) {
                    fos.write(datt);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("ok");

            pubfile = filePath; //pub 파일 저장

        }
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) { // inputfile = 1001
            //요청 코드와 결과코드 그리고 데이터(사진)이 정상적으로 선택된 상태

            uri = data.getData();
            String src = uri.getPath(); //경로 얻기
            System.out.println("==================" + src);

            Context context = getApplicationContext();

            filePath = RealPathUtil.getRealPathFromURI_API19(context, uri);
            System.out.println("==================" + filePath);

            try {
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(filePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    fos = new FileOutputStream("/data/user/0/com.example.pblpro/files/demo/input");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int datt = 0;
                while ((datt = fis.read()) != -1) {
                    fos.write(datt);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("ok");

            inputfile = filePath; //input파일 저장

        }


    }


}
