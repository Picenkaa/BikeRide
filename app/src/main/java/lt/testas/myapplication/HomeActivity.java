package lt.testas.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout =(Button) findViewById(R.id.button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}