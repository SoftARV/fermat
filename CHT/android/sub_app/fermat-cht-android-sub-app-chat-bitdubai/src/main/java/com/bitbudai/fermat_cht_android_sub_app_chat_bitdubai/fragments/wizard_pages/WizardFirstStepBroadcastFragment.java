package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.fragments.wizard_pages;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.sessions.ChatSession;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.settings.ChatSettings;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.util.ChtConstants;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CHTException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatModuleManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatPreferenceSettings;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

/**
 * Created by Lozadaa on 20/01/16.
*/
 public class WizardFirstStepBroadcastFragment extends AbstractFermatFragment
 {
 private static final String TAG = "WizardFirstStepBroadcastFragment";


     // Fermat Managers
     private ChatManager walletManager;
     EditText NombreLista;
     Toolbar toolbar;
     private ErrorManager errorManager;
     private SettingsManager<ChatSettings> settingsManager;
     private ChatPreferenceSettings chatSettings;

     public static WizardFirstStepBroadcastFragment newInstance() {
        return new WizardFirstStepBroadcastFragment();
     }

 @Override
     public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
         ChatModuleManager moduleManager = ((ChatSession) appSession).getModuleManager();
         try {
             walletManager = moduleManager.getChatManager();
         } catch (CHTException e) {
             errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
         }
         errorManager = appSession.getErrorManager();
     toolbar = getToolbar();
     toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.cht_ic_back_buttom));
    }


     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View layout = inflater.inflate(R.layout.cht_wizard_broadcast_one_step, container, false);
         final RadioButton radioA = (RadioButton) layout.findViewById(R.id.radioButton);
         final RadioButton radioB = (RadioButton) layout.findViewById(R.id.radioButton2);
         /*radioA.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(radioA.isChecked()){
                     radioA.setChecked(false);
                 }else{
                     radioA.setChecked(true);
                 }
                 if(radioB.isChecked()){
                     radioB.setChecked(false);
                 }
             }
         });

         radioB.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(radioB.isChecked()) {
                     radioB.setChecked(false);
                 }else{
                     radioB.setChecked(true);
                 }
                 if(radioA.isChecked()) {
                     radioA.setChecked(false);
                 }
             }
         });*/
         ShowDialogWelcome();
         Button cbutton = (Button) layout.findViewById(R.id.btncreate);
         cbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(radioA.isChecked() || radioB.isChecked())
                     if(radioA.isChecked())
                     changeActivity(Activities.CHT_CHAT_BROADCAST_WIZARD_TWO_DETAIL);
                     else
                     changeActivity(Activities.CHT_CHAT_BROADCAST_WIZARD_TWO_SCHEDULED_DETAIL);
                 else
                     Toast.makeText(getActivity(), "Please select at least one option", Toast.LENGTH_SHORT).show();
             }
         });
     return layout;
     }

     public void ShowDialogWelcome(){
         PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                 .setBody(R.string.cht_chat_body_broadcast_step_one)
                 .setSubTitle(R.string.cht_chat_subtitle_broadcast_step_one)
                 .setTextFooter(R.string.cht_chat_footer_broadcast_step_one)
                 .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                 .setBannerRes(R.drawable.cht_banner)
                 .setIconRes(R.drawable.chat_subapp)
                 .build();
         presentationDialog.show();
     }

     @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         menu.clear();
         menu.add(0, ChtConstants.CHT_ICON_HELP, 0, "help").setIcon(R.drawable.ic_menu_help_cht)
                 .setVisible(true)
                 .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
         switch (id){
             case ChtConstants.CHT_ICON_HELP:
                 ShowDialogWelcome();
                 break;
             case android.R.id.home:
                 changeActivity(Activities.CHT_CHAT_OPEN_CHATLIST);
                 break;
         }
         return super.onOptionsItemSelected(item);
     }

     private void saveSettingAndGoNextStep() {
             //TODO: TEMPORAL SOLO PARA PRUEBAS AÑADIR SAVE SETTINGS
             changeActivity(Activities.CHT_CHAT_BROADCAST_WIZARD_TWO_DETAIL);
     }


 }