package cc.xyyrusfreud.camtranslate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ActivityTranslation extends AppCompatActivity {

    Toolbar toolbar;
    EditText Et1;
    EditText Et2;
    String st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CamTranslate");

        Et1 = (EditText) findViewById(R.id.et1);
        Et2 = (EditText) findViewById(R.id.et2);
        Bundle bundle = getIntent().getExtras();

        st = bundle.getString("Value");
        Et1.setText(st);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                edit();
                break;
            case R.id.back:
                back();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void edit() {
        Et1.setEnabled(true);
        Et2.setEnabled(true);
    }

    public void back() {
        Intent intent = new Intent(this, ExtractActivity.class);
        Et1.getText().clear();
        Et2.getText().clear();
        startActivity(intent);
    }
}





