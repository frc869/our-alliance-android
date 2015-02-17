package com.mechinn.android.ouralliance.adapter;

import java.io.File;
import java.net.URLConnection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.fragment.MultimediaContextDialogFragment;
import com.mechinn.android.ouralliance.greenDao.TeamScouting2014;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class MultimediaAdapter extends RecyclerView.Adapter<MultimediaAdapter.SimpleViewHolder> implements Callback {
    public static final String TAG = "MultimediaAdapter";
    private File[] multimedia;
    private File teamFileDirectory;
    private Activity activity;
    private Prefs prefs;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final ImageView image;
        public final Context context;

        public SimpleViewHolder(View view) {
            super(view);
            context = view.getContext();
            image = (ImageView) view.findViewById(R.id.galleryImage);
        }
    }

    public File getTeamFileDirectory() {
        return teamFileDirectory;
    }

    public void setTeamFileDirectory(File teamFileDirectory) {
        this.teamFileDirectory = teamFileDirectory;
    }

    public MultimediaAdapter(Activity activity, TeamScouting team) {
        this.activity = activity;
        prefs = new Prefs(activity);
        Log.d(TAG, team.toString());
        Log.d(TAG,"find images");
        buildImageSet(team);
    }
    public void buildImageSet(TeamScouting team) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            teamFileDirectory = new File(activity.getExternalFilesDir(null).getAbsoluteFile() + File.separator + prefs.getYear() + File.separator + team.getTeam().getTeamNumber());
            Log.d(TAG, teamFileDirectory.getAbsolutePath());
            if(!teamFileDirectory.mkdirs()) {
                multimedia = teamFileDirectory.listFiles();
            }
        }
        this.notifyDataSetChanged();
    }
    public File getItem(int position) {
        if(null==multimedia) {
            return null;
        }
        return multimedia[position];
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(null==multimedia) {
            return 0;
        }
        return multimedia.length;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(activity).inflate(R.layout.gallery_image, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.image.setTag(R.string.file, getItem(position));

        // Get the image URL for the current position.
        File file = getItem(position);

        Log.d(TAG, "get image");
        // Trigger the download of the URL asynchronously into the image view.
        final float scale = activity.getResources().getDisplayMetrics().density;
        Picasso.with(activity) //
                .load(file) //
                .fit().centerInside()
                .placeholder(R.drawable.ic_empty) //
                .error(R.drawable.ic_error) //
                .into(holder.image, this);
    }

    @Override
    public void onSuccess() {
        Log.d(TAG,"get image: success");
    }

    @Override
    public void onError() {
        Log.d(TAG,"get image: error");
        Toast.makeText(activity, "error loading image", Toast.LENGTH_SHORT).show();
    }
    public static void openMedia(View v) {
        File filename = (File) v.getTag(R.string.file);
        Log.d(TAG, filename.toString());
        String type = URLConnection.guessContentTypeFromName("file://"+filename.getAbsolutePath());
        Log.d(TAG, type);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://"+filename.getAbsolutePath()), type);
        try {
            v.getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(v.getContext(), "No known viewer for this file type", Toast.LENGTH_LONG).show();
        }
    }

}
