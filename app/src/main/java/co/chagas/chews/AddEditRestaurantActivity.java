package co.chagas.chews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import co.chagas.chews.database.DatabaseConnector;


public class AddEditRestaurantActivity extends ActionBarActivity {

    private long rowID; // id of restaurant being edited, if any

    // EditTexts for restaurant information
    private EditText nameEditText;
    private EditText typeEditText;
    private EditText addressEditText;
    private CheckBox drinkEditText;
    private RatingBar priceEditText;
    private EditText commentEditText;

    // called when Activity is first started
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call super onCreate
        setContentView(R.layout.activity_add_edit_restaurant); // inflate the UI

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        typeEditText = (EditText) findViewById(R.id.typeEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        drinkEditText = (CheckBox) findViewById(R.id.drinkCheckBox);
        priceEditText = (RatingBar) findViewById(R.id.priceRatingBar);
        commentEditText = (EditText) findViewById(R.id.commentEditText);

        Bundle extras = getIntent().getExtras(); // get Bundle of extras

        // if there are extras, use them to populate the EditTexts
        if(extras != null) {
            rowID = extras.getLong("row_id");
            nameEditText.setText(extras.getString("name"));
            typeEditText.setText(extras.getString("type"));
            addressEditText.setText(extras.getString("address"));
            drinkEditText.setText(extras.getInt("drink"));
            priceEditText.setNumStars(extras.getInt("price"));
            commentEditText.setText(extras.getString("comment"));
        } // end if

        // set event listener for the Save Restaurant Button
        Button saveRestaurantButton = (Button) findViewById(R.id.saveRestaurantButton);
        saveRestaurantButton.setOnClickListener(saveRestaurantButtonClicked);
    } // end method onCreate

    // respond to event generated when user clicks the Done Button
    View.OnClickListener saveRestaurantButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(nameEditText.getText().length() != 0) {
                AsyncTask<Object, Object, Object> saveRestaurantTask =
                        new AsyncTask<Object, Object, Object>() {
                            @Override
                            protected Object doInBackground(Object... params) {
                                saveRestaurant(); // save restaurant to the database
                                return null;
                            } // end method doInBackground

                            @Override
                        protected void onPostExecute(Object result) {
                                finish(); // return to the previous Activity
                            } // end method onPostExecute
                        }; // end AsyncTask

                // save the restaurant to the database using a separate thread
                saveRestaurantTask.execute((Object[]) null);
            } // end if
            else {
                // create a new AlertDialog Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(AddEditRestaurantActivity.this);

                // set dialog title and message, and provide Button to dismiss
                builder.setTitle(R.string.errorTitle);
                builder.setMessage(R.string.error_message);
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show(); // display the Dialog
            } // end else
        } // end method
    }; // end OnClickListener saveRestaurantButtonClicked

    // saves restaurant information to database
    private void saveRestaurant() {
        // get DatabaseConnector to interact with the SQLite database
        DatabaseConnector databaseConnector = new DatabaseConnector(this);

        if(getIntent().getExtras() == null) {
            // insert the contact information into the database
            databaseConnector.insertRestaurant(
                    nameEditText.getText().toString(),
                    typeEditText.getText().toString(),
                    addressEditText.getText().toString(),
                    drinkEditText.getText().toString(),
                    priceEditText.getNumStars(),
                    commentEditText.getText().toString());
        } // end if
        else {
            databaseConnector.updateRestaurant(rowID,
                    nameEditText.getText().toString(),
                    typeEditText.getText().toString(),
                    addressEditText.getText().toString(),
                    drinkEditText.getText().toString(),
                    priceEditText.getNumStars(),
                    commentEditText.getText().toString());
        } // end else
    } // end class saveRestaurant


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
} // end class AddEditRestaurantActivity
