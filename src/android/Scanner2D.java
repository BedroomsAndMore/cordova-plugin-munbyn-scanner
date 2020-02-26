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
    //EditText data1;
    Button btn;
    Barcode2DWithSoft barcode2DWithSoft=null;
    String seldata="ASCII";
    private ArrayAdapter adapterTagType;
    private Spinner spTagType;
    HomeKeyEventBroadCastReceiver     receiver;
   // @Override

    //@Override

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
    protected void onResume() {
        // TODO Auto-generated method stub

/*
        if (barcode2DWithSoft != null) {
            new InitTask().execute();
        }*/
        super.onResume();
    }

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
            //this.onResume(false);
            ScanBarcode();
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
