package app;

import static app.MainActivity.char_array;
import static app.MainActivity.map_array;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import dam.pgl.valorantTracker.R;

public class ModifyActivity1 extends AppCompatActivity {

    private Game game;
    private String user, nick;
    private ImageView map_img, char_img;
    private Spinner map_spinner, char_spinner;
    private Button btnNext;
    private boolean flagM, flagC;
    private int idM, idC, posM, posC;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify1);
        setTitle("Modificar Partida");

        map_img = findViewById(R.id.map_img);
        char_img = findViewById(R.id.char_img);
        map_spinner = findViewById(R.id.map_spinner);
        char_spinner = findViewById(R.id.char_spinner);
        btnNext = findViewById(R.id.btnNext);

        Bundle bundle = getIntent().getExtras();
        posM = -1;
        posC = -1;
        idM = -1;
        idC = -1;
        game = null;
        flagM = false;
        flagC = false;
        user = "";
        nick = "";
        if(bundle != null){
            game = (Game) bundle.getSerializable("Game");
            if(game != null) {
                flagM = true;
                flagC = true;
            }
            idM = game.getIdM();
            idC = game.getIdC();
            user = bundle.getString("user");
            nick = bundle.getString("nick");
        }

        ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(this,
                R.array.maps, android.R.layout.simple_spinner_item);

        adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        map_spinner.setAdapter(adapterM);

        map_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {

                        if(!flagM){
                            idM = map_array[position];
                            posM = position;
                            map_img.setImageResource(idM);
                        }else map_img.setImageResource(game.getIdM());
                        flagM = false;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

        if(game.getIdM() != 0) map_img.setImageResource(game.getIdM());

        ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this,
                R.array.characters, android.R.layout.simple_spinner_item);

        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        char_spinner.setAdapter(adapterC);

        char_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {

                        if(!flagC) {
                            idC = char_array[position];
                            posC = position;
                            char_img.setImageResource(idC);
                        }else char_img.setImageResource(game.getIdC());
                        flagC = false;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(idM != -1) game.setIdM(idM);
                if(idC != -1) game.setIdC(idC);
                if(idM != -1) game.setMap("" + map_spinner.getItemAtPosition(posM));
                if(idC != -1) game.setCharacter("" + char_spinner.getItemAtPosition(posC));
                Intent intent = new Intent(ModifyActivity1.this, ModifyActivity2.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Game", game);
                bundle.putString("user", user);
                bundle.putString("nick", nick);
                intent.putExtras(bundle);
                overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MnuOpc1:
                Intent intent1 = new Intent(ModifyActivity1.this, VisualizeGames.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("user", user);
                bundle1.putString("nick", nick);
                intent1.putExtras(bundle1);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                startActivity(intent1);
                return true;
            case R.id.MnuOpc2:
                Intent intent2 = new Intent(ModifyActivity1.this, HomeActivity.class);
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
