package com.example.pblpro;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {

    static Uri uri;  //경로 변수
    Integer sw = null;  //스위치변수로 upload-0 download=1


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button uploadbt = (Button) findViewById(R.id.uploadbt); //업로드 버튼 객체 생성
        Button downloadbt = (Button) findViewById(R.id.downloadbt); //다운로드 버튼 객체 생성
        uploadbt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent 생성 //이미지 요청
                intent.setType("*/*"); //파일포맷을 파일 전체로 정함
                intent = Intent.createChooser(intent, "choose a file");
                startActivityForResult(intent, 1000); // 이미지 요청 받음
                sw = 0;
            }

        });

        downloadbt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {  //**바뀐부분**
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent 생성 //이미지 요청
                intent.setType("*/*"); //파일포맷을 파일 전체로 정함
                intent = Intent.createChooser(intent, "choose a file");
                startActivityForResult(intent, 1000); // 이미지 요청 받음
                sw = 1;
            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {  //startActivity 코드가 넘어옴
        super.onActivityResult(requestCode, resultCode, data);

        String filePath = null;
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            //요청 코드와 결과코드 그리고 데이터(사진)이 정상적으로 선택된 상태

            uri = data.getData();
            String src = uri.getPath(); //경로 얻기
            System.out.println("==================" + src);

            Context context = getApplicationContext();

            filePath = RealPathUtil.getRealPathFromURI_API19(context, uri);
            System.out.println("============" + filePath);

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

        }
        System.out.println("ok");


        //**변한부분**
        if (sw == 0) {

            Intent intent = new Intent(MainActivity.this, UploadActivity.class);//업로드 액티비티로 전환
            intent.putExtra("filePath", filePath);
            startActivity(intent);

        } else {

            Intent intent = new Intent(MainActivity.this, DownloadActivity.class);//다운로드 액티비티로 전환
            intent.putExtra("filePath", filePath);
            startActivity(intent);

        }
        //***********


    }
}
