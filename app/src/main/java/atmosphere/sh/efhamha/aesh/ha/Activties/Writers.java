package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import atmosphere.sh.efhamha.aesh.ha.AdminApp.AdminActivity;
import atmosphere.sh.efhamha.aesh.ha.Fragments.ArticlesFragment;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.Models.WriterModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class Writers extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    TextView writer_name;
    ImageView writer_img;
    Spinner select_writer;
    LinearLayout layout;
    String selectedwriter;
    DatabaseReference reference  = FirebaseDatabase.getInstance().getReference();
    ArrayList<WriterModel>writers=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writers);

        select_writer=findViewById(R.id.writer_spiner);
        writer_img=findViewById(R.id.writer_img);
        writer_name=findViewById(R.id.writer_name);
        layout=findViewById(R.id.linearlay);

        loaddata();


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.writers, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        select_writer.setAdapter(adapter1);

        select_writer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedwriter = String.valueOf(parent.getItemAtPosition(position));


                for (WriterModel item : writers) {
                    if (item.getName().toLowerCase().contains(selectedwriter.toLowerCase())) {

                        Picasso.get()
                                .load(item.getImgurl())
                                .placeholder(R.drawable.user)
                                .error(R.drawable.user)
                                .into(writer_img);

                        writer_name.setText(item.getName());
                        layout.setVisibility(View.VISIBLE);


                        ArticlesFragment.writer_name=item.getName();
                        Fragment ArticlesFragment = new ArticlesFragment();
                        loadFragment(ArticlesFragment);

                        break;

                    }
                }



                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    private void loaddata()
    {
        reference.child("Writers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    writers.add(dataSnapshot1.getValue(WriterModel.class));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void loadFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_writer, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {


            ArticlesFragment.writer_name="";
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

    }

}


