package lt.testas.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import lt.testas.myapplication.Model.Users;
import lt.testas.myapplication.Prevalent.Prevalent;

public class LoginActivity extends AppCompatActivity {

    private EditText innum, inpass;
    private Button login;
    private ProgressBar loadingbar;
    private String parentDbname = "Users";
    private com.rey.material.widget.CheckBox checkBoxme;
    private TextView AdminLink, NotAdminlink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login = (Button) findViewById(R.id.loginas);
        innum = (EditText) findViewById(R.id.login_email);
        inpass = (EditText) findViewById(R.id.login_pass);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminlink =(TextView) findViewById(R.id.not_admin_panel_link);
        loadingbar = new ProgressBar(this);
        checkBoxme = (com.rey.material.widget.CheckBox) findViewById(R.id.check);
        Paper.init(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginuser();
            }
        });
AdminLink.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        login.setText("Prisijungti kaip adminstratorius");
        AdminLink.setVisibility(View.INVISIBLE);
        NotAdminlink.setVisibility(View.VISIBLE);
        parentDbname = "Admins";
    }
});
NotAdminlink.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        login.setText("Prisijungti");
        AdminLink.setVisibility(View.VISIBLE);
        NotAdminlink.setVisibility(View.INVISIBLE);
        parentDbname = "Users";
    }
});



    }
    public String EncodeString(String string) {
        return string.replace(".", ",");
    }
    public String DecodeString(String string) {
        return string.replace(",", ".");
    }

            private void Loginuser() {
                String pastas = innum.getText().toString();
                String slapt = inpass.getText().toString();

                 if(TextUtils.isEmpty(pastas)){
                    Toast.makeText(LoginActivity.this, "Irašykite savo e.paštą", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(slapt)){
                    Toast.makeText(LoginActivity.this, "Irašykite slaptažodį", Toast.LENGTH_SHORT).show();
                }
                else{
                     loadingbar.isShown();
                   loadingbar.setVisibility(View.VISIBLE);
                  AllowAccesToAccount(pastas,slapt);
                 }



            }

    private void AllowAccesToAccount(final String pastas, final String slapt) {

if(checkBoxme.isChecked()){
    Paper.book().write(Prevalent.UserEmailkey,pastas);
    Paper.book().write(Prevalent.UserPasskey,slapt);
}

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbname).child(EncodeString(pastas)).exists()){
                    Users usersData = snapshot.child(parentDbname).child(EncodeString(pastas)).getValue(Users.class);
                    if(usersData.getEmail().equals(EncodeString(pastas))){
                        if(usersData.getPass().equals(slapt)){

                            if(parentDbname.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Sėkmingai prisijungta", Toast.LENGTH_SHORT).show();
                                loadingbar.setVisibility(View.GONE);
                                Intent intent = new Intent(LoginActivity.this,AdminAddNewProductActivity.class);
                                startActivity(intent);
                            } else if(parentDbname.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Sėkmingai prisijungta", Toast.LENGTH_SHORT).show();
                                loadingbar.setVisibility(View.GONE);
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }

                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Neteisingi prisijungimo duomenys", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else
                    {
                        Toast.makeText(LoginActivity.this, "vartotojas su tokiu e.paštu "+pastas+" neegzituoja", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "Susikurkite paskyrą, arba teisingai įveskite duomenis", Toast.LENGTH_SHORT).show();
                        loadingbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

}
