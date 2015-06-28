package it.works.rehydrate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import it.works.it.dregenerate.R;

public class MainActivity extends Activity implements AfterAsyncTask {
    private TextView value;
    private double previousValue;
    private double currentValue;
    private RequestTask requestTask;
    private Context context;
    private Timer timer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initPolling();
        getCurrentValue();
        value = (TextView) findViewById(R.id.value);
        previousValue = Double.parseDouble(String.valueOf(value.getText()));
        context=this;
    }

    private void initPolling() {
        int delay = 0; // intial delay for 0 sec.
        int period = 10000; // repeat every 10 sec.

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Fetching weight form bottle ");
                try {
                    getCurrentValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay, period);
    }

    public void getCurrentValue() {
        requestTask = new RequestTask();
        requestTask.delegate = this;
        requestTask.execute("http://10.16.23.159:5000/getcurrentvalue.json");
    }
    @Override
    public void onComplete(String result) {
        currentValue = Double.parseDouble(result);
        System.out.println("Obtained weight is: " + currentValue);
        if (previousValue == currentValue) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Drink Water!!!")
                    .setMessage("You are Dehydrating....")
                    .create()
                    .show();
        } else {
            if(!requestTask.isCancelled()) requestTask.cancel(true);
            this.value.setText(String.valueOf(String.format("%.3f",currentValue)));
        }
        System.out.println("Completed...............");
    }

    public void refresh(View view) {
        previousValue = Double.parseDouble(String.valueOf(value.getText()));
        try {
            getCurrentValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
