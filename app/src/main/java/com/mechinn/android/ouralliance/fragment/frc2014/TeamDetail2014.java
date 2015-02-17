package com.mechinn.android.ouralliance.fragment.frc2014;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.frc2014.TeamScouting2014FilterAdapter;
import com.mechinn.android.ouralliance.fragment.TeamDetailFragment;
import com.mechinn.android.ouralliance.greenDao.Event;
import com.mechinn.android.ouralliance.greenDao.TeamScouting2014;
import com.mechinn.android.ouralliance.greenDao.Wheel;
import com.mechinn.android.ouralliance.greenDao.dao.TeamScouting2014Dao;
import com.mechinn.android.ouralliance.greenDao.dao.WheelDao;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroup;
import com.mechinn.android.ouralliance.widget.UncheckableRadioGroupOnCheckedChangeListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import de.greenrobot.dao.async.AsyncOperation;

public class TeamDetail2014 extends TeamDetailFragment<TeamScouting2014> {
    public static final String TAG = "TeamDetail2014";
    public static final int maxPerimeter = 112;
    public static final int maxHeight = 84;
    public static final int maxDistance = 9999;

	@InjectView(R.id.team2014orientation) private AutoCompleteTextView orientation;
    @InjectView(R.id.team2014driveTrain) private AutoCompleteTextView driveTrain;
    @InjectView(R.id.team2014width) private EditText width;
    @OnEditorAction(R.id.team2014width) private boolean widthEdit(TextView v, int actionId, KeyEvent event) {
        if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (!event.isShiftPressed()) {
                checkPerimeter();
                return true; // consume.
            }
        }
        return false; // pass on to other listeners.
    }
    @OnFocusChange(R.id.team2014width) private void widthFocus(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkPerimeter();
        }
    }
    @InjectView(R.id.team2014length) private EditText length;
    @OnEditorAction(R.id.team2014length) private boolean lengthEdit(TextView v, int actionId, KeyEvent event) {
        if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (!event.isShiftPressed()) {
                checkPerimeter();
                return true; // consume.
            }
        }
        return false; // pass on to other listeners.
    }
    @OnFocusChange(R.id.team2014length) private void lengthFocus(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkPerimeter();
        }
    }
    @InjectView(R.id.team2014heightShooter) private EditText heightShooter;
    @OnEditorAction(R.id.team2014heightShooter) public boolean heightShooterEdit(TextView v, int actionId, KeyEvent event) {
        if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (!event.isShiftPressed()) {
                checkShooterHeight();
                return true; // consume.
            }
        }
        return false; // pass on to other listeners.
    }
    @OnFocusChange(R.id.team2014heightShooter) private void heightShooterFocus(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkShooterHeight();
        }
    }
    @InjectView(R.id.team2014heightMax) private EditText heightMax;
    @OnEditorAction(R.id.team2014heightMax) public boolean heightMaxEdit(TextView v, int actionId, KeyEvent event) {
        if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (!event.isShiftPressed()) {
                checkMaxHeight();
                return true; // consume.
            }
        }
        return false; // pass on to other listeners.
    }
    @OnFocusChange(R.id.team2014heightMax) private void heightMaxFocus(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkMaxHeight();
        }
    }
    @InjectView(R.id.team2014shooterType) private UncheckableRadioGroup shooterTypes;
    @InjectView(R.id.team2014shooterGroup) private LinearLayout shooterGroup;
    @InjectView(R.id.team2014lowGoal) private CheckBox lowGoal;
    @InjectView(R.id.team2014highGoal) private CheckBox highGoal;
    @InjectView(R.id.team2014shootingDistanceGroup) private LinearLayout shootingDistanceGroup;
    @InjectView(R.id.team2014shootingDistance) private EditText shootingDistance;
    @OnEditorAction(R.id.team2014shootingDistance) public boolean shootingDistanceEdit(TextView v, int actionId, KeyEvent event) {
        if (null!=event && (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (!event.isShiftPressed()) {
                checkShootingDistance();
                return true; // consume.
            }
        }
        return false; // pass on to other listeners.
    }
    @OnFocusChange(R.id.team2014shootingDistance) private void shootingDistanceFocus(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkShootingDistance();
        }
    }
    @InjectView(R.id.team2014passGround) private CheckBox passGround;
    @InjectView(R.id.team2014passAir) private CheckBox passAir;
    @InjectView(R.id.team2014passTruss) private CheckBox passTruss;
    @InjectView(R.id.team2014pickupGround) private CheckBox pickupGround;
    @InjectView(R.id.team2014pickupCatch) private CheckBox pickupCatch;
    @InjectView(R.id.team2014pusher) private CheckBox pusher;
    @InjectView(R.id.team2014blocker) private CheckBox blocker;
    @InjectView(R.id.team2014humanPlayer) private RatingBar humanPlayer;
    @InjectView(R.id.team2014noAuto) private CheckBox noAuto;
    @InjectView(R.id.team2014driveAuto) private CheckBox driveAuto;
    @InjectView(R.id.team2014lowAuto) private CheckBox lowAuto;
    @InjectView(R.id.team2014highAuto) private CheckBox highAuto;
    @InjectView(R.id.team2014hotAuto) private CheckBox hotAuto;

	private TeamScouting2014FilterAdapter orientationsAdapter;
	private TeamScouting2014FilterAdapter driveTrainsAdapter;
    private AsyncOperation onScoutingLoaded;

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        if(onScoutingLoaded == operation) {
            if (operation.isCompletedSucessfully()) {
                List<TeamScouting2014> result = (List<TeamScouting2014>) operation.getResult();
                Log.d(TAG, "Count: " + result.size());
                orientationsAdapter.swapList(result);
                driveTrainsAdapter.swapList(result);
            } else {

            }
        } else {
            super.onAsyncOperationCompleted(operation);
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		View seasonView = inflater.inflate(R.layout.fragment_team_detail_2014, getSeason(), false);
        ButterKnife.inject(this, seasonView);
		shooterTypes.setOnCheckedChangeListener(new UncheckableRadioGroupOnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                super.onCheckedChanged(group, checkedId);
                if (null != getScouting()) {
                    switch (checkedId) {
                        default:
                        case R.id.none:
                            shooterGroup.setVisibility(View.GONE);
                            lowGoal.setChecked(false);
                            highGoal.setChecked(false);
                            shootingDistanceGroup.setVisibility(View.GONE);
                            shootingDistance.setText("");
                            lowAuto.setVisibility(View.GONE);
                            lowAuto.setChecked(false);
                            highAuto.setVisibility(View.GONE);
                            highAuto.setChecked(false);
                            hotAuto.setVisibility(View.GONE);
                            hotAuto.setChecked(false);
                            break;
                        case R.id.dumper:
                            shooterGroup.setVisibility(View.VISIBLE);
                            lowGoal.setChecked(getScouting().getLowGoal());
                            highGoal.setVisibility(View.GONE);
                            highGoal.setChecked(false);
                            shootingDistanceGroup.setVisibility(View.GONE);
                            shootingDistance.setText("");
                            lowAuto.setVisibility(View.VISIBLE);
                            lowAuto.setChecked(getScouting().getLowAuto());
                            highAuto.setVisibility(View.GONE);
                            highAuto.setChecked(false);
                            hotAuto.setVisibility(View.GONE);
                            hotAuto.setChecked(false);
                            break;
                        case R.id.shooter:
                            shooterGroup.setVisibility(View.VISIBLE);
                            lowGoal.setChecked(getScouting().getLowGoal());
                            highGoal.setVisibility(View.VISIBLE);
                            highGoal.setChecked(getScouting().getHighGoal());
                            shootingDistanceGroup.setVisibility(View.VISIBLE);
                            if (0 != getScouting().getShootingDistance()) {
                                shootingDistance.setText(Double.toString(getScouting().getShootingDistance()));
                            }
                            lowAuto.setChecked(getScouting().getLowAuto());
                            lowAuto.setVisibility(View.VISIBLE);
                            highAuto.setChecked(getScouting().getHighAuto());
                            highAuto.setVisibility(View.VISIBLE);
                            hotAuto.setChecked(getScouting().getHotAuto());
                            hotAuto.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        });
		getSeason().addView(seasonView);
		return rootView;
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orientationsAdapter = new TeamScouting2014FilterAdapter(getActivity(), null, TeamScouting2014FilterAdapter.Type.ORIENTATION);
        orientation.setAdapter(orientationsAdapter);
        orientation.setThreshold(1);
        driveTrainsAdapter = new TeamScouting2014FilterAdapter(getActivity(), null, TeamScouting2014FilterAdapter.Type.DRIVETRAIN);
        driveTrain.setAdapter(driveTrainsAdapter);
        driveTrain.setThreshold(1);
    }
	
	@Override
	public void onStart() {
		super.onStart();
	}

    @Override
    public void onResume() {
        super.onResume();
        if (this.getPrefs().getYear() != 0 && getTeamId() != 0) {
            onScoutingLoaded = getAsync().loadAll(TeamScouting2014.class);
            setScoutuingLoaded(getAsync().queryUnique(getDaoSession().getWheelDao().queryBuilder().where(TeamScouting2014Dao.Properties.TeamId.eq(getTeamId())).build()));
        }
    }
	
	@Override
	public void setView() {
		super.setView();
		String num;
		orientation.setText(getScouting().getOrientation());
		driveTrain.setText(getScouting().getDriveTrain());
		//check if its 0, if so empty the string so the user doesnt go crazy
		if(0!=getScouting().getWidth()) {
			num = Double.toString(getScouting().getWidth());
			width.setText(num);
		}
		if(0!=getScouting().getLength()) {
			num = Double.toString(getScouting().getLength());
			length.setText(num);
		}
		if(0!=getScouting().getHeightShooter()) {
			num = Double.toString(getScouting().getHeightShooter());
			heightShooter.setText(num);
		}
		if(0!=getScouting().getHeightMax()) {
			num = Double.toString(getScouting().getHeightMax());
			heightMax.setText(num);
		}
        switch(getScouting().getShooterType()) {
            case 0:
                shooterTypes.programaticallyCheck(R.id.none);
                break;
            case 1:
                shooterTypes.programaticallyCheck(R.id.dumper);
                break;
            case 2:
                shooterTypes.programaticallyCheck(R.id.shooter);
                break;
        }
		lowGoal.setChecked(getScouting().getLowGoal());
        highGoal.setChecked(getScouting().getHighGoal());
        if(0!=getScouting().getShootingDistance()) {
            num = Double.toString(getScouting().getShootingDistance());
            shootingDistance.setText(num);
        }
        passGround.setChecked(getScouting().getPassGround());
        passAir.setChecked(getScouting().getPassAir());
        passTruss.setChecked(getScouting().getPassTruss());
        pickupGround.setChecked(getScouting().getPickupGround());
        pickupCatch.setChecked(getScouting().getPickupCatch());
        pusher.setChecked(getScouting().getPusher());
		blocker.setChecked(getScouting().getBlocker());
        humanPlayer.setRating(getScouting().getHumanPlayer().floatValue());
        noAuto.setChecked(getScouting().getNoAuto());
        driveAuto.setChecked(getScouting().getDriveAuto());
        lowAuto.setChecked(getScouting().getLowAuto());
        highAuto.setChecked(getScouting().getHighAuto());
        hotAuto.setChecked(getScouting().getHotAuto());
	}
	
	@Override
	public void updateScouting() {
		super.updateScouting();
		getScouting().setOrientation(orientation.getText().toString());
		getScouting().setDriveTrain(driveTrain.getText().toString());
		getScouting().setWidth(Utility.getDoubleFromText(width.getText()));
		getScouting().setLength(Utility.getDoubleFromText(length.getText()));
		getScouting().setHeightShooter(Utility.getDoubleFromText(heightShooter.getText()));
		getScouting().setHeightMax(Utility.getDoubleFromText(heightMax.getText()));
        switch(shooterTypes.getCheckedRadioButtonId()) {
            case R.id.none:
                getScouting().setShooterType(0);
                break;
            case R.id.dumper:
                getScouting().setShooterType(1);
                break;
            case R.id.shooter:
                getScouting().setShooterType(2);
                break;
        }
		getScouting().setLowGoal(lowGoal.isChecked());
        getScouting().setHighGoal(highGoal.isChecked());
        getScouting().setShootingDistance(Utility.getDoubleFromText(shootingDistance.getText()));
		getScouting().setPassGround(passGround.isChecked());
		getScouting().setPassAir(passAir.isChecked());
		getScouting().setPassTruss(passTruss.isChecked());
		getScouting().setPickupGround(pickupGround.isChecked());
		getScouting().setPickupCatch(pickupCatch.isChecked());
		getScouting().setPusher(pusher.isChecked());
		getScouting().setBlocker(blocker.isChecked());
        getScouting().setHumanPlayer((double)humanPlayer.getRating());
        getScouting().setNoAuto(noAuto.isChecked());
        getScouting().setDriveAuto(driveAuto.isChecked());
        getScouting().setLowAuto(lowAuto.isChecked());
        getScouting().setHighAuto(highAuto.isChecked());
        getScouting().setHotAuto(hotAuto.isChecked());
	}
	
	public void checkPerimeter() {
		try {
	        int widthVal = Integer.parseInt(width.getText().toString());
	        int lengthVal = Integer.parseInt(length.getText().toString());
	        int perimeter = 2*widthVal+2*lengthVal;
	        if(perimeter>maxPerimeter) {
	     	   Toast.makeText(TeamDetail2014.this.getActivity(), "Perimeter exceeds "+maxPerimeter+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void checkShooterHeight() {
		try {
	        int shooterHeight = Integer.parseInt(heightShooter.getText().toString());
	        if(shooterHeight>maxHeight) {
	     	   Toast.makeText(TeamDetail2014.this.getActivity(), "Shooter height exceeds "+maxHeight+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void checkMaxHeight() {
		try {
	        int maxHeight = Integer.parseInt(heightMax.getText().toString());
	        if(maxHeight>maxHeight) {
	     	   Toast.makeText(TeamDetail2014.this.getActivity(), "Max height exceeds "+maxHeight+" inches!", Toast.LENGTH_SHORT).show();
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

    public void checkShootingDistance() {
        try {
            int maxDistance = Integer.parseInt(shootingDistance.getText().toString());
            if(maxDistance>maxDistance) {
                Toast.makeText(TeamDetail2014.this.getActivity(), "Max distance exceeds "+maxDistance, Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}