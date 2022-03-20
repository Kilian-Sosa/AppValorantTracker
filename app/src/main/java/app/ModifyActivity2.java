package app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;

import dam.pgl.valorantTracker.R;

public class ModifyActivity2 extends AppCompatActivity {

    private Game game;
    private String user, nick;
    private ImageView map_img;
    private EditText editScore, editPerformance;
    private TextView txtWin, txtView;
    private Button btnD, btnE, btnV, btnNext;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify2);
        setTitle("Modificar Partida");

        map_img = findViewById(R.id.map_img);
        btnD = findViewById(R.id.btnD);
        btnE = findViewById(R.id.btnE);
        btnV = findViewById(R.id.btnV);
        editScore = findViewById(R.id.editScore);
        editPerformance = findViewById(R.id.editPerformance);
        txtWin = findViewById(R.id.txtWin);
        txtView = findViewById(R.id.txtView);
        btnNext = findViewById(R.id.btnNext);

        Bundle bundle = getIntent().getExtras();
        game = null;
        user = "";
        nick = "";
        if(bundle != null){
            game = (Game) bundle.getSerializable("Game");
            user = bundle.getString("user");
            nick = bundle.getString("nick");
        }

        map_img.setImageResource(game.getIdM());
        editPerformance.setText(game.getPerformance());
        editScore.setText(game.getScore());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btnD -> txtWin.setText("DERROTA");
                    case R.id.btnE -> txtWin.setText("EMPATE");
                    case R.id.btnV -> txtWin.setText("VICTORIA");
                }
            }
        };

        btnD.setOnClickListener(listener);
        btnE.setOnClickListener(listener);
        btnV.setOnClickListener(listener);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(txtWin.getText().toString().isEmpty())
                    txtView.setText("Elija el resultado de la partida");
                else if(editScore.getText().toString().isEmpty() || editPerformance.getText().toString().isEmpty())
                    txtView.setText("Debe añadir las puntuaciones antes de avanzar");
                else if(!editScore.getText().toString().contains("-"))
                    txtView.setText("El formato de la puntuación de la partida debe ser <num>-<num>");
                else if(!editPerformance.getText().toString().contains("/"))
                    txtView.setText("El formato de su puntuación debe ser <num>/<num>/<num>");
                else{
                    game.setWin(txtWin.getText().toString());
                    game.setScore(editScore.getText().toString());
                    game.setPerformance(editPerformance.getText().toString());

                    Intent intent = new Intent(ModifyActivity2.this, ModifyActivity3.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Game", game);
                    bundle.putString("user", user);
                    bundle.putString("nick", nick);
                    intent.putExtras(bundle);
                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                    startActivity(intent);
                }
            }
        });

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
                Intent intent1 = new Intent(ModifyActivity2.this, ModifyActivity1.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Game", game);
                bundle1.putString("user", user);
                bundle1.putString("nick", nick);
                intent1.putExtras(bundle1);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                startActivity(intent1);
                return true;
            case R.id.MnuOpc2:
                Intent intent2 = new Intent(ModifyActivity2.this, HomeActivity.class);
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