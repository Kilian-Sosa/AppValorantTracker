package app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import dam.pgl.valorantTracker.R;

public class AboutActivity  extends AppCompatActivity {

    private String nick, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        nick = "";
        user = "";
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            nick = bundle.getString("nick");
            user = bundle.getString("user");
        }
        setTitle(nick);
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
                Intent intent1 = new Intent(AboutActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nick", nick);
                bundle.putString("user", user);
                intent1.putExtras(bundle);
                startActivity(intent1);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                return true;
            case R.id.MnuOpc2:
                Intent intent2 = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}