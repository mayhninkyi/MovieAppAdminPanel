package com.mhk.movieappadminpanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MoviePopUp extends DialogFragment {
    public MovieModel movieModel;
    public String id="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.movie_popup,container,false);
        Button btn_close=v.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Button btn_save=v.findViewById(R.id.movie_popup_save);
        final EditText name=v.findViewById(R.id.movie_name_edt);
        final EditText image_link=v.findViewById(R.id.movie_img_edt);
        final EditText video_link=v.findViewById(R.id.movie_video_edt);
        final Spinner movie_cat=v.findViewById(R.id.movie_cat_edt);
        final Spinner movie_series=v.findViewById(R.id.movie_series_edt);

        final ArrayList<String> category_names=new ArrayList<String>();
        final ArrayList<String> series_names=new ArrayList<String>();

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference categories=db.collection("categories");
        CollectionReference series=db.collection("series");
        if(movieModel!=null){
            name.setText(movieModel.getMovieName());
            image_link.setText(movieModel.getMovieImageLink());
            video_link.setText(movieModel.getMovieVideoLink());
            categories.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot snapshot:queryDocumentSnapshots){
                        CategoryModel c=snapshot.toObject(CategoryModel.class);
                        category_names.add(c.categoryName);
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,category_names);
                    movie_cat.setAdapter(adapter);
                    for (int i=0;i<category_names.size();i++){
                        if(category_names.get(i).equals(movieModel.movieCategory)){
                            movie_cat.setSelection(i);
                            break;
                        }
                    }
                }
            });
            series.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        SeriesModel s=snapshot.toObject(SeriesModel.class);
                        series_names.add(s.series_name);
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,series_names);
                    movie_series.setAdapter(adapter);
                    for (int i=0;i<series_names.size();i++){
                        if(series_names.get(i).equals(movieModel.movieSeries)){
                            movie_series.setSelection(i);
                            break;
                        }
                    }
                }
            });
        }


        btn_save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(movieModel==null){
                            MovieModel m=new MovieModel(name.getText().toString(),image_link.getText().toString(),video_link.getText().toString(),category_names.get(movie_cat.getSelectedItemPosition()),series_names.get(movie_series.getSelectedItemPosition()));
                            FirebaseFirestore db=FirebaseFirestore.getInstance();
                            CollectionReference ref=db.collection("movies");
                            ref.add(m);
                            name.setText("");
                            image_link.setText("");
                            video_link.setText("");
                            movie_cat.setSelection(0);
                            movie_series.setSelection(0);
                            MoviesFragment.loadMovies();
                            Toast.makeText(getContext(),"save success",Toast.LENGTH_LONG).show();

                        }
                        else {
                            MovieModel m=new MovieModel(name.getText().toString(),image_link.getText().toString(),video_link.getText().toString(),category_names.get(movie_cat.getSelectedItemPosition()),series_names.get(movie_series.getSelectedItemPosition()));
                            FirebaseFirestore db=FirebaseFirestore.getInstance();
                            CollectionReference ref=db.collection("movies");
                            ref.document(id).set(m);
                            name.setText("");
                            image_link.setText("");
                            video_link.setText("");
                            movie_cat.setSelection(0);
                            movie_series.setSelection(0);
                            MoviesFragment.loadMovies();
                            movieModel=null;
                            id="";
                            Toast.makeText(getContext(),"update success",Toast.LENGTH_LONG).show();

                        }
                    }
                }
        );
        return v;
    }
}
