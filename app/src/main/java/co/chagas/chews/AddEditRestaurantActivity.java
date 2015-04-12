package co.chagas.chews;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class AddEditRestaurantActivity extends ActionBarActivity {

    private long rowID; // id of restaurant being edited, if any

    // EditTexts for restaurant information
    private EditText nameEditText;
    private EditText typeEditText;
    private EditText addressEditText;
    private EditText drinkEditText;
    private EditText priceEditText;
    private EditText commentEditText;

    // called when Activity is first started
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call super onCreate
        setContentView(R.layout.activity_add_edit_restaurant); // inflate the UI

        nameEditText = (EditText) findViewById(R.id.nameEditText);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit_restaurant, menu);
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
