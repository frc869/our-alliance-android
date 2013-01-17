package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Team;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class DeleteTeamDialogFragment extends DialogFragment {
	private static final String tag = "DeleteTeamDialog";
	public static final String TEAM_ARG = "team";
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface Listener {
        public void onDeleteDialogPositiveClick(Team team);
        public void onDeleteDialogNegativeClick(DialogFragment dialog, int id);
    }
    
    Listener listener;
    private Team team;
    
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
    	team = (Team) this.getArguments().getSerializable(TEAM_ARG);
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.deleteTeam)
			.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
					// Send the positive button event back to the host activity
					listener.onDeleteDialogPositiveClick(team);
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
					// Send the negative button event back to the host activity
					listener.onDeleteDialogNegativeClick(DeleteTeamDialogFragment.this, id);
				}
			});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
