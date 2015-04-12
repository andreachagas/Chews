package co.chagas.chews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Objects;

import co.chagas.chews.database.DatabaseConnector;


public class ViewRestaurantActivity extends ActionBarActivity {

    private long rowID; // selected restaurant's name
    private TextView nameTextView; // displays restaurant's name
    private TextView typeTextView; // displays restaurant's type
    private TextView addressTextView; // displays address
    private CheckBox drinkCheckBox; // displays if restaurant has drinks
    private RatingBar priceRatingBar; // displays pricing option
    private TextView commentTextView; // displays comments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);

        // get the EditTexts
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        typeTextView = (TextView) findViewById(R.id.typeTextView);
        addressTextView = (TextView) findViewById(R.id.addressTextView);
        drinkCheckBox = (CheckBox) findViewById(R.id.drinkCheckBox);
        priceRatingBar = (RatingBar) findViewById(R.id.priceRatingBar);
        commentTextView = (TextView) findViewById(R.id.commentTextView);

        // get the selected restaurant's row ID
        Bundle extras = getIntent().getExtras();
        rowID = extras.getLong("row_id");
    } // end method onCreate

    // called when the activity is first created
    @Override
    protected void onResume() {
        super.onResume();

        // create new LoadRestaurantTask and execute it
        new LoadRestaurantTask().execute(rowID);
    } // end method onResume

    // performs database query outside GUI thread
    private class LoadRestaurantTask extends AsyncTask<Long, Object, Cursor> {
        DatabaseConnector databaseConnector = new DatabaseConnector(ViewRestaurantActivity.this);

        // perform the database access
        @Override
        protected Cursor doInBackground(Long... params) {
            databaseConnector.open();

            // get a cursor containing all data on given entry
            return databaseConnector.getOneRestaurant(params[0]);
        } // end method doInBackground

        // use the Cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            result.moveToFirst(); // move to first item

            // get the column index for each data item
            int nameIndex = result.getColumnIndex("name");
            int typeIndex = result.getColumnIndex("type");
            int addressIndex = result.getColumnIndex("address");
            int drinkIndex = result.getColumnIndex("drink");
            int priceIndex = result.getColumnIndex("price");
            int commentIndex = result.getColumnIndex("comment");

            // fill TextViews, CheckBox and RatingBar with data
            nameTextView.setText(result.getString(nameIndex));
            typeTextView.setText(result.getString(typeIndex));
            addressTextView.setText(result.getString(addressIndex));
            drinkCheckBox.setText(result.getInt(drinkIndex));
            priceRatingBar.setNumStars(result.getInt(priceIndex));
            commentTextView.setText(result.getString(commentIndex));

            result.close(); // close the result cursor
            databaseConnector.close(); // close database connection
        } //  end method onPostExecute
    } // end class LoadRestaurantTask

    // create the Activity's menu from a menu resource XML file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_restaurant, menu);
        return true;
    } // end method onCreateOptionsMenu

    // handle choice from options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) { // switch based on selected MenuItem's ID
            case R.id.editItem:
                // create an Intent to launch the AddEditRestaurant Activity
                Intent addEditRestaurant = new Intent(this, AddEditRestaurantActivity.class);

                // pass the selected restaurant's data as extras with the Intent
                addEditRestaurant.putExtra("row_id", rowID);
                addEditRestaurant.putExtra("name", nameTextView.getText());
                addEditRestaurant.putExtra("type", typeTextView.getText());
                addEditRestaurant.putExtra("address", addressTextView.getText());
                addEditRestaurant.putExtra("drink", drinkCheckBox.getText());
                addEditRestaurant.putExtra("price", priceRatingBar.getNumStars());
                addEditRestaurant.putExtra("comment", commentTextView.getText());
                startActivity(addEditRestaurant); // start the Activity
                return true;
            case R.id.deleteItem:
                deleteRestaurant(); // delete the displayed restaurant
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } // end switch
    } // end method onOptionItemSelected

    // delete a restaurant
    private void deleteRestaurant(){
        // create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewRestaurantActivity.this);

        builder.setTitle(R.string.confirm_title); // title bar string
        builder.setMessage(R.string.confirm_delete); // message confirming deletion

        // provide an OK button that simply dismisses the dialog
        builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final DatabaseConnector databaseConnector = new DatabaseConnector(ViewRestaurantActivity.this);

                // create an AsyncTask that deletes the contact in another thread,
                // then calls finish after the deletion
                AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>() {
                    @Override
                    protected Object doInBackground(Long... params) {
                        databaseConnector.deleteRestaurant(params[0]);
                        return null;
                    } // end method doInBackground

                    @Override
                    protected void onPostExecute(Object result) {
                        finish(); // return to the RestaurantActivity
                    } // end method onPostExecute
                }; // end new AsyncTask

                // execute the AsyncTask to delete contact at rowID
                deleteTask.execute(new Long[] {rowID});
            } // end method onClick
        }); // end anonymous inner class and call to method setPositiveButton

        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show(); // display the Dialog
    }  // end method deleteRestaurant
} // end class ViewRestaurantActivity
