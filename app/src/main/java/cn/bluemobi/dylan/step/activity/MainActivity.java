package cn.bluemobi.dylan.step.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.bluemobi.dylan.step.R;
import cn.bluemobi.dylan.step.adapter.RecordAdapter;
import cn.bluemobi.dylan.step.database.DbAdapter;
import cn.bluemobi.dylan.step.record.PathRecord;
import cn.bluemobi.dylan.step.step.UpdateUiCallBack;
import cn.bluemobi.dylan.step.step.service.StepService;
import cn.bluemobi.dylan.step.step.utils.SharedPreferencesUtils;
import cn.bluemobi.dylan.step.view.StepArcView;

/**
 * 记步主页
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_data;
    private StepArcView cc;
    private TextView tv_set;
    private TextView tv_isSupport;
    private SharedPreferencesUtils sp;
    private Button startRun;
    private ImageView next;
    private TextView journeyNum;
    private TextView time;
    private void assignViews() {
        tv_data = (TextView) findViewById(R.id.tv_data);
        cc = (StepArcView) findViewById(R.id.cc);
        tv_set = (TextView) findViewById(R.id.tv_set);
        tv_isSupport = (TextView) findViewById(R.id.tv_isSupport);
        startRun = findViewById(R.id.startRun);
        next = findViewById(R.id.next);
        time = findViewById(R.id.runTime);
        journeyNum = findViewById(R.id.journeyNum);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        initData();
        addListener();
        setLastPath();
    }

    private void addListener() {
        tv_set.setOnClickListener(this);
        tv_data.setOnClickListener(this);
        startRun.setOnClickListener(this);
        next.setOnClickListener(this);
    }


    private void initData() {
        sp = new SharedPreferencesUtils(this);
        //获取用户设置的计划锻炼步数，没有设置过的话默认7000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        //设置当前步数为0
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        tv_isSupport.setText("计步中...");
        setupService();
    }


    private  void setLastPath(){
        DbAdapter mDataBaseHelper = new DbAdapter(this);
        PathRecord mRecord = new PathRecord();
        mDataBaseHelper.open();
        if(mDataBaseHelper.queryLastRecord()!=null){
        mRecord = mDataBaseHelper.queryLastRecord();
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
        if(mRecord.getDistance()<1000){
        journeyNum.setText(decimalFormat.format(mRecord.getDistance()));
        }else{
            journeyNum.setText(decimalFormat.format(mRecord.getDistance()/1000d));
        }
        int seconds =(int) mRecord.getDuration();
        time.setText(String.format(Locale.CHINA,"%02d:%02d:%02d",seconds/3600 % 24, seconds / 60 % 60, seconds % 60));
        }
    }

    private boolean isBind = false;
    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
            cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
                    cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_set:
                startActivity(new Intent(this, SetPlanActivity.class));
                break;
            case R.id.tv_data:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case R.id.startRun:
                startActivity(new Intent(this, MapActivity.class));
                break;
            case R.id.next:
                startActivity(new Intent(this,RecordActivity.class));
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }
}
