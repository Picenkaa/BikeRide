package lt.testas.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import lt.testas.myapplication.Model.Users;
import lt.testas.myapplication.Prevalent.Prevalent;

public class MainActivity extends AppCompatActivity {
private Button login,register;
    private ProgressBar loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login =(Button) findViewById(R.id.login);
        register =(Button) findViewById(R.id.register);
        loadingbar = new ProgressBar(this);
        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register_Activity.class);
                startActivity(intent);
            }
        });
String emailkey = Paper.book().read(Prevalent.UserEmailkey);
        String passkey = Paper.book().read(Prevalent.UserPasskey);
if(emailkey!="" && passkey != ""){
    if(!TextUtils.isEmpty(emailkey) && !TextUtils.isEmpty(passkey)){
        AllowAccess(emailkey,passkey);
        loadingbar.isShown();
        loadingbar.setVisibility(View.VISIBLE);
    }
}

    }
    public String EncodeString(String string) {
        return string.replace(".", ",");
    }
    public String DecodeString(String string) {
        return string.replace(",", ".");
    }

    private void AllowAccess(final String pastas, final String slapt) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(EncodeString(pastas)).exists()){
                    Users usersData = snapshot.child("Users").child(EncodeString(pastas)).getValue(Users.class);
                    if(usersData.getEmail().equals(EncodeString(pastas))){
                        if(usersData.getPass().equals(slapt)){
                            Toast.makeText(MainActivity.this, "Sėkmingai prisijungta", Toast.LENGTH_SHORT).show();
                            loadingbar.setVisibility(View.GONE);
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Neteisingi prisijungimo duomenys", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else
                {
                    Toast.makeText(MainActivity.this, "vartotojas su tokiu e.paštu "+pastas+" neegzituoja", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Susikurkite paskyrą, arba teisingai įveskite duomenis", Toast.LENGTH_SHORT).show();
                    loadingbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}