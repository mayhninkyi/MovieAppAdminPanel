package com.mhk.movieappadminpanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CategoryPopUP extends DialogFragment {

    public CategoryModel categoryModel;
    public String id="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.category_popup,container,false);
        final Button savebtn=v.findViewById(R.id.popup_save_btn);
        final EditText edt=v.findViewById(R.id.category_txt);
        if(categoryModel!=null){
            edt.setText(categoryModel.getCategoryName());
        }

        savebtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!edt.getText().toString().equals("")&& edt.getText().toString()!=null) {
                            if (categoryModel == null) {
                                CategoryModel c = new CategoryModel(edt.getText().toString());
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference ref = db.collection("categories");
                                ref.add(c);
                                edt.setText("");
                                CategoryFragment.getCategory();
                                Toast.makeText(getContext(), "save success", Toast.LENGTH_LONG).show();
                            }
                            else {
                                CategoryModel c = new CategoryModel(edt.getText().toString());
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference ref = db.collection("categories");
                                ref.add(c);
                                edt.setText("");
                                CategoryFragment.getCategory();
                                Toast.makeText(getContext(), "edit success", Toast.LENGTH_LONG).show();
                                categoryModel=null;
                                id="";
                            }

                        }

                        else {
                            Toast.makeText(getContext(),"please fill",Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
        Button cat_cancel=v.findViewById(R.id.popup_cancel_btn);
        cat_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(categoryModel!=null){
                            categoryModel=null;
                            id="";
                        }
                        edt.setText("");
                    }
                }
        );

        return v;
    }
}
