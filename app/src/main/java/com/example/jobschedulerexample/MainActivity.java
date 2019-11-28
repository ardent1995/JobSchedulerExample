package com.example.jobschedulerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int JOB_ID = 123;
    private Button btnScheduleJob, btnCancelJob;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScheduleJob = findViewById(R.id.btn_schedule_job);
        btnCancelJob = findViewById(R.id.btn_cancel_job);

        btnScheduleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName componentName = new ComponentName(MainActivity.this,ExampleJobService.class);
                JobInfo jobInfo = new JobInfo.Builder(JOB_ID,componentName)
                        .setRequiresCharging(true)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                        .setPersisted(true)
                        .setPeriodic(15*60*1000)
                        .build();

                JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                int resultCode = jobScheduler.schedule(jobInfo);
                if(resultCode == JobScheduler.RESULT_SUCCESS){
                    Log.d(TAG, "btnScheduleJob onClick: Job Scheduled");
                }else {
                    Log.d(TAG, "btnScheduleJob onClick: Job Scheduling failed");
                }
            }
        });
        btnCancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                jobScheduler.cancel(JOB_ID);
                Log.d(TAG, "btnCancelJob onClick: Job Canceled");
            }
        });
    }
}
