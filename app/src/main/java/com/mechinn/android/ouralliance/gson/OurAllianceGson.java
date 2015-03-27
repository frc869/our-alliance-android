package com.mechinn.android.ouralliance.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.Wheel2015;
import com.mechinn.android.ouralliance.gson.frc2015.MatchScouting2015Adapter;
import com.mechinn.android.ouralliance.gson.frc2015.TeamScouting2015Adapter;
import com.mechinn.android.ouralliance.gson.frc2015.Wheel2015Adapter;
import com.mechinn.android.ouralliance.rest.JsonDateAdapter;

import java.util.Date;

/**
 * Created by mechinn on 3/10/15.
 */
public class OurAllianceGson {
    public static final Gson BUILDER = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new JsonDateAdapter())
            .registerTypeAdapter(Team.class, new TeamAdapter())
            .registerTypeAdapter(Event.class, new EventAdapter())
            .registerTypeAdapter(EventTeam.class, new EventTeamAdapter())
            .registerTypeAdapter(Match.class, new MatchAdapter())
            .registerTypeAdapter(TeamScouting2015.class, new TeamScouting2015Adapter())
            .registerTypeAdapter(Wheel2015.class, new Wheel2015Adapter())
            .registerTypeAdapter(MatchScouting2015.class, new MatchScouting2015Adapter())
            .create();
}