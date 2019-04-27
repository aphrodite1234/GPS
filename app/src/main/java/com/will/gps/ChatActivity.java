package com.will.gps;

import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.will.gps.adapter.ChatMessageAdapter;
import com.will.gps.bean.ChatEntity;

import java.util.List;

/**
 * Created by MaiBenBen on 2019/4/27.
 */

public class ChatActivity {
    private int friendId;
    private String friendName;
    private ListView chatMeessageListView;
    private ChatMessageAdapter chatMessageAdapter;
    private Button sendButton;
    private ImageButton emotionButton;
    private EditText inputEdit;
    private List<ChatEntity> chatList;
    private Handler handler;


}
