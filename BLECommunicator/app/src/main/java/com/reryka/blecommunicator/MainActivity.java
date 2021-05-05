package com.reryka.blecommunicator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Bluetooth機能の有効化要求時の識別コード
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    // アダプタ：Bluetooth処理に必要
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android端末がBLEをサポートしているかの確認
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_is_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Bluetoothアダプタの取得
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            // Android端末がBluetoothをサポートしていない
            Toast.makeText(this, R.string.bluetooth_is_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Android端末のBluetooth機能の有効化要求
        requestBluetoothFeature();
    }

    // Android端末のBluetooth機能の有効化要求
    private void requestBluetoothFeature(){
        if(bluetoothAdapter.isEnabled())return;

        // デバイスのBluetooth機能が有効になっていないときは有効化要求（ダイアログ表示）
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent,REQUEST_ENABLE_BLUETOOTH);
    }

    // 機能の有効化ダイアログの操作結果
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        switch (requestCode){
            case REQUEST_ENABLE_BLUETOOTH:
                // Bluetooth有効化要求
                if(Activity.RESULT_CANCELED == resultCode){
                    // 有効にされなかった
                    Toast.makeText(this,R.string.bluetooth_is_not_working,Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}