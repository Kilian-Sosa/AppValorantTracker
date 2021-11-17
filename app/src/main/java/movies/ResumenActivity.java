package movies;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import dam.pgl.films.R;

public class ResumenActivity extends AppCompatActivity {

    private TextView txtSinop,txtTtle,txtId;
    private ImageView img;
    private Button btnCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_result_activity);

        txtSinop=findViewById(R.id.textSinopsis);
        txtTtle=findViewById(R.id.textTitle);
        img=findViewById(R.id.imageFilm);
        btnCredits=findViewById(R.id.btnCredits);
        txtId=findViewById(R.id.textId);

        Bundle b= getIntent().getExtras();

        int id=b.getInt("id");
        String text=b.getString("sinopsis");
        String title=b.getString("titulo");
        int image=Integer.parseInt(b.getString("imagen"));

        txtId.setText(String.valueOf(id));
        img.setImageResource(image);
        txtTtle.setText(title);
        txtSinop.setText(text);


        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
