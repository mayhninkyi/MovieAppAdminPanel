package com.mhk.movieappadminpanel;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.Layout;
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
public class SeriesFragment extends Fragment {

    static ArrayList<SeriesModel> seriesModels=new ArrayList<>();
    static ArrayList<String> ids=new ArrayList<String>();
    static LayoutInflater layoutInflater;
    static Context context;
    static ListView listView;
    static FragmentManager fragmentManager;

    public SeriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_series, container, false);
        FloatingActionButton fbtn=v.findViewById(R.id.series_float_btn);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesPopUp popup=new SeriesPopUp();
                FragmentManager fm=getFragmentManager();
                popup.show(fm,"show series");
            }
        });

        layoutInflater=getLayoutInflater();
        context=getContext();
       listView=v.findViewById(R.id.series_list);
       fragmentManager=getFragmentManager();
        loadSeries();
        final EditText search_series=v.findViewById(R.id.search_series);
        search_series.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(search_series.getText().toString().equals("")){
                    loadSeries();
                }
                else {
                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                    CollectionReference ref=db.collection("series");
                    ref.whereEqualTo("series_name",s.toString())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    seriesModels.clear();
                                    ids.clear();
                                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                        ids.add(snapshot.getId());
                                        seriesModels.add(snapshot.toObject(SeriesModel.class));
                                    }
                                    SeriesAdapter adapter = new SeriesAdapter(seriesModels);
                                    listView.setAdapter(adapter);
                                }
                            });
                }
            }
        });
        return v;
    }
    public static void loadSeries(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference ref=db.collection("series");
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                seriesModels.clear();
                ids.clear();
                for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                    ids.add(snapshot.getId());
                    seriesModels.add(snapshot.toObject(SeriesModel.class));
                }
                SeriesAdapter adapter=new SeriesAdapter(seriesModels);
                listView.setAdapter(adapter);
            }
        });

    }
private static class SeriesAdapter extends BaseAdapter {
        ArrayList<SeriesModel> seriesModels=new ArrayList<>();

    public SeriesAdapter(ArrayList<SeriesModel> seriesModels) {
        this.seriesModels = seriesModels;
    }

    @Override
    public int getCount() {
        return seriesModels.size();
    }

    @Override
    public Object getItem(int position) {
        return seriesModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.movie_list, null);
        TextView sr = v.findViewById(R.id.moviesr_txt);
        TextView name = v.findViewById(R.id.moviename_txt);
        final ImageView img = v.findViewById(R.id.movieimg);
        sr.setText(String.valueOf(position + 1));
        name.setText(seriesModels.get(position).series_name);
        Glide.with(context)
                .load(seriesModels.get(position).getSeries_img_link())
                .override(150, 200)
                .into(img);
        RelativeLayout item = (RelativeLayout) v.findViewById(R.id.menus);
        item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final PopupMenu popupMenu=new PopupMenu(context,img);
                MenuInflater inflater=popupMenu.getMenuInflater();
                inflater.inflate(R.menu.popupmenu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.delete_menu){
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setTitle("Comfirmation")
                                    .setMessage("Are you sure to delete?")
                                    .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            CollectionReference ref = db.collection("series");
                                            ref.document(ids.get(position)).delete();
                                            loadSeries();
                                        }
                                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                        }

                        if (item.getItemId()==R.id.edit_menu) {
                            SeriesPopUp popUp=new SeriesPopUp();
                            popUp.seriesModel=seriesModels.get(position);
                            popUp.id=ids.get(position);
                            popUp.show(fragmentManager,"edit series");

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

