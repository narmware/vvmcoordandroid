package com.narmware.vvmcoordinator.adapter;

import android.app.Notification;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.vvmcoordinator.R;
import com.narmware.vvmcoordinator.db.RealmController;
import com.narmware.vvmcoordinator.fragment.NotificationFragment;
import com.narmware.vvmcoordinator.pojo.NotificationItems;
import com.narmware.vvmcoordinator.pojo.SchoolDetails;
import com.narmware.vvmcoordinator.realm_classes.RealmRecyclerViewAdapter;
import com.narmware.vvmcoordinator.support.Constants;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rohitsavant on 20/09/18.
 */

public class NotificationsAdapter extends RealmRecyclerViewAdapter<NotificationItems> {

    private static Context context;
    private static Realm realm;

    public NotificationsAdapter(Context context) {
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_msg_type, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder1, final int position) {
        realm = RealmController.getInstance().getRealm();
        //final RealmResults<NotificationItems> results = realm.where(NotificationItems.class).findAll();

        final NotificationItems notificationDetails = getItem(position);
        final MyViewHolder holder= (MyViewHolder) viewholder1;

        holder.mTitle.setText(notificationDetails.getNot_title());
        holder.mDesc.setText(notificationDetails.getNot_msg());
        holder.mDate.setText(notificationDetails.getNot_date());

        if(notificationDetails.isRead()==true)
        {
            holder.mTitle.setTextColor(context.getResources().getColor(R.color.grey_600));
            holder.mDesc.setTextColor(context.getResources().getColor(R.color.grey_600));
            holder.mDate.setTextColor(context.getResources().getColor(R.color.grey_600));
        }
        holder.mItem=notificationDetails;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTitle,mDesc, mDate;
        NotificationItems mItem;
            View view;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTitle=itemView.findViewById(R.id.txt_title);
            mDesc=itemView.findViewById(R.id.txt_desc);
            mDate=itemView.findViewById(R.id.txt_date);

            view=itemView;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationItems results = realm.where(NotificationItems.class).equalTo("not_id",mItem.getNot_id()).findFirst();
                    realm.beginTransaction();
                    results.setRead(true);
                    realm.commitTransaction();

                    mTitle.setTextColor(context.getResources().getColor(R.color.grey_600));
                    mDesc.setTextColor(context.getResources().getColor(R.color.grey_600));
                    mDate.setTextColor(context.getResources().getColor(R.color.grey_600));

                }
            });
        }
    }

    @Override
    public int getItemCount() {

        if (getRealmAdapter() != null) {

            if(getRealmAdapter().getCount()==0)
            {
                NotificationFragment.mEmptyLinear.setVisibility(View.VISIBLE);
            }
            else{
                NotificationFragment.mEmptyLinear.setVisibility(View.INVISIBLE);
            }
            return getRealmAdapter().getCount();
        }
        return 0;
    }
}
