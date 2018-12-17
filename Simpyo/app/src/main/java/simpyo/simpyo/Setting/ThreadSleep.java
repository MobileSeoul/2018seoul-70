package simpyo.simpyo.Setting;

import android.os.AsyncTask;

public class ThreadSleep extends AsyncTask<Integer, Integer, Integer> {

    public ThreadSleep() {
        //set context variables if required
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        int seconds = integers[0];

        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       return 0;
    }


}