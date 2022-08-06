package com.finalyearproject.daretodonate.Activities;

import static com.finalyearproject.daretodonate.Models.BotMessageModel.viewType.RECEIVE_MESSAGE;
import static com.finalyearproject.daretodonate.Models.BotMessageModel.viewType.SEND_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.finalyearproject.daretodonate.Adapters.BotMessageAdapter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Models.BotMessageModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Utils.D2DBackend;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText questionTextField;
    private Button askBtn;

    private RecyclerView chatRecyclerView;
    private List<BotMessageModel> botMessageModelList;
    private BotMessageAdapter botMessageAdapter;
    private JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        questionTextField = findViewById(R.id.question);
        askBtn = findViewById(R.id.ask_btn);

        botMessageModelList = new ArrayList<>();
        botMessageModelList.add(new BotMessageModel(RECEIVE_MESSAGE, "Hi! I'm RaktBot."));
        botMessageModelList.add(new BotMessageModel(RECEIVE_MESSAGE, "I am here to help you with blood search, blood donation FAQ's and anything about E-RaktKosh."));
        botMessageModelList.add(new BotMessageModel(RECEIVE_MESSAGE, "Type 'help' for help and 'contact' for contact details."));
        data = new JSONObject();
        botMessageAdapter = new BotMessageAdapter(botMessageModelList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chatRecyclerView.setAdapter(botMessageAdapter);

        askBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botMessageModelList.add(new BotMessageModel(SEND_MESSAGE, questionTextField.getText().toString()));
                try {
                    data.put("question", botMessageModelList.get(botMessageModelList.size()-1).getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                questionTextField.setText("");
                botMessageAdapter.notifyDataSetChanged();
                chatRecyclerView.smoothScrollToPosition(botMessageModelList.size()-1);
                new D2DBackend(ChatActivity.this, new BackendResponseListener() {
                    @Override
                    public void backendResponse(boolean isError, JSONObject response) {
                        try {
                            botMessageModelList.add(new BotMessageModel(RECEIVE_MESSAGE, response.getString("message")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        botMessageAdapter.notifyDataSetChanged();
                        chatRecyclerView.smoothScrollToPosition(botMessageModelList.size()-1);
                    }
                }).postRequest(getResources().getString(R.string.api_bot), data, new JSONObject());
            }
        });

    }
}