package movies;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import dam.pgl.films.R;


public class aboutmeActivity extends AppCompatActivity {

    private TextView txt1,txt2;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutme_activity);

        txt1=findViewById(R.id.txtHighSchool);
        txt2=findViewById(R.id.txtStudentName);
        img=findViewById(R.id.imageView4);
        int imgID=R.drawable.ic_face;

        img.setImageResource(imgID);
        txt1.setText("IES EL RINCÓN - 2021 PGV");
        txt2.setText("Alexander Sánchez Lantigua");



    }
}
