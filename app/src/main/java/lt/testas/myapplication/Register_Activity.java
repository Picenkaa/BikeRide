package lt.testas.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_Activity extends AppCompatActivity {
private Button CreateAccount;
private EditText name,email,pass;
private ProgressBar loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        CreateAccount = (Button) findViewById(R.id.registerr);
        name = (EditText) findViewById(R.id.regsiter_name);
        email = (EditText) findViewById(R.id.regsiter_email);
        pass = (EditText) findViewById(R.id.register_pass);
        loadingbar = new ProgressBar(this);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }

            private void CreateAccount() {
                String vardas = name.getText().toString();
                String pastas = email.getText().toString();
                String slapt = pass.getText().toString();
if(TextUtils.isEmpty(vardas)){
    Toast.makeText(Register_Activity.this, "Irašykite savo varda", Toast.LENGTH_SHORT).show();
}
                else if(TextUtils.isEmpty(pastas)){
                    Toast.makeText(Register_Activity.this, "Irašykite savo e.paštą", Toast.LENGTH_SHORT).show();
                }
               else if(TextUtils.isEmpty(slapt) ){
                    Toast.makeText(Register_Activity.this, "Irašykite slaptažodį", Toast.LENGTH_SHORT).show();
                }
else if(!isValidPassword(slapt)){
    Toast.makeText(Register_Activity.this, "Slaptažodis turi tureti bent 6 simbolius, viena dydziaja raide, maza bei skaiciu", Toast.LENGTH_LONG).show();
}
else if(!isValidEmail(pastas)){
    Toast.makeText(Register_Activity.this, "Irašykite tinkama e.paštą", Toast.LENGTH_LONG).show();
}
               else{
loadingbar.isShown();
loadingbar.setVisibility(View.VISIBLE);
Validateemail(vardas,pastas,slapt);
               }
            }
            public boolean isValidPassword(final String password) {

                Pattern pattern;
                Matcher matcher;

                final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";

                pattern = Pattern.compile(PASSWORD_PATTERN);
                matcher = pattern.matcher(password);

                return matcher.matches();

            }
            public boolean isValidEmail(CharSequence target) {
                return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
            }

            public String EncodeString(String string) {
                return string.replace(".", ",");
            }
            public String DecodeString(String string) {
                return string.replace(",", ".");
            }

            private void Validateemail(final String vardas, final String pastas, final String slapt) {
               final DatabaseReference RootRef;
               RootRef = FirebaseDatabase.getInstance().getReference();


               RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      if(!(dataSnapshot.child("Users").child(EncodeString(pastas)).exists())){
                          HashMap<String,Object> userdataMap = new HashMap<>();
                          userdataMap.put("name",vardas);
                          userdataMap.put("email",EncodeString(pastas));
                          userdataMap.put("pass",slapt);
                          FirebaseAuth auth = FirebaseAuth.getInstance();
                          FirebaseUser user = auth.getCurrentUser();

                          RootRef.child("Users").child(EncodeString(pastas)).updateChildren(userdataMap)
                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if(task.isSuccessful()){


                                              Toast.makeText(Register_Activity.this, "Paskyra sėkmingai sukurta", Toast.LENGTH_SHORT).show();
                                              loadingbar.setVisibility(View.GONE);
                                              Intent intent = new Intent(Register_Activity.this,LoginActivity.class);
                                              startActivity(intent);

                                          }
                                          else{
                                              loadingbar.setVisibility(View.GONE );
                                              Toast.makeText(Register_Activity.this, "Serverio klaida, bandykite dar", Toast.LENGTH_SHORT).show();
                                          }
                                      }
                                  });

                      }
                      else{
                          Toast.makeText(Register_Activity.this, pastas+ " vartotojas jau egzistuoja", Toast.LENGTH_SHORT).show();
                          loadingbar.setVisibility(View.GONE);
                          Toast.makeText(Register_Activity.this, "Išbandykite kita e.paštą ", Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(Register_Activity.this,MainActivity.class);
                          startActivity(intent);


                      }


                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
        });
    }
}