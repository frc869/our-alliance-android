package com.mechinn.android.ouralliance.view;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.List;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on devdeloping a Settings UI.
 */
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, LoaderCallbacks<Cursor> {
	private static final String TAG = "SettingsFragment";
	private Prefs prefs;
	private SeasonDataSource seasonData;
	private CompetitionDataSource competitionData;
	private String seasonPrefString;
	private String compPrefString;
	private String resetDBPrefString;
	private ListPreference season;
	private ListPreference comp;
	private Preference resetDB;
	private Preference changelog;
	private Preference about;
	private Setup setup;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        prefs = new Prefs(this.getActivity());
		setup = new Setup(this.getActivity());
        //builds list from DB
        seasonData = new SeasonDataSource(this.getActivity());
        competitionData = new CompetitionDataSource(this.getActivity());
        seasonPrefString = this.getString(R.string.pref_season);
        compPrefString = this.getString(R.string.pref_comp);
        resetDBPrefString = this.getString(R.string.pref_resetDB);
        season = (ListPreference) getPreferenceScreen().findPreference(seasonPrefString);
        comp = (ListPreference) getPreferenceScreen().findPreference(compPrefString);
        resetDB = (Preference) getPreferenceScreen().findPreference(compPrefString);
        resetDB.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					//setup.reset();
	            	SettingsFragment.this.getLoaderManager().restartLoader(SettingsActivity.LOADER_SEASON, null, SettingsFragment.this);
	            	SettingsFragment.this.getLoaderManager().restartLoader(SettingsActivity.LOADER_COMPETITION, null, SettingsFragment.this);
					return true;
				}
			});
        changelog = (Preference) getPreferenceScreen().findPreference(this.getString(R.string.pref_changeLog));
        changelog.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					// dialog code here
					return true;
				}
			});
        about = (Preference) getPreferenceScreen().findPreference(this.getString(R.string.pref_about));
        about.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					// dialog code here
					return true;
				}
			});
    }
	
	@Override
	public void onResume() {
	    super.onResume();
	    // Set up a listener whenever a key changes
	    prefs.setChangeListener(this);
        this.getLoaderManager().initLoader(SettingsActivity.LOADER_SEASON, null, this);
    	this.getLoaderManager().restartLoader(SettingsActivity.LOADER_COMPETITION, null, this);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    // Unregister the listener whenever a key changes
	    prefs.unsetChangeListener(this);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		long seasonID = Long.parseLong(season.getValue());
    	Log.d(TAG,"season: "+seasonID);
		long compID = Long.parseLong(comp.getValue());
     	Log.d(TAG,"comp: "+compID);
		switch(id) {
			case SettingsActivity.LOADER_SEASON:
				return seasonData.getAll();
			case SettingsActivity.LOADER_COMPETITION:
				return competitionData.getAllCompetitions(seasonID);
			case SettingsActivity.LOADER_SEASON_SUMMARY:
				return seasonData.get(seasonID);
			case SettingsActivity.LOADER_COMPETITION_SUMMARY:
				return competitionData.get(compID);
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		switch(loader.getId()) {
			case SettingsActivity.LOADER_SEASON:
				setSeasonList(cursor);
				break;
			case SettingsActivity.LOADER_COMPETITION:
				setCompList(cursor);
				break;
			case SettingsActivity.LOADER_SEASON_SUMMARY:
				setSeasonSummary(cursor);
				break;
			case SettingsActivity.LOADER_COMPETITION_SUMMARY:
				setCompSummary(cursor);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
			case SettingsActivity.LOADER_SEASON:
				setSeasonList(null);
				break;
			case SettingsActivity.LOADER_COMPETITION:
				setSeasonList(null);
				break;
			case SettingsActivity.LOADER_SEASON_SUMMARY:
				setSeasonSummary(null);
				break;
			case SettingsActivity.LOADER_COMPETITION_SUMMARY:
				setCompSummary(null);
				break;
		}
	}
	
	private void setSeasonList(Cursor cursor) {
		try {
			List<Season> seasons = SeasonDataSource.getList(cursor);
			CharSequence[] seasonsViews = new CharSequence[seasons.size()];
			CharSequence[] seasonsIds = new CharSequence[seasons.size()];
			for(int i=0;i<seasons.size();++i) {
				seasonsViews[i] = seasons.get(i).toString();
				seasonsIds[i] = Long.toString(seasons.get(i).getId());
			}
	        season.setEntries(seasonsViews);
	        season.setEntryValues(seasonsIds);
        	this.getLoaderManager().restartLoader(SettingsActivity.LOADER_SEASON_SUMMARY, null, this);
		} catch (Exception e) {
			e.printStackTrace();
	        season.setEntries(new CharSequence[]{"Loading"});
	        season.setEntryValues(new CharSequence[]{"0"});
			season.setSummary(getActivity().getString(R.string.pref_season_summary));
			comp.setEnabled(false);
		}
	}
	
	private void setCompList(Cursor cursor) {
		try {  
			List<Competition> comps = CompetitionDataSource.getList(cursor);
			CharSequence[] compsViews = new CharSequence[comps.size()];
			CharSequence[] compsIds = new CharSequence[comps.size()];
			for(int i=0;i<comps.size();++i) {
				compsViews[i] = comps.get(i).toString();
				compsIds[i] = Long.toString(comps.get(i).getId());
			}
	        comp.setEntries(compsViews);
	        comp.setEntryValues(compsIds);
        	this.getLoaderManager().restartLoader(SettingsActivity.LOADER_COMPETITION_SUMMARY, null, this);
		} catch (Exception e) {
			e.printStackTrace();
			comp.setEntries(new CharSequence[]{"Loading"});
	        comp.setEntryValues(new CharSequence[]{"0"});
	        comp.setSummary(getActivity().getString(R.string.pref_comp_summary));
		}
	}
	
	private void setSeasonSummary(Cursor cursor) {
		try {
			Season thisSeason = SeasonDataSource.getSingle(cursor);
        	season.setSummary(thisSeason.toString());
        	comp.setEnabled(true);
        } catch (Exception e) {
        	e.printStackTrace();
			season.setSummary(getActivity().getString(R.string.pref_season_summary));
			comp.setEnabled(false);
        }
	}
	
	private void setCompSummary(Cursor cursor) {
		try {
        	Competition thisComp = CompetitionDataSource.getSingle(cursor);
        	comp.setSummary(thisComp.toString());
        } catch (Exception e) {
        	e.printStackTrace();
	        comp.setSummary(getActivity().getString(R.string.pref_comp_summary));
        }
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, key);
		if(key.equals(seasonPrefString)) {
        	this.getLoaderManager().restartLoader(SettingsActivity.LOADER_SEASON_SUMMARY, null, this);
        	this.getLoaderManager().restartLoader(SettingsActivity.LOADER_COMPETITION, null, this);
		} else if(key.equals(compPrefString)) {
        	this.getLoaderManager().restartLoader(SettingsActivity.LOADER_COMPETITION_SUMMARY, null, this);
		} else if(key.equals(resetDBPrefString)) {
			
		}
	}
}
