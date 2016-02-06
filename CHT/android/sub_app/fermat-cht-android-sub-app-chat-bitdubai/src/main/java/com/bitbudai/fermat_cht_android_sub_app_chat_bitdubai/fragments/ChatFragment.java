package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.adapters.ChatAdapter;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.adapters.ChatAdapterView;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.models.ChatMessage;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.sessions.ChatSession;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.settings.ChatSettings;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;
import com.bitdubai.fermat_cht_api.all_definition.enums.ChatStatus;
import com.bitdubai.fermat_cht_api.all_definition.enums.MessageStatus;
import com.bitdubai.fermat_cht_api.all_definition.enums.TypeMessage;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantGetMessageException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantGetNetworkServicePublicKeyException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantSaveChatException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantSaveMessageException;
import com.bitdubai.fermat_cht_api.layer.middleware.interfaces.Contact;
import com.bitdubai.fermat_cht_api.layer.middleware.utils.ChatImpl;
import com.bitdubai.fermat_cht_api.layer.middleware.utils.MessageImpl;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatModuleManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


//import android.widget.LinearLayout;
//import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
//import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
//import com.bitdubai.fermat_cht_api.layer.chat_module.interfaces.ChatModuleManager;
//import com.bitdubai.fermat_cht_api.layer.platform_service.error_manager.interfaces.ErrorManager;

/**
 * Chat Fragment
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 30/12/15.
 * @version 1.0
 * Update by Miguel Payarez on 15/01/2016
 */

public class ChatFragment extends AbstractFermatFragment {//ActionBarActivity

    // Fermat Managers
    private ChatManager chatManager;
    private ChatModuleManager moduleManager;
    private ErrorManager errorManager;
    private SettingsManager<ChatSettings> settingsManager;
    private ChatSession chatSession;


    ArrayList<String> historialmensaje = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    Map<String,Contact>others=new HashMap<String,Contact>();
    Map<String,Contact>me=new HashMap<String,Contact>();
    String TAG="CHT_ChatFragment";

    boolean chatwascreate=false;
    UUID contactid;
    UUID chatid;
    String remotepk;
    PlatformComponentType remotepct;
    List<String> chatmessages = new ArrayList<>();

    private EditText messageET;
    private ListView messagesContainer;
    public Button sendBtn;
    private ChatAdapter adapter;
    public ArrayList<ChatMessage> chatHistory;



    public static ChatFragment newInstance() { return new ChatFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            chatSession = ((ChatSession) appSession);
            moduleManager = chatSession.getModuleManager();
            chatManager = moduleManager.getChatManager();
            errorManager = appSession.getErrorManager();
            whattodo();
        } catch (Exception e) {
            if (errorManager != null)
                errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }
    }

    void findvalues(Contact contact){ //With contact Id find chatid,pkremote,actortype

        try {
            remotepk = contact.getRemoteActorPublicKey();
            remotepct = contact.getRemoteActorType();

           /* for (int i=0; i<chatManager.getContacts().size();i++){
                if(contactid.equals(chatManager.getContacts().get(i).getContactId())){
                    remotepk=chatManager.getContacts().get(i).getRemoteActorPublicKey();
                    remotepct=chatManager.getContacts().get(i).getRemoteActorType();
                }
            }*/
            for (int i = 0; i < chatManager.getMessages().size(); i++) {
                if (contactid.equals(chatManager.getMessages().get(i).getContactId())) {
                    chatid = chatManager.getMessages().get(i).getChatId();
                }
            }

        }catch (CantGetMessageException e) {
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }catch(Exception e){
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);

        }

    }


    void whattodo(){
        try {
            Contact con = chatSession.getSelectedContact();
            contactid = con.getContactId();
            if (appSession.getData("whocallme").equals("chatlist")) {

                contactid = (UUID) appSession.getData("contactid");

                findvalues(con);//findvalues(contactid);
                chatwascreate = true;
            } else if (appSession.getData("whocallme").equals("contact")) {  //fragment contact call this fragment
                findvalues(con);
                if (chatid != null) {
                    chatwascreate = true;
                } else {
                    chatwascreate = false;
                }
            }

        }catch(Exception e){
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);

        }
    }

    void findmessage(){
        String message;
        String inorout;
        int messsize;

        try {
            historialmensaje.clear();
            if(chatid!=null){
            messsize=chatManager.getMessageByChatId(chatid).size();
            for (int i = 0; i < messsize; i++) {
                message=chatManager.getMessageByChatId(chatid).get(i).getMessage();
                inorout=chatManager.getMessageByChatId(chatid).get(i).getType().toString();
                historialmensaje.add(inorout+"@#@#"+message);

            }
            }else{
                    Toast.makeText(getActivity(),"chatid null", Toast.LENGTH_SHORT).show();
            }

        }catch (CantGetMessageException e) {

            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }catch (Exception e){
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {//private void initControls() {}

        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.chat, container, false);

        messagesContainer = (ListView) layout.findViewById(R.id.messagesContainer);
        messageET = (EditText) layout.findViewById(R.id.messageEdit);
        sendBtn = (Button) layout.findViewById(R.id.chatSendButton);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        final ChatAdapterView adaptador;
   //   final FermatTextView meLabel = (FermatTextView) layout.findViewById(R.id.meLbl);
  //    FermatTextView companionLabel = (FermatTextView) layout.findViewById(R.id.friendLabel);
        RelativeLayout contain = (RelativeLayout) layout.findViewById(R.id.container);
 //     companionLabel.setText("My Contact");
        ListView lstOpciones;


        findmessage();
        adaptador = new ChatAdapterView(getActivity(), historialmensaje);
        lstOpciones = (ListView) layout.findViewById(R.id.messagesContainer);
        lstOpciones.setAdapter(adaptador);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                try {
                    ChatImpl chat=new ChatImpl();
                    MessageImpl message=new MessageImpl();
                    Long dv=System.currentTimeMillis();


                   if(chatwascreate) {

                       chat.setChatId(chatid);
                       chat.setObjectId(UUID.randomUUID());
                       chat.setStatus(ChatStatus.VISSIBLE);
                       chat.setChatName("DeathNote");
                       chat.setDate(new Timestamp(dv));
                       chat.setLastMessageDate(new Timestamp(dv));
                       chat.setLocalActorPublicKey(chatManager.getNetworkServicePublicKey());
                       chat.setLocalActorType(PlatformComponentType.ACTOR_ASSET_ISSUER);
                       chat.setRemoteActorPublicKey(remotepk);
                       chat.setRemoteActorType(remotepct);


                       message.setChatId(chatid);
                       message.setMessageId(UUID.randomUUID());
                       message.setMessage(messageText);
                       message.setMessageDate(new Timestamp(dv));
                       message.setStatus(MessageStatus.CREATED);
                       message.setType(TypeMessage.OUTGOING);
                       message.setContactId(contactid);
                       chatManager.saveMessage(message);

                       findmessage();
                       adaptador.refreshEvents(historialmensaje);
                       Toast.makeText(getActivity(),"Message Created", Toast.LENGTH_SHORT).show();

                   }else{

                       UUID newchatid=UUID.randomUUID();

                       chat.setChatId(newchatid);
                       chat.setObjectId(UUID.randomUUID());
                       chat.setStatus(ChatStatus.VISSIBLE);
                       chat.setChatName("DeathNote");
                       chat.setDate(new Timestamp(dv));
                       chat.setLastMessageDate(new Timestamp(dv));
                       chat.setLocalActorPublicKey(chatManager.getNetworkServicePublicKey());
                       chat.setLocalActorType(PlatformComponentType.ACTOR_ASSET_ISSUER);
                       chat.setRemoteActorPublicKey(remotepk);
                       chat.setRemoteActorType(remotepct);

                       chatManager.saveChat(chat);

                       message.setChatId(newchatid);
                       message.setMessageId(UUID.randomUUID());
                       message.setMessage(messageText);
                       message.setMessageDate(new Timestamp(dv));
                       message.setStatus(MessageStatus.CREATED);
                       message.setType(TypeMessage.OUTGOING);
                       message.setContactId(contactid);

                       chatManager.saveMessage(message);

                       findmessage();

                       adaptador.refreshEvents(historialmensaje);
                       Toast.makeText(getActivity(),"Sending message", Toast.LENGTH_SHORT).show();
                       messageET.setText("");


                   }
                } catch (CantSaveMessageException e) {

                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                } catch (CantSaveChatException e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                } catch (CantGetNetworkServicePublicKeyException e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                } catch (Exception e){
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);

                }
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                        try {
                            //System.out.println("Threar UI corriendo");
                            //TODO: fix this
                                findmessage();
                                adaptador.refreshEvents(historialmensaje);
                        } catch (Exception e) {

                            //TODO: fix this
                            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);

                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });
        return layout;
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

}
