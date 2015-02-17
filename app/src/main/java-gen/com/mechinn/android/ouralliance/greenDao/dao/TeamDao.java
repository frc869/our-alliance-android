package com.mechinn.android.ouralliance.greenDao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.mechinn.android.ouralliance.greenDao.Team;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TEAM.
*/
public class TeamDao extends AbstractDao<Team, Long> {

    public static final String TABLENAME = "TEAM";

    /**
     * Properties of entity Team.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Modified = new Property(1, java.util.Date.class, "modified", false, "MODIFIED");
        public final static Property TeamNumber = new Property(2, int.class, "teamNumber", false, "TEAM_NUMBER");
        public final static Property Nickname = new Property(3, String.class, "nickname", false, "NICKNAME");
        public final static Property Website = new Property(4, String.class, "website", false, "WEBSITE");
        public final static Property Locality = new Property(5, String.class, "locality", false, "LOCALITY");
        public final static Property Region = new Property(6, String.class, "region", false, "REGION");
        public final static Property Country = new Property(7, String.class, "country", false, "COUNTRY");
        public final static Property Key = new Property(8, String.class, "key", false, "KEY");
        public final static Property RookieYear = new Property(9, Integer.class, "rookieYear", false, "ROOKIE_YEAR");
    };

    private DaoSession daoSession;


    public TeamDao(DaoConfig config) {
        super(config);
    }
    
    public TeamDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TEAM' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'MODIFIED' INTEGER NOT NULL ," + // 1: modified
                "'TEAM_NUMBER' INTEGER NOT NULL UNIQUE ," + // 2: teamNumber
                "'NICKNAME' TEXT," + // 3: nickname
                "'WEBSITE' TEXT," + // 4: website
                "'LOCALITY' TEXT," + // 5: locality
                "'REGION' TEXT," + // 6: region
                "'COUNTRY' TEXT," + // 7: country
                "'KEY' TEXT," + // 8: key
                "'ROOKIE_YEAR' INTEGER);"); // 9: rookieYear
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TEAM'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Team entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getModified().getTime());
        stmt.bindLong(3, entity.getTeamNumber());
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(4, nickname);
        }
 
        String website = entity.getWebsite();
        if (website != null) {
            stmt.bindString(5, website);
        }
 
        String locality = entity.getLocality();
        if (locality != null) {
            stmt.bindString(6, locality);
        }
 
        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(7, region);
        }
 
        String country = entity.getCountry();
        if (country != null) {
            stmt.bindString(8, country);
        }
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(9, key);
        }
 
        Integer rookieYear = entity.getRookieYear();
        if (rookieYear != null) {
            stmt.bindLong(10, rookieYear);
        }
    }

    @Override
    protected void attachEntity(Team entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Team readEntity(Cursor cursor, int offset) {
        Team entity = new Team( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            new java.util.Date(cursor.getLong(offset + 1)), // modified
            cursor.getInt(offset + 2), // teamNumber
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // nickname
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // website
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // locality
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // region
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // country
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // key
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9) // rookieYear
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Team entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setModified(new java.util.Date(cursor.getLong(offset + 1)));
        entity.setTeamNumber(cursor.getInt(offset + 2));
        entity.setNickname(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setWebsite(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLocality(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRegion(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCountry(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setKey(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRookieYear(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Team entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Team entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}