package com.mechinn.android.ouralliance.fragment;

import java.io.File;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.MultimediaAdapter;
import com.mechinn.android.ouralliance.adapter.WheelAdapter;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.Wheel;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.Wheel2014;
import com.mechinn.android.ouralliance.event.MultimediaDeletedEvent;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.lucasr.twowayview.ItemClickSupport;
import org.lucasr.twowayview.widget.TwoWayView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public abstract class TeamDetailFragment<Scouting extends TeamScouting> extends Fragment {
    public static final String TAG = "TeamDetailFragment";
	public static final String TEAM_ARG = "team";
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

	private Prefs prefs;
	private View rootView;
	private Button picture;
    private Button video;
	private TwoWayView gallery;
    private TextView notes;
	private Button addWheel;
    private ListView wheels;
	private MultimediaAdapter multimedia;

    private WheelAdapter wheelsAdapter;

    private LinearLayout season;
	private long teamId;
	private Scouting scouting;
    private Event event;

	public Prefs getPrefs() {
		return prefs;
	}
	public void setPrefs(Prefs prefs) {
		this.prefs = prefs;
	}
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	public Scouting getScouting() {
		return scouting;
	}
	public void setScouting(Scouting scouting) {
		this.scouting = scouting;
	}
	public LinearLayout getSeason() {
		return season;
	}
	public void setSeason(LinearLayout season) {
		this.season = season;
	}
    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
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
    		teamId = savedInstanceState.getLong(Team.TAG, 0);
    		Log.d(TAG, "team: "+teamId);
        }
        
        rootView = inflater.inflate(R.layout.fragment_team_detail, container, false);
		rootView.setVisibility(View.GONE);
        picture = (Button) rootView.findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a media file name
                if(null!=multimedia && null!=multimedia.getTeamFileDirectory()) {
                    String timeStamp = dateFormat.format(new Date());
                    File mediaFile = new File(multimedia.getTeamFileDirectory().getPath().replaceFirst("file://", "") + File.separator + "IMG_"+ timeStamp + ".jpg");
                    Log.d(TAG,mediaFile.getAbsolutePath());
                    // create Intent to take a picture and return control to the calling application
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile)); // set the image file name

                    // start the image capture Intent
                    startActivityForResult(intent, R.id.picture_capture_code);
                }
            }
        });
        video = (Button) rootView.findViewById(R.id.video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a media file name
                if(null!=multimedia && null!=multimedia.getTeamFileDirectory()) {
                    String timeStamp = dateFormat.format(new Date());
                    File mediaFile = new File(multimedia.getTeamFileDirectory().getPath().replaceFirst("file://", "") + File.separator + "VID_"+ timeStamp + ".mp4");
                    Log.d(TAG,mediaFile.getAbsolutePath());
                    // create Intent to take a picture and return control to the calling application
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile)); // set the image file name
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

                    // start the image capture Intent
                    startActivityForResult(intent, R.id.video_capture_code);
                }
            }
        });
		if (this.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			if(!Utility.isIntentAvailable(this.getActivity(),MediaStore.ACTION_IMAGE_CAPTURE)) {
		        picture.setVisibility(View.GONE);
			}
			if(!Utility.isIntentAvailable(this.getActivity(),MediaStore.ACTION_VIDEO_CAPTURE)) {
		        video.setVisibility(View.GONE);
			}
	    } else {
	        picture.setVisibility(View.GONE);
	        video.setVisibility(View.GONE);
	    }
        gallery = (TwoWayView) rootView.findViewById(R.id.gallery);
        gallery.setHasFixedSize(true);
        gallery.setLongClickable(true);
        final ItemClickSupport galleryClick = ItemClickSupport.addTo(gallery);
        galleryClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int i, long l) {
                File filename = (File) view.getTag(R.string.file);
                Log.d(TAG, filename.toString());
                String type = URLConnection.guessContentTypeFromName("file://" + filename.getAbsolutePath());
                Log.d(TAG, type);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + filename.getAbsolutePath()), type);
                try {
                    TeamDetailFragment.this.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(TeamDetailFragment.this.getActivity(), "No known viewer for this file type", Toast.LENGTH_LONG).show();
                }
            }
        });
        galleryClick.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView recyclerView, View view, int i, long l) {
                DialogFragment dialog = new MultimediaContextDialogFragment();
                Bundle dialogArgs = new Bundle();
                File filename = (File) view.getTag(R.string.file);
                dialogArgs.putSerializable(MultimediaContextDialogFragment.IMAGE, filename);
                dialog.setArguments(dialogArgs);
                dialog.show(TeamDetailFragment.this.getFragmentManager(), "Multimedia context menu");
                return false;
            }
        });
        notes = (TextView) rootView.findViewById(R.id.notes);
        addWheel = (Button) rootView.findViewById(R.id.addWheel);
        wheels = (ListView) rootView.findViewById(R.id.wheels);
        wheels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Wheel newWheel = new Wheel2014();
                newWheel.setTeamScouting(getScouting());
                newWheel.asyncSave();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        season = (LinearLayout) rootView.findViewById(R.id.season);
		return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wheelsAdapter = new WheelAdapter(getActivity(), null);
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.picture_capture_code) {
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
            	if(null!=data && null!=data.getData()) {
            		Toast.makeText(this.getActivity(), "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            	} else {
            		Toast.makeText(this.getActivity(), "Image saved", Toast.LENGTH_LONG).show();
            	}
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        } else if (requestCode == R.id.video_capture_code) {
            if (resultCode == Activity.RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
            	if(null!=data && null!=data.getData()) {
	                Toast.makeText(this.getActivity(), "Video saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
	        	} else {
	        		Toast.makeText(this.getActivity(), "Video saved", Toast.LENGTH_LONG).show();
	        	}
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
            // Set article based on argument passed in
    		teamId = getArguments().getLong(TEAM_ARG, 0);
    		Log.d(TAG, "team: "+teamId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefs.getYear() != 0 && teamId != 0) {
            loadEvent();
            loadWheelTypes();
            loadWheels();
        }
    }

	@Override
	public void onPause() {
		if(null!=scouting) {
			updateScouting();
			commitUpdatedScouting();
		}
		super.onPause();
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TEAM_ARG, teamId);
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
		this.getActivity().setTitle(Integer.toString(scouting.getTeam().getTeamNumber())+": "+scouting.getTeam().getNickname());
		multimedia = new MultimediaAdapter(this.getActivity(),scouting);
		Log.d(TAG,"thumbs: "+multimedia.getItemCount());
		gallery.setAdapter(multimedia);
		Log.d(TAG,"imageviews: "+gallery.getChildCount());
		notes.setText(scouting.getNotes());
	}
	
	public void updateScouting() {
		for(int i=0;i<wheels.getChildCount(); ++i) {
			LinearLayout theWheelView = (LinearLayout) wheels.getChildAt(i);
            Wheel theWheel = (Wheel) theWheelView.getTag();
			CharSequence type = ((TextView) theWheelView.getChildAt(R.id.wheelType)).getText();
			theWheel.setWheelType(type.toString());
			CharSequence size = ((TextView) theWheelView.getChildAt(R.id.wheelSize)).getText();
			theWheel.setWheelSize(Utility.getDoubleFromText(size));
			CharSequence count = ((TextView) theWheelView.getChildAt(R.id.wheelCount)).getText();
			theWheel.setWheelCount(Utility.getIntFromText(count));
			//see if we should update or insert or just tell the user there isnt enough info
            theWheel.asyncSave();
		}
		scouting.setNotes(notes.getText().toString());
	}
	
	public void commitUpdatedScouting() {
        this.getScouting().asyncSave();
	}

    public void onEventMainThread(MultimediaDeletedEvent event) {
        if(null!=multimedia) {
            multimedia.buildImageSet(scouting);
        }
    }
    public void loadEvent() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                Event event = Model.load(Event.class,prefs.getComp());
                EventBus.getDefault().post(new LoadEvent(event));
            }
        });
    }
    public void onEventMainThread(Event eventTeamsChanged) {
        loadEvent();
    }
    public void onEventMainThread(LoadEvent event) {
        setEvent(event.getEvent());
    }
    private class LoadEvent {
        Event event;
        public LoadEvent(Event event) {
            this.event = event;
        }
        public Event getEvent() {
            return event;
        }
    }
    public void loadWheelTypes() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                try {
                    List<Wheel> wheelTypes = null;
                    switch (prefs.getYear()) {
                        case 2014:
                            wheelTypes = new Select().from(Wheel2014.class).groupBy(Wheel2014.WHEEL_TYPE).execute();
                            break;
                    }
                    if (null != wheelTypes) {
                        EventBus.getDefault().post(new LoadWheelTypes(wheelTypes));
                    }
                } catch(NullPointerException e) {

                }
            }
        });
    }
    public void onEventMainThread(Wheel wheelsChanged) {
        loadWheelTypes();
        loadWheels();
    }
    public void onEventMainThread(LoadWheelTypes event) {
        wheelsAdapter.swapWheelTypes(event.getWheels());
    }
    private class LoadWheelTypes {
        List<Wheel> wheels;
        public LoadWheelTypes(List<Wheel> wheels) {
            this.wheels = wheels;
        }
        public List<Wheel> getWheels() {
            return wheels;
        }
    }
    public void loadWheels() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                try {
                    List<Wheel> wheels = null;
                    switch (prefs.getYear()) {
                        case 2014:
                            wheels = new Select().from(Wheel2014.class).where(Wheel2014.TEAM_SCOUTING+"=?", getTeamId()).execute();
                            break;
                    }
                    if (null != wheels) {
                        EventBus.getDefault().post(new LoadWheelTypes(wheels));
                    }
                } catch(NullPointerException e) {

                }
            }
        });
    }
    public void onEventMainThread(LoadWheels event) {
        wheelsAdapter.swapList(event.getWheels());
    }
    private class LoadWheels {
        List<Wheel> wheels;
        public LoadWheels(List<Wheel> wheels) {
            this.wheels = wheels;
        }
        public List<Wheel> getWheels() {
            return wheels;
        }
    }
    public void loadScouting() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                Scouting scouting = null;
                try {
                    switch (prefs.getYear()) {
                        case 2014:
                            scouting = new Select().from(TeamScouting2014.class).where(TeamScouting2014.TEAM+"=?", getTeamId()).executeSingle();
                            break;
                    }
                } catch(NullPointerException e) {
                    switch (prefs.getYear()) {
                        case 2014:
                            Team team = Model.load(Team.class,getTeamId());
                            scouting = (Scouting) new TeamScouting2014();
                            scouting.setTeam(team);
                            break;
                    }
                }
                if(null!=scouting) {
                    EventBus.getDefault().post(new LoadScouting(scouting));
                }
            }
        });
    }
    public void onEventMainThread(LoadScouting scouting) {
        setScouting(scouting.getScouting());
        setView();
        rootView.setVisibility(View.VISIBLE);
    }
    private class LoadScouting {
        Scouting scouting;
        public LoadScouting(Scouting scouting) {
            this.scouting = scouting;
        }
        public Scouting getScouting() {
            return scouting;
        }
    }
}
