package com.freaky.id.moviecatalogue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public EditText mEtFind;
    public Button mBtnFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            mEtFind = findViewById(R.id.et_title);
            mBtnFind = findViewById(R.id.btn_cari);

    }

}
