package com.mpaas.aar.demo.analytics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.mobile.framework.app.ui.BaseAppCompatActivity;
import com.mpaas.mas.adapter.api.MPLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by xingcheng on 2018/7/27.
 */

public class AnalyticsActivity extends BaseAppCompatActivity {

    private static final String TAG = "AnalyticsActivity";
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        MPLogger.reportLaunchTime(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.analytic_title);
        }

        // userId 这里根据不同设备生成了唯一的id，实际由业务方自定义
        userId = MPLogger.getUserId();

        initView();

        goTest();
    }

    private void initView() {

        ((TextView) findViewById(R.id.user_active_tips)).setText(getResources().getString(R.string.user_active_tips, userId));

        findViewById(R.id.user_active_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testUserActive();
            }
        });

        findViewById(R.id.behavior_log_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testBehaviorLog();
            }
        });

        findViewById(R.id.crash_log_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCrashLog();
            }
        });

        findViewById(R.id.block_log_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testBlockLog();
            }
        });

        findViewById(R.id.anr_log_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testANRLog();
            }
        });

        findViewById(R.id.automation_log_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testAutomationLog();
            }
        });

        findViewById(R.id.log_report_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testLogReport();
            }
        });
    }

    private void testUserActive() {
        // 设置 userId 并上报用户登录，白名单功能也将使用这里设置的 userId
        MPLogger.reportUserLogin(userId);
        // 只设置 userId 可调用
//        MPLogger.setUserId(userId);
        // 获取 userId
//        MPLogger.getUserId();

        Toast.makeText(this, getString(R.string.user_active_result), Toast.LENGTH_SHORT).show();
    }

    private void testBehaviorLog() {
        // logId 描述当前埋点的关键词，控制台自定义分析中事件id对应该字段
        // logId 不能为空
        String logId = "PayResults";

        // bizType 标识业务类型，相同 bizType 的自定义日志将遵循同样的上报策略，存储在同一个文件中，文件名为进程名_bizType
        // bizType 可以为空，默认为 userbehavor
        String bizType = "Pay";

        // params 为自定义参数，控制台自定义分析中事件的自定义属性对应这些键值对
        // params 可以为空
        Map<String, String> params = new HashMap<>();
        params.put("time", "2018-07-27");

        // 可调用多个重载方法
//        MPLogger.event(logId);
//        MPLogger.event(logId, bizType);
//        MPLogger.event(logId, params);
        MPLogger.event(logId, bizType, params);

        Toast.makeText(this, getString(R.string.behavior_log_result), Toast.LENGTH_SHORT).show();
    }

    private void testCrashLog() {
        throw new RuntimeException(getString(R.string.mock_crash));
    }

    private void testAutomationLog() {
        startActivity(new Intent(this, AutomationActivity.class));
    }

    private void testBlockLog() {
        Random random = new Random();
        int i = random.nextInt(3);
        if (i == 0) {
            makeBlock1();
        } else if (i == 1) {
            makeBlock2();
        } else {
            makeBlock3();
        }

        Toast.makeText(this, getString(R.string.block_log_result), Toast.LENGTH_SHORT).show();
    }

    private void makeBlock1() {
        MPLogger.info(TAG, "makeBlock1");
        doMakeBlock();
    }

    private void makeBlock2() {
        MPLogger.info(TAG, "makeBlock2");
        doMakeBlock();
    }

    private void makeBlock3() {
        MPLogger.info(TAG, "makeBlock3");
        doMakeBlock();
    }

    private void doMakeBlock() {
        // 模拟一段卡顿，框架自动捕获
        int time = 3 * 1000;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            MPLogger.warn(TAG, e);
        }
    }

    private void testANRLog() {
        // 模拟一段卡死，框架自动捕获
        int time = 6 * 1000;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            MPLogger.warn(TAG, e);
        }

        Toast.makeText(this, getString(R.string.anr_log_result), Toast.LENGTH_SHORT).show();
    }

    private void testLogReport() {
        MPLogger.uploadAll();
        Toast.makeText(this, getString(R.string.report_log_result), Toast.LENGTH_SHORT).show();
    }

    // 内部测试使用，开发者无需关注
    private void goTest() {
        try {
            Class healthActivity = Class.forName("com.mpaas.diagnose.ui.HealthBizSelectActivity");
            Intent intent = new Intent(this, healthActivity);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
