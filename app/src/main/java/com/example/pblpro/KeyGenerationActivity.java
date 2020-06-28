package com.example.pblpro;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import co.junwei.cpabe.*;   //CPABE import


public class KeyGenerationActivity extends AppCompatActivity {
    private ListAdapter adapter;
    private Button add;
    private Button remove;
    private Button result;
    private ListView listView;
    private int num;


    public ArrayList<ListItem> listViewItemList = new ArrayList<ListItem>(); //리스트뷰
    private ArrayList<ListItem> filteredItemList = listViewItemList; //리스트뷰 임시저장소


    //암호화에 필요한 변수
    static String dir;  //절대경로
    static String pubfile; //경로에 생성
    static String mskfile; //경로에 생성
    static String prvfile;  //경로에 생성
    static String encfile; // 암호화 파일
    static String decfile; //복호화 파일

    static String attr_str;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keygeneration);

        Intent intent = getIntent(); //데이터 수신
        encfile = intent.getStringExtra("filePath");//암호화된 파일
        System.out.println("===================/////=" + encfile); //


        num = 1;

        add = (Button) findViewById(R.id.add);
        remove = (Button) findViewById(R.id.rem);
        result = (Button) findViewById(R.id.res);
        listView = (ListView) findViewById(R.id.list);

        adapter = new ListAdapter();

        listView.setAdapter(adapter);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    //여기서부터 다시 시작
                Toast.makeText(KeyGenerationActivity.this, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                adapter.addItem(num + "", "", num);
                adapter.notifyDataSetChanged();
                num++;

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(KeyGenerationActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                adapter.delItem();
                adapter.notifyDataSetChanged();
                num--;
            }
        });

        result.setOnClickListener(new View.OnClickListener() {  //완료 버튼 누를 때
            @Override
            public void onClick(View v) {
                Toast.makeText(KeyGenerationActivity.this, "입력되었습니다.", Toast.LENGTH_SHORT).show();
                System.out.println(adapter.getItem(0).toString());

                for (int i = 0; i < listViewItemList.size(); i++) {
                    System.out.println("*********" + listViewItemList.get(i).getName());
                    System.out.println("********" + listViewItemList.get(i).getProperty());
                    if (i == 0) {
                        attr_str = listViewItemList.get(i).getProperty();
                    } //속성저장
                    else {
                        attr_str = attr_str.concat("" + listViewItemList.get(i).getProperty());
                    } //띄어쓰기로 속성 구분하면 안됨
                    System.out.println("***********" + attr_str);
                }


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
                String demo = dirPath + "/dec"; //dec 폴더 생성

                System.out.println("============" + dirPath);

                File file = new File(demo);
                file.mkdir();

                //일치하는 폴더가 없으면 폴더 생성
                if (!file.exists()) {
                    file.mkdir();
                    System.out.println("make dir success");
                }

                pubfile = dirPath + "/demo/pub_key";  //경로에 생성
                mskfile = dirPath + "/demo/master_key"; //경로에 생성
                prvfile = dirPath + "/demo/prv_key"; //경로에 생성
                decfile = dirPath + "/demo/input.new"; //복호화된 파일 생성


                dir = demo;
                System.out.println("=======================" + dir);


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

                System.out.println("//start to keygen");
                try {
                    enc.keygen(pubfile, prvfile, mskfile, attr_str); //키생성
                    System.out.println("//end to keygen");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //복호화
//                System.out.println("//start to dec");
//                try {
//                    System.out.println(pubfile);
//                    System.out.println(mskfile);
//                    System.out.println(prvfile);
//                    enc.dec(pubfile, prvfile, encfile, decfile);
//                    System.out.println("//end to dec");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        });
    }


    //어뎁터 시작
    public class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return filteredItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int pos = filteredItemList.get(position).getNum();
            final Context context = parent.getContext();
            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.lyt_listview_list, parent, false);
                holder.editText1 = (EditText) convertView.findViewById(R.id.editText1);
                holder.editText2 = (EditText) convertView.findViewById(R.id.editText2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ref = position;

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            final EditText editText1 = (EditText) convertView.findViewById(R.id.editText1);

            // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
            final ListItem listViewItem = filteredItemList.get(position);

            holder.editText1.setText(listViewItem.getName());
            holder.editText2.setText(listViewItem.getProperty());


            holder.editText1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    filteredItemList.get(holder.ref).setName(s.toString());
                }
            });
            holder.editText2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    filteredItemList.get(holder.ref).setProperty(s.toString());
                }
            });

            return convertView;
        }


        public void addItem(String name, String property, int num) {
            ListItem item = new ListItem();
            item.setName(name);
            item.setProperty(property);
            item.setNum(num);

            listViewItemList.add(item);
        }

        public void delItem() {
            if (listViewItemList.size() < 1) {
            } else {
                listViewItemList.remove(listViewItemList.size() - 1);
            }
        }
    }
}
