package com.example.uploadfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MenuAddGridView extends Activity {
    GridView gridview;
    SimpleAdapter gridviewAdapter;
    ImageButton openFolder,addSingle,addFolder,backFolder;
    String sdcardFilePath,thisFilePath,selectFilePath;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
        this.setContentView(R.layout.menuaddgridview);
        
        sdcardFilePath=Environment.getExternalStorageDirectory().getAbsolutePath();//�õ�sdcardĿ¼
        thisFilePath=sdcardFilePath;
        
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
        //����gridView������
        updategridviewAdapter(thisFilePath);
        gridview.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                selectFilePath=(String) item.get("ItemFilePath");
                
                if(item.get("type").equals("isDirectory"))//�ж��Ƿ����ļ���
                {
                    openFolder.setVisibility(View.VISIBLE);//���ļ���ť�ɼ�
                    addSingle.setVisibility(View.INVISIBLE);//ѡ������ť���ɼ�
                    addFolder.setVisibility(View.VISIBLE);//ѡ�������ļ��а�ť�ɼ�
                }
                else if(item.get("type").equals("isMp3"))
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
            this.setResult(0);
    }
    private File[] folderScan(String path)
    {
        File file=new File(path);
        File[] files=file.listFiles();
        return files;
    }
    //����gridView������
    private void updategridviewAdapter(String filePath)
    {
        File[] files=folderScan(filePath);
        ArrayList<HashMap<String, Object>> lstImageItem = getFileItems(files);
        gridviewAdapter = new SimpleAdapter(MenuAddGridView.this,lstImageItem,R.layout.menuaddgridview_item,new String[] {"ItemImage","ItemText"}, new int[] {R.id.MenuAddGridView_ItemImage,R.id.MenuAddGridView_ItemText});
        gridview.setAdapter(gridviewAdapter);
        gridviewAdapter.notifyDataSetChanged();
    }
    //�б�ѭ���ж��ļ�����Ȼ���ṩ���ݸ�Adapter��
    private ArrayList<HashMap<String, Object>> getFileItems(File[] files)
    {
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        //ѭ������listImageItem����
        if(files==null)
        {
            return null;
        }
        for(int i=0;i<files.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String fileName=files[i].getName();//�õ�file��
            map.put("ItemText", fileName);
            if(files[i].isDirectory())//�ж��Ƿ����ļ���
            {
                map.put("ItemImage", R.drawable.folder);//��ʾ�ļ���ͼ��
                map.put("type", "isDirectory");
            }
            else if(files[i].isFile())//�ж��Ƿ����ļ�
            {
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
            }
            map.put("ItemFilePath", files[i].getAbsolutePath());//�����ļ�����·��
            
            lstImageItem.add(map);
        }
        return lstImageItem;
    }
    private ArrayList<String> getResultArrayList(ArrayList<HashMap<String, Object>> al)
    {
        ArrayList<String> musicResult=new ArrayList<String>();
        for(int i=0;i<al.size();i++)
        {
            HashMap<String, Object> map=al.get(i);
            String type=(String) map.get("type");
            String itemFilePath=(String) map.get("ItemFilePath");
            if(type.equals("isMp3"))
            {
                musicResult.add(itemFilePath);
            }
        }
        return musicResult;
    }
    class buttonOnClickListener implements OnClickListener
    {
        ArrayList<String> Result;
        Intent intent;
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.MenuAddGridView_button_openfolder://���ļ���
                    updategridviewAdapter(selectFilePath);//��ȡ�ļ��������ݲ���ʾ
                    thisFilePath=selectFilePath;//��¼��ǰĿ¼·��
                    openFolder.setVisibility(View.INVISIBLE);
                    addSingle.setVisibility(View.INVISIBLE);
                    addFolder.setVisibility(View.INVISIBLE);
                    break;
                case R.id.MenuAddGridView_button_addSingle:
                    Result=new ArrayList<String>();
                    Result.add(selectFilePath);
                    intent=new Intent(MenuAddGridView.this,MainActivity.class);
                    intent.putStringArrayListExtra("Result", Result);
                    intent.putExtra("verify", "ok");
                //    MenuAddGridView.this.setResult(1, intent);
                    startActivity(intent);
                    MenuAddGridView.this.onDestroy();
                    break;
                case R.id.MenuAddGridView_button_addFolder:
                    //�õ��ļ���������mp3�ļ�
                    Result=getResultArrayList(getFileItems(folderScan(selectFilePath)));
                    intent=new Intent();
                    //���ظ���һ��activity����
                    intent.putStringArrayListExtra("Result", Result);
                    MenuAddGridView.this.setResult(1, intent);
                    MenuAddGridView.this.onDestroy();
                    break;
                case R.id.MenuAddGridView_button_backFolder://�����ϼ�Ŀ¼
                    if(!thisFilePath.equals(sdcardFilePath))
                    {
                        File thisFile=new File(thisFilePath);//�õ���ǰĿ¼
                        String parentFilePath=thisFile.getParent();//�ϼ���Ŀ¼·��
                        updategridviewAdapter(parentFilePath);//����ϼ�Ŀ¼���ݲ���ʾ
                        thisFilePath=parentFilePath;//���õ�ǰĿ¼·��
                        
                        openFolder.setVisibility(View.INVISIBLE);
                        addSingle.setVisibility(View.INVISIBLE);
                        addFolder.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        MenuAddGridView.this.onDestroy();
                    }
                    break;
            }
        }
        
    }
    protected void onDestroy() {
        this.finish();
        super.onDestroy();
    }
    
}