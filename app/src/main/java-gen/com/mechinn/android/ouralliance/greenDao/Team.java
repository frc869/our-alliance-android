package com.mechinn.android.ouralliance.greenDao;

import java.util.List;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.mechinn.android.ouralliance.greenDao.dao.EventTeamDao;
import com.mechinn.android.ouralliance.greenDao.dao.TeamDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table TEAM.
 */
public class Team extends com.mechinn.android.ouralliance.data.OurAllianceObject  implements Comparable<Team>, java.io.Serializable {

    private Long id;
    /** Not-null value. */
    private java.util.Date modified;
    private int teamNumber;
    private String nickname;
    private String website;
    private String locality;
    private String region;
    private String country;
    private String key;
    private Integer rookieYear;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TeamDao myDao;

    private List<EventTeam> events;

    // KEEP FIELDS - put your custom fields here
    public final static String TAG = "Team";
    // KEEP FIELDS END

    public Team() {
    }

    public Team(Long id) {
        this.id = id;
    }

    public Team(Long id, java.util.Date modified, int teamNumber, String nickname, String website, String locality, String region, String country, String key, Integer rookieYear) {
        this.id = id;
        this.modified = modified;
        this.teamNumber = teamNumber;
        this.nickname = nickname;
        this.website = website;
        this.locality = locality;
        this.region = region;
        this.country = country;
        this.key = key;
        this.rookieYear = rookieYear;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTeamDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public java.util.Date getModified() {
        return modified;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setModified(java.util.Date modified) {
        this.modified = modified;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getRookieYear() {
        return rookieYear;
    }

    public void setRookieYear(Integer rookieYear) {
        this.rookieYear = rookieYear;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<EventTeam> getEvents() {
        if (events == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EventTeamDao targetDao = daoSession.getEventTeamDao();
            List<EventTeam> eventsNew = targetDao._queryTeam_Events(id);
            synchronized (this) {
                if(events == null) {
                    events = eventsNew;
                }
            }
        }
        return events;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetEvents() {
        events = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    public String toString() {
        return this.getTeamNumber()+": "+this.getNickname();
    }
    public int compareTo(Team another) {
        return this.getTeamNumber() - another.getTeamNumber();
    }
    public boolean equals(Object data) {
        if(!(data instanceof Team)) {
            return false;
        }
        return  getTeamNumber()==((Team) data).getTeamNumber()
                && getNickname().equals(((Team)data).getNickname());
    }
    // KEEP METHODS END

}