package com.mechinn.android.ouralliance;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.mechinn.android.ouralliance.data.MatchListInterface;
import com.mechinn.android.ouralliance.data.MatchScoutingInterface;
import com.mechinn.android.ouralliance.data.TeamRankingsInterface;
import com.mechinn.android.ouralliance.data.TeamScoutingInterface;
import com.mechinn.android.ouralliance.providers.MatchListProvider;
import com.mechinn.android.ouralliance.providers.MatchScoutingProvider;
import com.mechinn.android.ouralliance.providers.TeamScoutingProvider;

public class OurAllianceCSVWriter {
	private final String TAG = "csvtool";
	private Activity activity;
	private SimpleDateFormat timeFormatter;
	private File dir;
	private File matchListFile;
	private File matchScoutingFile;
	private File teamRankingsFile;
	private File teamScoutingFile;
	
	private MatchListInterface matchList;
	private MatchScoutingInterface matchScouting;
	private TeamRankingsInterface teamRankings;
	private TeamScoutingInterface teamScouting;
	
	private List<String[]> matchListStrings;
	private List<String[]> matchScoutingStrings;
	private List<String[]> teamRankingsStrings;
	private List<String[]> teamScoutingStrings;
	
	public OurAllianceCSVWriter(Activity act) {
		activity = act;
        timeFormatter = new SimpleDateFormat("hh:mma");
        timeFormatter.setTimeZone(TimeZone.getDefault());
        String packageName = activity.getPackageName();
        File externalPath = Environment.getExternalStorageDirectory();
        File appFiles = new File(externalPath.getAbsolutePath() +  "/Android/data/" + packageName + "/files");
		dir = new File(appFiles.getAbsolutePath()+"/csvExport/");
		if(!dir.exists()) {
			dir.mkdirs();
        }
		matchList = new MatchListInterface(activity);
		matchScouting = new MatchScoutingInterface(activity);
		teamRankings = new TeamRankingsInterface(activity);
		teamScouting = new TeamScoutingInterface(activity);
		matchListFile = new File(dir.getAbsolutePath(),"matchList-"+timeFormatter.format(new Date())+".csv");
		Log.i(TAG,matchListFile.toString());
		matchScoutingFile = new File(dir.getAbsolutePath(),"matchScouting-"+timeFormatter.format(new Date())+".csv");
		Log.i(TAG,matchScoutingFile.toString());
		teamRankingsFile = new File(dir.getAbsolutePath(),"teamRankings-"+timeFormatter.format(new Date())+".csv");
		Log.i(TAG,teamRankingsFile.toString());
		teamScoutingFile = new File(dir.getAbsolutePath(),"teamScouting-"+timeFormatter.format(new Date())+".csv");
		Log.i(TAG,teamScoutingFile.toString());
		matchListStrings = new ArrayList<String[]>();
		matchScoutingStrings = new ArrayList<String[]>();
		teamRankingsStrings = new ArrayList<String[]>();
		teamScoutingStrings = new ArrayList<String[]>();
		int i;
		String[] holder;
		Log.i(TAG,"Finished setting up");
		Cursor cursor = matchList.fetchAllMatches();
		if(cursor!=null && !cursor.isClosed() && cursor.getCount()>0){
			Log.i(TAG,"Exporting match list");
			holder = new String[cursor.getColumnCount()];
			for(i = 0; i<cursor.getColumnCount();++i) {
				holder[i] = cursor.getColumnName(i);
			}
			matchListStrings.add(holder);
			do {
        		holder = new String[cursor.getColumnCount()];
	        	for(i=0;i<cursor.getColumnCount();++i) {
	            	String colName = cursor.getColumnName(i);
	            	if(colName.equals(MatchListProvider.KEY_TIME)) {
	            		holder[i] = timeFormatter.format(new Date(cursor.getLong(i)));
	            	} else if(colName.equals(MatchListProvider.KEY_MATCH_NUM) || colName.equals(MatchListProvider.KEY_RED1) || 
	            			colName.equals(MatchListProvider.KEY_RED2) || colName.equals(MatchListProvider.KEY_RED3) ||
	            			colName.equals(MatchListProvider.KEY_BLUE1) || colName.equals(MatchListProvider.KEY_BLUE2) || 
	            			colName.equals(MatchListProvider.KEY_BLUE3) || colName.equals(DatabaseConnection._LASTMOD) ||
	            			colName.equals(DatabaseConnection._ID)){
	            		holder[i] = Integer.toString(cursor.getInt(i));
	            	}
	            }
	        	matchListStrings.add(holder);
			} while(cursor.moveToNext());
		}
		cursor = matchScouting.fetchAllMatches();
		if(cursor!=null && !cursor.isClosed() && cursor.getCount()>0){
			Log.i(TAG,"Exporting match scouting");
			holder = new String[cursor.getColumnCount()];
			for(i = 0; i<cursor.getColumnCount();++i) {
				holder[i] = cursor.getColumnName(i);
			}
			matchScoutingStrings.add(holder);
			do {
        		holder = new String[cursor.getColumnCount()];
	        	for(i=0;i<cursor.getColumnCount();++i) {
	            	String colName = cursor.getColumnName(i);
	            	if(colName.equals(MatchScoutingProvider.KEY_TOP) || colName.equals(MatchScoutingProvider.KEY_MID) || 
	            			colName.equals(MatchScoutingProvider.KEY_BOT) || colName.equals(DatabaseConnection._LASTMOD) ||
	            			colName.equals(DatabaseConnection._ID)){
	            		holder[i] = Integer.toString(cursor.getInt(i));
	            	} else if(colName.equals(MatchScoutingProvider.KEY_SLOT) || colName.equals(MatchScoutingProvider.KEY_NOTES)) {
	            		holder[i] = '"'+cursor.getString(i)+'"';
	            	} else if(colName.equals(MatchScoutingProvider.KEY_BROKE) || colName.equals(MatchScoutingProvider.KEY_AUTO_BRIDGE) ||
	            			colName.equals(MatchScoutingProvider.KEY_AUTO_SHOOTER) || colName.equals(MatchScoutingProvider.KEY_BALANCE)) {
	            		holder[i] = Boolean.toString(cursor.getInt(i)==0?false:true);
	            	} else if(colName.equals(MatchScoutingProvider.KEY_SHOOTER)) {
	            		switch(cursor.getInt(i)) {
	            			default:
	            				holder[i] = "No Shooting";
	            				break;
	            			case 1:
	            				holder[i] = "Dunker";
	        					break;
	        				case 2:
	        					holder[i] = "Shooter";
	        					break;
	            		}
	            	} else {
	            		for(String eachCompetition : TeamScoutingProvider.COMPETITIONS) {
	            			if(colName.equals(eachCompetition)) {
	            				switch(cursor.getInt(i)) {
			            			case 0:holder[i] = "no";break;
			            			default:holder[i] = "yes";
			            		}
            	        	}
	                    }
	            	}
	            }
	        	matchScoutingStrings.add(holder);
			} while(cursor.moveToNext());
		}
		cursor = teamScouting.fetchAllTeams();
		if(cursor!=null && !cursor.isClosed() && cursor.getCount()>0){
			Log.i(TAG,"Exporting team scouting");
			holder = new String[cursor.getColumnCount()];
			for(i = 0; i<cursor.getColumnCount();++i) {
				holder[i] = cursor.getColumnName(i);
			}
			teamScoutingStrings.add(holder);
			do {
        		holder = new String[cursor.getColumnCount()];
	        	for(i=0;i<cursor.getColumnCount();++i) {
	            	String colName = cursor.getColumnName(i);
            		if(colName.equals(TeamScoutingProvider.KEY_ORIENTATION) || colName.equals(TeamScoutingProvider.KEY_WHEEL1_TYPE) || 
            				colName.equals(TeamScoutingProvider.KEY_WHEEL2_TYPE) || colName.equals(TeamScoutingProvider.KEY_DEAD_WHEEL_TYPE) || 
	            			colName.equals(TeamScoutingProvider.KEY_NOTES)){
	            		holder[i] = '"'+cursor.getString(i)+'"';
	            	} else if (colName.equals(TeamScoutingProvider.KEY_NUM_WHEELS)|| colName.equals(TeamScoutingProvider.KEY_TEAM) || colName.equals(DatabaseConnection._LASTMOD) ||
	            			colName.equals(DatabaseConnection._ID) || colName.equals(TeamScoutingProvider.KEY_RANK) || colName.equals(TeamScoutingProvider.KEY_WHEEL_TYPES)) {
	            		holder[i] = Integer.toString(cursor.getInt(i));
	            	} else if (colName.equals(TeamScoutingProvider.KEY_TRACKING) || colName.equals(TeamScoutingProvider.KEY_FENDER_SHOOTER) || 
	            			colName.equals(TeamScoutingProvider.KEY_KEY_SHOOTER) || colName.equals(TeamScoutingProvider.KEY_BARRIER) || 
	            			colName.equals(TeamScoutingProvider.KEY_CLIMB) || colName.equals(TeamScoutingProvider.KEY_AUTO_BRIDGE) ||
	            			colName.equals(TeamScoutingProvider.KEY_AUTO_SHOOTER) || colName.equals(TeamScoutingProvider.KEY_DEAD_WHEEL)) {
	            		switch(cursor.getInt(i)) {
	            			case 0:holder[i] = "no";break;
	            			default:holder[i] = "yes";
	            		}
	            	} else if (colName.equals(TeamScoutingProvider.KEY_AVG_HOOPS) || colName.equals(TeamScoutingProvider.KEY_AVG_BALANCE) || 
	            			colName.equals(TeamScoutingProvider.KEY_AVG_BROKE) || colName.equals(TeamScoutingProvider.KEY_WHEEL1_DIAMETER) || 
	            			colName.equals(TeamScoutingProvider.KEY_WHEEL2_DIAMETER) || colName.equals(TeamScoutingProvider.KEY_AVG_AUTO) ||
	            			colName.equals(TeamScoutingProvider.KEY_WIDTH) || colName.equals(TeamScoutingProvider.KEY_HEIGHT)) {
	            		holder[i] = Double.toString(cursor.getDouble(i));
	            	} else if (colName.equals(TeamScoutingProvider.KEY_SHOOTING_RATING) || colName.equals(TeamScoutingProvider.KEY_BALANCING_RATING)) {
	            		holder[i] = Float.toString(cursor.getFloat(i));
	            	} else {
	            		for(String eachCompetition : TeamScoutingProvider.COMPETITIONS) {
	            			if(colName.equals(eachCompetition)) {
	            				switch(cursor.getInt(i)) {
			            			case 0:holder[i] = "no";break;
			            			default:holder[i] = "yes";
			            		}
            	        	}
	                    }
	            	}
	            }
	        	teamScoutingStrings.add(holder);
			} while(cursor.moveToNext());
		}
	}
	
	public String writeMatchList() throws IOException {
		CSVWriter writer = new CSVWriter(matchListFile, ",");
		writer.writeAll(matchListStrings);
		writer.close();
		return matchListFile.toString();
	}
	
	public String writeMatchScouting() throws IOException {
		CSVWriter writer = new CSVWriter(matchScoutingFile, ",");
		writer.writeAll(matchScoutingStrings);
		writer.close();
		return matchScoutingFile.toString();
	}
	
	public String writeTeamRankings() {
//		try {
//			CSVWriter writer = new CSVWriter(new FileWriter(teamRankingsFile), ',');
//			writer.writeAll(teamRankingsStrings);
//			writer.close();
//			Toast.makeText(activity, "Wrote match list to "+matchListFile, Toast.LENGTH_SHORT).show();
//			return teamRankingsFile.toString();
//		} catch (IOException e) {
//			Toast.makeText(activity, "Unable to write match list to CSV.", Toast.LENGTH_SHORT).show();
//		}
		return "";
	}
	
	public String writeTeamScouting() throws IOException {
		CSVWriter writer = new CSVWriter(teamScoutingFile, ",");
		writer.writeAll(teamScoutingStrings);
		writer.close();
		return teamScoutingFile.toString();
	}
}
