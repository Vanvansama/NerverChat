package com.example.liufan.nerverchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by LIUFAN on 2017/5/10.
 */

public class ChatLayoutAdapter extends RecyclerView.Adapter<ChatLayoutAdapter.BaseAdapter> {

    private ArrayList<ItemModel> datalist = new ArrayList();

    public void replaceAll (ArrayList<ItemModel> list){
        datalist.clear();
        if(list != null && list.size()>0){
            datalist.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ItemModel> list) {
        if (datalist != null && list != null) {
            datalist.addAll(list);
            notifyItemRangeChanged(datalist.size(), list.size());
        }
    }

    @Override
    public ChatLayoutAdapter.BaseAdapter onCreateViewHolder(ViewGroup parent,int viewType){
        switch(viewType){
            case ItemModel.CHAT_A:
                return new ChatFromViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_from, parent, false));
            case ItemModel.CHAT_B:
                return new ChatToViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_to, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatLayoutAdapter.BaseAdapter holder, int position){
        holder.setData(datalist.get(position).object);
    }

    @Override
    public int getItemViewType(int position) {
        return datalist.get(position).type;
    }

    @Override
    public int getItemCount() {
        return datalist != null ? datalist.size() : 0;
    }

    public class BaseAdapter extends RecyclerView.ViewHolder {

        public BaseAdapter(View itemView) {
            super(itemView);
        }
        void setData(Object object) {

        }
    }

    private class ChatFromViewHolder extends BaseAdapter{
        private ImageView ic_user;
        private TextView tv_Message;

        public ChatFromViewHolder(View view){
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv_Message = (TextView) itemView.findViewById(R.id.tvMessage);
        }

        @Override
        void setData(Object object){
            super.setData(object);
            ChatModel model = (ChatModel) object;
            Picasso.with(itemView.getContext()).load(model.getIcon()).placeholder(R.mipmap.ic_launcher).into(ic_user);
            tv_Message.setText(model.getContent());        }
    }

    private class ChatToViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv_Message;

        public ChatToViewHolder(View view) {
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv_Message = (TextView) itemView.findViewById(R.id.tvMessage);

        }

        @Override
        void setData(Object object) {
            super.setData(object);
            ChatModel model = (ChatModel) object;
            Picasso.with(itemView.getContext()).load(model.getIcon()).placeholder(R.mipmap.ic_launcher).into(ic_user);
            tv_Message.setText(model.getContent());
        }
    }
}
