package app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import dam.pgl.valorantTracker.R;

public class PassChangerActivity extends AppCompatActivity {

    private EditText edtPassword,edtEmail;
    private Button btnChange;
    private Button btnReturn;
    private TextInputLayout txtLay;
    private SQLiteDatabase db;
    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_change);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnChange = findViewById(R.id.btnChange);
        btnReturn = findViewById(R.id.btnReturn);
        txtLay = findViewById(R.id.passLay);
        txtLay.setErrorEnabled(true);

        nick = "";
        DBSQLiteHelper data =
                new DBSQLiteHelper(this, "VTrackerDataBase", null, 1);
        db = data.getWritableDatabase();


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtLay.setError(null);
                boolean flag = false;
                String email = edtEmail.getText().toString();
                String[] campos = {"*"};
                Cursor c = db.query("Users", campos, null, null, null, null, null);
                if (c.moveToFirst()) {
                    do {
                        String em = c.getString(0);
                        if (em.equals(email)) {
                            flag = true;
                            nick = c.getString(2);
                            break;
                        }
                    }while (c.moveToNext()) ;
                }

                if (checkPassword(edtPassword.getText().toString()) && flag) {
                    Intent intent = new Intent(PassChangerActivity.this, MainActivity.class);

                    deleteAccount(email);
                    String pass = edtPassword.getText().toString();
                    ContentValues newUser = new ContentValues();
                    newUser.put("email", email);
                    newUser.put("password", pass);
                    newUser.put("nick", nick);
                    db.insert("Users", null, newUser);

                    Bundle bundle = new Bundle();
                    bundle.putString("Email", email);
                    bundle.putString("Password", pass);
                    intent.putExtras(bundle);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                    startActivity(intent);
                }
                else {
                    txtLay.setError("Ese correo no existe o la contraseña no tiene ni mayúculas y escriba al menos un número.");
                }
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                startActivity(new Intent(PassChangerActivity.this, MainActivity.class));
            }
        });
    }


    public boolean checkPassword(String p){
        int num = 0;
        int big = 0;
        for(int i = 0; i < p.length(); i++){
            if(Character.isDigit(p.charAt(i))) {
                num++;
                continue;
            }
            if(Character.isUpperCase(p.charAt(i))) big++;
        }
        if(num > 0 && big > 0)return true;
        else{
            return false;
        }
    }

    public void deleteAccount(String r){
        String[] campos = {r};
        db.delete("Users","email=?", campos);
    }
}
