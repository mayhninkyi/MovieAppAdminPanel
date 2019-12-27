package com.mhk.movieappadminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout datapanel;
    RelativeLayout splashpanel;

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            splashpanel.setVisibility(View.GONE);
            datapanel.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashpanel=findViewById(R.id.splishpanel);
        datapanel=findViewById(R.id.datapanel);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        handler.postDelayed(runnable,4000);

        setFragment(new MoviesFragment());
        BottomNavigationView botnav=findViewById(R.id.botnav);
        botnav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if(R.id.movie_ic== menuItem.getItemId()){
                            setFragment(new MoviesFragment());
                        }
                        if(R.id.series_ic== menuItem.getItemId()){
                            setFragment(new SeriesFragment());
                        }
                        if(R.id.category_ic== menuItem.getItemId()){
                            setFragment(new CategoryFragment());
                        }
                        return true;
                    }
                }
        );
    }

    public void setFragment(Fragment fra){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fra);
        fragmentTransaction.commit();
    }
}
