package algonquin.cst2335.li000808;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.li000808.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.li000808.databinding.ReceiveMessageBinding;
import algonquin.cst2335.li000808.databinding.SentMessageBinding;
import data.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText=itemView.findViewById(R.id.message);
            timeText=itemView.findViewById(R.id.time);

            itemView.setOnClickListener(clk ->{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                    builder.setTitle("Question:")
                            .setMessage("Do you want to delete the message: " + messageText.getText())
                            .setPositiveButton("Yes", (dialog, cl) -> {
                                int position = getAbsoluteAdapterPosition();
                                ChatMessage m = messages.get(position);
                                Executor thread3 = Executors.newSingleThreadExecutor();
                                thread3.execute(() -> {
                                    mDAO.deleteMessage(m);
                                    messages.remove(position);
                                    runOnUiThread(() ->{
                                        myAdapter.notifyItemRemoved(position);
                                    });
                                });
                                ChatMessage removedMessage = messages.get(position);
                                Snackbar.make(messageText,"You deleted message #"+ position, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", click -> {
                                            messages.add(position,removedMessage);
                                            myAdapter.notifyItemInserted(position);
                                        })
                                        .show();
                            })
                            .setNegativeButton("No", (dialog, cl) -> {
                            })
                            .create()
                            .show();
                    });

        }
    }
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;
    ChatRoomViewModel chatModel ;
    private ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if(messages == null)
        {
//            messages = new ArrayList<>();
//            chatModel.messages.postValue(messages);
            chatModel.messages.postValue(messages = new ArrayList<>());
        }
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
// Create the database instance and initialize the DAO
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        binding.sendButton.setOnClickListener(click -> {
            String message = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            ChatMessage chatMessage = new ChatMessage(message, currentDateandTime, true);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            // Insert the ChatMessage into the database
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);
            });
            // Clear previous text:
            binding.textInput.setText("");
        });


        binding.receiveButton.setOnClickListener(click -> {
            String message = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            ChatMessage chatMessage = new ChatMessage(message, currentDateandTime, false);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);

            // Clear previous text:
            binding.textInput.setText("");
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
                holder.timeText.setText(chatMessage.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage chatMessage = messages.get(position);
                if (chatMessage.isSentButton()) {
                    return 0; // Sent message
                } else {
                    return 1; // Received message
                }
            }


        });
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }
    }
}