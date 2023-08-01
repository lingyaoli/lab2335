package algonquin.cst2335.li000808;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.li000808.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.li000808.databinding.ReceiveMessageBinding;
import algonquin.cst2335.li000808.databinding.SentMessageBinding;
import data.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {

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
        if (messages == null) {
            messages = new ArrayList<>();
            chatModel.messages.postValue(messages);
        }
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);
// Create the database instance and initialize the DAO
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {

            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue); //newValue is the newly set ChatMessage
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.addToBackStack("null")
                    .replace(R.id.fragmentLocation, chatFragment)
                    .commit();
        });

        if(messages == null) {

            messages = new ArrayList<>();
            chatModel.messages.postValue(messages);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()->{

                List<ChatMessage> fromDatabase = mDAO.getAllMessages();
                messages.addAll(fromDatabase);

                // update the recycle view
                runOnUiThread(()->{
                    binding.recycleView.setAdapter(myAdapter);
                });
            });
        }

        binding.sendButton.setOnClickListener(click -> {
            String message = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage newMessage = new ChatMessage(message, currentDateandTime, true);
            messages.add(newMessage);
            chatModel.messages.setValue(messages);
            myAdapter.notifyItemInserted(messages.size() - 1);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                newMessage.id = (int) mDAO.insertMessage(newMessage);
            });
            binding.textInput.setText("");
        });


        binding.receiveButton.setOnClickListener(click -> {
            String message = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage newMessage = new ChatMessage(message, currentDateandTime, false);
            messages.add(newMessage);
            chatModel.messages.setValue(messages);
            myAdapter.notifyItemInserted(messages.size() - 1);
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                newMessage.id = (int) mDAO.insertMessage(newMessage);
            });

            binding.textInput.setText("");
        });

        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
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


        };
        binding.recycleView.setAdapter(myAdapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_1) {
            // Handle the action when "item_1" (garbage can icon) is clicked
            // Show an alert dialog asking the user if they want to delete the message
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this message?")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // Code to delete the selected ChatMessage
                        int position = myAdapter.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            ChatMessage selectedMessage = messages.get(position);
                            messages.remove(selectedMessage);
                            chatModel.messages.setValue(messages);
                            myAdapter.notifyItemRemoved(position);
                        }
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        // User canceled the dialog, do nothing
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (item.getItemId() == R.id.item_about) {
            // Handle the action when "item_about" (About) is clicked
            // Show a toast with version and creator information
            String version = "Version 1.0";
            String creator = "YourName"; // Replace "YourName" with your actual name
            String toastMessage = version + ", created by " + creator;
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        }

        return true;
    }




    public class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText=itemView.findViewById(R.id.message);
            timeText=itemView.findViewById(R.id.time);

            itemView.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);
            });

        }
    }

}
