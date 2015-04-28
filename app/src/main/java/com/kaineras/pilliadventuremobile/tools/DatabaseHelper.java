package com.kaineras.pilliadventuremobile.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kaineras.pilliadventuremobile.pojo.ImagesProperties;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created the first version by kaineras on 9/02/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    private Dao<ImagesProperties, Integer> mImageDao;
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "comics.db";
    private static final int DATABASE_VERSION = 1;
    private static final String NAME = "name";
    private static final String LANG = "lang";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    Dao<ImagesProperties, Integer> getImageDao() throws SQLException {
        if (mImageDao == null) {
            mImageDao = getDao(ImagesProperties.class);
        }
        return mImageDao;
    }


    public ImagesProperties getLastImage() throws SQLException {
        mImageDao = getImageDao();
        QueryBuilder queryBuilder = mImageDao.queryBuilder();
        queryBuilder.orderBy(NAME, false);
        return mImageDao.queryForFirst(queryBuilder.prepare());
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(LOG_TAG, "Creating database.");
            TableUtils.createTable(connectionSource, ImagesProperties.class);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Failed to create database.", e);
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //Don't used until next version
    }

    public ImagesProperties getImageByNameAndLanguage(String dateImage, String language) throws SQLException {
        mImageDao = getImageDao();
        QueryBuilder queryBuilder = mImageDao.queryBuilder();
        queryBuilder.setWhere(queryBuilder.where().like(NAME,dateImage).and().like(LANG,language));
        return mImageDao.queryForFirst(queryBuilder.prepare());
    }

    public void saveImageProperties(ImagesProperties imagesProperties) {
        try {
            Dao<ImagesProperties, Integer> dao = getImageDao();
            dao.create(imagesProperties);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Failed to SaveImage.", e);
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void deleteImagePropertiesAfterToday(){
        Tools tools = new Tools();
        try {
            DeleteBuilder<ImagesProperties, Integer> deleteBuilder = getImageDao().deleteBuilder();
            deleteBuilder.where().gt(NAME,tools.calendarToString(Calendar.getInstance()));
            deleteBuilder.delete();
        }catch (SQLException e) {
            Log.e(LOG_TAG, "Failed to Clean DB.", e);
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
