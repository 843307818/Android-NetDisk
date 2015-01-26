package com.example.uploadfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.uploadfiles.MenuAddGridView.buttonOnClickListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MenuAddGridView_Down extends Activity{
	GridView gridview;
    SimpleAdapter gridviewAdapter;
    ImageButton openFolder,addSingle,addFolder,backFolder;
    String sdcardFilePath,thisFilePath,selectFilePath;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
        this.setContentView(R.layout.menuaddgridview);
        
        openFolder=(ImageButton) this.findViewById(R.id.MenuAddGridView_button_openfolder);
        openFolder.setVisibility(View.INVISIBLE);//���ò��ɼ�
        openFolder.setOnClickListener(new buttonOnClickListener());//��Ӽ�����
        addSingle=(ImageButton) this.findViewById(R.id.MenuAddGridView_button_addSingle);
        addSingle.setVisibility(View.INVISIBLE);
        addSingle.setOnClickListener(new buttonOnClickListener());
        addFolder=(ImageButton) this.findViewById(R.id.MenuAddGridView_button_addFolder);
        addFolder.setVisibility(View.INVISIBLE);
        addFolder.setOnClickListener(new buttonOnClickListener());
        backFolder=(ImageButton) this.findViewById(R.id.MenuAddGridView_button_backFolder);
        backFolder.setOnClickListener(new buttonOnClickListener());
        
        gridview=(GridView) this.findViewById(R.id.MenuAddGridView_gridview);
        updategridviewAdapter();
        
        gridview.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                selectFilePath = (String) item.get("ItemText");
                //selectFilePath=(String) item.get("ItemFilePath");
                
                /*if(item.get("type").equals("isDirectory"))//�ж��Ƿ����ļ���
                {
                    openFolder.setVisibility(View.VISIBLE);//���ļ���ť�ɼ�
                    addSingle.setVisibility(View.INVISIBLE);//ѡ������ť���ɼ�
                    addFolder.setVisibility(View.VISIBLE);//ѡ�������ļ��а�ť�ɼ�
                }*/
                if(item.get("type").equals("isMp3"))
                {
                    openFolder.setVisibility(View.INVISIBLE);
                    addSingle.setVisibility(View.VISIBLE);
                    addFolder.setVisibility(View.INVISIBLE);
                }
                else
                {
                    openFolder.setVisibility(View.INVISIBLE);
                    addSingle.setVisibility(View.VISIBLE);
                    addFolder.setVisibility(View.INVISIBLE);
                }
                
            }});
	}
	
	private void updategridviewAdapter()
    {
        //File[] files=folderScan(filePath);
		Bundle b = new Bundle();
		b = this.getIntent().getExtras();
		ArrayList<String> namelist = b.getStringArrayList("namelist");
		String names[] = (String[])namelist.toArray(new String[namelist.size()]);
		ArrayList<HashMap<String, Object>> lstImageItem = getFileItems(names);
		
		
        gridviewAdapter = new SimpleAdapter(MenuAddGridView_Down.this,lstImageItem,R.layout.menuaddgridview_item,new String[] {"ItemImage","ItemText"}, new int[] {R.id.MenuAddGridView_ItemImage,R.id.MenuAddGridView_ItemText});
        gridview.setAdapter(gridviewAdapter);
        gridviewAdapter.notifyDataSetChanged();
    }
	
	private ArrayList<HashMap<String, Object>> getFileItems(String[] names)
    {
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        //ѭ������listImageItem����
        if(names==null)
        {
            return null;
        }
        for(int i=0;i<names.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String fileName=names[i];//�õ�file��
            map.put("ItemText", fileName);
            /*if(files[i].isDirectory())//�ж��Ƿ����ļ���,ֱ���ų��������
            {
                map.put("ItemImage", R.drawable.folder);//��ʾ�ļ���ͼ��
                map.put("type", "isDirectory");
            }*/
            //else if(files[i].isFile())//�ж��Ƿ����ļ�
            //{
                if(fileName.contains(".mp3"))//�ж��Ƿ���MP3�ļ�
                {
                    map.put("ItemImage", R.drawable.mp3flie);//����MP3ͼ��
                    map.put("type", "isMp3");
                }
                else
                {
                    map.put("ItemImage", R.drawable.otherfile);//�����MP3�ļ�ͼ��
                    map.put("type", "isOthers");
                }
            //}
            //map.put("ItemFilePath", files[i].getAbsolutePath());//�����ļ�����·��
            
            lstImageItem.add(map);
        }
        return lstImageItem;
    }
	
	
	class buttonOnClickListener implements OnClickListener
    {
        ArrayList<String> Result;
        Intent intent;
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.MenuAddGridView_button_addSingle:
                    Result=new ArrayList<String>();
                    Result.add(selectFilePath);
                    intent=new Intent(MenuAddGridView_Down.this,MainActivity.class);
                    intent.putStringArrayListExtra("Result_down", Result);
                    intent.putExtra("verify", "ok");
                //    MenuAddGridView.this.setResult(1, intent);
                    startActivity(intent);
                    MenuAddGridView_Down.this.onDestroy();
                    break;
            }
        }
        
    }
	
	protected void onDestroy() {
        this.finish();
        super.onDestroy();
    }

}
