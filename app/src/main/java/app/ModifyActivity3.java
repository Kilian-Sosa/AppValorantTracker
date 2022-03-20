package app;

import android.app.DatePickerDialog;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import dam.pgl.valorantTracker.R;

public class ModifyActivity3 extends AppCompatActivity {

    private Game game;
    private String user, nick;
    private SQLiteDatabase db;
    private ImageView map_img;
    private TextView txtView;
    private EditText editDate, editNotes;
    private Button btnNext;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify3);
        setTitle("Modificar Partida");

        map_img = findViewById(R.id.map_img);
        editDate = findViewById(R.id.editDate);
        editNotes = findViewById(R.id.editNotes);
        btnNext = findViewById(R.id.btnNext);
        txtView = findViewById(R.id.txtView);

        Bundle bundle = getIntent().getExtras();
        game = null;
        user = "";
        nick = "";
        if(bundle != null){
            game = (Game) bundle.getSerializable("Game");
            user = bundle.getString("user");
            nick = bundle.getString("nick");
        }

        editDate.setText(game.getDate());
        editNotes.setText(game.getNotes());
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


                    ContentValues cv = new ContentValues();

                    cv.put("user", user);
                    cv.put("idM", game.getIdM());
                    cv.put("map", game.getMap());
                    cv.put("idC", game.getIdC());
                    cv.put("character", game.getCharacter());
                    cv.put("win", game.getWinN());
                    cv.put("score", game.getScore());
                    cv.put("performance", game.getPerformance());
                    cv.put("date", game.getDate());
                    cv.put("notes", game.getNotes());
                    cv.put("shared", game.getShared());
                    db.update("Records", cv, "id = ?", new String[]{game.getID() + ""});

                    String[] fields = {"*"};
                    Cursor cursor = db.query("Records", fields, null, null, null, null, null);

                    while(cursor.moveToNext())
                        game.setID(cursor.getInt(0));

                    Toast.makeText(ModifyActivity3.this, "Partida modificada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ModifyActivity3.this, DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Game", game);
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
                Intent intent1 = new Intent(ModifyActivity3.this, ModifyActivity2.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Game", game);
                bundle1.putString("user", user);
                bundle1.putString("nick", nick);
                intent1.putExtras(bundle1);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                startActivity(intent1);
                return true;
            case R.id.MnuOpc2:
                Intent intent2 = new Intent(ModifyActivity3.this, HomeActivity.class);
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
