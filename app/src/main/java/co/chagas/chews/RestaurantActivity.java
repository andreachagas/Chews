package co.chagas.chews;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import co.chagas.chews.database.DatabaseConnector;


public class RestaurantActivity extends ListActivity {

    public static final String ROW_ID = "row_id"; // Intent extra key
    private ListView restaurantListView; // the ListActivity's ListView
    private CursorAdapter restaurantAdapter; // adapter for ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call super's onCreate
        restaurantListView = getListView(); // get the built-in ListView
        restaurantListView.setOnItemClickListener(viewRestaurantListener);

        // map each contact's name to a TextView in the ListView layout
        String[] from = new String[]{"name"};
        int[] to = new int[]{R.id.restaurantTextView};
        CursorAdapter restaurantAdapter = new SimpleCursorAdapter(RestaurantActivity.this,
                R.layout.restaurant_list_item, null, from, to);
        setListAdapter(restaurantAdapter);
    } // end method onCreate

    @Override
    protected void onResume() {
        super.onResume(); // call super's onResume method

        // create new GetRestaurantTask and execute it
        new GetRestaurantTask().execute((Object[]) null);
    } // end method onResume

    @Override
    protected void onStop() {
        Cursor cursor = restaurantAdapter.getCursor(); // get current Cursor

        if(cursor != null)
            cursor.deactivate(); // deactivate it

        restaurantAdapter.changeCursor(null); // adapter now has no Cursor
        super.onStop();
    } // end method onStop

    // performs database query outside GUI thread
    private class GetRestaurantTask extends AsyncTask<Object, Object, Cursor> {

        DatabaseConnector databaseConnector = new DatabaseConnector(RestaurantActivity.this);

        // perform the database access
        @Override
        protected Cursor doInBackground(Object... params) {
            databaseConnector.open();

            // get a cursor containing all restaurants
            return databaseConnector.getAllRestaurants();
        } // end method doInBackground

        // use the Cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result) {
            restaurantAdapter.changeCursor(result); // set the adapter's Cursor
            databaseConnector.close();
        } // end method onPostExecute
    } // end class GetRestaurantTask

    // create the Activity's menu from a menu resource XML file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant, menu);
        return true;
    }

    // handle choice from options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //create a new Intent to launch the AddEditRestaurantActivity
        Intent addNewRestaurant = new Intent(RestaurantActivity.this, AddEditRestaurantActivity.class);
        startActivity(addNewRestaurant); // start the AddEditRestaurant Activity
        return super.onOptionsItemSelected(item); // call super's method
    } // end method

    // event listener that responds to the user touching a contact's name in the ListView
    AdapterView.OnItemClickListener viewRestaurantListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // create an Intent to launch the ViewRestaurant Activity
            Intent viewRestaurant = new Intent(RestaurantActivity.this, ViewRestaurantActivity.class);

            // pass the selected contact's row ID as an extra with the Intent
            viewRestaurant.putExtra(ROW_ID, id);
            startActivity(viewRestaurant); // start the ViewRestaurant Activity
        } // end method onItemClick
    }; // end viewRestaurantListener
} // end class Restaurant
