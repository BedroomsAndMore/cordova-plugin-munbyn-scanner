package cordova.plugin.ipda0502d.scanner;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

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
import org.apache.cordova.*;
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


    String TAG="Scanner2D";
    String barCode="";
    EditText data1;
    Button btn;
    Barcode2DWithSoft barcode2DWithSoft=null;
    String seldata="ASCII";
    private ArrayAdapter adapterTagType;
    private Spinner spTagType;

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
    @Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);

		sm = new ScanDevice();
	}
    
  /*  HomeKeyEventBroadCastReceiver     receiver;
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
                // TODO Auto-generated method stub

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
*/
  /*  @Override
    protected void onResume() {
         super.onResume();
    }*/
 /*   @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy");
        if(barcode2DWithSoft!=null){
            barcode2DWithSoft.stopScan();
            barcode2DWithSoft.close();
        }
        super.onDestroy();
        //android.os.Process.killProcess(Process.myPid());
    }*/
   /* public Barcode2DWithSoft.ScanCallback  ScanBack= new Barcode2DWithSoft.ScanCallback(){
        @Override
        public void onScanComplete(int i, int length, byte[] bytes) {
            if (length < 1) {
                if (length == -1) {
                    data1.setText("Scan cancel");
                } else if (length == 0) {
                    data1.setText("Scan TimeOut");
                } else {
                    Log.i(TAG,"Scan fail");
                }
            }else{
                SoundManage.PlaySound(MainActivity.this, SoundManage.SoundType.SUCCESS);
                barCode="";


              //  String res = new String(dd,"gb2312");
                try {
                    Log.i("Ascii",seldata);
                    barCode = new String(bytes, 0, length, seldata);
                      zt();
                }
                catch (UnsupportedEncodingException ex)   {}
                data1.setText(barCode);
            }

        }
    };*/
   /* void zt() {

        Vibrator vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }*/
    private void ScanBarcode(){
        barcode2DWithSoft=Barcode2DWithSoft.getInstance();
        if(barcode2DWithSoft!=null) {
            Log.i(TAG,"ScanBarcode");

            barcode2DWithSoft.scan();
          // barcode2DWithSoft.setScanCallback(ScanBack);
        }
    }

  /*  public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==139 || keyCode==66){
            if(event.getRepeatCount()==0) {
                ScanBarcode();
                return true;
            }
        }
      //  return super.onKeyDown(keyCode, event);
    }*/
   // @Override
  /*  public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==139){
            if(event.getRepeatCount()==0) {
                barcode2DWithSoft.stopScan();
                return true;
            }
        }
      //  return super.onKeyUp(keyCode, event);
    }*/
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("init".equals(action)) {
			mMainCallback = callbackContext;
            //this.onResume(false);
            ScanBarcode();
			return true;
		} else if("onKeyDown".equals(action)) {
            String message = args.getString(0);
           // this.onKeyDown(action, callbackContext);
            return true;
        }
        callbackContext.error(action + " is not a supported action");
		return false;
    }
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

}


