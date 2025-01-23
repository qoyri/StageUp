package com.example.stageup.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stageup.R;
import com.example.stageup.data.model.RootResponse;
import com.example.stageup.data.network.MessagingService;
import com.example.stageup.data.network.RetrofitClient;
import com.example.stageup.data.model.Conversation;
import com.example.stageup.ui.message.MessagingActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.stageup.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Conversation> conversations = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, container, false);

        // Setup RecyclerView
        recyclerView = root.findViewById(R.id.recyclerViewConversations);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MessageAdapter(conversations, conversation -> {
            // Handle click, navigate to MessagingActivity
            Intent intent = new Intent(requireContext(), MessagingActivity.class);
            intent.putExtra("conversationId", conversation.getConversationId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Load conversations
        loadConversations();

        return root;
    }

    private void loadConversations() {
        MessagingService service = RetrofitClient.getInstance().create(MessagingService.class);

        String token = new PreferenceManager(requireContext()).getAuthToken();

        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "Authentication token not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<RootResponse> call = service.getConversations("Bearer " + token);
        call.enqueue(new Callback<RootResponse>() {
            @Override
            public void onResponse(@NonNull Call<RootResponse> call, @NonNull Response<RootResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Conversation> conversationList = response.body().getValues();

                    if (conversationList == null || conversationList.isEmpty()) {
                        Toast.makeText(requireContext(), "No conversations found.", Toast.LENGTH_SHORT).show();
                    } else {
                        conversations.clear();
                        conversations.addAll(conversationList); // Ajout des conversations correctes
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch conversations: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}