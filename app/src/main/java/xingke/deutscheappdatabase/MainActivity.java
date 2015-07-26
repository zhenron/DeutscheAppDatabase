package xingke.deutscheappdatabase;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends Activity {

    // Database Helper
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Resources
        Resources res = getResources();
        String[] my_adjektiven = res.getStringArray(R.array.my_adjektiven);
        String[] my_substantiven = res.getStringArray(R.array.my_substantiven);

        db = new DatabaseHelper(getApplicationContext());

        //Creating the adjektiven database
/*
        for(int i=0; i<my_adjektiven.length; i++){
            db.createAdjektiv(new Adjketiv(my_adjektiven[i].toString()));
        }
*/

        //Creating the substantiven database
/*
        String[] substantiv_split;
        for(int j=0; j<my_substantiven.length; j++){
            substantiv_split = my_substantiven[j].split("\\s+");
            db.createSubstantiv(new Substantiv(substantiv_split[0].toString(), substantiv_split[1].toString()));
        }
*/

        // Inserting Adjektiven in db
        //long adjektiv1_id = db.createAdjektiv(new Adjketiv("rot"));

        //Log.e("Adjektive Count", "Adjektive count: " + db.getAdjektivCount());

        // Getting all Adjektiven
        //Log.d("Get Adjektiven", "Getting All Adjektiven");

        //List<Adjketiv> allAdjektiven = db.getAllAdjektiven();
        //for (Adjketiv adjketiv : allAdjektiven) {
        //    Log.d("Adjektiv", adjketiv.getAdjektiv());
        //}

        // Deleting an Adjektiv
        //Log.d("Delete Adjektiv", "Deleting an Adjektiv");
        //Log.d("Tag Count", "Tag Count Before Deleting: " + db.getAdjektivCount());

        //db.deleteAdjektiv(1);
        //db.deleteAdjektiv(3);

        //Log.d("Tag Count", "Tag Count After Deleting: " + db.getAdjektivCount());

        //db.deleteAllAdjektiven();

        //String[] substantiv1 = my_substantiven[0].split("\\s+");
        //Toast.makeText(getApplicationContext(),
        //        "artikel: " + substantiv1[0] + ", substantiv: " + substantiv1[1]
        //        , Toast.LENGTH_SHORT).show();

        int count = db.getAdjektivCount();
        int count_subs = db.getSubstantivCount();
        // Querying all Adjektiven
        List<Adjketiv> adjektiven = db.getAllAdjektiven();
        // Querying all Substantiven
        List<Substantiv> substantiven = db.getAllSunstantiven();

        // Print out properties
        for (Adjketiv adjketiv : adjektiven) {
            String log = "Id: " + adjketiv.getId() +
                    "/" + Integer.toString(count) +
                    " , Adjektiv: " + adjketiv.getAdjektiv();
            // Writing Adjektiven to log
            Log.d("AllAdjektiven", log);
//            Toast.makeText(getApplicationContext(),
//                    log, Toast.LENGTH_SHORT).show();
//            db.deleteTodoItem(ti)
        }

        // Print out Substantiv properties
        for (Substantiv substantiv : substantiven) {
            String log = "Id: " + substantiv.getId() +
                    "/" + Integer.toString(count_subs) +
                    ", Artikel: " + substantiv.getArtikel() +
                    ", Substantiv: " + substantiv.getSubstantiv();
            // Writing Adjektiven to log
            Log.d("AllSubstantiven", log);
            Toast.makeText(getApplicationContext(),
                    log, Toast.LENGTH_SHORT).show();
            //db.deleteTodoItem(ti)
        }

        // Don't forget to close database connection
        db.closeDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
