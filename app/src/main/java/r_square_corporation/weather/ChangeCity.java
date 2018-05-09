package r_square_corporation.weather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChangeCity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city_layout);
        ImageButton backbutton = (ImageButton) findViewById(R.id.back_button);
        final EditText cityna =(EditText)findViewById(R.id.entercity);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cityna.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String city = cityna.getText().toString();
                Intent mainactive =new Intent(ChangeCity.this,MainActivity.class);
                mainactive.putExtra("citynam",city);

                startActivity(mainactive);
                return false;
            }
        });



    }
}
