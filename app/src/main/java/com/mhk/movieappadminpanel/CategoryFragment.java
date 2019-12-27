package com.mhk.movieappadminpanel;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
public class CategoryFragment extends Fragment {

static ListView listView;
static Context context;
static ArrayList<String> categoryids=new ArrayList<String>();
static LayoutInflater layoutInflater;
static FragmentManager fragmentManager;
    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        fragmentManager=getFragmentManager();
        context=getContext();
        layoutInflater=getLayoutInflater();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.float_btn);
        floatingActionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategoryPopUP categoryPopUP = new CategoryPopUP();
                        FragmentManager fragmentManager = getFragmentManager();
                        categoryPopUP.show(fragmentManager, "show category");
                    }
                }
        );
        listView = view.findViewById(R.id.category_list);
        final ArrayList<CategoryModel> categoryModels = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference categoryRef = db.collection("categories");

        categoryRef.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            categoryModels.add(snapshot.toObject(CategoryModel.class));
                            categoryids.add(snapshot.getId());
                        }
                        CategoryAdapter adapter=new CategoryAdapter(categoryModels);
                        listView.setAdapter(adapter);

                    }
                });
        final EditText search_cat=view.findViewById(R.id.search_category);
        search_cat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(search_cat.getText().toString().equals("")){
                    categoryRef.get().addOnSuccessListener(
                            new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                        categoryModels.add(snapshot.toObject(CategoryModel.class));
                                        categoryids.add(snapshot.getId());
                                    }
                                    CategoryAdapter adapter=new CategoryAdapter(categoryModels);
                                    listView.setAdapter(adapter);

                                }
                            });
                }
                else {
                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                    CollectionReference ref=db.collection("categories");
                    ref.whereEqualTo("categoryName",s.toString())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    categoryModels.clear();
                                    categoryids.clear();
                                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                                        categoryModels.add(snapshot.toObject(CategoryModel.class));
                                        categoryids.add(snapshot.getId());

                                    }
                                    CategoryAdapter adapter=new CategoryAdapter(categoryModels);
                                    listView.setAdapter(adapter);
                                }
                            });

                }
            }
        });

        return view;
    }
    public static void getCategory(){
       final ArrayList<CategoryModel> categoryModels = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference categoryRef = db.collection("categories");
        categoryRef.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        categoryModels.clear();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            categoryModels.add(snapshot.toObject(CategoryModel.class));
                            categoryids.add(snapshot.getId());
                        }
                        CategoryAdapter adapter=new CategoryAdapter(categoryModels);
                        listView.setAdapter(adapter);

                    }
                });

    }

    private static class CategoryAdapter extends BaseAdapter {
        ArrayList<CategoryModel> categoryModels = new ArrayList<>();
        public CategoryAdapter(ArrayList<CategoryModel> categoryModels) {
            this.categoryModels=categoryModels;
        }


        @Override
        public int getCount() {
            return categoryModels.size();
        }

        @Override
        public Object getItem(int position) {
            return categoryModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v=layoutInflater.inflate(R.layout.category_list,null);
            TextView categorySr=v.findViewById(R.id.category_sr);
            TextView categoryName=v.findViewById(R.id.category_name);
            categorySr.setText(String.valueOf(position+1));
            categoryName.setText(categoryModels.get(position).categoryName);
            ImageView cat_delete=v.findViewById(R.id.cat_delete);
            cat_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Comfirmation!")
                            .setMessage("Are you sure to delete?")
                            .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                                    CollectionReference ref=db.collection("categories");
                                    ref.document(categoryids.get(position)).delete();
                                    ref.get().addOnSuccessListener(
                                            new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                    categoryids.clear();
                                                    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
                                                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                        categoryModels.add(snapshot.toObject(CategoryModel.class));
                                                        categoryids.add(snapshot.getId());
                                                    }
                                                    CategoryAdapter adapter=new CategoryAdapter(categoryModels);
                                                    listView.setAdapter(adapter);

                                                }
                                            });
                                }
                            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();


                }
            });
            ImageView cat_edit=v.findViewById(R.id.cat_edit);
            cat_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryPopUP popup=new CategoryPopUP();
                    popup.categoryModel=categoryModels.get(position);
                    popup.id=categoryids.get(position);
                    popup.show(fragmentManager,"edit category");
                }
            });
            return v;
        }
    }

}