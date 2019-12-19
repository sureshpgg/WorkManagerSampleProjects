package com.example.workmanager_basic_function;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String MESSAGE_STATUS = "message_status";
    TextView tvStatus;
    Button btnSend, btnStorageNotLow, btnBatteryNotLow, btnRequiresCharging, btnDeviceIdle,
            btnNetworkType;
    OneTimeWorkRequest mRequest;
    WorkManager mWorkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        tvStatus = findViewById(R.id.tvStatus);
        btnSend = findViewById(R.id.btnSend);
        mWorkManager = WorkManager.getInstance();
    }

    private void initViews() {
        tvStatus = findViewById(R.id.tvStatus);
        btnSend = findViewById(R.id.btnSend);
        btnStorageNotLow = findViewById(R.id.buttonStorageNotLow);
        btnBatteryNotLow = findViewById(R.id.buttonBatteryNotLow);
        btnRequiresCharging = findViewById(R.id.buttonRequiresCharging);
        btnDeviceIdle = findViewById(R.id.buttonDeviceIdle);
        btnNetworkType = findViewById(R.id.buttonNetworkType);
        btnSend.setOnClickListener(this);
        btnStorageNotLow.setOnClickListener(this);
        btnBatteryNotLow.setOnClickListener(this);
        btnRequiresCharging.setOnClickListener(this);
        btnDeviceIdle.setOnClickListener(this);
        btnNetworkType.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        tvStatus.setText("");
        Constraints mConstraints;
        switch (v.getId()) {
            case R.id.btnSend:
                mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
                break;
            case R.id.buttonStorageNotLow:
                /**
                 * Constraints
                 * If TRUE task execute only when storage's is not low
                 */
                mConstraints = new Constraints.Builder().setRequiresStorageNotLow(true).build();
                /**
                 * OneTimeWorkRequest with requiresStorageNotLow Constraints
                 */
                mRequest =
                        new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(mConstraints)
                                .build();

                break;
            case R.id.buttonBatteryNotLow:
                /**
                 * Constraints
                 * If TRUE task execute only when battery isn't low
                 */
                mConstraints = new Constraints.Builder().setRequiresBatteryNotLow(true).build();
                /**
                 * OneTimeWorkRequest with requiresBatteryNotLow Constraints
                 */
                mRequest =
                        new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(mConstraints)
                                .build();
                break;
            case R.id.buttonRequiresCharging:
                /**
                 * Constraints
                 * If TRUE while the device is charging
                 */
                mConstraints = new Constraints.Builder().setRequiresCharging(true).build();
                /**
                 * OneTimeWorkRequest with requiresCharging Constraints
                 */
                mRequest =
                        new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(mConstraints)
                                .build();
                break;
            case R.id.buttonDeviceIdle:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    /**
                     * Constraints
                     * If TRUE while the  device is idle
                     */
                    mConstraints = new Constraints.Builder().setRequiresDeviceIdle(true).build();
                    /**
                     * OneTimeWorkRequest with requiresDeviceIdle Constraints
                     */
                    mRequest =
                            new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(mConstraints)
                                    .build();
                }
                break;
            case R.id.buttonNetworkType:
                /**
                 * Constraints
                 * Network type is conneted
                 */
                mConstraints =
                        new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();


                /**
                 * OneTimeWorkRequest with requiredNetworkType Connected Constraints
                 */
                mRequest =
                        new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(mConstraints)
                                .build();
                break;
            default:
                break;
        }
        /**
         * Fetch the particular task status using request ID
         */
        mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    tvStatus.append(state.toString() + "\n");
                }
            }
        });
        /**
         * Enqueue the WorkRequest
         */
        mWorkManager.enqueue(mRequest);
    }
}
