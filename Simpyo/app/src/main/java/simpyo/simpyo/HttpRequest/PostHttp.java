package simpyo.simpyo.HttpRequest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import simpyo.simpyo.Setting.Setting;

public class PostHttp extends AsyncTask<String, String, String> {

    public PostHttp() {
        //set context variables if required
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0]; // URL to call

        String postParameter = null;
        String res = null;

        for (int i = 1; i < params.length; i++) {
            if (i == 1) postParameter = "param1=" + params[i];
            else postParameter = postParameter+"&param"+i+"=" + params[i];
        }

        Log.d(Setting.Tag,"Parameter : "+postParameter);
        OutputStream out = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);

            out = new BufferedOutputStream(urlConnection.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(postParameter);
            writer.flush();
            writer.close();
            out.close();

            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = br.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            br.close();

            res = response.toString();

            Log.d("SiriusKp", res); // 로그 띄우기기

            urlConnection.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }


}