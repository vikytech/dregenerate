package it.works.rehydrate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import it.works.it.dregenerate.R;

public class MainActivity extends Activity implements AfterAsyncTask {
    private TextView value;
    private int previousValue;
    private int currentValue;
    private static RequestTask requestTask;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCurrentValue();
        value = (TextView) findViewById(R.id.value);
        previousValue = Integer.parseInt(String.valueOf(value.getText()));
        context=this;
    }


    public void getCurrentValue() {
        requestTask = new RequestTask();
        requestTask.delegate = this;
        requestTask.execute("http://10.16.23.159:5000/getcurrentvalue.json");
    }

    @Override
    public void onComplete(String result) {
        currentValue = Integer.parseInt(result);
        if (previousValue == currentValue) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Drink Water!!!")
                    .setMessage("You are Dehydrating....")
                    .create()
                    .show();
        } else {
            this.value.setText(String.valueOf(currentValue));
        }
        System.out.println("Completed...............");
    }

    public void refresh(View view) {
        previousValue = Integer.parseInt(String.valueOf(value.getText()));
        try {
            getCurrentValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
