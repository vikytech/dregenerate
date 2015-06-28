package it.works.rehydrate;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask implements AfterAsyncTask {

    @Override
    public void run() {
        RequestTask requestTask = new RequestTask();
        requestTask.delegate = this;
        requestTask.execute("http://10.16.23.159:5000/getcurrentvalue.json");
    }

    @Override
    public void onComplete(String result) {
    }
}
