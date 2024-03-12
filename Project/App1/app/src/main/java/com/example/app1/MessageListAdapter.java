package com.example.app1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app1.model.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageModel> messeges;
    String userid;
    public MessageListAdapter(Context context,ArrayList<MessageModel> messeges,String userid){
        this.context=context;
        this.messeges=messeges;
        this.userid=userid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        if(viewType==1){
            View v= LayoutInflater.from(context)
                    .inflate(R.layout.samplesendermsgdesign,parent,false);
            return new SenderViewHolder(v);
        }
        else{
             View v=LayoutInflater.from(context)
                     .inflate(R.layout.samplereceivermsgdesign,parent,false);
             return new ReceiverViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder) holder).
                    txt_sendermsg.setText(messeges.get(position).message);
            ((SenderViewHolder) holder).
                    txt_sendermsgtime.setText(messeges.get(position).time);

            //txt_sendermsg.setText(messeges.get(position).message);
            //txt_sendermsgtime.setText(messeges.get(position).time);
        }
        else{

            ((ReceiverViewHolder) holder).
                    txt_receivermsg.setText(messeges.get(position).message);
            ((ReceiverViewHolder) holder).
                    txt_receivermsgtime.setText(messeges.get(position).time);

            //txt_receivermsg.setText(messeges.get(position).message);
            //txt_receivermsgtime.setText(messeges.get(position).time);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Confirmation!");
                alert.setMessage("Delete Message?");
                alert.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().
                                child("message").child(userid).child(messeges.get(position).id)
                                 .setValue(null);
                        dialogInterface.dismiss();
                    }
                });
                alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                     dialogInterface.dismiss();
                    }
                });
                alert.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return messeges.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messeges.get(position).senderid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return 1;
        }
        else {
            return 2;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView txt_sendermsg,txt_sendermsgtime;

        public SenderViewHolder(@NonNull View itemView) {

            super(itemView);
            txt_sendermsg=itemView.findViewById(R.id.txt_sendermsg);
            txt_sendermsgtime=itemView.findViewById(R.id.txt_sendermsgtime);

        }
    }
    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView txt_receivermsg,txt_receivermsgtime;
        public ReceiverViewHolder(@NonNull View itemView) {

            super(itemView);
            txt_receivermsg=itemView.findViewById(R.id.txt_receivermsg);
            txt_receivermsgtime=itemView.findViewById(R.id.txt_receivermsgtime);
        }
    }
}
