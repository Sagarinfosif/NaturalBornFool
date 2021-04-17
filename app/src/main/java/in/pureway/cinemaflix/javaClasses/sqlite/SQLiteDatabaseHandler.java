package in.pureway.cinemaflix.javaClasses.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.pureway.cinemaflix.models.ModelLoginRegister;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "photofit_db";
    private static final String TABLE_NAME = "photofit_profile";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_ONLINE_STATUS = "onlineStatus";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_BIO = "bio";
    private static final String KEY_STATUS = "status";
    private static final String KEY_SOCIAL_ID = "social_id";
    private static final String KEY_DOB = "dob";
    private static final String KEY_VIDEO = "video";
    private static final String KEY_REG_ID = "reg_id";
    private static final String KEY_DEVICE_TYPE = "device_type";
    private static final String KEY_LOGIN_TYPE = "login_type";
    private static final String KEY_CREATED = "created";
    private static final String KEY_UPDATED = "updated";
    private static final String[] COLUMNS = {KEY_ID, KEY_ONLINE_STATUS, KEY_USERNAME, KEY_NAME, KEY_EMAIL, KEY_PHONE, KEY_PASSWORD, KEY_IMAGE
            , KEY_BIO, KEY_STATUS, KEY_SOCIAL_ID, KEY_DOB, KEY_VIDEO, KEY_REG_ID, KEY_DEVICE_TYPE, KEY_LOGIN_TYPE, KEY_CREATED, KEY_UPDATED};


    private static SQLiteDatabaseHandler sInstance;

    // ...

    public static synchronized SQLiteDatabaseHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new SQLiteDatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }


    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + "( "
                + "id INTEGER PRIMARY KEY, " + "onlineStatus TEXT, "
                + "userName TEXT, " + "name TEXT," + "email TEXT," + "phone TEXT," + "password TEXT," + "image TEXT," + "bio TEXT," + "status TEXT," + "social_id TEXT," + "dob TEXT," + "video TEXT," + "reg_Id TEXT," + "device_type TEXT," + "login_type TEXT," + "created TEXT," + "updated TEXT)";

        db.execSQL(CREATION_TABLE);
        Log.i("database", "created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteDB(Context context) {
        context.deleteDatabase(DATABASE_NAME);
        Log.i("database", "deleted");
    }

    public ModelLoginRegister.Details getModel(int id, Context context) {
        SQLiteDatabase db = this.getReadableDatabase();

        String getPost = "SELECT * FROM " + TABLE_NAME + ";";
        Cursor cursor = getReadableDatabase().rawQuery(getPost, null);

        Log.d("database", cursor.getCount() + " post rows");
//        Cursor cursor = db.query(TABLE_NAME, // a. table
//                COLUMNS, // b. column names
//                "id = ?", // c. selections
//                new String[]{String.valueOf(id)}, // d. selections args
//                null, // e. group by
//                null, // f. having
//                null, // g. order by
//                null); // h. limit

        ModelLoginRegister modelLoginRegister = new ModelLoginRegister();
//        if (cursor .moveToFirst()) {
//            do {
//                modelLoginRegister.getDetails().setId(String.valueOf(cursor.getString(0)));
//                modelLoginRegister.getDetails().setOnlineStatus(String.valueOf(cursor.getString(1)));
//                modelLoginRegister.getDetails().setUsername(String.valueOf(cursor.getString(2)));
//                modelLoginRegister.getDetails().setName(String.valueOf(cursor.getString(3)));
//                modelLoginRegister.getDetails().setEmail(String.valueOf(cursor.getString(4)));
//                modelLoginRegister.getDetails().setPhone(String.valueOf(cursor.getString(5)));
//                modelLoginRegister.getDetails().setPassword(String.valueOf(cursor.getString(6)));
//                modelLoginRegister.getDetails().setImage(String.valueOf(cursor.getString(7)));
//                modelLoginRegister.getDetails().setBio(String.valueOf(cursor.getString(8)));
//                modelLoginRegister.getDetails().setStatus(String.valueOf(cursor.getString(9)));
//                modelLoginRegister.getDetails().setSocialId(String.valueOf(cursor.getString(10)));
//                modelLoginRegister.getDetails().setDob(String.valueOf(cursor.getString(11)));
//                modelLoginRegister.getDetails().setVideo(String.valueOf(cursor.getString(12)));
//                modelLoginRegister.getDetails().setRegId(String.valueOf(cursor.getString(13)));
//                modelLoginRegister.getDetails().setDeviceType(String.valueOf(cursor.getString(14)));
//                modelLoginRegister.getDetails().setLoginType(String.valueOf(cursor.getString(15)));
//                modelLoginRegister.getDetails().setCreated(String.valueOf(cursor.getString(16)));
//                modelLoginRegister.getDetails().setUpdated(String.valueOf(cursor.getString(17)));
//
//            }
//            while (cursor.moveToNext());
//        }
        return modelLoginRegister.getDetails();
    }


    public void addModel(ModelLoginRegister.Details modelLoginRegister) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, modelLoginRegister.getId());
        values.put(KEY_ONLINE_STATUS, modelLoginRegister.getOnlineStatus());
        values.put(KEY_USERNAME, modelLoginRegister.getUsername());
        values.put(KEY_NAME, modelLoginRegister.getName());
        values.put(KEY_EMAIL, modelLoginRegister.getEmail());
        values.put(KEY_PHONE, modelLoginRegister.getPhone());
        values.put(KEY_PASSWORD, modelLoginRegister.getPassword());
//        values.put(KEY_IMAGE, modelLoginRegister.getImage());
        values.put(KEY_BIO, modelLoginRegister.getBio());
        values.put(KEY_STATUS, modelLoginRegister.getStatus());
        values.put(KEY_SOCIAL_ID, modelLoginRegister.getSocialId());
        values.put(KEY_DOB, modelLoginRegister.getDob());
        values.put(KEY_VIDEO, modelLoginRegister.getVideo());
        values.put(KEY_REG_ID, modelLoginRegister.getRegId());
        values.put(KEY_DEVICE_TYPE, modelLoginRegister.getDeviceType());
//        values.put(KEY_LOGIN_TYPE, modelLoginRegister.getLoginType());
        values.put(KEY_CREATED, modelLoginRegister.getCreated());
        values.put(KEY_UPDATED, modelLoginRegister.getUpdated());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        Log.i("database","modelAdded");
    }

    public int updateModel(ModelLoginRegister.Details modelLoginRegister) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, modelLoginRegister.getName());
        values.put(KEY_ID, modelLoginRegister.getId());
        values.put(KEY_ONLINE_STATUS, modelLoginRegister.getOnlineStatus());
        values.put(KEY_USERNAME, modelLoginRegister.getUsername());
        values.put(KEY_EMAIL, modelLoginRegister.getEmail());
        values.put(KEY_PHONE, modelLoginRegister.getPhone());
        values.put(KEY_PASSWORD, modelLoginRegister.getPassword());
        values.put(KEY_IMAGE, modelLoginRegister.getImage());
        values.put(KEY_BIO, modelLoginRegister.getBio());
        values.put(KEY_STATUS, modelLoginRegister.getStatus());
        values.put(KEY_SOCIAL_ID, modelLoginRegister.getSocialId());
        values.put(KEY_DOB, modelLoginRegister.getDob());
        values.put(KEY_VIDEO, modelLoginRegister.getVideo());
        values.put(KEY_REG_ID, modelLoginRegister.getRegId());
        values.put(KEY_DEVICE_TYPE, modelLoginRegister.getDeviceType());
        values.put(KEY_LOGIN_TYPE, modelLoginRegister.getLoginType());
        values.put(KEY_CREATED, modelLoginRegister.getCreated());
        values.put(KEY_UPDATED, modelLoginRegister.getUpdated());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "id = ?", // selections
                new String[]{String.valueOf(modelLoginRegister.getId())});

        db.close();

        return i;
    }

}
