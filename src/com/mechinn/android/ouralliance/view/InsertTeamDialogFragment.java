package com.mechinn.android.ouralliance.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Team;

public class InsertTeamDialogFragment extends DialogFragment {
	public static final String tag = "InsertTeamDialog";
	public static final String TEAM_ARG = "team";
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface Listener {
        public void onInsertDialogPositiveClick(boolean update, Team team);
        public void onInsertDialogNegativeClick(DialogFragment dialog);
    }
    
    Listener listener;
    private View dialog;
    private TextView teamNumber;
    private TextView teamName;
    private Team team;
    private boolean update;
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	// Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
        	listener = (Listener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement "+tag+".Listener");
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialog = inflater.inflate(R.layout.dialog_team_insert, null);
		teamNumber = (TextView) dialog.findViewById(R.id.editTeamNumber);
		teamName = (TextView) dialog.findViewById(R.id.editTeamName);
		int yes;
		try {
			team = (Team) this.getArguments().getSerializable(TEAM_ARG);
    		teamNumber.setText(Integer.toString(team.getNumber()));
    		teamName.setText(team.getName());
    		yes = R.string.update;
    		update = true;
		} catch(NullPointerException e) {
			team = new Team();
			yes = R.string.create;
			update = false;
		}
		builder.setView(dialog)
			.setPositiveButton(yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					team.setNumber(teamNumber.getText());
					team.setName(teamName.getText());
					listener.onInsertDialogPositiveClick(update, team);
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Send the negative button event back to the host activity
					listener.onInsertDialogNegativeClick(InsertTeamDialogFragment.this);
				}
			});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
