package xingke.deutscheappdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity {

    // Database Helper
    DatabaseHelper db;

    //Database Statistics
    private List<MyStatistic> myStatistics;
    private List<Statistic> statistics;

    //Just some variables I need
    private String[] artikeln = {"bestimmtem", "unbestimmtem", "ohne"};
    private String[] kasus = {"nominativ", "akkusativ", "dativ", "genitiv"};
    private String[] genus = {"maskulinum", "femininum", "neutrum", "plural"};
    private String[] bes_artikeln = {"der", "das", "die", "den", "dem", "des"};
    private String[] unbes_artikeln = {"ein", "eine", "einen", "einem", "einer", "eines"};
    private String[] endungen_adj = {"-e","-en", "-es", "-er", "-em"};
    private String[] endungen_sub = {"-n","-s","-es", "-"};
    private String artikel_case, kasus_case, genus_case;
    private int n_kasus, rdm_subs_id, myCase;
    private ArrayList<Integer> myMaskulinenIds, myFemininumIds, myNeutrumIds, myPluralIds;          //For storing the ids by genus
    private String selected_sub, selected_adj, rb_selectedArtikel, rb_selectedAdjektiv, rb_selectedSubstantiv;

    //Texts Views
    private TextView tv_kasus0, tv_kasus1, tv_kasus2, tv_kasus3, tv_adjektiv, tv_substantiv;
    private RadioButton rb_artikel1, rb_artikel2, rb_artikel3, rb_artikel4, rb_artikel5, rb_artikel6;
    private RadioButton rb_adjektiv1, rb_adjektiv2, rb_adjektiv3, rb_adjektiv4, rb_adjektiv5;
    private RadioButton rb_substantiv1, rb_substantiv2,rb_substantiv3,rb_substantiv4;
    //private RadioGroup rg_artikel,rg_substantiv,rg_adjektiv;
    private Button b_next, b_check;
    private Random random;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assignments
        tv_kasus0 = (TextView) findViewById(R.id.kasus0_id);
        tv_kasus1 = (TextView) findViewById(R.id.kasus1_id);
        tv_kasus2 = (TextView) findViewById(R.id.kasus2_id);
        tv_kasus3 = (TextView) findViewById(R.id.kasus3_id);
        tv_adjektiv = (TextView) findViewById(R.id.adjektiv_id);
        tv_substantiv = (TextView) findViewById(R.id.substantiv_id);

        rb_artikel1 = (RadioButton) findViewById(R.id.artikel1);
        rb_artikel2 = (RadioButton) findViewById(R.id.artikel2);
        rb_artikel3 = (RadioButton) findViewById(R.id.artikel3);
        rb_artikel4 = (RadioButton) findViewById(R.id.artikel4);
        rb_artikel5 = (RadioButton) findViewById(R.id.artikel5);
        rb_artikel6 = (RadioButton) findViewById(R.id.artikel6);

        rb_adjektiv1 = (RadioButton) findViewById(R.id.adjektiv1);
        rb_adjektiv2 = (RadioButton) findViewById(R.id.adjektiv2);
        rb_adjektiv3 = (RadioButton) findViewById(R.id.adjektiv3);
        rb_adjektiv4 = (RadioButton) findViewById(R.id.adjektiv4);
        rb_adjektiv5 = (RadioButton) findViewById(R.id.adjektiv5);

        rb_substantiv1 = (RadioButton) findViewById(R.id.substantiv1);
        rb_substantiv2 = (RadioButton) findViewById(R.id.substantiv2);
        rb_substantiv3 = (RadioButton) findViewById(R.id.substantiv3);
        rb_substantiv4 = (RadioButton) findViewById(R.id.substantiv4);

        //Setting AdjektivenEndungen in Radiobuttons
        rb_adjektiv1.setText(endungen_adj[0]);                     // {"-e","-en", "-es", "-er", "-em"};
        rb_adjektiv2.setText(endungen_adj[1]);
        rb_adjektiv3.setText(endungen_adj[2]);
        rb_adjektiv4.setText(endungen_adj[3]);
        rb_adjektiv5.setText(endungen_adj[4]);

        //Setting SubstantivenEndungen in Radiobuttons
        rb_substantiv1.setText(endungen_sub[0]);
        rb_substantiv2.setText(endungen_sub[1]);
        rb_substantiv3.setText(endungen_sub[2]);
        rb_substantiv4.setText(endungen_sub[3]);

        b_next = (Button) findViewById(R.id.button_next);
        b_check = (Button) findViewById(R.id.button_check);

        random = new Random();

        //Get Resources
        Resources res = getResources();
        String[] my_adjektiven = res.getStringArray(R.array.my_adjektiven);
        String[] my_substantiven = res.getStringArray(R.array.my_substantiven);
        String[] my_dek_art = res.getStringArray(R.array.my_dek_art);
        String[] my_dek_adj = res.getStringArray(R.array.my_dek_adj);
        String[] my_dek_sub = res.getStringArray(R.array.my_dek_sub);

        //Creating the Database helper
        db = new DatabaseHelper(getApplicationContext());

        //Creating the adjektiven database if it doesn't exist
        if(db.getAdjektivCount() == 0){
            for(int i=0; i<my_adjektiven.length; i++){
                db.createAdjektiv(new Adjketiv(my_adjektiven[i].toString()));
            }
        }

        //Creating the substantiven database if it doesn't exist
        if(db.getSubstantivCount() == 0){
            String[] substantiv_split;
            for(int j=0; j<my_substantiven.length; j++){
                substantiv_split = my_substantiven[j].split("\\s+");
                db.createSubstantiv(new Substantiv(substantiv_split[0].toString(), substantiv_split[1].toString()));
            }
        }

        //Creating the statistics database if it doesn't exist
        if(db.getStatisticsCount() == 0){
            int count = 0;
            for(int k = 0; k < artikeln.length ; k++){
                for(int l = 0; l < kasus.length; l++){
                    for(int m = 0; m < genus.length; m++){
                        db.createStatistic(new Statistic(artikeln[k].toString(), kasus[l].toString(), genus[m].toString(),
                                my_dek_art[count].toString(), my_dek_adj[count].toString(), my_dek_sub[count].toString()));
                        count++;
                    }
                }
            }
        }

        //Getting the ids of the diferent genus
        myMaskulinenIds = db.getSubstantivenIdsByGenus("m");
        myFemininumIds = db.getSubstantivenIdsByGenus("f");
        myNeutrumIds = db.getSubstantivenIdsByGenus("n");
        myPluralIds = db.getSubstantivenIdsByGenus("p");

        n_kasus = db.getStatisticsCount();                      //Get the number of kasus
        myStatistics = db.getStatistics();                     //Get myStatistics
        statistics = db.getAllStatistics();                     //Get all the statistics table

        /*
        =========================================
         */
        NextCase();
        /*
        =========================================
         */


        //Toast.makeText(getApplicationContext(),
        //        db.getSubstantiv(random.nextInt(myMaskulinenIds.size())).getArtikel() + " "
        //                + db.getSubstantiv(random.nextInt(myMaskulinenIds.size())).getSubstantiv(), Toast.LENGTH_LONG).show();

        //Toast.makeText(getApplicationContext(),
        //       Integer.toString(my_case) + "/16=" + Integer.toString(my_case/16) + " - artikel: " + artikel_case + ", kasus: " + kasus_case + ", genus: " + genus_case, Toast.LENGTH_LONG).show();



        //tv_adjektiv.setText((db.getAdjektiv(34)).getAdjektiv());
        //tv_substantiv.setText((db.getSubstantiv(3)).getSubstantiv());

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
/*
        int count_adjs = db.getAdjektivCount();
        int count_subs = db.getSubstantivCount();
        // Querying all Adjektiven
        List<Adjketiv> adjektiven = db.getAllAdjektiven();
        // Querying all Substantiven
        List<Substantiv> substantiven = db.getAllSunstantiven();
        // Print out Adjektiven properties
        for (Adjketiv adjketiv : adjektiven) {
            String log = "Id: " + adjketiv.getId() +
                    "/" + Integer.toString(count_adjs) +
                    " , Adjektiv: " + adjketiv.getAdjektiv();
            // Writing Adjektiven to log
            Log.d("AllAdjektiven", log);
//            Toast.makeText(getApplicationContext(),
//                    log, Toast.LENGTH_SHORT).show();
//            db.deleteTodoItem(ti)
        }
        // Print out Substantiv properties
        int count_subs = db.getSubstantivCount();
        // Querying all Substantiven
        List<Substantiv> substantiven = db.getAllSunstantiven();
        for (Substantiv substantiv : substantiven) {
            String log = "Id: " + substantiv.getId() +
                    "/" + Integer.toString(count_subs) +
                    ", Artikel: " + substantiv.getArtikel() +
                    ", Substantiv: " + substantiv.getSubstantiv();
            // Writing Adjektiven to log
            Log.d("AllSubstantiven", log);
            //Toast.makeText(getApplicationContext(),
            //        log, Toast.LENGTH_SHORT).show();
            //db.deleteTodoItem(ti)
        }
        int count_stats = db.getStatisticsCount();
        // Print out Statistics properties
        for (Statistic statistic : statistics) {
            String log = "Id: " + statistic.getId() +
                    "/" + Integer.toString(count_stats) +
                    ", Artikel: " + statistic.getArtikel() +
                    ", Kasus: " + statistic.getKasus() +
                    ", Genus: " + statistic.getGenus() +
                    ", Dek_art: " + statistic.getDek_art() +
                    ", Dek_adj: " + statistic.getDek_adj() +
                    ", Dek_sub: " + statistic.getDek_sub() +
                    ", Ocurrences: " + statistic.getOccurrences() +
                    ", Rights: " + statistic.getRights() +
                    ", Wrongs: " + statistic.getWrongs() +
                    ", Success rate: " + statistic.getSuccess_rate();
            // Writing Adjektiven to log
            Log.d("AllStatistics", log);
            //Toast.makeText(getApplicationContext(),
            //        log, Toast.LENGTH_SHORT).show();
            //db.deleteTodoItem(ti)
        }
*/
        b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                =========================================
                 */
                        NextCase();
                /*
                =========================================
                 */

                //Toast.makeText(getApplicationContext(),
                //        db.getSubstantiv(random.nextInt(myMaskulinenIds.size())).getArtikel() + " "
                //               + db.getSubstantiv(random.nextInt(myMaskulinenIds.size())).getSubstantiv(), Toast.LENGTH_LONG).show();
            }
        });

        // Don't forget to close database connection
        db.closeDB();
    }

    public void onButtonCheckClicked(View view){
        int my_fcase = (myCase - 1) / 16;
        int check = 0b000;
        String title, message;
        String myFckArtikel = statistics.get(myCase - 1).getDek_art();
        String myFckAdjektiv = statistics.get(myCase - 1).getDek_adj();
        String myFckSubstantiv = statistics.get(myCase - 1).getDek_sub();
        String art_dek, adj_endung = "", sub_endung = "";

        if(my_fcase == 2){}
        else if(rb_selectedArtikel.equals(myFckArtikel)){           //bitwise AND &, bitwise exclusive OR ^, bitwise inclusive OR |
            //Toast.makeText(getApplicationContext(),
            //        "artikel korrekt", Toast.LENGTH_SHORT).show();
            check = check | 0b001;
        }

        if(rb_selectedAdjektiv.equals(myFckAdjektiv)){
            //Toast.makeText(getApplicationContext(),
            //        "adjektiv korrekt", Toast.LENGTH_SHORT).show();
            check = check | 0b010;
        }

        if(rb_selectedSubstantiv.equals(myFckSubstantiv)){
            //Toast.makeText(getApplicationContext(),
            //        "substantiv korrekt", Toast.LENGTH_SHORT).show();
            check = check | 0b100;
        }

        for(int i = 0; i < myFckAdjektiv.length() - 1; i++){
            adj_endung += myFckAdjektiv.charAt(i+1);
        }
        for(int i = 0; i < myFckSubstantiv.length() - 1; i++){
            sub_endung += myFckSubstantiv.charAt(i+1);
        }
        if(myFckArtikel.length() == 1){
            art_dek = "";
        }
        else{
            art_dek = myFckArtikel.toString();
        }

        message = art_dek + " " + selected_adj + adj_endung + " " + db.getSubstantiv(rdm_subs_id).getSubstantiv() + sub_endung;

        if(check == 0b111){       //Correct!
            title = "Korrekt!";
        }
        else{
            title = "Falsch! :(";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Nächste",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        sortStatistics();                                       //Sort MyStatistics
                        myCase = chooseCase();
                        artikel_case = statistics.get(myCase - 1).getArtikel();
                        kasus_case = statistics.get(myCase - 1).getKasus();
                        genus_case = statistics.get(myCase - 1).getGenus();

                        tv_kasus0.setText(Integer.toString(myCase));
                        tv_kasus1.setText(artikel_case);
                        tv_kasus2.setText(kasus_case);
                        tv_kasus3.setText(genus_case);

                        setRadioButtons(myCase);

                        if(genus_case.equals("maskulinum")){               //{"maskulinum", "femininum", "neutrum", "plural"};
                            rdm_subs_id = random.nextInt(myMaskulinenIds.size());
                            rdm_subs_id = myMaskulinenIds.get(rdm_subs_id);
                            selected_sub = "der " + db.getSubstantiv(rdm_subs_id).getSubstantiv();
                        }
                        else if(genus_case.equals("femininum")){
                            rdm_subs_id = random.nextInt(myFemininumIds.size());
                            rdm_subs_id = myFemininumIds.get(rdm_subs_id);
                            selected_sub = "die " + db.getSubstantiv(rdm_subs_id).getSubstantiv();
                        }
                        else if(genus_case.equals("neutrum")){
                            rdm_subs_id = random.nextInt(myNeutrumIds.size());
                            rdm_subs_id = myNeutrumIds.get(rdm_subs_id);
                            selected_sub = "das " + db.getSubstantiv(rdm_subs_id).getSubstantiv();
                        }
                        else if(genus_case.equals("plural")){
                            rdm_subs_id = random.nextInt(myPluralIds.size());
                            rdm_subs_id = myPluralIds.get(rdm_subs_id);
                            selected_sub = "die " + db.getSubstantiv(rdm_subs_id).getSubstantiv();
                        }

                        selected_adj = db.getAdjektiv(random.nextInt(db.getAdjektivCount()) + 1).getAdjektiv();

                        tv_substantiv.setText(selected_sub);
                        tv_adjektiv.setText(selected_adj);

                    }
                })
                .setNegativeButton("Zurück",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


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

    public void sortStatistics(){                                                          //No se si funciona este metodo
        for (int i=0; i<(myStatistics.size() - 1); i++){
            for (int j=i+1; j<myStatistics.size(); j++){
                if(myStatistics.get(i).getRate() > myStatistics.get(j).getRate()){
                    double rate_aux = myStatistics.get(i).getRate();
                    int id_aux = myStatistics.get(i).getId();
                    myStatistics.get(i).setRate(myStatistics.get(j).getRate());
                    myStatistics.get(i).setId(myStatistics.get(j).getId());
                    myStatistics.get(j).setRate(rate_aux);
                    myStatistics.get(j).setId(id_aux);
                }
            }
        }
    }

    public int chooseCase(){
        int randomCase = random.nextInt(6);
        int randomCase2;

        if(randomCase == 0){                                //case easy 1/6
            randomCase2 = random.nextInt(n_kasus - (n_kasus*2/3)) + (n_kasus*2/3 + 1);          //[33-48]
        }
        else if (randomCase > 0 && randomCase < 3){         //case medium 2/6
            randomCase2 = random.nextInt(n_kasus*2/3 - (n_kasus/3)) + (n_kasus/3 + 1);          //[17-32]
        }
        else{                                               //case hard 3/6
            randomCase2 = random.nextInt(n_kasus/3) + 1;                                        //[1-16]
        }
        return randomCase2;
    }

    public void setRadioButtons(int my_case){
        int my_fcase = (my_case - 1) / 16;

        switch(my_fcase) {
            case 0:                     //Bestimmtem Artikel case
                rb_artikel1.setVisibility(View.VISIBLE);
                rb_artikel2.setVisibility(View.VISIBLE);
                rb_artikel3.setVisibility(View.VISIBLE);
                rb_artikel4.setVisibility(View.VISIBLE);
                rb_artikel5.setVisibility(View.VISIBLE);
                rb_artikel6.setVisibility(View.VISIBLE);
                rb_artikel1.setText(bes_artikeln[0]);
                rb_artikel2.setText(bes_artikeln[1]);
                rb_artikel3.setText(bes_artikeln[2]);
                rb_artikel4.setText(bes_artikeln[3]);
                rb_artikel5.setText(bes_artikeln[4]);
                rb_artikel6.setText(bes_artikeln[5]);
                //Toast.makeText(getApplicationContext(),
                //       "Bestimmtem Artikel case", Toast.LENGTH_SHORT).show();
                break;

            case 1:                     //Unbestimmtem Artikel case
                rb_artikel1.setVisibility(View.VISIBLE);
                rb_artikel2.setVisibility(View.VISIBLE);
                rb_artikel3.setVisibility(View.VISIBLE);
                rb_artikel4.setVisibility(View.VISIBLE);
                rb_artikel5.setVisibility(View.VISIBLE);
                rb_artikel6.setVisibility(View.VISIBLE);
                rb_artikel1.setText(unbes_artikeln[0]);
                rb_artikel2.setText(unbes_artikeln[1]);
                rb_artikel3.setText(unbes_artikeln[2]);
                rb_artikel4.setText(unbes_artikeln[3]);
                rb_artikel5.setText(unbes_artikeln[4]);
                rb_artikel6.setText(unbes_artikeln[5]);
                //Toast.makeText(getApplicationContext(),
                //        "Unbestimmtem Artikel case", Toast.LENGTH_SHORT).show();
                break;

            case 2:                     //Ohne Artikel case
                rb_artikel1.setVisibility(View.INVISIBLE);
                rb_artikel2.setVisibility(View.INVISIBLE);
                rb_artikel3.setVisibility(View.INVISIBLE);
                rb_artikel4.setVisibility(View.INVISIBLE);
                rb_artikel5.setVisibility(View.INVISIBLE);
                rb_artikel6.setVisibility(View.INVISIBLE);
                //Toast.makeText(getApplicationContext(),
                //        "Ohne Artikel case", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(getApplicationContext(),
                        "FUCKING ERROR CASE", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRadioButtonArtikelClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.artikel1:
                if (checked)
                    rb_selectedArtikel= rb_artikel1.getText().toString();
                break;
            case R.id.artikel2:
                if (checked)
                    rb_selectedArtikel= rb_artikel2.getText().toString();
                break;
            case R.id.artikel3:
                if (checked)
                    rb_selectedArtikel= rb_artikel3.getText().toString();
                break;
            case R.id.artikel4:
                if (checked)
                    rb_selectedArtikel= rb_artikel4.getText().toString();
                break;
            case R.id.artikel5:
                if (checked)
                    rb_selectedArtikel= rb_artikel5.getText().toString();
                break;
            case R.id.artikel6:
                if (checked)
                    rb_selectedArtikel= rb_artikel6.getText().toString();
                break;
        }
    }

    public void onRadioButtonAdjektivClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.adjektiv1:
                if (checked)
                    rb_selectedAdjektiv= rb_adjektiv1.getText().toString();
                break;
            case R.id.adjektiv2:
                if (checked)
                    rb_selectedAdjektiv= rb_adjektiv2.getText().toString();
                break;
            case R.id.adjektiv3:
                if (checked)
                    rb_selectedAdjektiv= rb_adjektiv3.getText().toString();
                break;
            case R.id.adjektiv4:
                if (checked)
                    rb_selectedAdjektiv= rb_adjektiv4.getText().toString();
                break;
            case R.id.adjektiv5:
                if (checked)
                    rb_selectedAdjektiv= rb_adjektiv5.getText().toString();
                break;
        }
    }

    public void onRadioButtonSubstantivClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.substantiv1:
                if (checked)
                    rb_selectedSubstantiv= rb_substantiv1.getText().toString();
                break;
            case R.id.substantiv2:
                if (checked)
                    rb_selectedSubstantiv= rb_substantiv2.getText().toString();
                break;
            case R.id.substantiv3:
                if (checked)
                    rb_selectedSubstantiv= rb_substantiv3.getText().toString();
                break;
            case R.id.substantiv4:
                if (checked)
                    rb_selectedSubstantiv= rb_substantiv4.getText().toString();
                break;
        }
    }

    public void NextCase(){
        sortStatistics();                                       //Sort MyStatistics
        myCase = chooseCase();                                  //choose a case
        artikel_case = statistics.get(myCase - 1).getArtikel(); // bes, unbes, ohne
        kasus_case = statistics.get(myCase - 1).getKasus();     //nom, akk, gen, dat
        genus_case = statistics.get(myCase - 1).getGenus();     // mask, fem, neu, pl

        //Update the text views
        tv_kasus0.setText(Integer.toString(myCase));
        tv_kasus1.setText(artikel_case);
        tv_kasus2.setText(kasus_case);
        tv_kasus3.setText(genus_case);

        //Update the radio buttons for artikel
        setRadioButtons(myCase);

        //Select a subtantiv
        if(genus_case.equals("maskulinum")){               //{"maskulinum", "femininum", "neutrum", "plural"};
            rdm_subs_id = myMaskulinenIds.get(random.nextInt(myMaskulinenIds.size()));
            selected_sub = "der " + db.getSubstantiv(rdm_subs_id).getSubstantiv();
        }
        else if(genus_case.equals("femininum")){
            rdm_subs_id = myFemininumIds.get(random.nextInt(myFemininumIds.size()));
            selected_sub = "die " + db.getSubstantiv(rdm_subs_id).getSubstantiv();
        }
        else if(genus_case.equals("neutrum")){
            rdm_subs_id = myNeutrumIds.get(random.nextInt(myNeutrumIds.size()));
            selected_sub = "das " + db.getSubstantiv(rdm_subs_id).getSubstantiv();
        }
        else if(genus_case.equals("plural")){
            rdm_subs_id = myPluralIds.get(random.nextInt(myPluralIds.size()));
            selected_sub = "die " + db.getSubstantiv(rdm_subs_id).getSubstantiv();
        }

        //Select an adjektiv
        selected_adj = db.getAdjektiv(random.nextInt(db.getAdjektivCount()) + 1).getAdjektiv();

        //Update the substantiv and adjektiv text views
        tv_substantiv.setText(selected_sub);
        tv_adjektiv.setText(selected_adj);
    }
}
