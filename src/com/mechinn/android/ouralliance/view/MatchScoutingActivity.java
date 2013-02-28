package com.mechinn.android.ouralliance.view;

import java.sql.SQLException;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2013.Match2013;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
import com.mechinn.android.ouralliance.data.source.frc2013.Match2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.view.frc2013.MatchDetail2013;
import com.mechinn.android.ouralliance.view.frc2013.MatchList2013;

import android.app.Activity;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MatchScoutingActivity extends Activity implements OnBackStackChangedListener, MatchListFragment.Listener, InsertMatchDialogFragment.Listener, DeleteMatchDialogFragment.Listener {
	public static final String TAG = MatchScoutingActivity.class.getSimpleName();
	private Prefs prefs;
	private MatchListFragment<?, ?> matchListFrag;
	private MatchDetailFragment<?, ?> matchDetailFragment;
	private int listFrag;
	private int detailFrag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_scouting);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getFragmentManager().addOnBackStackChangedListener(this);
		prefs = new Prefs(this);
		setListFrag();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.ouralliance, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		Intent intent;
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	if(this.getFragmentManager().getBackStackEntryCount()>0) {
	        		this.getFragmentManager().popBackStack();
	        	} else {
	        		this.finish();
	        	}
	        	return true;
	        case R.id.settings:
	        	intent = new Intent(this, SettingsActivity.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void setListFrag() {
        // Create an instance of ExampleFragment
		switch(prefs.getYear()) {
			case 2013:
				matchListFrag = new MatchList2013();
				break;
			default:
				throw new ClassCastException("Must give year!");
		}
        // Add the fragment to the 'fragment_container' FrameLayout
        if (this.findViewById(R.id.fragment_container) != null) {
        	listFrag = R.id.fragment_container;
    		detailFrag = R.id.fragment_container;
		} else {
        	listFrag = R.id.list_fragment;
			detailFrag = R.id.detail_fragment;
		}
        getFragmentManager().beginTransaction().replace(listFrag, matchListFrag).commitAllowingStateLoss();
	}

	public void onMatchSelected(Match match) {
//		if(null!=teamDetailFragment) {
//			teamDetailFragment.updateScouting();
//			teamDetailFragment.commitUpdatedScouting();
//		}
		Log.d(TAG, match.toString());
		// The user selected the headline of an article from the HeadlinesFragment

        Bundle args = new Bundle();
        args.putLong(Match.TAG, match.getId());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch(prefs.getYear()) {
			case 2013:
				matchDetailFragment = new MatchDetail2013();
	            break;
	        default:
	        	Toast.makeText(this, "Error could not find year", Toast.LENGTH_LONG).show();
	            transaction.commit();
	            return;
		}
		matchDetailFragment.setArguments(args);
        transaction.replace(detailFrag, matchDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
	}

	public void onBackStackChanged() {
		Log.i(TAG, "back stack changed ");
        if (getFragmentManager().getBackStackEntryCount() < 1){
        	this.setTitle(R.string.matches);
        }
	}

	public void onInsertMatchDialogPositiveClick(boolean update, Match match) {
		if(update) {
	    	matchListFrag.updateMatch(match);
		} else {
			matchListFrag.insertMatch(match);
		}
	}

	@Override
	public void onDeleteDialogPositiveClick(long match) {
		matchListFrag.deleteMatch(match);
	}
}
