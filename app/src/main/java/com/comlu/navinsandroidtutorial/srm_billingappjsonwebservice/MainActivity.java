package com.comlu.navinsandroidtutorial.srm_billingappjsonwebservice;


import android.app.ProgressDialog;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {
    EditText v1;
    EditText v2;
    TextView t1;
    TextView t2;
    int discount=20;
    //URL to get JSON Array
    private static String url = "http://navinsandroidtutorial.comlu.com/files/Pricelist_json.json";

    //JSON Node Names
    private static final String TAG_USER = "itemPrices";

    private static final String TAG_ITEMPRICE1 = "itemPrice1";
    private static final String TAG_ITEMPRICE2 = "itemPrice2";

    JSONArray jsonArray = null;
    String id;
    String itemPrice1;
    String itemPrice2;
    JSONParser jParser;
    //     createNecessaryObjects();

    //prepareObjectsForDisplay();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        v1=(EditText)findViewById(R.id.editText);
        v2=(EditText)findViewById(R.id.editText1);

        t1=(TextView)findViewById(R.id.textView1);
        t2=(TextView)findViewById(R.id.textView2);
        // Creating new JSON Parser


        // Getting JSON from URL
        new DownloadData().execute(url
               );
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton) {
                    discount = 10;
                    Toast.makeText(getBaseContext(), "10 % discount selected",
                            Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.radioButton2) {
                    discount = 20;
                    Toast.makeText(getBaseContext(), "20 % discount selected",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
        Button submit=(Button)findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    int val1 = Integer.parseInt(v1.getText().toString());
                    int val2 = Integer.parseInt(v2.getText().toString());
                    double result = val1*Integer.parseInt(itemPrice1 )+ val2*Integer.parseInt(itemPrice2);
                    result = result - result * ((double) discount / 100);

                    Intent i = new Intent(MainActivity.this, ResultActivity.class);
                    i.putExtra("result", result);
                    startActivity(i);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "One of the field entry is not entered or Wrongly entered",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
        Button modify=(Button)findViewById(R.id.button1);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadData().execute(url
                );

            }
        });

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

class DownloadData extends AsyncTask<String, String, String> {

    ProgressDialog pd = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(MainActivity.this);
        pd.setTitle("Converting...");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();

    }

    @Override
    protected String doInBackground(String... params) {

        String url = params[0];

        // Creating JSON Parser instance

        JSONParser jParser = new JSONParser();

       JSONObject  jsonObj=jParser.getJSONFromUrl(url);

        try {
            // Getting JSON Array
            jsonArray = jsonObj.getJSONArray(TAG_USER);
            JSONObject c = jsonArray.getJSONObject(0);
            Log.d("JSON array", c.toString());

            itemPrice1 = c.getString(TAG_ITEMPRICE1);
            itemPrice2 = c.getString(TAG_ITEMPRICE2);
            Log.d("itemPrice1", itemPrice1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            t1.setText(" x "+Integer.parseInt(itemPrice1)+" Rs");
            t2.setText(" x " + Integer.parseInt(itemPrice2) + " Rs");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        pd.dismiss();

    }
}
}