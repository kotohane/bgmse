package com.kanae.bgmse.file;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kanae.bgmse.R;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.music.MusicPool;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.kanae.bgmse.MainActivity.musicPool;

public class FileAddActivity extends AppCompatActivity {

    TextView textViewChoosePath;
    TextView textViewInputContent;
    TextView textViewExit;
    TextView textViewAdd;
    RadioGroup radioGroup;

    String select_file_path;
    String main_path;
    String input_content;
    int checkedId;

    SourceChooser sourceChooser;
    MagnetSaver magnetSaver = new MagnetSaver();
    FileCopy fc = new FileCopy();

    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);

        textViewChoosePath = (TextView)findViewById(R.id.text_init_choose_path);
        textViewInputContent = (TextView)findViewById(R.id.text_init_input_content);

        textViewExit = (TextView)findViewById(R.id.text_add_items_exit);
        textViewAdd = (TextView)findViewById(R.id.text_add_items_add);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroupType);


        checkedId = 2;
        select_file_path = "";
        main_path  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bgmse";

        //exit
        textViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileAddActivity.this.finish();
            }
        });

        //add
        textViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openFile();
                input_content = textViewInputContent.getText().toString();
                if(add_file()){
                    FileAddActivity.this.finish();
                }
            }
        });

        //choose file
        textViewChoosePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });

        radioGroup.setOnCheckedChangeListener(new MyRadioButtonListener());
        sourceChooser = new SourceChooser(FileAddActivity.this);

    }

    class MyRadioButtonListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int incheckedId) {
            switch (incheckedId) {
                case R.id.radioButtonSE:
                    checkedId = 0;
                    break;
                case R.id.radioButtonBGM:
                    checkedId = 1;
                    break;
            }
        }
    }

    private void openFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try{
            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    select_file_path = sourceChooser.getPath(this, uri);
                    textViewChoosePath.setText(select_file_path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean add_file(){
        File file = new File(select_file_path);
        if(!file.exists()){
            AlertDialog diag = new AlertDialog.Builder(this)
                    .setTitle("bgmse")
                    .setMessage("file path not legal")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            diag.show();
            return false;
        }
        if(input_content.isEmpty()){
            AlertDialog diag = new AlertDialog.Builder(this)
                    .setTitle("bgmse")
                    .setMessage("Please enter content")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            diag.show();
            return false;
        }
        if(checkedId ==2){
            AlertDialog diag = new AlertDialog.Builder(this)
                    .setTitle("bgmse")
                    .setMessage("Please choose sound type")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            diag.show();
            return false;
        }

        String file_name = file.getName();
        File dest_file = new File(main_path + "/se/"+file_name);
        fc.copyFile(dest_file,file);
        magnetSaver.AddMagnet(new Magnet(exExtensionName(file_name),input_content,checkedId,musicPool.getMagnetList().size()));
        musicPool = new MusicPool();
        Intent intent = new Intent();
        intent.setAction("action.refreshSEView");
        sendBroadcast(intent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String exExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

}
