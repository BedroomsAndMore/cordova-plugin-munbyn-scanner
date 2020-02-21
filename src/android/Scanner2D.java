package cordova.plugin.ipda0502d.scanner;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import com.zebra.adc.decoder.Barcode2DWithSoft;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class Scanner2D extends CordovaPlugin {
    ScanDevice sm;
	private final static String SCAN_ACTION = "scan.rcv.message";
	private final static String EVENT_PREFIX = "scanner";
    private CallbackContext mMainCallback;

    @Override
    public void onResume(boolean multitasking) {

       
       // super.onResume(multitasking);
      /*  IntentFilter filter = new IntentFilter();
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
        mMainCallback.sendPluginResult(pluginResult);*/
        super.onResume(multitasking);
    
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("init".equals(action)) {
			mMainCallback = callbackContext;
			this.onResume(false);
			return true;
		} /*else if("coolMethod".equals(action)) {
            String message = args.getString(0);
            this.coolMethod(action, callbackContext);
            return true;
        }*/
        callbackContext.error(action + " is not a supported action");
		return false;
    }
}
