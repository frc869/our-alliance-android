package com.mechinn.android.ouralliance.fragment;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.widget.*;

import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.data.*;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.EventTeamDragSortListAdapter;
import com.mechinn.android.ouralliance.data.frc2014.ExportTeamScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.Sort2014;
import com.mechinn.android.ouralliance.activity.MatchScoutingActivity;
import com.mechinn.android.ouralliance.event.BluetoothEvent;
import com.mechinn.android.ouralliance.greenDao.EventTeam;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;
import com.mechinn.android.ouralliance.greenDao.dao.EventTeamDao;
import com.mechinn.android.ouralliance.greenDao.dao.TeamDao;
import com.mechinn.android.ouralliance.greenDao.dao.TeamScouting2014Dao;
import com.mechinn.android.ouralliance.rest.thebluealliance.GetEventTeams;
import com.mobeta.android.dslv.DragSortListView;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;

import java.util.List;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.event.EventBus;

public class TeamListFragment extends Fragment implements AsyncOperationListener {
    public static final String TAG = "TeamListFragment";
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Listener mCallback;
    private DragSortListView dslv;
    private int selectedPosition;
	private Prefs prefs;
	private EventTeamDragSortListAdapter adapter;
    private BluetoothAdapter bluetoothAdapter;
    private boolean bluetoothOn;
    private Sort2014 sort;
    private Spinner sortTeams;
    private GetEventTeams downloadTeams;
    private DaoSession daoSession;
    private AsyncSession async;
    private AsyncOperation eventLoader;

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        if(eventLoader == operation) {
            if (operation.isCompletedSucessfully()) {
                List<EventTeam> teams = (List<EventTeam>) operation.getResult();
                switch (prefs.getYear()) {
                    case 2014:
                        adapter.showDrag(sort.equals(Sort2014.RANK));
                        break;
                }
                adapter.swapList(teams);
                getActivity().invalidateOptionsMenu();
            } else {
                getActivity().invalidateOptionsMenu();
            }
        }
    }

    public interface Listener {
        public void onTeamSelected(long team);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		prefs = new Prefs(this.getActivity());
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        sort = Sort2014.RANK;
        daoSession = ((OurAlliance) this.getActivity().getApplication()).getDaoSession();
        async = ((OurAlliance) this.getActivity().getApplication()).getAsyncSession();
        async.setListener(this);
        downloadTeams = new GetEventTeams(this.getActivity());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_team_list, container, false);
        sortTeams = (Spinner) rootView.findViewById(R.id.sortTeams);
        sortTeams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(prefs.getYear()) {
                    case 2014:
                        sort = Sort.sort2014List.get(position);
                        break;
                }
                reloadTeams();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        dslv = (DragSortListView) rootView.findViewById(android.R.id.list);
    	return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    	setRetainInstance(true);
		registerForContextMenu(dslv);
		dslv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	selectItem(position);
			}
		});
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			int position = savedInstanceState.getInt(STATE_ACTIVATED_POSITION);
			if (position == ListView.INVALID_POSITION) {
				dslv.setItemChecked(selectedPosition, false);
			} else {
				dslv.setItemChecked(position, true);
			}
			selectedPosition = position;
		}
        adapter = new EventTeamDragSortListAdapter(getActivity(), null);
        dslv.setAdapter(adapter);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "start");
        EventBus.getDefault().register(this);
//		this.getActivity().registerForContextMenu(this.getListView());
        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.list_fragment) != null) {
        	dslv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        if(null!=adapter) {
            adapter.notifyDataSetChanged();
        }
        if(null!=bluetoothAdapter) {
            bluetoothOn = bluetoothAdapter.isEnabled();
        }
    }

    public void onEventMainThread(BluetoothEvent event) {
        switch (event.getState()) {
            case STATE_OFF:
            case STATE_TURNING_OFF:
                bluetoothOn = false;
                break;
            case STATE_ON:
            case STATE_TURNING_ON:
                bluetoothOn = true;
                break;
        }
        getActivity().invalidateOptionsMenu();
    }

    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"resume");
        Log.d(TAG,"CompID: "+prefs.getComp());
        if(prefs.getComp()>0) {
            if(prefs.getComp()>0 && !prefs.isEventTeamsDownloaded()) {
                downloadTeams.refreshEventTeams();
            }
            switch(prefs.getYear()) {
                case 2014:
                    ArrayAdapter<Sort2014> sort2014Adapter = new ArrayAdapter<Sort2014>(getActivity(),android.R.layout.simple_list_item_1, Sort.sort2014List);
                    sortTeams.setAdapter(sort2014Adapter);
                    break;
            }
            sortTeams.setSelection(0);
        } else {
            emptyTeams();
        }
    }

    @Override
    public void onDestroy() {
        downloadTeams.quit();
        super.onDestroy();
    }

    private void emptyTeams() {

    }

    private void reloadTeams() {
        EventTeamDao eventTeamDao = daoSession.getEventTeamDao();

        switch(prefs.getYear()) {
            case 2014:
                QueryBuilder<EventTeam> builder = eventTeamDao.queryBuilder().where(EventTeamDao.Properties.EventId.eq(prefs.getComp()));
                switch(sort) {
                    case NUMBER:
                        builder.orderDesc(TeamDao.Properties.TeamNumber);
                        break;
                    case ORIENTATION:
                        builder.orderAsc(TeamScouting2014Dao.Properties.Orientation);
                        break;
                    case DRIVETRAIN:
                        builder.orderAsc(TeamScouting2014Dao.Properties.DriveTrain);
                        break;
                    case HEIGHTSHOOTER:
                        builder.orderDesc(TeamScouting2014Dao.Properties.HeightShooter);
                        break;
                    case HEIGHTMAX:
                        builder.orderDesc(TeamScouting2014Dao.Properties.HeightMax);
                        break;
                    case SHOOTERTYPE:
                        builder.orderDesc(TeamScouting2014Dao.Properties.ShooterType);
                        break;
                    case SHOOTGOAL:
                        builder.orderDesc(TeamScouting2014Dao.Properties.HighGoal,TeamScouting2014Dao.Properties.LowGoal);
                        break;
                    case SHOOTINGDISTANCE:
                        builder.orderDesc(TeamScouting2014Dao.Properties.ShootingDistance);
                        break;
                    case PASS:
                        builder.orderDesc(TeamScouting2014Dao.Properties.PassTruss,TeamScouting2014Dao.Properties.PassAir,TeamScouting2014Dao.Properties.PassGround);
                        break;
                    case PICKUP:
                        builder.orderDesc(TeamScouting2014Dao.Properties.PickupCatch,TeamScouting2014Dao.Properties.PickupGround);
                        break;
                    case PUSHER:
                        builder.orderAsc(TeamScouting2014Dao.Properties.Pusher);
                        break;
                    case BLOCKER:
                        builder.orderAsc(TeamScouting2014Dao.Properties.Blocker);
                        break;
                    case HUMANPLAYER:
                        builder.orderAsc(TeamScouting2014Dao.Properties.HumanPlayer);
                        break;
                    case AUTONOMOUS:
                        builder.orderDesc(TeamScouting2014Dao.Properties.HotAuto,TeamScouting2014Dao.Properties.HighAuto,TeamScouting2014Dao.Properties.LowAuto,TeamScouting2014Dao.Properties.DriveAuto,TeamScouting2014Dao.Properties.NoAuto);
                        break;
                    default:
                        builder.orderAsc(EventTeamDao.Properties.Rank);
                        break;
                }
                eventLoader = async.queryList(builder.build());
                break;
        }

    }
    
    private void selectItem(int position) {
        // Notify the parent activity of selected item
        mCallback.onTeamSelected(adapter.getItem(position).getTeam().getId());
        
        // Set the item as checked to be highlighted when in two-pane layout
        dslv.setItemChecked(position, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.action_request_enable_bluetooth) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothTransfer();
            }
        }
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        try {
            if (selectedPosition != ListView.INVALID_POSITION) {
                // Serialize and persist the activated item position.
                outState.putInt(STATE_ACTIVATED_POSITION, selectedPosition);
            }
        } catch (IllegalStateException e) {
            Log.d(TAG,"",e);
        }
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.team_list, menu);
	}

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.matchList).setVisible(prefs.getComp()>0 && null!=adapter && adapter.getCount() > 5);
        menu.findItem(R.id.insertTeamScouting).setVisible(prefs.getComp()>0);
        menu.findItem(R.id.importTeamScouting).setVisible(prefs.getComp()>0);
        menu.findItem(R.id.exportTeamScouting).setVisible(null!=adapter && adapter.getCount()>0);
        menu.findItem(R.id.bluetoothTeamScouting).setVisible(null != adapter && adapter.getCount() > 0 && bluetoothAdapter != null);
        if(bluetoothOn) {
            menu.findItem(R.id.bluetoothTeamScouting).setIcon(R.drawable.ic_action_bluetooth_searching);
        } else {
            menu.findItem(R.id.bluetoothTeamScouting).setIcon(R.drawable.ic_action_bluetooth);
        }
        menu.findItem(R.id.refreshCompetitionTeams).setVisible(prefs.getComp()>0);
    }

    private void bluetoothTransfer() {
        DialogFragment newFragment = new TransferBluetoothDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TransferBluetoothDialogFragment.TYPE, Import.Type.TEAMSCOUTING2014);
        newFragment.setArguments(bundle);
        newFragment.show(this.getFragmentManager(), "Bluetooth Transfer Team Scouting");
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.matchList:
                Intent intent = new Intent(this.getActivity(), MatchScoutingActivity.class);
                startActivity(intent);
	            return true;
	        case R.id.insertTeamScouting:
                DialogFragment newFragment = new InsertTeamDialogFragment();
                newFragment.show(this.getFragmentManager(), "Add Team");
	            return true;
	        case R.id.exportTeamScouting:
                new ExportTeamScouting2014(this.getActivity()).execute();
	            return true;
            case R.id.importTeamScouting:
                new Import(this.getActivity(),new Handler(new Handler.Callback(){
                    @Override
                    public boolean handleMessage(Message msg) {
                        if(null!=msg.getData().getString(Import.RESULT)) {
                            Toast.makeText(getActivity(),msg.getData().getString(Import.RESULT),Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        return false;
                    }
                }),Import.Type.TEAMSCOUTING2014).start();
                return true;
            case R.id.bluetoothTeamScouting:
                if(!bluetoothAdapter.isEnabled()) {
                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetoothIntent, R.id.action_request_enable_bluetooth);
                } else {
                    bluetoothTransfer();
                }
                return true;
            case R.id.refreshCompetitionTeams:
                downloadTeams.refreshEventTeams();
                return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = this.getActivity().getMenuInflater();
	    inflater.inflate(R.menu.team_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
	    DialogFragment dialog;
	    switch (item.getItemId()) {
	        case R.id.open:
	        	selectItem(position);
	            return true;
//	        case R.id.edit:
//		        	dialog = new InsertTeamDialogFragment();
//		            Bundle updateArgs = new Bundle();
//		            updateArgs.putSerializable(InsertTeamDialogFragment.TEAM_ARG, adapter.get(position).getTeam());
//		            dialog.setArguments(updateArgs);
//		        	dialog.show(this.getFragmentManager(), "Edit Team");
//	            return true;
	        case R.id.delete:
                dialog = new DeleteTeamDialogFragment();
                Bundle deleteArgs = new Bundle();
                deleteArgs.putSerializable(DeleteTeamDialogFragment.TEAM_ARG, adapter.getItem(position));
                dialog.setArguments(deleteArgs);
                dialog.show(this.getFragmentManager(), "Delete Team");
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
}