package ysn.com.demo.checkview;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ysn.com.view.checkview.CheckView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckView checkView1, checkView2, checkView3, checkView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkView1 = findViewById(R.id.main_activity_check_view1);
        checkView2 = findViewById(R.id.main_activity_check_view2);
        checkView3 = findViewById(R.id.main_activity_check_view3);
        checkView4 = findViewById(R.id.main_activity_check_view4);

        checkView1.setOnClickListener(this);
        checkView2.setOnClickListener(this);
        checkView3.setOnClickListener(this);
        checkView4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_activity_check_view1:
                checkView1.toggle(true);
                break;
            case R.id.main_activity_check_view2:
                checkView2.toggle(true);
                break;
            case R.id.main_activity_check_view3:
                checkView3.toggle();
                break;
            case R.id.main_activity_check_view4:
                checkView4.toggle(true);
                break;
            default:
        }
    }
}
