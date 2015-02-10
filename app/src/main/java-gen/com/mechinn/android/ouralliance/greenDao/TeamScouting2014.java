package com.mechinn.android.ouralliance.greenDao;

import java.util.List;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.mechinn.android.ouralliance.greenDao.dao.MatchScouting2014Dao;
import com.mechinn.android.ouralliance.greenDao.dao.MultimediaDao;
import com.mechinn.android.ouralliance.greenDao.dao.TeamDao;
import com.mechinn.android.ouralliance.greenDao.dao.TeamScouting2014Dao;
import com.mechinn.android.ouralliance.greenDao.dao.WheelDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table TEAM_SCOUTING2014.
 */
public class TeamScouting2014 extends com.mechinn.android.ouralliance.OurAllianceObject  implements Comparable<TeamScouting2014> {

    private Long id;
    /** Not-null value. */
    private java.util.Date modified;
    private long teamId;
    private String notes;
    private String orientation;
    private String driveTrain;
    private Double width;
    private Double length;
    private Double heightShooter;
    private Double heightMax;
    private Integer shooterType;
    private Boolean lowGoal;
    private Boolean highGoal;
    private Double shootingDistance;
    private Boolean passGround;
    private Boolean passAir;
    private Boolean passTruss;
    private Boolean pickupGround;
    private Boolean pickupCatch;
    private Boolean pusher;
    private Boolean blocker;
    private Double humanPlayer;
    private Boolean noAuto;
    private Boolean driveAuto;
    private Boolean lowAuto;
    private Boolean highAuto;
    private Boolean hotAuto;
    private Long multimediaId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TeamScouting2014Dao myDao;

    private Team team;
    private Long team__resolvedKey;

    private List<Wheel> wheels;
    private List<MatchScouting2014> matches;
    private List<Multimedia> multimedia;

    // KEEP FIELDS - put your custom fields here
    public final static String TAG = "TeamScouting2014";
    // KEEP FIELDS END

    public TeamScouting2014() {
    }

    public TeamScouting2014(Long id) {
        this.id = id;
    }

    public TeamScouting2014(Long id, java.util.Date modified, long teamId, String notes, String orientation, String driveTrain, Double width, Double length, Double heightShooter, Double heightMax, Integer shooterType, Boolean lowGoal, Boolean highGoal, Double shootingDistance, Boolean passGround, Boolean passAir, Boolean passTruss, Boolean pickupGround, Boolean pickupCatch, Boolean pusher, Boolean blocker, Double humanPlayer, Boolean noAuto, Boolean driveAuto, Boolean lowAuto, Boolean highAuto, Boolean hotAuto, Long multimediaId) {
        this.id = id;
        this.modified = modified;
        this.teamId = teamId;
        this.notes = notes;
        this.orientation = orientation;
        this.driveTrain = driveTrain;
        this.width = width;
        this.length = length;
        this.heightShooter = heightShooter;
        this.heightMax = heightMax;
        this.shooterType = shooterType;
        this.lowGoal = lowGoal;
        this.highGoal = highGoal;
        this.shootingDistance = shootingDistance;
        this.passGround = passGround;
        this.passAir = passAir;
        this.passTruss = passTruss;
        this.pickupGround = pickupGround;
        this.pickupCatch = pickupCatch;
        this.pusher = pusher;
        this.blocker = blocker;
        this.humanPlayer = humanPlayer;
        this.noAuto = noAuto;
        this.driveAuto = driveAuto;
        this.lowAuto = lowAuto;
        this.highAuto = highAuto;
        this.hotAuto = hotAuto;
        this.multimediaId = multimediaId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTeamScouting2014Dao() : null;
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

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getDriveTrain() {
        return driveTrain;
    }

    public void setDriveTrain(String driveTrain) {
        this.driveTrain = driveTrain;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getHeightShooter() {
        return heightShooter;
    }

    public void setHeightShooter(Double heightShooter) {
        this.heightShooter = heightShooter;
    }

    public Double getHeightMax() {
        return heightMax;
    }

    public void setHeightMax(Double heightMax) {
        this.heightMax = heightMax;
    }

    public Integer getShooterType() {
        return shooterType;
    }

    public void setShooterType(Integer shooterType) {
        this.shooterType = shooterType;
    }

    public Boolean getLowGoal() {
        return lowGoal;
    }

    public void setLowGoal(Boolean lowGoal) {
        this.lowGoal = lowGoal;
    }

    public Boolean getHighGoal() {
        return highGoal;
    }

    public void setHighGoal(Boolean highGoal) {
        this.highGoal = highGoal;
    }

    public Double getShootingDistance() {
        return shootingDistance;
    }

    public void setShootingDistance(Double shootingDistance) {
        this.shootingDistance = shootingDistance;
    }

    public Boolean getPassGround() {
        return passGround;
    }

    public void setPassGround(Boolean passGround) {
        this.passGround = passGround;
    }

    public Boolean getPassAir() {
        return passAir;
    }

    public void setPassAir(Boolean passAir) {
        this.passAir = passAir;
    }

    public Boolean getPassTruss() {
        return passTruss;
    }

    public void setPassTruss(Boolean passTruss) {
        this.passTruss = passTruss;
    }

    public Boolean getPickupGround() {
        return pickupGround;
    }

    public void setPickupGround(Boolean pickupGround) {
        this.pickupGround = pickupGround;
    }

    public Boolean getPickupCatch() {
        return pickupCatch;
    }

    public void setPickupCatch(Boolean pickupCatch) {
        this.pickupCatch = pickupCatch;
    }

    public Boolean getPusher() {
        return pusher;
    }

    public void setPusher(Boolean pusher) {
        this.pusher = pusher;
    }

    public Boolean getBlocker() {
        return blocker;
    }

    public void setBlocker(Boolean blocker) {
        this.blocker = blocker;
    }

    public Double getHumanPlayer() {
        return humanPlayer;
    }

    public void setHumanPlayer(Double humanPlayer) {
        this.humanPlayer = humanPlayer;
    }

    public Boolean getNoAuto() {
        return noAuto;
    }

    public void setNoAuto(Boolean noAuto) {
        this.noAuto = noAuto;
    }

    public Boolean getDriveAuto() {
        return driveAuto;
    }

    public void setDriveAuto(Boolean driveAuto) {
        this.driveAuto = driveAuto;
    }

    public Boolean getLowAuto() {
        return lowAuto;
    }

    public void setLowAuto(Boolean lowAuto) {
        this.lowAuto = lowAuto;
    }

    public Boolean getHighAuto() {
        return highAuto;
    }

    public void setHighAuto(Boolean highAuto) {
        this.highAuto = highAuto;
    }

    public Boolean getHotAuto() {
        return hotAuto;
    }

    public void setHotAuto(Boolean hotAuto) {
        this.hotAuto = hotAuto;
    }

    public Long getMultimediaId() {
        return multimediaId;
    }

    public void setMultimediaId(Long multimediaId) {
        this.multimediaId = multimediaId;
    }

    /** To-one relationship, resolved on first access. */
    public Team getTeam() {
        long __key = this.teamId;
        if (team__resolvedKey == null || !team__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TeamDao targetDao = daoSession.getTeamDao();
            Team teamNew = targetDao.load(__key);
            synchronized (this) {
                team = teamNew;
            	team__resolvedKey = __key;
            }
        }
        return team;
    }

    public void setTeam(Team team) {
        if (team == null) {
            throw new DaoException("To-one property 'teamId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.team = team;
            teamId = team.getId();
            team__resolvedKey = teamId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Wheel> getWheels() {
        if (wheels == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WheelDao targetDao = daoSession.getWheelDao();
            List<Wheel> wheelsNew = targetDao._queryTeamScouting2014_Wheels(id);
            synchronized (this) {
                if(wheels == null) {
                    wheels = wheelsNew;
                }
            }
        }
        return wheels;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetWheels() {
        wheels = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<MatchScouting2014> getMatches() {
        if (matches == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MatchScouting2014Dao targetDao = daoSession.getMatchScouting2014Dao();
            List<MatchScouting2014> matchesNew = targetDao._queryTeamScouting2014_Matches(id);
            synchronized (this) {
                if(matches == null) {
                    matches = matchesNew;
                }
            }
        }
        return matches;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetMatches() {
        matches = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Multimedia> getMultimedia() {
        if (multimedia == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MultimediaDao targetDao = daoSession.getMultimediaDao();
            List<Multimedia> multimediaNew = targetDao._queryTeamScouting2014_Multimedia(id);
            synchronized (this) {
                if(multimedia == null) {
                    multimedia = multimediaNew;
                }
            }
        }
        return multimedia;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetMultimedia() {
        multimedia = null;
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
        return	"ID: "+this.getId()+
                " Mod: "+this.getModified()+
                " Notes: "+this.getNotes()+
                " Orientation: "+this.getOrientation()+
                " Drive Train: "+this.getDriveTrain()+
                " Width: "+this.getWidth()+
                " Length: "+this.getLength()+
                " Height Shooter: "+this.getHeightShooter()+
                " Height Max: "+this.getHeightMax()+
                " Shooter Type: "+this.getShooterType()+
                " Low Goal: "+this.getLowGoal()+
                " High Goal: "+this.getHighGoal()+
                " Shooting Distance: "+this.getShootingDistance()+
                " Pass Ground: "+this.getPassGround()+
                " Pass Air: "+this.getPassAir()+
                " Pass Truss: "+this.getPassTruss()+
                " Pickup Ground: "+this.getPickupGround()+
                " Pickup Catch: "+this.getPickupCatch()+
                " Pusher: "+this.getPusher()+
                " Blocker: "+this.getBlocker()+
                " Human Player: "+this.getHumanPlayer()+
                " No Autonomous Mode: "+this.getNoAuto()+
                " Drive Autonomous Mode: "+this.getDriveAuto()+
                " Low Autonomous Mode: "+this.getLowAuto()+
                " High Autonomous Mode: "+this.getHighAuto()+
                " Hot Autonomous Mode: "+this.getHotAuto();
    }
    public int compareTo(TeamScouting2014 another) {
        return this.getTeam().compareTo(another.getTeam());
    }
    public boolean equals(Object data) {
        if(!(data instanceof TeamScouting2014)) {
            return false;
        }
        return  getTeam().equals(((TeamScouting2014)data).getTeam()) &&
                getNotes().equals(((TeamScouting2014)data).getNotes()) &&
                getOrientation().equals(((TeamScouting2014)data).getOrientation()) &&
                getDriveTrain().equals(((TeamScouting2014)data).getDriveTrain()) &&
                getWidth()==((TeamScouting2014)data).getWidth() &&
                getLength()==((TeamScouting2014)data).getLength() &&
                getHeightShooter()==((TeamScouting2014)data).getHeightShooter() &&
                getHeightMax()==((TeamScouting2014)data).getHeightMax() &&
                getShooterType()==((TeamScouting2014)data).getShooterType() &&
                getLowGoal()==((TeamScouting2014)data).getLowGoal() &&
                getHighGoal()==((TeamScouting2014)data).getHighGoal() &&
                getShootingDistance()==((TeamScouting2014)data).getShootingDistance() &&
                getPassGround()==((TeamScouting2014)data).getPassGround() &&
                getPassAir()==((TeamScouting2014)data).getPassAir() &&
                getPassTruss()==((TeamScouting2014)data).getPassTruss() &&
                getPickupGround()==((TeamScouting2014)data).getPickupGround() &&
                getPickupCatch()==((TeamScouting2014)data).getPickupCatch() &&
                getPusher()==((TeamScouting2014)data).getPusher() &&
                getBlocker()==((TeamScouting2014)data).getBlocker() &&
                getHumanPlayer()==((TeamScouting2014)data).getHumanPlayer() &&
                getNoAuto()==((TeamScouting2014)data).getNoAuto() &&
                getDriveAuto()==((TeamScouting2014)data).getDriveAuto() &&
                getLowAuto()==((TeamScouting2014)data).getLowAuto() &&
                getHighAuto()==((TeamScouting2014)data).getHighAuto() &&
                getHotAuto()==((TeamScouting2014)data).getHotAuto();
    }
    // KEEP METHODS END

}
