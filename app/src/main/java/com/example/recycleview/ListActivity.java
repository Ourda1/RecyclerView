package com.example.recycleview;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ShareCompat;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleview.adapter.StarAdapter;
import com.example.recycleview.beans.Star;
import com.example.recycleview.service.StarService;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private List<Star> stars;
    private RecyclerView recyclerView;
    private StarAdapter starAdapter = null;
    private StarService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);

        stars = new ArrayList<>();
        service = StarService.getInstance();
        init();

        recyclerView = findViewById(R.id.recycle_view);
        starAdapter = new StarAdapter(this, service.findAll());
        recyclerView.setAdapter(starAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("ListActivity", "onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        setTitle("Stars");
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (starAdapter != null) {
                    starAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.share){
            String txt = "Stars";
            String mimeType = "text/plain";
            // Créer un intent explicite plutôt que d'utiliser ShareCompat
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, txt);
            sendIntent.setType(mimeType);

            // Récupérer toutes les applications qui peuvent gérer cet intent
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(sendIntent, 0);
            List<Intent> targetedShareIntents = new ArrayList<>();

            // Liste des packages à exclure
            List<String> excludePackages = new ArrayList<>();
            excludePackages.add("com.android.contacts");
            excludePackages.add("com.android.mms");
            excludePackages.add("com.android.email");
            excludePackages.add("com.google.android.contacts");
            excludePackages.add("com.google.android.apps.messaging");
            // Ajoutez d'autres packages à exclure si nécessaire

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                boolean excluded = false;

                // Vérifier si le package est dans la liste d'exclusion
                for (String excludePkg : excludePackages) {
                    if (packageName.contains(excludePkg)) {
                        excluded = true;
                        break;
                    }
                }

                if (!excluded) {
                    Intent targetedIntent = new Intent(Intent.ACTION_SEND);
                    targetedIntent.setType(mimeType);
                    targetedIntent.putExtra(Intent.EXTRA_TEXT, txt);
                    targetedIntent.setPackage(packageName);
                    targetedShareIntents.add(targetedIntent);
                }
            }

            // Si des intents sont trouvés après filtrage
            if (!targetedShareIntents.isEmpty()) {
                Intent chooserIntent = Intent.createChooser(
                        targetedShareIntents.remove(0), "Partager via");

                if (!targetedShareIntents.isEmpty()) {
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                            targetedShareIntents.toArray(new Intent[0]));
                }

                startActivity(chooserIntent);
            } else {
                // Fallback au cas où aucune application n'est trouvée après filtrage
                Toast.makeText(this, "Aucune application disponible pour partager",
                        Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void init() {
        service.create(new Star("Kate Bosworth", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9lhLNHTV-_QDBxl8XAapdAWSubkZvCAsyFQ&s", 3.5f));
        service.create(new Star("George Clooney", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/George_Clooney_66%C3%A8me_Festival_de_Venise_%28cropped2%29.jpg/800px-George_Clooney_66%C3%A8me_Festival_de_Venise_%28cropped2%29.jpg", 2.5f));
        service.create(new Star("Michelle Rodri", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Michelle_Rodriguez_2010_cropped.jpg/220px-Michelle_Rodriguez_2010_cropped.jpg", 3f));
        service.create(new Star("Emma Watson", "https://staticeu.sweet.tv/images/cache/person_profiles/BCE3AAISAJRXGIAD/22537-emma-uotson.jpg", 5f));
        service.create(new Star("Louise Bourgoin", "https://image.tmdb.org/t/p/h632/n83zu056tAm0wXHVrNk3khr8Gm4.jpg", 4f));
        service.create(new Star("Tom Holland", "https://resizing.flixster.com/G909Vhn6piIvxT-5i585P6-7I0o=/fit-in/352x330/v2/https://resizing.flixster.com/-XZAfHZM39UwaGJIFWKAE8fS0ak=/v3/t/assets/733885_v9_bc.jpg", 5f));
        service.create(new Star("Will Smith", "https://fr.web.img3.acsta.net/pictures/20/01/16/09/48/3201727.jpg", 4f));
        service.create(new Star("Tom Holland", "https://resizing.flixster.com/G909Vhn6piIvxT-5i585P6-7I0o=/fit-in/352x330/v2/https://resizing.flixster.com/-XZAfHZM39UwaGJIFWKAE8fS0ak=/v3/t/assets/733885_v9_bc.jpg", 5f));
        service.create(new Star("Kate Bosworth", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9lhLNHTV-_QDBxl8XAapdAWSubkZvCAsyFQ&s", 3.5f));
        service.create(new Star("George Clooney", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/George_Clooney_66%C3%A8me_Festival_de_Venise_%28cropped2%29.jpg/800px-George_Clooney_66%C3%A8me_Festival_de_Venise_%28cropped2%29.jpg", 2.5f));
        service.create(new Star("Michelle Rodri", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Michelle_Rodriguez_2010_cropped.jpg/220px-Michelle_Rodriguez_2010_cropped.jpg", 3f));
        service.create(new Star("Will Smith", "https://fr.web.img3.acsta.net/pictures/20/01/16/09/48/3201727.jpg", 5f));
        service.create(new Star("Louise Bourgoin", "https://image.tmdb.org/t/p/h632/n83zu056tAm0wXHVrNk3khr8Gm4.jpg", 4f));
    }

}