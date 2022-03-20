package app;

import static android.database.DatabaseUtils.queryNumEntries;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Comparator;

import dam.pgl.valorantTracker.R;

public class HomeActivity extends AppCompatActivity {

    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ListView list;
    private ArrayList<Game> games;
    private TextView txtEmailNav, txtView;
    private SQLiteDatabase db;
    private String user, nick;
    private boolean exists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Feed");

        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appbar.setBackgroundColor(getResources().getColor(R.color.lightRed));
        appbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navView);
        list = findViewById(R.id.listVisualizar);
        txtView = findViewById(R.id.txtView);
        View headerView = navView.getHeaderView(0);
        txtEmailNav = headerView.findViewById(R.id.txtEmailNav);
        games = new ArrayList();

        user = "";
        nick = "";
        exists = false;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            user = bundle.getString("user");
            nick = bundle.getString("nick");
            exists = bundle.getBoolean("Exists");
        }

        txtEmailNav.setText(nick);
        if(exists) drawerLayout.openDrawer(Gravity.LEFT);

        DBSQLiteHelper dataBase =
                new DBSQLiteHelper(this, "VTrackerDataBase", null, 1);
        db = dataBase.getReadableDatabase();

        boolean flag = false;
        if(queryNumEntries(db, "Records") == 0) txtView.setText("Las partidas compartidas por tus amigos aparecerán aquí.");
        else{
            String[] fields = {"*"};
            Cursor cursor1 = db.query("Users", fields, null, null, null, null, null);
            if (cursor1.moveToFirst()) {
                do {
                    String dbUser = cursor1.getString(0);
                    if (dbUser.equals(user)) {
                        String friends = cursor1.getString(3);
                        Cursor cursor2 = db.query("Records", fields, null, null, null, null, null);
                        if (cursor2.moveToFirst()) {
                            do {
                                String field = cursor2.getString(1);
                                if(friends != null)
                                    if (friends.contains(field)) {
                                        if(cursor2.getInt(11) == 1){
                                            flag = true;
                                            int idM = cursor2.getInt(2);
                                            String dbMap = cursor2.getString(3);
                                            int idC = cursor2.getInt(4);
                                            String dbChar = cursor2.getString(5);
                                            int dbWinN = cursor2.getInt(6);
                                            String dbScore = cursor2.getString(7);
                                            String dbPer = cursor2.getString(8);
                                            String dbDate = cursor2.getString(9);
                                            games.add(new Game(idM, idC, dbMap, dbChar, dbWinN, dbScore, dbPer, dbDate));
                                            games.get(games.size() - 1).setUser(field);
                                        }
                                    }
                            }while(cursor2.moveToNext());
                        }
                    }
                } while (cursor1.moveToNext());
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

                    if (year1 > year2)
                        return -1;
                    else if (year1 < year2)
                        return 1;

                    if (month1 > month2)
                        return -1;
                    else if (month1 < month2)
                        return 1;

                    if (day1 > day2)
                        return -1;
                    else if (day1 < day2)
                        return 1;
                    else return 0;
                }
            });

            HomeActivity.AdapterFeed adapter =
                    new HomeActivity.AdapterFeed(this, games);
            list.setAdapter(adapter);
        }else{
            txtView.setText("Las partidas compartidas por tus amigos aparecerán aquí.");
        }
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.menu1:
                                Intent intent1 = new Intent(HomeActivity.this, VisualizeGames.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("user", user);
                                bundle1.putString("nick", nick);
                                intent1.putExtras(bundle1);
                                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                                startActivity(intent1);
                                break;
                            case R.id.menu2:
                                Intent intent2 = new Intent(HomeActivity.this, AboutActivity.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("nick", nick);
                                intent2.putExtras(bundle2);
                                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                                startActivity(intent2);
                                break;
                            case R.id.menu3:
                                Intent intent3 = new Intent(HomeActivity.this, VisualizeUsers.class);
                                Bundle bundle3 = new Bundle();
                                bundle3.putString("user", user);
                                bundle3.putString("nick", nick);
                                intent3.putExtras(bundle3);
                                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                                startActivity(intent3);
                                break;
                            case R.id.menu4:
                                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ViewHolder {
        ImageView map_img;
        ImageView char_img;
        TextView txtAuthor;
        TextView txtWin;
        TextView txtScore;
        TextView txtPerformance;
        TextView txtDate;
    }

    class AdapterFeed extends ArrayAdapter<Game> {
        public AdapterFeed(Context context, ArrayList<Game> data) {
            super(context, R.layout.list_feed, data);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View item = convertView;
            HomeActivity.ViewHolder holder;
            if(item == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                item = inflater.inflate(R.layout.list_feed, null);

                holder = new HomeActivity.ViewHolder();
                holder.txtAuthor = item.findViewById(R.id.txtAuthor);
                holder.map_img = item.findViewById(R.id.map_img);
                holder.char_img = item.findViewById(R.id.char_img);
                holder.txtWin = item.findViewById(R.id.txtWin);
                holder.txtScore = item.findViewById(R.id.txtScore);
                holder.txtPerformance = item.findViewById(R.id.txtPerformance);
                holder.txtDate = item.findViewById(R.id.txtDate);

                item.setTag(holder);
            }else {
                holder = (HomeActivity.ViewHolder) item.getTag();
            }

            holder.txtAuthor.setText("Compartido por " + games.get(position).getUser());
            holder.map_img.setImageResource(games.get(position).getIdM());
            holder.char_img.setImageResource(games.get(position).getIdC());

            holder.txtWin.setText(games.get(position).getWin());
            holder.txtScore.setText(games.get(position).getScore());
            holder.txtPerformance.setText(games.get(position).getPerformance());
            holder.txtDate.setText(games.get(position).getDate());

            return(item);
        }
    }
}