package com.example.pblpro;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import co.junwei.cpabe.Cpabe;


public class PkslectActivity extends AppCompatActivity {

    private Button result;

    //복호화에 필요한 변수
    static Uri uri;  //경로 변수
    static String pubfile;
    static String prvfile;
    static String encfile;
    static String decfile; //복호화 파일


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pkslect);


        Button pksel = (Button) findViewById(R.id.pksel); //pksel 버튼 생성
        Button filesel = (Button) findViewById(R.id.filesel); //filesel 버튼 생성
        Button sksel = (Button) findViewById(R.id.sksel); //sksel 버튼 생성


        pksel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {  // pksel 버튼 선택시 파일 선택 요청
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent 생성 //이미지 요청
                intent.setType("*/*"); //파일포맷을 파일 전체로 정함
                intent = Intent.createChooser(intent, "choose a file");
                startActivityForResult(intent, 1000); // 이미지 요청 받음

            }

        });

        filesel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {  // filesel 버튼 선택시 파일 선택 요청
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent 생성 //이미지 요청
                intent.setType("*/*"); //파일포맷을 파일 전체로 정함
                intent = Intent.createChooser(intent, "choose a file");
                startActivityForResult(intent, 1001); // 이미지 요청 받음

            }

        });
        sksel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {  // sksel 버튼 선택시 파일 선택 요청
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent 생성 //이미지 요청
                intent.setType("*/*"); //파일포맷을 파일 전체로 정함
                intent = Intent.createChooser(intent, "choose a file");
                startActivityForResult(intent, 1002); // 이미지 요청 받음

            }

        });


        Button result = (Button) findViewById(R.id.res); //입력버튼저장

        result.setOnClickListener(new View.OnClickListener() {  //완료 버튼 누를 때
            @Override
            public void onClick(View v) {
                Toast.makeText(PkslectActivity.this, "입력되었습니다.", Toast.LENGTH_SHORT).show();

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

                decfile = dirPath + "/demo/input.new"; //복호화된 파일 생성

                Cpabe enc = new Cpabe(); //클래스 생성
                //복호화
                System.out.println("//start to dec");
                try {
                    System.out.println();
                    System.out.println("============="+pubfile+encfile+prvfile);
                    enc.dec(pubfile, prvfile, encfile, decfile);
                    System.out.println("//end to dec");
                } catch (Exception e) {
                    e.printStackTrace();

                }




            }
        });
    }











    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String filePath = null;
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {   // pubfile = 1000
            //요청 코드와 결과코드 그리고 데이터(사진)이 정상적으로 선택된 상태

            uri = data.getData();
            String src = uri.getPath(); //경로 얻기
            System.out.println("==================" + src);

            Context context = getApplicationContext();

            filePath = RealPathUtil.getRealPathFromURI_API19(context, uri);
            System.out.println("==================" + filePath);


            pubfile = filePath; //pub 파일 저장
            System.out.println("============="+pubfile);

        }
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) { //encfile = 1001
            //요청 코드와 결과코드 그리고 데이터(사진)이 정상적으로 선택된 상태

            uri = data.getData();
            String src = uri.getPath(); //경로 얻기
            System.out.println("==================" + src);

            Context context = getApplicationContext();

            filePath = RealPathUtil.getRealPathFromURI_API19(context, uri);
            System.out.println("==================" + filePath);

            encfile = filePath; //enc파일 저장
            System.out.println("ok");
            System.out.println("============="+encfile);



        }
        if (requestCode == 1002 && resultCode == RESULT_OK && data != null) { //prvfile = 1002
            //요청 코드와 결과코드 그리고 데이터(사진)이 정상적으로 선택된 상태

            uri = data.getData();
            String src = uri.getPath(); //경로 얻기
            System.out.println("==================" + src);

            Context context = getApplicationContext();

            filePath = RealPathUtil.getRealPathFromURI_API19(context, uri);
            System.out.println("==================" + filePath);

            System.out.println("ok");
            prvfile = filePath; //prv 파일 저장
            System.out.println("============="+prvfile);


        }

    }

}