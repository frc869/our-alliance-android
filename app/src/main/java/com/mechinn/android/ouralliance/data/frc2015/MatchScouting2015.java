package com.mechinn.android.ouralliance.data.frc2015;

import android.database.Cursor;
import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.TeamScouting;

import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

@Table(name = MatchScouting2015.TAG, id = MatchScouting2015.ID)
public class MatchScouting2015 extends MatchScouting {
    public final static String TAG = "MatchScouting2015";
    public final static String TEAM = TeamScouting2015.TAG;
    public final static String AUTO_STACKED = "autoStacked";
    public final static String AUTO_TOTES = "autoTotes";
    public final static String AUTO_CONTAINERS = "autoContainers";
    public final static String AUTO_LANDFILL = "autoLandfill";
    public final static String AUTO_MOVE = "autoMove";
    public final static String COOP = "coop";
    public final static String TOTES = "totes";
    public final static String CONTAINERS = "containers";
    public final static String LITTER = "litter";
    public final static String FOWLS = "fowls";
    @Column(name=TEAM, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, uniqueGroups = {MatchScouting.TAG}, onUniqueConflicts = {Column.ConflictAction.FAIL})
    private TeamScouting2015 teamScouting2015;
    @Column(name=TOTES)
    private Integer totes;
    @Column(name=CONTAINERS)
    private Integer containers;
    @Column(name=LITTER)
    private Integer litter;
    @Column(name=FOWLS)
    private Integer fowls;
    @Column(name=COOP)
    private Boolean coop;
    public MatchScouting2015() {}
    public MatchScouting2015(Cursor cursor) {
        this.loadFromCursor(cursor);
    }
    public TeamScouting2015 getTeamScouting2015() {
        return teamScouting2015;
    }
    public void setTeamScouting2015(TeamScouting2015 teamScouting2015) {
        this.teamScouting2015 = teamScouting2015;
    }
    public TeamScouting getTeamScouting() {
        return getTeamScouting2015();
    }
    public void setTeamScouting(TeamScouting teamScouting) {
        setTeamScouting2015((TeamScouting2015) teamScouting);
    }
    public Integer getTotes() {
        return totes;
    }
    public void setTotes(Integer totes) {
        this.totes = totes;
    }
    public Integer getContainers() {
        return containers;
    }
    public void setContainers(Integer containers) {
        this.containers = containers;
    }
    public Integer getLitter() {
        return litter;
    }
    public void setLitter(Integer litter) {
        this.litter = litter;
    }
    public Integer getFowls() {
        return fowls;
    }
    public void setFowls(Integer fowls) {
        this.fowls = fowls;
    }
    public Boolean getCoop() {
        return coop;
    }
    public void setCoop(Boolean coop) {
        this.coop = coop;
    }
    public boolean equals(Object data) {
        if (!(data instanceof MatchScouting2015)) {
            return false;
        }
        return super.equals(data) &&
                getTotes().equals(((MatchScouting2015)data).getTotes()) &&
                getContainers().equals(((MatchScouting2015)data).getContainers()) &&
                getLitter().equals(((MatchScouting2015)data).getLitter()) &&
                getFowls().equals(((MatchScouting2015)data).getFowls()) &&
                getCoop().equals(((MatchScouting2015)data).getCoop());
    }
    public void asyncSave() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                saveMod();
                EventBus.getDefault().post(MatchScouting2015.this);
            }
        });
    }
    public void asyncDelete() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                delete();
                EventBus.getDefault().post(MatchScouting2015.this);
            }
        });
    }
}