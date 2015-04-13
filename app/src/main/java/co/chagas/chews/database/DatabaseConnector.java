package co.chagas.chews.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;



/**
 * Created by andreachagas on 12/04/15.
 *
 * Provides easy connection and creation of Restaurants database
 */


public class DatabaseConnector {
    // database name
    private static final String DATABASE_NAME = "Restaurants";
    private SQLiteDatabase database; // database object
    private DatabaseOpenHelper databaseOpenHelper; // database helper

    // public constructor for DatabaseConnector
    public DatabaseConnector(Context context) {
        // create a new DatabaseOpenHelper
        databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    } // end DatabaseConnector constructor

    // open the database connection
    public void open() throws SQLException {
        // create or open database for reading/writing
        database = databaseOpenHelper.getWritableDatabase();
    } // end method

    // close the database connection
    public void close() throws SQLException {
        if(database != null)
            database.close(); // close the database connection
    } // end method close

    // insert a new restaurant in the database
    public void insertRestaurant(String name, String type, String address, String drink, int price,
                                 String comment) {
        ContentValues newRestaurant = new ContentValues();
        newRestaurant.put("name", name);
        newRestaurant.put("type", type);
        newRestaurant.put("address", address);
        newRestaurant.put("drink", drink);
        newRestaurant.put("price", price);
        newRestaurant.put("comment", comment);
        open(); // open the database
        database.insert("restaurants", null, newRestaurant);
        close(); // close the database
    } // end method insertRestaurant

    // insert a new restaurant in the database
    public void updateRestaurant(long id, String name, String type, String address, String drink, int price,
                                 String comment){
        ContentValues newRestaurant = new ContentValues();
        newRestaurant.put("name", name);
        newRestaurant.put("type", type);
        newRestaurant.put("address", address);
        newRestaurant.put("drink", drink);
        newRestaurant.put("price", price);
        newRestaurant.put("comment", comment);

        open();
        database.update("restaurants", editRestaurant, "_id=" + id, null);
        close();
    } // end method updateRestaurant

    // return a Cursor with all restaurant information in the database
    public Cursor getAllRestaurants() {
        return database.query("restaurants", new String[] {"_id", "name"}, null, null, null,
                null, null, "name");
    } // end method getAllRestaurants

    // get a Cursor containing all information about the contact specified by the given id
    public Cursor getOneRestaurant(long id) {
        return database.query("restaurants", null, "_id='" + id, null, null, null, null, null);
    } // end method getOneRestaurant

    // delete the restaurant specified by the given String name
    public void deleteRestaurant(long id) {
        open(); // open database
        database.delete("restaurants", "_id=" + id, null);
        close(); // close the database
    } // end method deleteRestaurant

    private class DatabaseOpenHelper extends SQLiteOpenHelper {
        // public constructor
        public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                  int version) {
            super(context, name, factory, version);
        } // end databaseopenhelper constructor

        // create the restaurants table when the database is created
        @Override
        public void onCreate(SQLiteDatabase db) {
            // query to create a new table named restaurants
            String createQuery = "CREATE TABLE restaurants" +
                    "(_id integer primary key autoincrement," +
                    "name TEXT, type TEXT, address TEXT, drink integer, price integer, comment TEXT)";
            db.execSQL(createQuery); // execute the query
        } // end method onCreate

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        } // end method onUpgrade
    } // end class DatabaseOpenHelper
} // end class DatabaseConnector
