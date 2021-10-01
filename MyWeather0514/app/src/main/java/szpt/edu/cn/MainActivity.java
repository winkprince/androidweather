package szpt.edu.cn;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Message;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private TextView textView;
    private Handler handler=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView =findViewById(R.id.textView);




        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj ;


                textView.setText(result);
            }
        };




    }

    public void startThread(View view) {
        GetData getDataThread = new GetData();
        getDataThread.start();

    }

    public class GetData extends Thread {
        @Override
        public void run() {
            super.run();


            try {
                getDataFromInternet();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }


    public void getDataFromInternet(){
        String urlString = "http://weather.123.duba.net/static/weather_info/101291001.html";
        String result="";
        URL url;
        try{
            url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader reader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader buffer = new BufferedReader(reader);
            String inputLine="";
            while((inputLine = buffer.readLine())!=null){
                result += inputLine;
            }
            reader.close();
            httpURLConnection.disconnect();
        }catch (IOException e){
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage();
        msg.obj = result;
        handler.sendMessage(msg);
    }

}