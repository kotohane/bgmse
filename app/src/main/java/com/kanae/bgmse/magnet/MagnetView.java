package com.kanae.bgmse.magnet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kanae.bgmse.R;

public class MagnetView extends LinearLayout {

    Magnet data;
    LayoutInflater layoutInflater;

    LinearLayout linearLayout;
    TextView textViewLabel;
    TextView textViewContent;

    private float posX;
    private float posY;

    public MagnetView(Context context, Magnet magnet){
        super(context);
        data = magnet;
        initView(context);
        setData();
    }

    private void initView(Context context){
        layoutInflater.from(context).inflate(R.layout.magnet, this, true);
        linearLayout = (LinearLayout) findViewById(R.id.magnet_layout);
        textViewLabel = (TextView) findViewById(R.id.magnet_label);
        textViewContent = (TextView) findViewById(R.id.magnet_content);
    }

    public void setData(){
        textViewLabel.setText(data.getLabel());
        textViewContent.setText(data.getContent());
    }

    public int getMusicFun(){
        return data.getMusicFun();
    }

    public String getLabel(){
        return data.getLabel();
    }

    public void setFavColor(){
        textViewLabel.setTextColor(this.getResources().getColor(R.color.colorFavHighlight));
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                posX = event.getX();
                posY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                setTranslationX(getX() + (event.getX() - posX));
                setTranslationY(getY() + (event.getY() - posY));
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }*/

}
