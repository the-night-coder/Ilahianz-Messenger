package com.nightcoder.ilahianz.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nightcoder.ilahianz.BuildConfig;
import com.nightcoder.ilahianz.ImageViewActivity;
import com.nightcoder.ilahianz.Models.Chats;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.ViewSupports;
import com.nightcoder.ilahianz.Utils.Time;
import com.squareup.picasso.Picasso;
import com.vanniktech.emoji.EmojiTextView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.Gravity.CENTER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_AUDIO;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_DOC;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_IMAGE;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int CHAT_TYPE_LEFT = 0;
    private static final int CHAT_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chats> mChats;

    public MessageAdapter(Context mContext, List<Chats> mChats) {
        this.mContext = mContext;
        this.mChats = mChats;
        this.fUser = MemorySupports.getUserInfo(mContext, KEY_ID);
    }

    private String fUser;

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CHAT_TYPE_LEFT)
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_item_reciver, parent, false));
        else
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_item_sender, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, final int position) {
        final Chats chat = mChats.get(position);
        if (chat.getIsSeen() != 1) {
            holder.status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_circle_blue_24dp));
            Log.d("MESSAGE seen", "true");
        } else if (chat.getIsDelivered() != 1) {
            holder.status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_circle_black_24dp));
            Log.d("MESSAGE del", "true");
        } else if (chat.getIsSent() != 1) {
            holder.status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_black_24dp));
            Log.d("MESSAGE Sent", "true");
        } else {
            holder.status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_schedule_black_24dp));
            Log.d("MESSAGE schedule", "true");
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(chat.getSender()).child("thumbnailURL");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                assert url != null;
                if (!url.equals("default")) {
                    Picasso.with(mContext).load(url).into(holder.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.message.setText(chat.getMessage());
        holder.date.setText(chat.getTimestamp());

        holder.imageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ImageViewActivity.class)
                        .putExtra("path", chat.getUrl()));
            }
        });
        if (chat.getType() == TYPE_IMAGE) {
            holder.imageContainer.setVisibility(View.VISIBLE);
            if (chat.getMessage().isEmpty())
                holder.message.setVisibility(View.GONE);
            else
                holder.message.setVisibility(View.VISIBLE);
            holder.docContainer.setVisibility(View.GONE);
            holder.audioContainer.setVisibility(View.GONE);
            final File localFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), chat.getReference());
            if (localFile.exists()) {
                Picasso.with(mContext).load(localFile).into(holder.imageView);
            } else {
                FirebaseStorage mStorage = FirebaseStorage.getInstance();
                StorageReference down = mStorage.getReferenceFromUrl(chat.getUrl());
                try {
                    if (localFile.createNewFile()) {
                        down.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Picasso.with(mContext).load(localFile).into(holder.imageView);
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (chat.getType() == TYPE_DOC) {
            holder.docContainer.setVisibility(View.VISIBLE);
            holder.message.setVisibility(View.GONE);
            holder.imageContainer.setVisibility(View.GONE);
            holder.audioContainer.setVisibility(View.GONE);
            final String[] fileDetails = chat.getMessage().split(",");
            holder.fileName.setText(fileDetails[0]);
            holder.fileSize.setText(fileDetails[1]);
            File localFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), fileDetails[0]);
            if (localFile.exists()) {
                changeDocOpenButtonIcon(holder, "OPEN", R.drawable.ic_insert_drive_file_black_24dp, View.GONE);
            }
            holder.openButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Download", "Pressed");
                    if (holder.openButton.getTag().equals("DOWNLOAD")) {
                        Log.d("Download", "downloading");
                        changeDocOpenButtonIcon(holder, "DOWNLOADING",
                                R.drawable.ic_schedule_black_24dp, View.VISIBLE);
                        FirebaseStorage mStorage = FirebaseStorage.getInstance();
                        StorageReference down = mStorage.getReferenceFromUrl(chat.getUrl());
                        try {
                            File localFile = new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS), fileDetails[0]);
                            if (localFile.createNewFile()) {
                                down.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            changeDocOpenButtonIcon(holder, "OPEN",
                                                    R.drawable.ic_insert_drive_file_black_24dp, View.GONE);
                                        } else {
                                            changeDocOpenButtonIcon(holder, "DOWNLOAD",
                                                    R.drawable.ic_arrow_downward_black_24dp, View.GONE);
                                        }
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (holder.openButton.getTag().equals("OPEN")) {
                        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/Download/" + fileDetails[0];
                        File file = new File(filePath);
                        Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
                        pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file);
                            mContext.grantUriPermission(mContext.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            pdfOpenIntent
                                    .setDataAndType(uri, "application/pdf")
                                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            pdfOpenIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        }
                        try {
                            mContext.startActivity(pdfOpenIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(mContext, "Couldn't find an application", Toast.LENGTH_SHORT).show();
                        }
                    } else if (holder.openButton.getTag().equals("DOWNLOADING")) {
                        Toast.makeText(mContext, "Downloading..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (chat.getType() == TYPE_AUDIO) {
            holder.docContainer.setVisibility(View.GONE);
            holder.message.setVisibility(View.GONE);
            holder.imageContainer.setVisibility(View.GONE);
            holder.audioContainer.setVisibility(View.VISIBLE);
            holder.mediaPlayer = new MediaPlayer();
            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Download/" + chat.getReference() + ".3gp";
            Log.d("FileNamePath", fileName);
            try {
                holder.mediaPlayer.setDataSource(fileName);
                holder.mediaPlayer.prepare();
                holder.playButtonIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                holder.playButtonIcon.setTag("PLAY");
                holder.duration.setText(Time.formateMilliSeccond(holder.mediaPlayer.getDuration()));
                holder.seekBar.setMax(holder.mediaPlayer.getDuration());
                holder.mediaPlayer.release();
            } catch (IOException e) {
                e.printStackTrace();
            }


            holder.playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.playButtonIcon.getTag().equals("DOWNLOAD")) {
                        FirebaseStorage mStorage = FirebaseStorage.getInstance();
                        StorageReference down = mStorage.getReferenceFromUrl(chat.getUrl());
                        try {
                            File localFile = new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS), chat.getReference() + ".3gp");
                            Log.d("Path", Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS).toString());
                            if (localFile.createNewFile()) {
                                down.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            changeAudioPlayerIcon(R.drawable.ic_play_arrow_black_24dp, holder, "PLAY");
                                        }
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (holder.playButtonIcon.getTag().equals("PLAY")) {
                        startPlaying(holder, chat.getReference());
                        changeAudioPlayerIcon(R.drawable.ic_stop_black_24dp, holder, "STOP");
                    } else if (holder.playButtonIcon.getTag().equals("STOP")) {
                        holder.mediaPlayer.release();
                        holder.timer.cancel();
                        holder.timer.purge();
                        holder.seekBar.setOnSeekBarChangeListener(null);
                        changeAudioPlayerIcon(R.drawable.ic_play_arrow_black_24dp, holder, "PLAY");
                    }
                }
            });
        } else {
            holder.imageContainer.setVisibility(View.GONE);
            holder.docContainer.setVisibility(View.GONE);
            holder.message.setVisibility(View.VISIBLE);
            holder.audioContainer.setVisibility(View.GONE);
        }

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onLongClick(View v) {
                if (chat.getSender().equals(MemorySupports.getUserInfo(mContext, KEY_ID))) {
                    final Dialog dialog = ViewSupports.materialDialog(mContext, CENTER, R.layout.message_metadata_dialog);
                    Button delete = dialog.findViewById(R.id.button_delete);
                    TextView isSent = dialog.findViewById(R.id.is_sent);
                    TextView isSeen = dialog.findViewById(R.id.is_seen);
                    TextView isDelivered = dialog.findViewById(R.id.is_delivered);
                    TextView message = dialog.findViewById(R.id.message);

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference("chats").child(chat.getReference());
                            holder.parent.setAnimation(AnimationUtils.loadAnimation(mContext,
                                    R.anim.error_dialog_exit_animation));
                            holder.parent.setVisibility(View.GONE);
                            notifyItemRemoved(position);
                            mChats.remove(position);
                            notifyItemRangeChanged(position, mChats.size());
                            reference.setValue(null);
                        }
                    });
                    if (chat.getIsSeen() != 1) {
                        isSeen.setText(Time.covertTimeToText(chat.getIsSeen()));
                    } else {
                        isSeen.setText("Not yet seen");
                    }
                    if (chat.getIsSent() != 1) {
                        isSent.setText(Time.covertTimeToText(chat.getIsSent()));
                    } else {
                        isSent.setText("Not yet sent");
                    }
                    if (chat.getIsDelivered() != 1) {
                        isDelivered.setText(Time.covertTimeToText(chat.getIsDelivered()));
                    } else {
                        isDelivered.setText("Not yet delivered");
                    }
                    message.setText(chat.getMessage());
                    dialog.show();
                    return true;
                }
                return false;
            }
        });

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.dateContainer.getVisibility() == View.VISIBLE) {
                    holder.dateContainer.setVisibility(View.GONE);
                    holder.dateContainer.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.snackbar_exit_animation));
                } else {
                    holder.dateContainer.setVisibility(View.VISIBLE);
                    holder.dateContainer.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.snackbar_enter_animation));
                }

            }
        });
    }

    private void syncDuration(final ViewHolder holder) {
        holder.timer = new Timer();
        holder.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                holder.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.seekBar.setProgress(holder.mediaPlayer.getCurrentPosition());
                        holder.duration.setText(Time.formateMilliSeccond(holder.mediaPlayer.getCurrentPosition()));
                        Log.d("MediaPlayer At", Time.formateMilliSeccond(holder.mediaPlayer.getCurrentPosition()));
                    }
                });
            }
        }, 0, 1000);
    }

    private void startPlaying(final ViewHolder holder, String key) {
        holder.mediaPlayer = new MediaPlayer();
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Download/" + key + ".3gp";
        Log.d("FileNamePath", fileName);
        try {
            holder.mediaPlayer.setDataSource(fileName);
            holder.mediaPlayer.prepare();
            holder.duration.setText(Time.formateMilliSeccond(holder.mediaPlayer.getDuration()));
            holder.seekBar.setMax(holder.mediaPlayer.getDuration());
            holder.mediaPlayer.start();
            syncDuration(holder);

            holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    holder.mediaPlayer.pause();
                    changeAudioPlayerIcon(R.drawable.ic_pause_black_24dp, holder, "PLAY");
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    holder.mediaPlayer.seekTo(seekBar.getProgress());
                    holder.mediaPlayer.start();
                    changeAudioPlayerIcon(R.drawable.ic_stop_black_24dp, holder, "STOP");
                }
            });

            holder.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mp.isPlaying()) {
                        holder.mediaPlayer.release();
                        changeAudioPlayerIcon(R.drawable.ic_play_arrow_black_24dp, holder, "PLAY");
                        holder.timer.cancel();
                        holder.timer.purge();
                        holder.seekBar.setOnSeekBarChangeListener(null);
                    }
                }
            });
            changeAudioPlayerIcon(R.drawable.ic_stop_black_24dp, holder, "STOP");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeAudioPlayerIcon(int icon, ViewHolder holder, String tag) {
        holder.playButtonIcon.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
        holder.playButtonIcon.setVisibility(View.GONE);
        holder.playButtonIcon.setImageDrawable(mContext.getResources().getDrawable(icon));
        holder.playButtonIcon.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
        holder.playButtonIcon.setVisibility(View.VISIBLE);
        holder.playButtonIcon.setTag(tag);
    }

    private void changeDocOpenButtonIcon(ViewHolder holder, String tag, int drawable, int progressVisible) {
        holder.openButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
        holder.openButton.setVisibility(View.GONE);
        holder.openButton.setImageResource(drawable);
        holder.openButton.setTag(tag);
        holder.loading.setVisibility(progressVisible);
        holder.openButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
        holder.openButton.setVisibility(View.VISIBLE);
    }
    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mChats.get(position).getReceiver().equals(fUser))
            return CHAT_TYPE_LEFT;
        else return CHAT_TYPE_RIGHT;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time, date;
        private EmojiTextView message;
        private ImageView status;
        private LinearLayout container, parent;
        private LinearLayout dateContainer;
        private CircleImageView profileImage;
        private CardView imageContainer;
        private ImageView imageView;
        private ProgressBar loading;
        private ImageButton openButton;
        private TextView fileName, fileSize;
        private RelativeLayout docContainer, audioContainer;
        private SeekBar seekBar;
        private ImageView playButtonIcon;
        private RelativeLayout playButton;
        private TextView duration;
        private MediaPlayer mediaPlayer;
        private Timer timer;
        private Handler handler = new Handler(Looper.getMainLooper());

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            container = itemView.findViewById(R.id.container);
            imageContainer = itemView.findViewById(R.id.image_container);
            imageView = itemView.findViewById(R.id.imageView);
            parent = itemView.findViewById(R.id.parent);
            dateContainer = itemView.findViewById(R.id.date_container);
            profileImage = itemView.findViewById(R.id.profile_image);
            loading = itemView.findViewById(R.id.progress);
            fileName = itemView.findViewById(R.id.file_name);
            fileSize = itemView.findViewById(R.id.file_size);
            openButton = itemView.findViewById(R.id.open_button_icon);
            docContainer = itemView.findViewById(R.id.attach_container);
            seekBar = itemView.findViewById(R.id.progressBar);
            playButtonIcon = itemView.findViewById(R.id.play_button_icon);
            playButton = itemView.findViewById(R.id.play_button);
            duration = itemView.findViewById(R.id.duration);
            audioContainer = itemView.findViewById(R.id.audio_container);
        }
    }
}
