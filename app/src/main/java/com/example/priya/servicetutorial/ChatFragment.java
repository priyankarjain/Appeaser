package com.example.priya.servicetutorial;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by priya on 1/17/2017.
 */

public class ChatFragment extends Fragment {
    private String source;
    private EditText et_msg;
    private Button bt_send;
    private RecyclerView chats_section;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SendMessageListener sendMessageListener;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private ArrayList<ChatModel> modelmessages;
    private ChatDataSource chatDataSource;

    public void setChatDataSource(ChatDataSource dataSource){
        this.chatDataSource = dataSource;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendMessageListener = (SendMessageListener) context;
        }catch (ClassCastException c){
            throw new ClassCastException(context.toString()+"must");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_fragment,container,false);
        et_msg = (EditText) rootView.findViewById(R.id.ed_msg);
        bt_send = (Button) rootView.findViewById(R.id.bt_send);
        chats_section  = (RecyclerView) rootView.findViewById(R.id.chats_section);
        modelmessages = (ArrayList<ChatModel>) chatDataSource.getAllChats(this.source);
        adapter = new MyAdapter();
        layoutManager = new LinearLayoutManager(getContext());
        chats_section.setLayoutManager(layoutManager);
        chats_section.setAdapter(adapter);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_msg.getText().toString();
                //int size = messages.size();
                int size = modelmessages.size();
                messages.add(new ChatMessage(s,"SEND"));
                ChatModel cm = chatDataSource.createChat(source,"SEND",s);
                modelmessages.add(cm);
                adapter.notifyItemInserted(size);
                chats_section.scrollToPosition(size);
                et_msg.setText("");
                Log.v("MSG",s);
                Log.v("Source",source);
                sendMessageListener.onSendMessage(ChatFragment.this.source,s);
            }
        });
        return rootView;
    }

    public void setSource(String source){
        this.source = source;
        Log.v("Source set",source);
    }

    public void receiveMessages(String message){
        int size = messages.size();
        messages.add(new ChatMessage(message,"RECV"));
        adapter.notifyItemInserted(size);
        chats_section.scrollToPosition(size);
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private class SendViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            public SendViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.txtMsg);
            }
        }

        private class RecvViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            public RecvViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.txtMsgRecv);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View rootView;
            switch (viewType){
                case 0:
                    rootView = inflater.inflate(R.layout.message_you,parent,false);
                    viewHolder = new RecvViewHolder(rootView);
                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.message_me,parent,false);
                    viewHolder = new SendViewHolder(rootView);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()){
                case 0:
                    RecvViewHolder viewHolder = (RecvViewHolder) holder;
                    //viewHolder.tv.setText(messages.get(position).message);
                    viewHolder.tv.setText(modelmessages.get(position).getMessage());
                    break;
                case 1:
                    SendViewHolder viewHolder1 = (SendViewHolder) holder;
                    //viewHolder1.tv.setText(messages.get(position).message);
                    viewHolder1.tv.setText(modelmessages.get(position).getMessage());
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            //ChatMessage chatMessage = messages.get(position);
            ChatModel cm = modelmessages.get(position);
            if(cm.getType().equals("RECV")){
                return 0;
            }else{
                return 1;
            }

            /*if(chatMessage.type.equals("RECV")){
                return 0;
            }else{
                return 1;
            }*/
        }

        @Override
        public int getItemCount() {
            return modelmessages.size();//messages.size();
        }
    }
}
