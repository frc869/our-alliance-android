package com.mechinn.android.ouralliance.data.source;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.error.MoreThanOneObjectThrowable;
import com.mechinn.android.ouralliance.error.NoObjectsThrowable;
import com.mechinn.android.ouralliance.error.OurAllianceException;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;

public class TeamDataSource extends AOurAllianceDataSource<Team> {
	private static final String TAG = "TeamDataSource";

	public TeamDataSource(Context context) {
		super(context);
	}

	@Override
	public Team insert(Team competitionTeam) throws OurAllianceException, SQLException {
		return insert(Team.URI, competitionTeam);
	}

	@Override
	public int update(Team data, String selection) throws OurAllianceException, SQLException {
		return update(Team.URI, data, selection);
	}

	@Override
	public int delete(String selection) throws OurAllianceException {
		return delete(Team.URI, selection);
	}

	@Override
	public CursorLoader query(String selection, String order) {
		return query(Team.URI,Team.ALLCOLUMNS, selection, order);
	}

	@Override
	public CursorLoader getAll() {
		return getAll(Team.NUMBER);
	}
	
	public CursorLoader get(int num) {
		return get(Team.NUMBER, num);
	}
	
	public static Team getSingle(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			if(cursor.getCount()==1) {
				cursor.moveToFirst();
				return Team.newFromCursor(cursor);
			} else if(cursor.getCount()<1) {
				throw new OurAllianceException(TAG,"CompetitionTeam not found in db.",new NoObjectsThrowable());
			} else {
				throw new OurAllianceException(TAG,"More than 1 result please contact developer.", new MoreThanOneObjectThrowable());
			}
		}
		throw new SQLException("Cursor is null");
	}
	
	public static List<Team> getList(Cursor cursor) throws OurAllianceException, SQLException {
		if(null!=cursor) {
			List<Team> teams = new ArrayList<Team>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Team team = Team.newFromCursor(cursor);
				Log.d(TAG, "get "+team);
				teams.add(team);
				cursor.moveToNext();
			}
			if(teams.isEmpty()) {
				throw new OurAllianceException(TAG,"No competitionTeams in db.",new NoObjectsThrowable());
			}
			return teams;
		}
		throw new SQLException("Cursor is null");
	}
}