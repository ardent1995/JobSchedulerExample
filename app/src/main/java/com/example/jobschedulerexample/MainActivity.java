package com.example.jobschedulerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int JOB_ID = 123;
    private Button btnScheduleJob, btnCancelJob,btnEnqueueJob;
    private EditText etInput;
    private static final String TAG = "MainActivity";
    public static final String KEY_INPUT_EXTRA = "inputExtra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScheduleJob = findViewById(R.id.btn_schedule_job);
        btnCancelJob = findViewById(R.id.btn_cancel_job);
        etInput = findViewById(R.id.et_input);
        btnEnqueueJob = findViewById(R.id.btn_enqueue_job);

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

        btnEnqueueJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jobIntentServiceIntent = new Intent(MainActivity.this,ExampleJobIntentService.class);
                jobIntentServiceIntent.putExtra(KEY_INPUT_EXTRA,etInput.getText().toString());
                ExampleJobIntentService.enqueueWork(MainActivity.this,jobIntentServiceIntent);
            }
        });
    }
}
