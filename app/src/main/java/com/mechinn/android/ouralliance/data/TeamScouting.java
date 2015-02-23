package com.mechinn.android.ouralliance.data;

import com.activeandroid.annotation.Column;

import java.util.List;

public abstract class TeamScouting extends OurAllianceObject implements Comparable<TeamScouting>, java.io.Serializable {
    public final static String TAG = "TeamScouting";
    public final static String TEAM = Team.TAG;
    public final static String NOTES = "notes";
    @Column(name=TEAM, onDelete = Column.ForeignKeyAction.CASCADE, notNull = true, onNullConflict = Column.ConflictAction.FAIL, unique = true, onUniqueConflict = Column.ConflictAction.FAIL)
    private Team team;
    @Column(name=NOTES)
    private String notes;
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public abstract List<? extends Wheel> getWheels();
    public abstract List<? extends MatchScouting> getMatches();
    public String toString() {
        return "ID: "+this.getId()+
                " Mod: "+this.getModified()+
                " Notes: "+this.getNotes();
    }
    public int compareTo(TeamScouting another) {
        return this.getTeam().compareTo(another.getTeam());
    }
    public boolean equals(Object data) {
        if (!(data instanceof TeamScouting)) {
            return false;
        }
        return getTeam().equals(((TeamScouting) data).getTeam()) &&
                getNotes().equals(((TeamScouting) data).getNotes());
    }
}
