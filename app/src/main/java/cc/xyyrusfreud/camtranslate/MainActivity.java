package cc.xyyrusfreud.camtranslate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    String st;
    Button btnStrt;
    Button btnHlp;
    Button btnExt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStrt = (Button) findViewById(R.id.btn1);
        btnHlp = (Button) findViewById(R.id.btn2);
        btnExt = (Button) findViewById(R.id.btn3);

        btnStrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivty2();
            }
        });
    }

    public void openActivty2() {
        Intent intent = new Intent(this, ExtractActivity.class);
        startActivity(intent);
    }
}
