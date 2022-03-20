package app;

import static android.database.DatabaseUtils.queryNumEntries;

import static app.MainActivity.char_array;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

import dam.pgl.valorantTracker.R;

public class VisualizeUsers extends AppCompatActivity {

    private ListView list;
    private ArrayList<String> users, friends;
    private ToggleButton toggleButton;
    private TextView txtView;
    private SQLiteDatabase db;
    private String user, nick;
    public boolean flag3, flag4;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_friends);
        setTitle("Amigos");

        list = findViewById(R.id.listVisualizar);
        toggleButton = findViewById(R.id.toggleButton);
        txtView = findViewById(R.id.txtView);
        users = new ArrayList();
        friends = new ArrayList();
        flag3 = false;
        flag4 = false;
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

        boolean flag1 = false;
        if(queryNumEntries(db, "Users") == 1) txtView.setText("Todavía no hay ningún usuario registrado.");
        else{
            String[] fields = {"*"};
            Cursor cursor = db.query("Users", fields, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String dbUser = cursor.getString(0);
                    if (!dbUser.equals(user)) {
                        flag1 = true;
                        String field2 = cursor.getString(3);
                        if(field2 != null)
                            if(field2.contains(user)) friends.add(dbUser);
                            else users.add(dbUser);
                        else users.add(dbUser);
                    }
                } while (cursor.moveToNext());
            }
        }
        if(flag1) {

            Comparator<String> comparator = new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {return s1.compareTo(s2);}};

            users.sort(comparator);
            friends.sort(comparator);

            VisualizeUsers.AdapterUsers adapter =
                    new VisualizeUsers.AdapterUsers(this, users);

            list.setAdapter(adapter);

            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(toggleButton.isChecked()){
                        txtView.setText("Pulse para eliminar un amigo");
                        list.setAdapter(new VisualizeUsers.AdapterUsers(getApplicationContext(), friends));
                    }else{
                        txtView.setText("Pulse para añadir un amigo");
                        list.setAdapter(new VisualizeUsers.AdapterUsers(getApplicationContext(), users));
                    }
                }
            });
            db = dataBase.getWritableDatabase();

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    updateDB(adapterView, view, pos, l);
                }
            });
        }else{
            txtView.setText("Todavía no hay ningún usuario registrado.");
        }
    }

    private void updateDB(AdapterView<?> adapterView, View view, int pos, long l){
        if(toggleButton.isChecked())
            new AlertDialog.Builder(VisualizeUsers.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Eliminar amigo")
                    .setMessage("¿Estás seguro?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            flag3 = false;
                            flag4 = false;
                            String[] fields = {"*"};
                            Cursor cursor = db.query("Users", fields, null, null, null, null, null);
                            if (cursor.moveToFirst()) {
                                do {
                                    String dbUser = cursor.getString(0);
                                    if (dbUser.equals(user)) {
                                        ContentValues cv = new ContentValues();
                                        cv.put("email", user);
                                        cv.put("password", cursor.getString(1));
                                        cv.put("nick", cursor.getString(2));

                                        String[] chain = cursor.getString(3).split(", ");
                                        String aux = "";
                                        for (String s : chain)
                                            if (!s.equals(friends.get(pos)))
                                                aux += s + ", ";

                                        cv.put("friends", aux);
                                        db.update("Users", cv, "email = ?", new String[]{user + ""});
                                        flag3 = true;
                                    } else if (dbUser.equals(friends.get(pos))) {
                                        ContentValues cv = new ContentValues();
                                        cv.put("email", dbUser);
                                        cv.put("password", cursor.getString(1));
                                        cv.put("nick", cursor.getString(2));

                                        String[] chain = cursor.getString(3).split(", ");
                                        String aux = "";
                                        for (String s : chain)
                                            if (!s.equals(user)) aux += s + ", ";

                                        cv.put("friends", aux);
                                        db.update("Users", cv, "email = ?", new String[]{dbUser + ""});
                                        flag4 = true;
                                    }
                                    if (flag3 && flag4) {
                                        Toast.makeText(VisualizeUsers.this, "Amigo eliminado", Toast.LENGTH_LONG).show();
                                        users.add(friends.get(pos));
                                        friends.remove(pos);
                                    }
                                }while(cursor.moveToNext());
                                list.setAdapter(new AdapterUsers(getApplicationContext(), friends));
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        flag3 = false;
        flag4 = false;
        String[] fields = {"*"};
        Cursor cursor = db.query("Users", fields, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String dbUser = cursor.getString(0);
                if(!toggleButton.isChecked()){
                    if (dbUser.equals(user)) {
                        ContentValues cv = new ContentValues();
                        cv.put("email", user);
                        cv.put("password", cursor.getString(1));
                        cv.put("nick", cursor.getString(2));
                        if(cursor.getString(3) == null) cv.put("friends", users.get(pos));
                        else cv.put("friends", cursor.getString(3) + ", " + users.get(pos));
                        db.update("Users", cv, "email = ?", new String[]{user + ""});
                        flag3 = true;
                    } else if (dbUser.equals(users.get(pos))) {
                        ContentValues cv = new ContentValues();
                        cv.put("email", dbUser);
                        cv.put("password", cursor.getString(1));
                        cv.put("nick", cursor.getString(2));
                        if(cursor.getString(3) == null) cv.put("friends", user);
                        else cv.put("friends", cursor.getString(3) + ", " + user);
                        db.update("Users", cv, "email = ?", new String[]{dbUser + ""});
                        flag4 = true;
                    }
                    if(flag3 && flag4){
                        Toast.makeText(VisualizeUsers.this, "Amigo añadido", Toast.LENGTH_LONG).show();
                        friends.add(users.get(pos));
                        users.remove(pos);
                    }
                }
            } while (cursor.moveToNext());
        }
        if(toggleButton.isChecked()) list.setAdapter(new VisualizeUsers.AdapterUsers(getApplicationContext(), friends));
        else list.setAdapter(new VisualizeUsers.AdapterUsers(getApplicationContext(), users));
    }

    public static class ViewHolder {
        ImageView char_img;
        TextView txtView;
    }

    class AdapterUsers extends ArrayAdapter<String> {
        ArrayList<String> data;

        public AdapterUsers(Context context, ArrayList<String> data) {
            super(context, R.layout.list_friends, data);
            this.data = data;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View item = convertView;
            VisualizeUsers.ViewHolder holder;
            if(item == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                item = inflater.inflate(R.layout.list_friends, null);

                holder = new VisualizeUsers.ViewHolder();
                holder.char_img = item.findViewById(R.id.char_img);
                holder.txtView = item.findViewById(R.id.txtView);

                item.setTag(holder);
            }else {
                holder = (VisualizeUsers.ViewHolder) item.getTag();
            }
            int num = (int) (Math.random() * 18);

            holder.char_img.setImageResource(char_array[num]);
            holder.txtView.setText(data.get(position));

            return(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MnuOpc1:
                Intent intent1 = new Intent(VisualizeUsers.this, HomeActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user", user);
                bundle1.putSerializable("nick", nick);
                intent1.putExtras(bundle1);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                startActivity(intent1);
                return true;
            case R.id.MnuOpc2:
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                startActivity(new Intent(VisualizeUsers.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}