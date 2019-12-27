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

public class SeriesPopUp extends DialogFragment {

    public SeriesModel seriesModel;
    public String id="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.series_popup,container,false);
        Button btn_close=v.findViewById(R.id.btn_close);
        final EditText name=v.findViewById(R.id.series_name_edt);
        final EditText image_link=v.findViewById(R.id.series_img_edt);
        final EditText video_link=v.findViewById(R.id.series_video_link_edt);
        final Spinner series_cat=v.findViewById(R.id.series_cat_spinner);
        final ArrayList<String> category_names=new ArrayList<>();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference ref=db.collection("categories");

        if(seriesModel!=null) {
            name.setText(seriesModel.series_name);
            image_link.setText(seriesModel.series_img_link);
            video_link.setText(seriesModel.series_vid_link);

            ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    category_names.clear();
                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        CategoryModel c=snapshot.toObject(CategoryModel.class);
                        category_names.add(c.categoryName);
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,category_names);
                    series_cat.setAdapter(adapter);
                    for(int i=0;i<category_names.size();i++){
                        if (category_names.get(i).equals(seriesModel.series_category)){
                            series_cat.setSelection(i);
                            break;
                        }
                    }
                }
            });
        }
        else {
            ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    category_names.clear();
                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        CategoryModel c=snapshot.toObject(CategoryModel.class);
                        category_names.add(c.categoryName);
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,category_names);
                    series_cat.setAdapter(adapter);

                }
            });
        }



        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button btnsave=v.findViewById(R.id.series_popup_save);
        btnsave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      if (seriesModel==null){
                          if(!name.getText().toString().equals("")
                                  && !image_link.getText().toString().equals("")
                                  && !video_link.getText().toString().equals("")){
                              SeriesModel s=new SeriesModel(name.getText().toString(),image_link.getText().toString(),category_names.get(series_cat.getSelectedItemPosition()),video_link.getText().toString());
                              FirebaseFirestore db=FirebaseFirestore.getInstance();
                              CollectionReference ref=db.collection("series");
                              ref.add(s);
                              name.setText("");
                              image_link.setText("");
                              series_cat.setSelection(0);
                              video_link.setText("");
                              SeriesFragment.loadSeries();
                              Toast.makeText(getContext(),"save success",Toast.LENGTH_LONG).show();
                          }
                      }
                      else {
                          if(!name.getText().toString().equals("")
                                  && !image_link.getText().toString().equals("")
                                  && !video_link.getText().toString().equals("")){
                              SeriesModel s=new SeriesModel(name.getText().toString(),image_link.getText().toString(),category_names.get(series_cat.getSelectedItemPosition()),video_link.getText().toString());
                              FirebaseFirestore db=FirebaseFirestore.getInstance();
                              CollectionReference ref=db.collection("series");
                              ref.document(id).set(s);
                              name.setText("");
                              image_link.setText("");
                              series_cat.setSelection(0);
                              video_link.setText("");
                              SeriesFragment.loadSeries();
                              seriesModel=null;
                              id="";
                              Toast.makeText(getContext(),"update success",Toast.LENGTH_LONG).show();

                          }
                      }
                    }
                });
        return v;
    }
}
