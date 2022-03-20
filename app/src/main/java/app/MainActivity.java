package app;

import static android.database.DatabaseUtils.queryNumEntries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;

import dam.pgl.valorantTracker.R;


public class MainActivity extends AppCompatActivity {
    private Button btnNext,btnRegister;
    private TextInputLayout txtInpEmail, txtInpContra;
    private static EditText edtEmail,edtContra;
    private TextView txtView;
    private SQLiteDatabase db;
    private String nick, user;
    private boolean exists;
    private boolean activateDemo = true;
    public static final int[] map_array = {R.drawable.map_ascent_img, R.drawable.map_bind_img,
            R.drawable.map_breeze_img, R.drawable.map_fracture_img,
            R.drawable.map_haven_img, R.drawable.map_icebox_img, R.drawable.map_split_img};
    public static final int[] char_array = {R.drawable.character_astra_img, R.drawable.character_breach_img,
            R.drawable.character_brimstone_img, R.drawable.character_chamber_img,
            R.drawable.character_cypher_img, R.drawable.character_jett_img, R.drawable.character_kay0_img,
            R.drawable.character_killjoy_img, R.drawable.character_neon_img, R.drawable.character_omen_img,
            R.drawable.character_phoenix_img, R.drawable.character_raze_img, R.drawable.character_reyna_img,
            R.drawable.character_sage_img, R.drawable.character_skye_img, R.drawable.character_sova_img,
            R.drawable.character_viper_img, R.drawable.character_yoru_img};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Iniciar Sesión");

        btnNext = findViewById(R.id.btnNext);
        btnRegister = findViewById(R.id.btnRegister);

        edtEmail = findViewById(R.id.txtEmail);
        edtContra = findViewById(R.id.txtPassword);

        txtInpEmail = findViewById(R.id.emailLayout);
        txtInpContra = findViewById(R.id.contraLayout);
        txtInpEmail.setErrorEnabled(true);
        txtInpContra.setErrorEnabled(true);
        txtView = findViewById(R.id.txtView);

        nick = "";
        user = "";
        exists = false;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            exists = bundle.getBoolean("Exists");
            edtEmail.setText(bundle.getString("Email"));
            edtContra.setText(bundle.getString("Password"));
        }

        DBSQLiteHelper dataBase =
                new DBSQLiteHelper(this, "VTrackerDataBase", null, 1);
        db = dataBase.getWritableDatabase();


        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean flag1 = false;
                boolean flag2 = false;
                txtInpContra.setError(null);
                txtInpEmail.setError(null);
                String email = edtEmail.getText().toString();
                String password = edtContra.getText().toString();

                String[] fields = {"*"};
                Cursor cursor = db.query("Users", fields, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String field1 = cursor.getString(0);
                        String field2 = cursor.getString(1);
                        if(field1.equals("demo"))
                            activateDemo = false;
                        if (field1.equals(email)) {
                            flag1 = true;
                            if (field2.equals(password)) {
                                nick = cursor.getString(2);
                                flag2 = true;
                                break;
                            }
                        }
                    } while (cursor.moveToNext());
                }

                if(email.equals("demo") && password.equals("demo") && activateDemo){
                    activateDemo = false;
                    exists = true;
                    ContentValues newRecord = new ContentValues();
                    newRecord.put("email", "demo");
                    newRecord.put("password", "demo");
                    newRecord.put("nick", "ApodoDemo");
                    db.insert("Users", null, newRecord);

                    setDemoRecords();

                    nick = "ApodoDemo";
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user", "demo");
                    bundle.putString("nick", nick);
                    bundle.putBoolean("Exists", exists);
                    intent.putExtras(bundle);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                    startActivity(intent);
                }else if(flag1 && flag2) {
                    if(nick == null) nick = email;
                    user = email;
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user", user);
                    bundle.putString("nick", nick);
                    bundle.putBoolean("Exists", exists);
                    intent.putExtras(bundle);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                    startActivity(intent);
                } else if (flag1 && !flag2) {
                    txtInpContra.setError("La contraseña está mal escrita. Recuerde que puede cambiar la contraseña si no la recuerda o registrarse si no tiene cuenta.");
                } else if(email.equals("demo@gmail.com")) {
                    txtInpEmail.setError("Introduzca la contraseña");
                } else {
                    txtInpEmail.setError("No se reconoce el correo. Regístrese si no tiene cuenta.");
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PassChangerActivity.class);
                startActivity(intent);
            }
        });
    }



    private void setDemoRecords(){
        DBSQLiteHelper dataBase =
                new DBSQLiteHelper(this, "VTrackerDataBase", null, 1);
        db = dataBase.getWritableDatabase();

        ContentValues newRecord = new ContentValues();

        newRecord.put("user", "demo");
        newRecord.put("idM", R.drawable.map_ascent_img);
        newRecord.put("map", "Ascent");
        newRecord.put("idC", R.drawable.character_omen_img);
        newRecord.put("character", "Omen");
        newRecord.put("win", 1);
        newRecord.put("score", "13-2");
        newRecord.put("performance", "21/4/3");
        newRecord.put("date", "01/01/2022");
        newRecord.put("notes", "Partida muy sencilla");
        newRecord.put("shared", 0);
        db.insert("Records", null, newRecord);

        newRecord.put("user", "demo");
        newRecord.put("idM", R.drawable.map_bind_img);
        newRecord.put("map", "Bind");
        newRecord.put("idC", R.drawable.character_phoenix_img);
        newRecord.put("character", "Phoenix");
        newRecord.put("win", 1);
        newRecord.put("score", "13-10");
        newRecord.put("performance", "15/7/1");
        newRecord.put("date", "03/01/2022");
        newRecord.put("notes", "El Omen era muy bueno");
        newRecord.put("shared", 0);
        db.insert("Records", null, newRecord);

        newRecord.put("user", "demo");
        newRecord.put("idM", R.drawable.map_fracture_img);
        newRecord.put("map", "Fracture");
        newRecord.put("idC", R.drawable.character_neon_img);
        newRecord.put("character", "Neon");
        newRecord.put("win", 0);
        newRecord.put("score", "9-13");
        newRecord.put("performance", "8/14/6");
        newRecord.put("date", "01/01/2022");
        newRecord.put("notes", "Bastante reñida, tengo que practicar mi puntería");
        newRecord.put("shared", 0);
        db.insert("Records", null, newRecord);

        newRecord.put("user", "demo");
        newRecord.put("idM", R.drawable.map_split_img);
        newRecord.put("map", "Split");
        newRecord.put("idC", R.drawable.character_yoru_img);
        newRecord.put("character", "Yoru");
        newRecord.put("win", 2);
        newRecord.put("score", "13-2");
        newRecord.put("performance", "13-13");
        newRecord.put("date", "12/01/2022");
        newRecord.put("notes", "Personaje muy complicado, mirar vídeos");
        newRecord.put("shared", 1);
        db.insert("Records", null, newRecord);

        newRecord.put("user", "demo");
        newRecord.put("idM", R.drawable.map_bind_img);
        newRecord.put("map", "Bind");
        newRecord.put("idC", R.drawable.character_reyna_img);
        newRecord.put("character", "Reyna");
        newRecord.put("win", 0);
        newRecord.put("score", "5-13");
        newRecord.put("performance", "21/4/3");
        newRecord.put("date", "20/01/2022");
        newRecord.put("notes", "Primera partida de Alexander");
        newRecord.put("shared", 0);
        db.insert("Records", null, newRecord);

        newRecord.put("user", "demo");
        newRecord.put("idM", R.drawable.map_breeze_img);
        newRecord.put("map", "Breeze");
        newRecord.put("idC", R.drawable.character_raze_img);
        newRecord.put("character", "Raze");
        newRecord.put("win", 0);
        newRecord.put("score", "8-13");
        newRecord.put("performance", "21/4/3");
        newRecord.put("date", "05/02/2022");
        newRecord.put("notes", "Compañeros muy malos...");
        newRecord.put("shared", 0);
        db.insert("Records", null, newRecord);

        newRecord.put("user", "demo");
        newRecord.put("idM", R.drawable.map_haven_img);
        newRecord.put("map", "Haven");
        newRecord.put("idC", R.drawable.character_viper_img);
        newRecord.put("character", "Viper");
        newRecord.put("win", 1);
        newRecord.put("score", "13-11");
        newRecord.put("performance", "16/9/5");
        newRecord.put("date", "05/02/2022");
        newRecord.put("notes", "");
        newRecord.put("shared", 1);
        db.insert("Records", null, newRecord);

        newRecord.put("user", "demo");
        newRecord.put("idM", R.drawable.map_icebox_img);
        newRecord.put("map", "Icebox");
        newRecord.put("idC", R.drawable.character_killjoy_img);
        newRecord.put("character", "Killjoy");
        newRecord.put("win", 1);
        newRecord.put("score", "15-13");
        newRecord.put("performance", "15/11/10");
        newRecord.put("date", "05/02/2022");
        newRecord.put("notes", "Este mapa es muy difícil, ¡cuidado!");
        newRecord.put("shared", 1);
        db.insert("Records", null, newRecord);
    }
}



