package app;

import static android.database.DatabaseUtils.queryNumEntries;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import dam.pgl.valorantTracker.R;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister, btnCancel;
    private TextInputLayout txtInpNick, txtInpEmail, txtInpContra;
    private EditText edtNick, edtEmail, edtContra;
    private SQLiteDatabase db;
    private boolean exists = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registrarse");

        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);
        edtNick = findViewById(R.id.edtNick);
        edtEmail = findViewById(R.id.edtEmail);
        edtContra = findViewById(R.id.edtPassword);
        txtInpNick = findViewById(R.id.nickLayout);
        txtInpEmail = findViewById(R.id.emailLayout);
        txtInpContra = findViewById(R.id.contraLayout);
        txtInpNick.setErrorEnabled(true);
        txtInpEmail.setErrorEnabled(true);
        txtInpContra.setErrorEnabled(true);

        DBSQLiteHelper dataBase =
                new DBSQLiteHelper(this, "VTrackerDataBase", null, 1);
        db = dataBase.getWritableDatabase();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flagBig = false;
                boolean flagNum = false;
                txtInpNick.setError(null);
                txtInpContra.setError(null);
                txtInpEmail.setError(null);

                if(edtEmail.getText().toString().equals("demo") && edtContra.getText().toString().equals("demo"))
                    txtInpEmail.setError("Ya la cuenta demo está creada");
                else{
                    String aux = edtContra.getText().toString();
                    for(int i = 0; i < aux.length(); i++){
                        if(Character.isUpperCase(aux.charAt(i))) {
                            flagBig = true;
                            continue;
                        }
                        if(Character.isDigit(aux.charAt(i))) {
                            flagNum = true;
                        }
                    }
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    String email = edtEmail.getText().toString();
                    if(email.trim().matches(emailPattern)){
                        if(flagBig && flagNum) {
                            exists = true;
                            addUser(edtNick.getText().toString(), email, aux);
                        }else{
                            txtInpContra.setError("La contraseña debe de tener al menos un caracter en mayúcula y al menos un número.");
                        }
                    }else{
                        txtInpEmail.setError("Escriba un correo correctamente");
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }
        });
    }

    public void addUser(String nick, String email, String ps){
        String[] fields = {"*"};
        Cursor cursor = db.query("Users", fields, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String field = cursor.getString(0);
                String field1 = cursor.getString(2);
                if(field.equals(email)) {
                    txtInpEmail.setError("Ya existe un usuario con dicho correo. Si lo desea puede cambiar la contraseña");
                    return;
                }else if(field1.equals(nick) && !field1.equals("")){
                    txtInpNick.setError("Ya existe una cuenta con dicho Nick");
                    return;
                }
            } while (cursor.moveToNext());
        }
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

        ContentValues newUser = new ContentValues();
        newUser.put("email", email);
        newUser.put("password",ps);
        newUser.put("nick", nick);
        db.insert("Users", null, newUser);

        Bundle bundle = new Bundle();
        bundle.putBoolean("Exists", exists);
        bundle.putString("Email", email);
        bundle.putString("Password", ps);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}