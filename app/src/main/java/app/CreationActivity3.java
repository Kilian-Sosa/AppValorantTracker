package app;

import static android.database.DatabaseUtils.queryNumEntries;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import dam.pgl.valorantTracker.R;

public class CreationActivity3 extends AppCompatActivity {

    private Game game;
    private String nick, user;
    private SQLiteDatabase db;
    private ImageView map_img;
    private TextView txtView;
    private EditText editDate, editNotes;
    private Button btnNext;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation3);
        setTitle("Añadir Partida");

        map_img = findViewById(R.id.map_img);
        editDate = findViewById(R.id.editDate);
        editNotes = findViewById(R.id.editNotes);
        btnNext = findViewById(R.id.btnNext);
        txtView = findViewById(R.id.txtView);

        Bundle bundle = getIntent().getExtras();
        game = null;
        nick = "";
        user = "";
        if(bundle != null){
            game = (Game) bundle.getSerializable("Game");
            nick = bundle.getString("nick");
            user = bundle.getString("user");
        }

        map_img.setImageResource(game.getIdM());

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.editDate) {
                    showDatePickerDialog();
                }
            }
        });

        DBSQLiteHelper dataBase =
                new DBSQLiteHelper(this, "VTrackerDataBase", null, 1);
        db = dataBase.getWritableDatabase();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(editDate.getText().toString().isEmpty() || editDate.getText().toString().equals("Fecha de la partida"))
                    txtView.setVisibility(View.VISIBLE);
                else{
                    txtView.setVisibility(View.INVISIBLE);
                    game.setDate(editDate.getText().toString());
                    game.setNotes(editNotes.getText().toString());

                    ContentValues newRecord = new ContentValues();

                    newRecord.put("user", user);
                    newRecord.put("idM", game.getIdM());
                    newRecord.put("map", game.getMap());
                    newRecord.put("idC", game.getIdC());
                    newRecord.put("character", game.getCharacter());
                    newRecord.put("win", game.getWinN());
                    newRecord.put("score", game.getScore());
                    newRecord.put("performance", game.getPerformance());
                    newRecord.put("date", game.getDate());
                    newRecord.put("notes", game.getNotes());
                    newRecord.put("shared", game.getShared());
                    db.insert("Records", null, newRecord);

                    String[] fields = {"*"};
                    Cursor cursor = db.query("Records", fields, null, null, null, null, null);

                    while(cursor.moveToNext())
                        game.setID(cursor.getInt(0));

                    Toast.makeText(CreationActivity3.this, "Partida añadida", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreationActivity3.this, DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Game", game);
                    bundle.putString("nick", nick);
                    bundle.putString("user", user);
                    intent.putExtras(bundle);
                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                    startActivity(intent);
                }
            }
        });

    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                editDate.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MnuOpc1:
                Intent intent1 = new Intent(CreationActivity3.this, CreationActivity2.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Game", game);
                bundle1.putString("user", user);
                bundle1.putString("nick", nick);
                intent1.putExtras(bundle1);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                startActivity(intent1);
                return true;
            case R.id.MnuOpc2:
                Intent intent2 = new Intent(CreationActivity3.this, HomeActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("user", user);
                bundle2.putString("nick", nick);
                intent2.putExtras(bundle2);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
