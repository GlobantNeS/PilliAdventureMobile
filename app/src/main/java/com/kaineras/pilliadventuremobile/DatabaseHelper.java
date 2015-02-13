package com.kaineras.pilliadventuremobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created the first version by kaineras on 9/02/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    private final static String LOG_TAG = DatabaseHelper.class.getSimpleName();
    private final static String ENGLISH_DATABASE_NAME = "eng_comics.db";
    private final static int DATABASE_VERSION = 1;
    private Dao<EnglishImagesProperties, Integer> mImageDao;

    private final static String ID = "_id";
    private final static String NAME = "name";
    private final static String DESC = "desc";

    public DatabaseHelper(Context context) {
        super(context, ENGLISH_DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Dao<EnglishImagesProperties, Integer> getEnglishImageDao() throws SQLException {
        if (mImageDao == null) {
            mImageDao = getDao(EnglishImagesProperties.class);
        }
        return mImageDao;
    }

    public List<EnglishImagesProperties> getPropertiesImage() throws SQLException {

        return getEnglishImageDao().queryForAll();
    }

    public List<EnglishImagesProperties> getImageByMonth(String year, String month)throws SQLException {
        mImageDao = getEnglishImageDao();
        QueryBuilder queryBuilder = mImageDao.queryBuilder();
        queryBuilder.setWhere(queryBuilder.orderBy(NAME,true).where().like(NAME,year+"-"+month+"-%"));
        return mImageDao.query(queryBuilder.prepare());
    }

    public EnglishImagesProperties getLastImage() throws SQLException {
        mImageDao = getEnglishImageDao();
        QueryBuilder queryBuilder = mImageDao.queryBuilder();
        queryBuilder.orderBy(NAME,false);
        //queryBuilder.setWhere(queryBuilder.orderBy().where());
        return mImageDao.queryForFirst(queryBuilder.prepare());
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(LOG_TAG, "Creating database.");
            TableUtils.createTable(connectionSource, EnglishImagesProperties.class);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Failed to create database.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
