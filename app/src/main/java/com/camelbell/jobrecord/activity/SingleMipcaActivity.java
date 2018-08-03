/****************************************************************
 *
 *	File name:	App.java
 *
 *	This file contain App class.
 *
 *	Last update: 	June 2, 2008.
 *
 *	Programmer:	Eran Porat
 *
 *	Copyright (c) 3GVision LTD.
 *
 *****************************************************************/

package com.camelbell.jobrecord.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.camelbell.jobrecord.R;
import com.threegvision.products.inigma_sdk_pro.ReferenceApp.AimView;
import com.threegvision.products.inigma_sdk_pro.ReferenceApp.AppView;
import com.threegvision.products.inigma_sdk_pro.sdk_pro.CTimedLog;
import com.threegvision.products.inigma_sdk_pro.sdk_pro.SDK;

import java.nio.charset.Charset;

import cn.yohoutils.StringUtil;

public class SingleMipcaActivity extends Activity implements SDK.Observer {
    private static final int SCAN_ID = Menu.FIRST;
    private static final int STOP_ID = Menu.FIRST + 1;
    private static final int ACQUIRE_ID = Menu.FIRST + 2;
    private static final int REVOKE_ID = Menu.FIRST + 3;

    AppView view = null;
    SDK sdk = null;
    AimView aimview = null;
    boolean AcquiringLicense = false;
    static SingleMipcaActivity This = null;

    public SingleMipcaActivity() {
        This = this;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        view = (AppView) findViewById(R.id.AppView);
        view.SetText("请耐心等待摄像头启动，谢谢！");
        sdk = new SDK(this, this);
        if (!sdk.IsLicenseValid()) {
            sdk.AcquireLicense();
        }

        /**
         * 如果证书OK则进入的时候就应该启动扫描功能
         */
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Scan();
            }
        }, 1000);


}

    public void onDestroy() {
        if (sdk != null)
            sdk.Close();
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, SCAN_ID, 0, R.string.Scan).setShortcut('0', 's');
        menu.add(0, STOP_ID, 0, R.string.Stop).setShortcut('1', 'x');
        menu.add(0, ACQUIRE_ID, 0, R.string.Acquire).setShortcut('2', 'a');
        menu.add(0, REVOKE_ID, 0, R.string.Revoke).setShortcut('3', 'r');
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(SCAN_ID).setVisible(!sdk.IsScanning() && (sdk.IsLicenseValid() || sdk.IsLicenseTemporarilyValid()));
        menu.findItem(STOP_ID).setVisible(sdk.IsScanning());
        menu.findItem(ACQUIRE_ID).setVisible(!sdk.IsScanning() && !sdk.IsLicenseValid() && !AcquiringLicense);
        menu.findItem(REVOKE_ID).setVisible(!sdk.IsScanning() && sdk.IsLicenseValid());
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        CTimedLog.log("Menu item " + itemId);

        switch (itemId) {
            case SCAN_ID:
                Scan();
                return true;
            case STOP_ID:
                Stop();
                return true;
            case ACQUIRE_ID:
                AcquireLicense();
                return true;
            case REVOKE_ID:
                RevokeLicense();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void Scan() {
        closeOptionsMenu();
        view.SetText("");
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        android.view.Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        Rect previewrect = new Rect(0, 0, width, height);
        CTimedLog.log("StartScanning called");
        ViewGroup sdkviewgroup = sdk.StartScanning(previewrect, SDK.Decode_QR | SDK.Decode_DataMatrix | SDK.Decode_EAN8 | SDK.Decode_EAN13 | SDK.Decode_EAN39 | SDK.Decode_EAN128 | SDK.Decode_PDF | SDK.Decode_NW7 | SDK.Decode_I2OF5 | SDK.Decode_GS1 | SDK.Decode_MICRO_QR | SDK.Decode_BlackOnWhite, SDK.Flash_Auto, 45);
        CTimedLog.log("StartScanning returned");
        if (sdkviewgroup != null) {
            int maxzoom = sdk.GetMaxZoom();
            sdk.Zoom(maxzoom / 2);
            aimview = new AimView(this);
            aimview.SetPreviewRect(previewrect);
            sdkviewgroup.addView(aimview);
            aimview.setVisibility(View.VISIBLE);
            setContentView(sdkviewgroup);
        }
    }

    void Stop() {
        sdk.Stop();
        if (aimview != null)
            aimview.setVisibility(View.GONE);
        closeOptionsMenu();
        setContentView(R.layout.main);
        view.SetText("");
    }

    void AcquireLicense() {
        AcquiringLicense = true;
        sdk.AcquireLicense();
        closeOptionsMenu();
        setContentView(R.layout.main);
        view.SetText("");
    }

    void RevokeLicense() {
        sdk.RevokeLicense();
        closeOptionsMenu();
        setContentView(R.layout.main);
        view.SetText("");
    }

    public void OnDecode(final int type, final int mode, final byte[] content) {
        Runnable r = new Runnable() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            public void run() {
                Stop();
                String tp = null;
                switch (type) {
                    case Type_EAN8:
                        tp = "EAN8";
                        break;
                    case Type_EAN13:
                        tp = "EAN13";
                        break;
                    case Type_QR:
                        tp = "QR";
                        break;
                    case Type_DataMatrix:
                        tp = "DataMatrix";
                        break;
                    case Type_EAN128:
                        tp = "EAN128";
                        break;
                    case Type_EAN39:
                        tp = "EAN39";
                        break;
                    case Type_PDF417:
                        tp = "PDF";
                        break;
                    case Type_NW7:
                        tp = "NW7";
                        break;
                    case Type_I2OF5:
                        tp = "I2OF5";
                        break;
                    case Type_GS1:
                        tp = "GS1";
                        break;
                    case Type_MICRO_QR:
                        tp = "MICRO_QR";
                        break;
                    default:
                        tp = "";
                }
                String md = null;
                switch (mode) {
                    case Mode_FUNC1:
                        md = "Func1";
                        break;
                    case Mode_FUNC2:
                        md = "Func2";
                        break;
                    default:
                        break;
                }

                String str = new String(content);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
                    str = new String(content, Charset.forName("ISO_8859_1"));
                String rawcontent = Raw2String(content);
                String word;
                if (md == null){
                    word = str + " (" + tp + ")" + " [" + rawcontent + "]";
                }else{
                    word = str + " (" + tp + ":" + md + ")" + " [" + rawcontent + "]";
                }


                if (StringUtil.isEmpty(word)) {
                    Toast.makeText(SingleMipcaActivity.this,
                            R.string.qr_codes_scan_nothing,Toast.LENGTH_SHORT).show();
                } else {
                    String code = subStringInfo(word);
                    Intent intent = new Intent();
                    intent.putExtra("codeNum", code);
                    setResult(400200, intent);
                    SingleMipcaActivity.this.finish();
                }
//                view.SetText(word);
            }
        };
        runOnUiThread(r);
    }

    //处理二维码的信息
    private String subStringInfo(String info) {
        String code  = "";
//        Toast.makeText(SingleMipcaActivity.this, "info:"+info, Toast.LENGTH_SHORT).show();
        String[] infos = info.split(" ");
//        Toast.makeText(SingleMipcaActivity.this, "infos size:"+infos.length, Toast.LENGTH_SHORT).show();
        for(int i = 0;i<infos.length;i++){
            if(infos[i].length() == 14||infos[i].length() == 15){
                code = infos[i];
            }
        }
        return code;
    }

    public void OnError(final int code) {
        final int error = code;
        Runnable r = new Runnable() {
            public void run() {
                Stop();
                String tp = "(to see menu, try to press and hold the multi-task button)";
                switch (error) {
                    case Error_NoError:
                        if (AcquiringLicense)
                            tp = "License acquired";
                        break;
                    case Error_CameraCanNotBeOpened:
                        tp = "Error_CameraCanNotBeOpened";
                        break;
                    case Error_ScaningTimeouted:
                        tp = "Error_ScaningTimeouted";
                        break;
                    case Error_LicenseNotAcquired:
                        tp = "Error_LicenseNotAcquired";
                        break;
                    case Error_GeneralError:
                        tp = "Error_GeneralError";
                        break;
                    case Error_APIError:
                        tp = "Error_APIError";
                        break;
                }
                view.SetText("请耐心等待摄像头启动，或者退出重试，谢谢！");
                AcquiringLicense = false;
            }
        };
        runOnUiThread(r);
    }

    String Raw2String(byte[] raw) {
        String res = new String();
        for (int i = 0; i < raw.length; i++) {
            if (i > 0)
                res += " ";
            //res+=""+(int)(raw[i]);
            res += String.format("%02x", raw[i]);
        }
        return res;
    }

    public void AutoFocus() {
        if (sdk != null)
            sdk.AutoFocus();
    }

    public static SingleMipcaActivity GetApp() {
        return This;
    }
}