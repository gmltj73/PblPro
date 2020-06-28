package com.example.pblpro;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import co.junwei.cpabe.Cpabe;


public class MainActivity extends AppCompatActivity {
    //git success
    String g;
    static Uri uri;  //경로 변수
    Integer sw = null;  //스위치변수로 upload-0 download=1
    int STORAGE_PERMISSION_CODE = 1;

    //액티비티 전환에 필요한 변수
    static String filePath = null;

    //암호화에 필요한 변수
    public static Context context_main;
    public static String dir;  //절대경로
    public static String pubfile; //경로에 생성
    public static String mskfile; //경로에 생성




    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context_main = this;    // 다른 액티비티에서 변수사용을 위해

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

        Cpabe enc = new Cpabe(); //클래스 생성


        Button uploadbt = (Button) findViewById(R.id.uploadbt); //업로드 버튼 객체 생성
        Button keygenerationbt = (Button) findViewById(R.id.keygenerationbt); //다운로드 버튼 객체 생성

        //권한 요청 설정
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }
        //여기까지

        uploadbt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {





                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent 생성 //이미지 요청
                intent.setType("*/*"); //파일포맷을 파일 전체로 정함
                intent = Intent.createChooser(intent, "choose a file");
                startActivityForResult(intent, 1000); // 이미지 요청 받음
                sw = 0;

            }

        });


        keygenerationbt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {

                //downloadactivity로 전환
                Intent intent = new Intent(MainActivity.this, KeyGenerationActivity.class);//다운로드 액티비티로 전환
                intent.putExtra("filePath", filePath);
                startActivity(intent);

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent 생성 //이미지 요청
//                intent.setType("*/*"); //파일포맷을 파일 전체로 정함
//                intent = Intent.createChooser(intent, "choose a file");
//                startActivityForResult(intent, 1000); // 이미지 요청 받음
//                sw = 1;

            }

        });


        /*
        Dropboxapi dropboxapi = new Dropboxapi();
        try {
            dropboxapi.createaccount();
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {  //startActivity 코드가 넘어옴
        super.onActivityResult(requestCode, resultCode, data);

//        String filePath = null;
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
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


        }
        System.out.println("ok");

        //**변한부분**
        if (sw == 0) {

            Intent intent = new Intent(MainActivity.this, UploadActivity.class);//업로드 액티비티로 전환
            intent.putExtra("filePath", filePath);
            startActivity(intent);

        } else {

//            Intent intent = new Intent(MainActivity.this, KeyGenerationActivity.class);//다운로드 액티비티로 전환
//            intent.putExtra("filePath", filePath);
//            startActivity(intent);

        }


    }
}