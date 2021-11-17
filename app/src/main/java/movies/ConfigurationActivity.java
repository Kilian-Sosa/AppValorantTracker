package movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import dam.pgl.films.R;

public class ConfigurationActivity extends AppCompatActivity {

    private EditText edt1,edt2,edt3;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        edt1=findViewById(R.id.editTextApiKey);
        edt2=findViewById(R.id.editTextEndPointCreditos);
        edt3=findViewById(R.id.editTextEndPointMovies);
        btnSave=findViewById(R.id.btnGuardar);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

                // Creating an Editor object to edit(write to the file)
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                // Storing the key and its value as the data fetched from edittext
                myEdit.putString("api", edt2.getText().toString());
                myEdit.putString("api_key", edt1.getText().toString());
                myEdit.putString("url_images", edt3.getText().toString());

                // Once the changes have been made,
                // we need to commit to apply those changes made,
                // otherwise, it will throw an error
                myEdit.commit();
            }
        });



    }
}
