package app;

import static android.database.DatabaseUtils.queryNumEntries;

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
import androidx.core.view.GravityCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import dam.pgl.valorantTracker.R;

public class VisualizeGames extends AppCompatActivity {

    private ListView list;
    private ArrayList<Game> games;
    private TextView txtView;
    private SQLiteDatabase db;
    private String user, nick;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_games);
        setTitle("Historial");

        list = findViewById(R.id.listVisualizar);
        txtView = findViewById(R.id.txtView);
        games = new ArrayList();

        Bundle bundle = getIntent().getExtras();
        user = "";
        nick = "";

        if(bundle != null){
            user = bundle.getString("user");
            nick = bundle.getString("nick");
        }else{
            txtView.setText("No ha llegado la información correctamente.");
        }

        DBSQLiteHelper dataBase =
                new DBSQLiteHelper(this, "VTrackerDataBase", null, 1);
        db = dataBase.getReadableDatabase();

        boolean flag = false;
        if(queryNumEntries(db, "Records") == 0) txtView.setText("Todavía no hay ninguna partida guardada.");
        else{
            String[] fields = {"*"};
            Cursor cursor = db.query("Records", fields, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String dbUser = cursor.getString(1);
                    if (dbUser.equals(user)) {
                        flag = true;
                        int id = cursor.getInt(0);
                        int idM = cursor.getInt(2);
                        String dbMap = cursor.getString(3);
                        int idC = cursor.getInt(4);
                        String dbChar = cursor.getString(5);
                        int dbWinN = cursor.getInt(6);
                        String dbScore = cursor.getString(7);
                        String dbPer = cursor.getString(8);
                        String dbDate = cursor.getString(9);
                        String dbNotes = cursor.getString(10);
                        if(dbNotes == null) dbNotes = "";
                        games.add(new Game(id, idM, idC, dbMap, dbChar, dbWinN, dbScore, dbPer, dbDate, dbNotes));
                    }
                } while (cursor.moveToNext());
            }
        }
        if(flag) {
            games.sort(new Comparator<Game>() {
                @Override
                public int compare(Game game, Game t1) {
                    //Orden descendente
                    String[] date1 = game.getDate().split("/");
                    String[] date2 = t1.getDate().split("/");
                    int year1 = Integer.parseInt(date1[2]);
                    int month1 = Integer.parseInt(date1[1]);
                    int day1 = Integer.parseInt(date1[0]);
                    int year2 = Integer.parseInt(date2[2]);
                    int month2 = Integer.parseInt(date2[1]);
                    int day2 = Integer.parseInt(date2[0]);

                    if(year1 > year2)
                        return -1;
                    else if(year1 < year2)
                        return 1;

                    if(month1 > month2)
                        return -1;
                    else if(month1 < month2)
                        return 1;

                    if(day1 > day2)
                        return -1;
                    else if(day1 < day2)
                        return 1;
                    else return 0;
                }
            });

            list.setLongClickable(true);
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                    Intent intent = new Intent(VisualizeGames.this, ModifyActivity1.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Game", (Game) list.getItemAtPosition(pos));
                    bundle.putString("user", user);
                    bundle.putString("nick", nick);
                    intent.putExtras(bundle);
                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                    startActivity(intent);

                    return false;
                }
            });

            VisualizeGames.AdapterGames adapter =
                    new VisualizeGames.AdapterGames(this, games);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    Intent intent = new Intent(VisualizeGames.this, DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Game", games.get(pos));
                    bundle.putString("user", user);
                    bundle.putString("nick", nick);
                    intent.putExtras(bundle);
                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                    startActivity(intent);
                }
            });
        }else{
            txtView.setText("Todavía no hay ninguna partida guardada.");
        }
    }

    public static class ViewHolder {
        ImageView map_img;
        ImageView char_img;
        TextView txtWin;
        TextView txtScore;
        TextView txtPerformance;
        TextView txtDate;
    }

    class AdapterGames extends ArrayAdapter<Game> {
        public AdapterGames(Context context, ArrayList<Game> data) {
            super(context, R.layout.list_games, data);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View item = convertView;
            ViewHolder holder;
            if(item == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                item = inflater.inflate(R.layout.list_games, null);

                holder = new ViewHolder();
                holder.map_img = item.findViewById(R.id.map_img);
                holder.char_img = item.findViewById(R.id.char_img);
                holder.txtWin = item.findViewById(R.id.txtWin);
                holder.txtScore = item.findViewById(R.id.txtScore);
                holder.txtPerformance = item.findViewById(R.id.txtPerformance);
                holder.txtDate = item.findViewById(R.id.txtDate);

                item.setTag(holder);
            }else {
                holder = (ViewHolder) item.getTag();
            }

            holder.map_img.setImageResource(games.get(position).getIdM());
            holder.char_img.setImageResource(games.get(position).getIdC());

            holder.txtWin.setText(games.get(position).getWin());
            holder.txtScore.setText(games.get(position).getScore());
            holder.txtPerformance.setText(games.get(position).getPerformance());
            holder.txtDate.setText(games.get(position).getDate());

            return(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MnuOpc1:
                Intent intent1 = new Intent(VisualizeGames.this, CreationActivity1.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user", user);
                bundle1.putSerializable("nick", nick);
                intent1.putExtras(bundle1);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                startActivity(intent1);
                return true;
            case R.id.MnuOpc2:
                Intent intent2 = new Intent(VisualizeGames.this, HomeActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("user", user);
                bundle2.putSerializable("nick", nick);
                intent2.putExtras(bundle2);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}