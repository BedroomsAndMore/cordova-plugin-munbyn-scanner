package cordova.plugin.ipda0502d.scanner;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.rscja.deviceapi.RFIDWithISO14443A;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class Scanner2D extends CordovaPlugin {
    //ScanDevice sm;
	private final static String SCAN_ACTION = "scan.rcv.message";
	private final static String EVENT_PREFIX = "scanner";
    private CallbackContext mMainCallback;


    String TAG="MainActivity";
    String barCode="";
    //EditText data1;
    Button btn;
    Barcode2DWithSoft barcode2DWithSoft=null;
    String seldata="ASCII";
    private ArrayAdapter adapterTagType;
    private Spinner spTagType;
    HomeKeyEventBroadCastReceiver     receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barcode2DWithSoft=Barcode2DWithSoft.getInstance();

        receiver = new HomeKeyEventBroadCastReceiver();
        registerReceiver(receiver, new IntentFilter("com.rscja.android.KEY_DOWN"));


        data1= (EditText) findViewById(R.id.editText);
        btn=(Button)findViewById(R.id.button);
        spTagType=(Spinner)findViewById(R.id.spTagType);
        adapterTagType = ArrayAdapter.createFromResource(this,
                R.array.arrayTagType, android.R.layout.simple_spinner_item);

        adapterTagType
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spTagType.setAdapter(adapterTagType);
        spTagType.setSelection(1);

        spTagType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                //获取选中值
                Spinner spinner = (Spinner) adapterView;
                 seldata = (String) spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanBarcode();


            }
        });

        new InitTask().execute();
    }

    @Override

    class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";//home key
        static final String SYSTEM_RECENT_APPS = "recentapps";//long home key

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.rscja.android.KEY_DOWN")) {
                int reason = intent.getIntExtra("Keycode",0);
                //getStringExtra
                boolean long1 = intent.getBooleanExtra("Pressed",false);
                // home key处理点
                if(reason==280 || reason==66){

                        ScanBarcode();


                }
               // Toast.makeText(getApplicationContext(), "home key="+reason+",long1="+long1, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub


            boolean reuslt=false;
            if(barcode2DWithSoft!=null) {
                reuslt=  barcode2DWithSoft.open(MainActivity.this);
                Log.i(TAG,"open="+reuslt);

            }
            return reuslt;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
//                barcode2DWithSoft.setParameter(324, 1);
//                barcode2DWithSoft.setParameter(300, 0); // Snapshot Aiming
//                barcode2DWithSoft.setParameter(361, 0); // Image Capture Illumination

                // interleaved 2 of 5
                barcode2DWithSoft.setParameter(6, 1);
                barcode2DWithSoft.setParameter(22, 0);
                barcode2DWithSoft.setParameter(23, 55);
                barcode2DWithSoft.setParameter(402, 1);
                Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_SHORT).show();
            }
            mypDialog.cancel();
        }

        @Override
        protected void onPreExecute() {
            
            super.onPreExecute();

            mypDialog = new ProgressDialog(MainActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }

    }


    @Override
    public void onResume(boolean multitasking) {

       
        super.onResume(multitasking);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        this.cordova.getActivity().registerReceiver(mScanReceiver, filter);

        if (sm.isScanOpened() && sm.getOutScanMode() != 0) {
            sm.setOutScanMode(0);
        }

        JSONArray jsEvent = new JSONArray();
        jsEvent.put(EVENT_PREFIX + "PluginResume");
        int isOpen  = (sm.isScanOpened() ? 1 : 0);
        int vibrate  = (sm.getScanVibrateState() ? 1 : 0);
        int beep  = (sm.getScanBeepState() ? 1 : 0);
        jsEvent.put(isOpen << 2 | vibrate << 1 | beep);
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, jsEvent);
        pluginResult.setKeepCallback(true);
        mMainCallback.sendPluginResult(pluginResult);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==139 || keyCode==66){
            if(event.getRepeatCount()==0) {
                ScanBarcode();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private void ScanBarcode(){
        if(barcode2DWithSoft!=null) {
            Log.i(TAG,"ScanBarcode");

            barcode2DWithSoft.scan();
            barcode2DWithSoft.setScanCallback(ScanBack);
        }
    }
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("init".equals(action)) {
			mMainCallback = callbackContext;
			this.onResume(false);
			return true;
		} else if("onKeyDown".equals(action)) {
            String message = args.getString(0);
            this.onKeyDown(action, callbackContext);
            return true;
        }
        callbackContext.error(action + " is not a supported action");
		return false;
    }
}
