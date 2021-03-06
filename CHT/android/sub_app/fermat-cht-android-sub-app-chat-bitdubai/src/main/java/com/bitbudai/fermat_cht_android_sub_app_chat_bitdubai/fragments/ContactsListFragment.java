package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.fragments;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.adapters.ContactListAdapter;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.sessions.ChatSession;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.settings.ChatSettings;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.util.ChtConstants;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.util.cht_dialog_connections;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;
import com.bitdubai.fermat_cht_api.layer.identity.interfaces.ChatIdentity;
import com.bitdubai.fermat_cht_api.layer.middleware.interfaces.ChatUserIdentity;
import com.bitdubai.fermat_cht_api.layer.middleware.interfaces.Contact;
import com.bitdubai.fermat_cht_api.layer.middleware.utils.ContactImpl;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatActorCommunityInformation;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatActorCommunitySelectableIdentity;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatModuleManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatPreferenceSettings;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Contact List fragment
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 05/01/16
 * @version 1.0
 *
 */
public class ContactsListFragment extends AbstractFermatFragment implements ContactListAdapter.AdapterCallback, cht_dialog_connections.AdapterCallbackContacts {

//    // Bundle key for saving previously selected search result item
//    //private static final String STATE_PREVIOUSLY_SELECTED_KEY =      "SELECTED_ITEM";
    private ContactListAdapter adapter; // The main query adapter
    //private ImageLoader mImageLoader; // Handles loading the contact image in a background thread
//    private String mSearchTerm; // Stores the current search query term
//
//    //private OnContactsInteractionListener mOnContactSelectedListener;
//
//    // Stores the previously selected search item so that on a configuration change the same item
//    // can be reselected again
//    private int mPreviouslySelectedSearchItem = 0;
// public ArrayList<ContactList> contactList;
    public List<Contact> contacts;
//    private ListView contactsContainer;
    //private Contact contactl;//= new Contact();
//
//    // Whether or not the search query has changed since the last time the loader was refreshed
//    private boolean mSearchQueryChanged;

    // Whether or not this fragment is showing in a two-pane layout
    //private boolean mIsTwoPaneLayout;

    // Whether or not this is a search result view of this fragment, only used on pre-honeycomb
    // OS versions as search results are shown in-line via Action Bar search from honeycomb onward
    //private boolean mIsSearchResultView = false;
    private ChatManager chatManager;
    //private ChatModuleManager moduleManager;
    private ErrorManager errorManager;
    private SettingsManager<ChatSettings> settingsManager;
    private ChatSession chatSession;
    private ChatPreferenceSettings chatSettings;
    //private Toolbar toolbar;
    ListView list;
    // Defines a tag for identifying log entries
    String TAG="CHT_ContactsListFragment";
    ArrayList<String> contactname=new ArrayList<>();
    ArrayList<Bitmap> contacticon=new ArrayList<>();
    ArrayList<String> contactid=new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView noData;
    View layout;
    TextView noDatalabel;
    private static final int MAX = 20;
    private int offset = 0;
    private SearchView searchView;

    public static ContactsListFragment newInstance() {
        return new ContactsListFragment();}

    @Override
    public void onMethodCallback() {//solution to access to another activity clicking the photo icon of the list
        changeActivity(Activities.CHT_CHAT_OPEN_CONTACT_DETAIL, appSession.getAppPublicKey());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            chatSession=((ChatSession) appSession);
            chatManager= chatSession.getModuleManager();
            //chatManager=moduleManager.getChatManager();
            errorManager=appSession.getErrorManager();
            //toolbar = getToolbar();
            //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.cht_ic_back_buttom));
            adapter=new ContactListAdapter(getActivity(), contactname, contacticon, contactid, chatManager,
                    null, errorManager, chatSession, appSession, this);
            chatSettings = null;

        }catch (Exception e)
        {
            if(errorManager!=null)
                errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT,UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,e);
        }
        try {
            chatSettings = chatManager.getSettingsManager().loadAndGetSettings(appSession.getAppPublicKey());
        }catch (Exception e) {
            chatSettings = null;
        }
        // Check if this fragment is part of a two-pane set up or a single pane by reading a
        // boolean from the application resource directories. This lets allows us to easily specify
        // which screen sizes should use a two-pane layout by setting this boolean in the
        // corresponding resource size-qualified directory.
        //mIsTwoPaneLayout = getResources().getBoolean(R.bool.has_two_panes);

        // Let this fragment contribute menu items
        //setHasOptionsMenu(true);

        // Create the main contacts adapter
        //adapter=new ContactListAdapter(getActivity(), contactname, contacticon, contactid);
//
//        if (savedInstanceState != null) {
//            // If we're restoring state after this fragment was recreated then
//            // retrieve previous search term and previously selected search
//            // result.
//            mSearchTerm = savedInstanceState.getString(SearchManager.QUERY);
//            mPreviouslySelectedSearchItem =
//                    savedInstanceState.getInt(STATE_PREVIOUSLY_SELECTED_KEY, 0);
//        }

        /*
         * An ImageLoader object loads and resizes an image in the background and binds it to the
         * QuickContactBadge in each item layout of the ListView. ImageLoader implements memory
         * caching for each image, which substantially improves refreshes of the ListView as the
         * user scrolls through it.
         *
         * To learn more about downloading images asynchronously and caching the results, read the
         * Android training class Displaying Bitmaps Efficiently.
         *
         * http://developer.android.com/training/displaying-bitmaps/
         */
//        mImageLoader = new ImageLoader(getContext(), getListPreferredItemHeight()) {
//            @Override
//            protected Bitmap processBitmap(Object data) {
//                // This gets called in a background thread and passed the data from
//                // ImageLoader.loadImage().
//                return loadContactPhotoThumbnail((String) data, getImageSize());
//            }
//        };
//
//        // Set a placeholder loading image for the image loader
//        mImageLoader.setLoadingImage(R.drawable.ic_contact_picture_holo_light);

        // Add a cache to the image loader
        //mImageLoader.addImageCache(getRuntimeManager(), 0.1f);

    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        long viewId = view.getId();
//        try{
//            if (viewId == R.id.icon) {
//                goToContactDetail(chatManager, moduleManager, chatSession,
//                        appSession, errorManager, contactid.get(position));
//            } else {
//                appSession.setData("whocallme", "contact");
//                appSession.setData(ChatSession.CONTACT_DATA, chatManager.getChatActorbyConnectionId(contactid.get(position)));
//                changeActivity(Activities.CHT_CHAT_OPEN_MESSAGE_LIST, appSession.getAppPublicKey());
//            }
//        }catch(CantGetContactException e) {
//            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
//        }catch (Exception e){
//            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
//        }
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = inflater.inflate(R.layout.contact_list_fragment, container, false);
        noData=(ImageView) layout.findViewById(R.id.nodata);
        noDatalabel = (TextView) layout.findViewById(R.id.nodatalabel);
        layout.setBackgroundResource(R.drawable.fondo);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        try {
            List <ChatActorCommunityInformation> con= chatManager
                    .listAllConnectedChatActor(chatManager.newInstanceChatActorCommunitySelectableIdentity(chatManager.
                            getIdentityChatUsersFromCurrentDeviceUser().get(0)), MAX, offset); //null;//chatManager.getContacts();
            if(con!=null){
                int size = con.size();
                if (size > 0) {
                    for (int i=0;i<size;i++){
                        contactname.add(con.get(i).getAlias());
                        contactid.add(con.get(i).getPublicKey());
                        ByteArrayInputStream bytes = new ByteArrayInputStream(con.get(i).getImage());
                        BitmapDrawable bmd = new BitmapDrawable(bytes);
                        contacticon.add(bmd.getBitmap());
                    }
                    noData.setVisibility(View.GONE);
                    noDatalabel.setVisibility(View.GONE);
                    ColorDrawable bgcolor = new ColorDrawable(Color.parseColor("#F9F9F9"));
                    layout.setBackground(bgcolor);
                }else{

                    noData.setVisibility(View.VISIBLE);
                    noDatalabel.setVisibility(View.VISIBLE);
                }
            }
        }catch (Exception e){
            if (errorManager != null)
                errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeActivity(Activities.CHT_CHAT_OPEN_CHATLIST, appSession.getAppPublicKey());
//            }
//        });

        adapter=new ContactListAdapter(getActivity(), contactname, contacticon, contactid, chatManager,
                null, errorManager, chatSession, appSession, this);
        list=(ListView)layout.findViewById(R.id.list);
        list.setAdapter(adapter);
        //registerForContextMenu(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()/*new AdapterView.OnItemClickListener()*/ {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //public void onClick(View view) {
                try {
                    //Object selection =  parent.getItemAtPosition(position);

                    //if (selection == R.id.icon) {
                    //    appSession.setData(ChatSession.CONTACT_DATA, chatManager.getChatActorbyConnectionId(contactid.get(position)));
                    //    changeActivity(Activities.CHT_CHAT_OPEN_CONTACT_DETAIL, appSession.getAppPublicKey());
                    //} else {
                    appSession.setData("whocallme", "contact");
                    //TODO: metodo nuevo que lo buscara del module del identity//chatManager.getChatUserIdentities();
//                    for (ChatActorCommunityInformation cont: chatManager.listAllConnectedChatActor(
//                            (ChatActorCommunitySelectableIdentity) chatManager.
//                                    getIdentityChatUsersFromCurrentDeviceUser().get(0), 2000, offset))
//                    {
                        //null;//chatManager.getContacts();
                        Contact contact=new ContactImpl();
                        contact.setRemoteActorPublicKey(contactid.get(position));
                        contact.setAlias(contactname.get(position));
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        contacticon.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        contact.setProfileImage(byteArray);
                        appSession.setData(ChatSession.CONTACT_DATA, contact);
                       // appSession.setData(ChatSession.CONTACT_DATA, contactid.get(position));//chatManager.getChatActorbyConnectionId(contactid.get(position)));
//                    }
                    changeActivity(Activities.CHT_CHAT_OPEN_MESSAGE_LIST, appSession.getAppPublicKey());
                    // }
                //} catch (CantGetContactException e) {
                //    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                } catch (Exception e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                }
            }
        });

        /*list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    appSession.setData(ChatSession.CONTACT_DATA, chatManager.getChatActorbyConnectionId(contactid.get(position)));
                    changeActivity(Activities.CHT_CHAT_OPEN_CONTACT_DETAIL, appSession.getAppPublicKey());
                }catch(CantGetContactException e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                }catch (Exception e){
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                }
                return true;
            }
        });*/

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                try {
                    Toast.makeText(getActivity(), "Contacts Updated", Toast.LENGTH_SHORT).show();
                    List<ChatActorCommunityInformation> con = chatManager
                            .listAllConnectedChatActor(chatManager.newInstanceChatActorCommunitySelectableIdentity(chatManager.
                                    getIdentityChatUsersFromCurrentDeviceUser().get(0)), MAX, offset); //null;//chatManager.getContacts();
                    if (con != null) {
                        int size = con.size();
                        if (size > 0) {
                            contactname.clear();
                            contactid.clear();
                            contacticon.clear();
                            for (int i = 0; i < con.size(); i++) {
                                contactname.add(con.get(i).getAlias());
                                contactid.add(con.get(i).getPublicKey());
                                ByteArrayInputStream bytes = new ByteArrayInputStream(con.get(i).getImage());
                                BitmapDrawable bmd = new BitmapDrawable(bytes);
                                contacticon.add(bmd.getBitmap());
                            }
                            final ContactListAdapter adaptador =
                                    new ContactListAdapter(getActivity(), contactname, contacticon, contactid, chatManager,
                                            null, errorManager, chatSession, appSession, null);
                            adaptador.refreshEvents(contactname, contacticon, contactid);
                            list.invalidateViews();
                            list.requestLayout();
                            noData.setVisibility(View.GONE);
                        } else {
                            noData.setVisibility(View.VISIBLE);
                        }
                    }else
                        noData.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                }
                mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2500);
            }
        });
        // Inflate the list fragment layout
        return layout;//return inflater.inflate(R.layout.contact_list_fragment, container, false);
    }

    @Override
    public void onMethodCallbackContacts() {//solution to access to update contacts view
        try {
            List <ChatActorCommunityInformation> con= chatManager
                    .listAllConnectedChatActor(
                            (ChatActorCommunitySelectableIdentity) chatManager.
                                    getIdentityChatUsersFromCurrentDeviceUser().get(0), MAX, offset); //null;//chatManager.getContacts();
            if (con.size() > 0) {
                contactname.clear();
                contactid.clear();
                contacticon.clear();
                for (int i=0;i<con.size();i++){
                    contactname.add(con.get(i).getAlias());
                    contactid.add(con.get(i).getPublicKey());
                    ByteArrayInputStream bytes = new ByteArrayInputStream(con.get(i).getImage());
                    BitmapDrawable bmd = new BitmapDrawable(bytes);
                    contacticon.add(bmd.getBitmap());
                }
                final ContactListAdapter adaptador =
                        new ContactListAdapter(getActivity(), contactname, contacticon, contactid, chatManager,
                                null, errorManager, chatSession, appSession, null);
                adaptador.refreshEvents(contactname, contacticon, contactid);
                list.invalidateViews();
                list.requestLayout();
            }} catch (Exception e) {
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        // Inflate the menu items
        inflater.inflate(R.menu.contact_list_menu, menu);
        // Locate the search item
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals(searchView.getQuery().toString())) {
                    adapter.getFilter().filter(s);
                }
                return false;
            }
        });
        if (chatSession.getData("filterString") != null) {
            String filterString = (String) chatSession.getData("filterString");
            if (filterString.length() > 0) {
                searchView.setQuery(filterString, true);
                searchView.setIconified(false);
            }
        }
        menu.add(0, ChtConstants.CHT_ICON_HELP, 0, "help").setIcon(R.drawable.ic_menu_help_cht)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == ChtConstants.CHT_ICON_HELP){
            PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                    .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setBannerRes(R.drawable.cht_banner)
                    .setIconRes(R.drawable.chat_subapp)
                    .setSubTitle(R.string.cht_chat_subtitle)
                    .setBody(R.string.cht_chat_body)
                    .setTextFooter(R.string.cht_chat_footer)
                    .build();
            presentationDialog.show();
            return true;
        }

        if (id == R.id.menu_search) {
            return true;
        }

        if (id == R.id.menu_error_report) {
            changeActivity(Activities.CHT_CHAT_OPEN_SEND_ERROR_REPORT, appSession.getAppPublicKey());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // This method uses APIs from newer OS versions than the minimum that this app supports. This
    // annotation tells Android lint that they are properly guarded so they won't run on older OS
    // versions and can be ignored by lint.


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.list) {
            MenuInflater inflater = new MenuInflater(getContext());
            inflater.inflate(R.menu.contact_list_context_menu, menu);
        }
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//        try{
//            //TODO: metodo nuevo que lo buscara del module del identity//chatManager.getChatUserIdentities();
////            for (ChatActorCommunityInformation cont: chatManager.listAllConnectedChatActor(
////                    (ChatActorCommunitySelectableIdentity) chatManager.
////                            getIdentityChatUsersFromCurrentDeviceUser().get(0), 2000, offset))
////            {
//                //null;//chatManager.getContacts();
//           // appSession.setData(ChatSession.CONTACT_DATA, contactid.get(position));
//            //appSession.setData(ChatSession.CONTACT_DATA, cont.getPublicKey());//chatManager.getChatActorbyConnectionId(contactid.get(position)));
//           // }
//        }catch (Exception e){
//            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
//        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id =item.getItemId();
        if (id == R.id.menu_view_contact) {
            try {
                //Contact con = chatSession.getSelectedContact();// type ChatActorCommunityInformation
                //TODO: metodo nuevo que lo buscara del module del identity//chatManager.getChatUserIdentities();
//                for (ChatActorCommunityInformation cont: chatManager.listAllConnectedChatActor(
//                        (ChatActorCommunitySelectableIdentity) chatManager.
//                                getIdentityChatUsersFromCurrentDeviceUser().get(0), 2000, offset))
//                {
                    //null;//chatManager.getContacts();
                    appSession.setData(ChatSession.CONTACT_DATA, contactid.get(info.position));//appSession.setData(ChatSession.CONTACT_DATA, cont.getPublicKey());//chatManager.getChatActorbyConnectionId(contactid.get(position)));
//                }

                changeActivity(Activities.CHT_CHAT_OPEN_CONTACT_DETAIL, appSession.getAppPublicKey());
            //}catch(CantGetContactException e) {
            //    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            }catch (Exception e){
                errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            }
            return true;
        }
        if (id == R.id.menu_edit_contact) {
            //Contact con = chatSession.getSelectedContact();//type ChatActorCommunityInformation
            try {
                appSession.setData(ChatSession.CONTACT_DATA, contactid.get(info.position));
                //appSession.setData(ChatSession.CONTACT_DATA, null);//chatManager.getChatActorbyConnectionId(con.getContactId()));
                changeActivity(Activities.CHT_CHAT_EDIT_CONTACT, appSession.getAppPublicKey());
            }catch (Exception e){
                errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            }
            return true;
        }
        if (id == R.id.menu_block_contact) {
            //changeActivity(Activities.CHT_CHAT_OPEN_CONTACTLIST, appSession.getAppPublicKey());
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
