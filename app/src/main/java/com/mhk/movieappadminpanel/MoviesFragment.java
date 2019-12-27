package com.mhk.movieappadminpanel;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.pool.GlideTrace;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    static ArrayList<MovieModel> movieModels=new ArrayList<>();
    static ArrayList<String> ids=new ArrayList<>();
    static LayoutInflater layoutInflater;
    static Context context;
    static ListView listView;
    static FragmentManager fragmentManager;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_movies, container, false);
        FloatingActionButton fbtn=v.findViewById(R.id.movie_float_btn);
        fbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MoviePopUp popup=new MoviePopUp();
                        FragmentManager fm=getFragmentManager();
                        popup.show(fm,"show movies");
                    }
                }
        );
        layoutInflater=getLayoutInflater();
        context=getContext();
        listView=v.findViewById(R.id.movie_list);
        fragmentManager=getFragmentManager();
        loadMovies();
        final EditText search_movie=v.findViewById(R.id.search_movie);
        search_movie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(search_movie.getText().toString().equals("")){
                    loadMovies();
                }
                else {
                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                    CollectionReference ref=db.collection("movies");
                    ref.whereEqualTo("movieName",s.toString())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    movieModels.clear();
                                    ids.clear();
                                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                                        movieModels.add(snapshot.toObject(MovieModel.class));
                                        ids.add(snapshot.getId());
                                    }
                                    MovieAdapter adapter=new MovieAdapter();
                                    listView.setAdapter(adapter);
                                }
                            });
                }
            }
        });
        return v;
    }
public static void loadMovies(){
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    CollectionReference ref=db.collection("movies");
    ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            movieModels.clear();
            ids.clear();
            for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                movieModels.add(snapshot.toObject(MovieModel.class));
                ids.add(snapshot.getId());
            }
            MovieAdapter adapter=new MovieAdapter();
            listView.setAdapter(adapter);
        }
    });
}
private static class MovieAdapter extends BaseAdapter{
    public MovieAdapter() {
    }

    @Override
    public int getCount() {
        return movieModels.size();
    }

    @Override
    public Object getItem(int position) {
        return movieModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v=layoutInflater.inflate(R.layout.movie_list,null);
        TextView sr=v.findViewById(R.id.moviesr_txt);
        TextView name=v.findViewById(R.id.moviename_txt);
        final ImageView img=v.findViewById(R.id.movieimg);
        sr.setText(String.valueOf(position+1));
        name.setText(movieModels.get(position).movieName);
        Glide.with(context)
                .load(movieModels.get(position).getMovieImageLink())
                .override(150,200)
                .into(img);
        RelativeLayout menus=(RelativeLayout)v.findViewById(R.id.menus);
        menus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context,img);
                MenuInflater inflater=popupMenu.getMenuInflater();
                inflater.inflate(R.menu.popupmenu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.delete_menu){
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setTitle("Confirmation")
                                    .setMessage("Are you sure to delete?")
                                    .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseFirestore db=FirebaseFirestore.getInstance();
                                            CollectionReference ref=db.collection("movies");
                                            ref.document(ids.get(position)).delete();
                                            loadMovies();
                                        }
                                    })
                                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                        if(item.getItemId()==R.id.edit_menu){
                            MoviePopUp popUp=new MoviePopUp();
                            popUp.movieModel=movieModels.get(position);
                            popUp.id=ids.get(position);
                            popUp.show(fragmentManager,"edit movie");
                            loadMovies();
                        }
                        return true;
                    }
                });
                return true;
            }
        });
        return v;
    }
}
}
