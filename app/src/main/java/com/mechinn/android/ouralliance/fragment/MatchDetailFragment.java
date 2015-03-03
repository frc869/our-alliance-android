package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

public abstract class MatchDetailFragment extends Fragment {
    public static final String TAG = "MatchDetailFragment";
    public static final String SCOUTING_ARG = "team";

    private Prefs prefs;
    private long scoutingId;

    private View rootView;
    private TextView notes;
    private LinearLayout season;

	private MatchScouting match;

    public View getRootView() {
        return rootView;
    }
    public void setRootView(View rootView) {
        this.rootView = rootView;
    }
    public long getScoutingId() {
        return scoutingId;
    }
    public void setScoutingId(long scoutingId) {
        this.scoutingId = scoutingId;
    }
    public LinearLayout getSeason() {
    return season;
}
    public void setSeason(LinearLayout season) {
        this.season = season;
    }
	public MatchScouting getMatch() {
		return match;
	}
	public void setMatch(MatchScouting match) {
		this.match = match;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new Prefs(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
    	setRetainInstance(true);
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            scoutingId = savedInstanceState.getLong(EventTeam.TAG, 0);
            Log.d(TAG, "team: "+ scoutingId);
        }
    	
//    	OnClickListener teamButton = new OnClickListener() {
//			public void onClick(View v) {
//				Team team = (Team) v.getTag();
//	        	Intent intent = new Intent(MatchDetailFragment.this.getActivity(), TeamScoutingActivity.class);
//	        	Bundle args = new Bundle();
//	        	args.putString(TeamScoutingActivity.MATCHNAME_ARG, match.toString());
//	        	args.putLong(TeamScoutingActivity.MATCH_ARG,match.getId());
//                args.putLong(TeamScoutingActivity.TEAM_ARG,team.getId());
//	        	intent.putExtras(args);
//	            startActivity(intent);
//		    }
//		};
        
        rootView = inflater.inflate(R.layout.fragment_match_detail, container, false);
        notes = (TextView) rootView.findViewById(R.id.matchNotes);
        season = (LinearLayout) rootView.findViewById(R.id.season);
		return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            scoutingId = getArguments().getLong(SCOUTING_ARG, 0);
            Log.d(TAG, "team: " + scoutingId);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "team: " + getScoutingId());
        loadMatchScouting();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "pausing");
        if(null!=getMatch()) {
            Log.d(TAG,"saving "+getMatch());
            updateMatch();
            this.getMatch().asyncSave();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SCOUTING_ARG, scoutingId);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void setView() {
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setTitle(match.getTeamScouting().getTeam().toString());
        notes.setText(match.getNotes());
	}
	
	public void updateMatch() {
        match.setNotes(notes.getText().toString());
	}

    public abstract void loadMatchScouting();
}
