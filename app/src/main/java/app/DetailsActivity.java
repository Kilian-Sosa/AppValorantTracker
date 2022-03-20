package app;

import static android.database.DatabaseUtils.queryNumEntries;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import dam.pgl.valorantTracker.R;

public class DetailsActivity extends AppCompatActivity {

    private Game game;
    private String user, nick;
    private EditText editNotes;
    private SQLiteDatabase db;
    private ImageView map_img, char_img;
    private TextView txtWin, txtScore, txtPerformance;
    private Button btnUpdate, btnRemove;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        map_img = findViewById(R.id.map_img);
        char_img = findViewById(R.id.char_img);
        txtWin = findViewById(R.id.txtWin);
        txtScore = findViewById(R.id.txtScore);
        txtPerformance = findViewById(R.id.txtPerformance);
        editNotes = findViewById(R.id.editNotes);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnRemove = findViewById(R.id.btnRemove);
        
        Bundle bundle = getIntent().getExtras();
        game = null;
        user = "";
        nick = "";
        if(bundle != null){
            game = (Game) bundle.getSerializable("Game");
            user = bundle.getString("user");
            nick = bundle.getString("nick");
        }else{
            editNotes.setText("No ha llegado la información correctamente.");
        }

        setTitle(game.getDate());
        map_img.setImageResource(game.getIdM());
        char_img.setImageResource(game.getIdC());

        txtWin.setText(game.getWin());
        txtScore.setText(game.getScore());
        txtPerformance.setText(game.getPerformance());
        editNotes.setText(game.getNotes());

        DBSQLiteHelper dataBase =
                new DBSQLiteHelper(this, "VTrackerDataBase", null, 1);
        db = dataBase.getWritableDatabase();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!editNotes.getText().toString().equals(game.getNotes())){
                    game.setNotes(editNotes.getText().toString());

                    String[] fields = {"*"};
                    Cursor cursor = db.query("Records", fields, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            String field2 = cursor.getString(1);
                            if (field2.equals(user)) {
                                int field1 = cursor.getInt(0);

                                if(field1 == game.getID()){
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

                                    db.update("Records", cv, "id = ?", new String[]{field1 + ""});
                                    break;
                                }
                            }
                        } while (cursor.moveToNext());
                    }
                }

                Intent intent = new Intent(DetailsActivity.this, ModifyActivity1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Game", game);
                bundle.putString("user", user);
                bundle.putString("nick", nick);
                intent.putExtras(bundle);
                overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                startActivity(intent);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] fields = {"*"};
                Cursor cursor = db.query("Records", fields, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String field2 = cursor.getString(1);
                        if (field2.equals(user)) {
                            int field1 = cursor.getInt(0);

                            if(field1 == game.getID()){
                                db.delete("Records", "id = ?", new String[]{field1 + ""});
                                break;
                            }
                        }
                    } while (cursor.moveToNext());
                }

                Intent intent = new Intent(DetailsActivity.this, VisualizeGames.class);
                Bundle bundle = new Bundle();
                bundle.putString("user", user);
                bundle.putString("nick", nick);
                intent.putExtras(bundle);
                overridePendingTransition(R.anim.zoom_back_out, R.anim.zoom_back_in);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MnuOpc1:
                game.setShared(1);
                String[] fields = {"*"};
                Cursor cursor = db.query("Records", fields, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String field2 = cursor.getString(1);
                        if (field2.equals(user)) {
                            int field1 = cursor.getInt(0);

                            if(field1 == game.getID()){
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

                                db.update("Records", cv, "id = ?", new String[]{field1 + ""});
                                break;
                            }
                        }
                    } while (cursor.moveToNext());
                }
                Toast.makeText(this, "Se ha compartido la partida", Toast.LENGTH_LONG).show();
                return true;
            case R.id.MnuOpc2:
                Intent intent1 = new Intent(DetailsActivity.this, VisualizeGames.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("user", user);
                bundle1.putString("nick", nick);
                intent1.putExtras(bundle1);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                startActivity(intent1);
                return true;
            case R.id.MnuOpc3:
                Intent intent2 = new Intent(DetailsActivity.this, HomeActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("user", user);
                bundle2.putString("nick", nick);
                intent2.putExtras(bundle2);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
