package com.nightcoder.ilahianz.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.Gravity.CENTER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
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
            holder.message.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(chat.getUrl()).into(holder.imageView);
        } else if (chat.getType() == TYPE_DOC) {
            holder.docContainer.setVisibility(View.VISIBLE);
            final String[] fileDetails = chat.getMessage().split(",");
            holder.fileName.setText(fileDetails[1]);
            holder.fileSize.setText(fileDetails[0]);

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
                                    Environment.DIRECTORY_DOWNLOADS), fileDetails[1]);
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
                                + "/Download/" + fileDetails[1];
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
        } else {
            holder.imageContainer.setVisibility(View.GONE);
            holder.docContainer.setVisibility(View.GONE);
            holder.message.setVisibility(View.VISIBLE);
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
        private RelativeLayout docContainer;

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
        }
    }
}
