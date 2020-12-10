package com.example.datetest;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nlf.calendar.EightChar;
import com.nlf.calendar.Lunar;
import com.nlf.calendar.util.LunarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private TextView nongliTv;
    private TextView baziTv;
    private TextView wuxingTv;
    private TextView chongshaTv;
    private TextView pengzuTv;
    private TextView xishenTv;
    private TextView fushenTv;
    private TextView caishenTv;
    private TextView yiTv;
    private TextView jiTv;
    private Lunar lunarNow;
    private String nongliStr;
    private EightChar eightChar;
    private String baziStr;
    private TextView shichenTv;
    private int index = 0;
    SimpleDateFormat shichen = new SimpleDateFormat("HH:mm");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        //每分钟刷新时辰
        Timer Timer = new Timer();
        Timer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshData();
            }
        },0,60000);
        //每天00：00刷新天的数据
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,00);
        Date time = calendar.getTime();
        Timer dayTimer = new Timer();
        dayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                initData();
            }
        },time,1000*60*60*24);
//        Lunar lunar = new Lunar(2080, 4, 17);
//        Log.d("suangua", "-----  " + lunarNow.toFullString() + "  -----");
//        Log.d("suangua", "-----  " + lunar.toFullString() + "  -----");
    }

    /**
     * 获取数据
     */
    private void initData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshDayData();
            }
        });
    }

    private void refreshDayData() {
        Toast.makeText(MainActivity.this,"刷新定时天数据",Toast.LENGTH_SHORT).show();
        Log.d("shuaxinshuju","刷新定时天数据");
        //数据来源
        lunarNow = new Lunar(new Date());

        eightChar = lunarNow.getEightChar();

        //农历年月日
        nongliStr = "农历" + lunarNow.getMonthInChinese() + "月" + lunarNow.getDayInChinese();//农历日期
        nongliTv.setText(nongliStr);
        //八字年月日时
        baziStr = eightChar.getYear() + lunarNow.getYearShengXiao() + "年" + eightChar.getMonth() + "月" + eightChar.getDay() + "日" + eightChar.getTime() + "时";
        baziTv.setText(baziStr);


        //五行
        String wuxingStr = "五行  " + lunarNow.getDayNaYin();
        SpannableString wuxingSS = new SpannableString(wuxingStr);
        ForegroundColorSpan wuxingF = new ForegroundColorSpan(Color.parseColor("#000000"));
        wuxingSS.setSpan(wuxingF, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wuxingTv.setText(wuxingSS);
        //冲煞
        String chongsha = "冲煞  " + "冲" + lunarNow.getDayChongDesc() + "  煞" + lunarNow.getDaySha();
        SpannableString chongshaSS = new SpannableString(chongsha);
        ForegroundColorSpan chongshaF = new ForegroundColorSpan(Color.parseColor("#000000"));
        chongshaSS.setSpan(chongshaF, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        chongshaTv.setText(chongshaSS);
        //彭祖
        String pengzuStr = lunarNow.getPengZuGan() + "\n" + lunarNow.getPengZuZhi();
        pengzuTv.setText(pengzuStr);

        //喜神
        String xishenStr = "喜神" + lunarNow.getDayPositionXiDesc();
        SpannableString xishenSS = new SpannableString(xishenStr);
        ForegroundColorSpan xishenF = new ForegroundColorSpan(Color.parseColor("#000000"));
        xishenSS.setSpan(xishenF, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        xishenTv.setText(xishenSS);
        //福神
        String fushenStr = "福神" + lunarNow.getDayPositionFuDesc();
        SpannableString fushenSS = new SpannableString(fushenStr);
        ForegroundColorSpan fushenF = new ForegroundColorSpan(Color.parseColor("#000000"));
        fushenSS.setSpan(fushenF, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        fushenTv.setText(fushenSS);
        //财神
        String caishenStr = "财神" + lunarNow.getDayPositionCaiDesc();
        SpannableString caishenSS = new SpannableString(caishenStr);
        ForegroundColorSpan caishenF = new ForegroundColorSpan(Color.parseColor("#000000"));
        caishenSS.setSpan(caishenF, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        caishenTv.setText(caishenSS);
        //今日宜
        List<String> jinriYiJi = new ArrayList<>();
        jinriYiJi.addAll(lunarNow.getDayYi());
        String jinriYiStr = getReplace(jinriYiJi);
        yiTv.setText(jinriYiStr);
        //今日忌
        jinriYiJi.clear();
        jinriYiJi.addAll(lunarNow.getDayJi());
        String jinriJiStr = getReplace(jinriYiJi);
        jiTv.setText(jinriJiStr);
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"开始刷新数据--" + "目前是"+index++ + "次",Toast.LENGTH_SHORT).show();
                //时辰
                shichenTv.setText(LunarUtil.convertTime(shichen.format(new Date())) + "时");
                Log.d("shichen","当前循环次数 ： " + index + "  次");
            }
        });

    }

    /**
     * 切割字符串
     * @param jinriYiJi
     * @return
     */
    private String getReplace(List<String> jinriYiJi) {
        return jinriYiJi.toString().replace("[", "").replace("]", "").replace(",", " ");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //农历日期
        nongliTv = findViewById(R.id.nongli_tv);
        //八字年月日
        baziTv = findViewById(R.id.bazi_tv);
        //时辰
        shichenTv = findViewById(R.id.shichen_tv);

        //纳音五行
        wuxingTv = findViewById(R.id.wuxing_tv);
        //冲煞
        chongshaTv = findViewById(R.id.chongsha_tv);
        //彭祖
        pengzuTv = findViewById(R.id.pengzu_tv_2);

        //喜神
        xishenTv = findViewById(R.id.xishen_tv);
        //福神
        fushenTv = findViewById(R.id.fushen_tv);
        //财神
        caishenTv = findViewById(R.id.caishen_tv);

        //今日宜
        yiTv = findViewById(R.id.yi_tv2);
        //今日忌
        jiTv = findViewById(R.id.ji_tv2);


    }
}