package com.bitdubai.reference_niche_wallet.loss_protected_wallet.fragments.wallet_final_version;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.android_fermat_ccp_loss_protected_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.CircularProgressBar;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.ReferenceWallet;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.enums.VaultType;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Activity;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.loss_protected_wallet.interfaces.BitcoinLossProtectedWalletSpend;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.exceptions.CantListCryptoWalletIntraUserIdentityException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.LossProtectedWalletSettings;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantGetCryptoLossProtectedWalletException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantGetLossProtectedBalanceException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantListLossProtectedSpendingException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantListLossProtectedTransactionsException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantRequestLossProtectedAddressException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWallet;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWalletContact;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWalletIntraUserIdentity;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWalletTransaction;
import com.bitdubai.fermat_cer_api.all_definition.interfaces.ExchangeRate;
import com.bitdubai.fermat_cer_api.layer.provider.interfaces.CurrencyExchangeRateProviderManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.LossProtectedWalletConstants;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.animation.AnimationManager;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.enums.ShowMoneyType;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.popup.PresentationBitcoinWalletDialog;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.WalletUtils;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.session.LossProtectedWalletSession;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.session.SessionConstant;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.widget.Toast.makeText;
import static com.bitdubai.android_fermat_ccp_loss_protected_wallet_bitcoin.R.drawable.earning_icon;

/**
 * Created by Gian Barboza on 18/04/16.
 */
public class HomeFragment extends AbstractFermatFragment<LossProtectedWalletSession,ResourceProviderManager> implements OnChartValueSelectedListener {

    LossProtectedWalletSession lossProtectedWalletSession;
    SettingsManager<LossProtectedWalletSettings> settingsManager;
    BlockchainNetworkType blockchainNetworkType;
    /**
     * Manager
     * */
    private LossProtectedWallet lossProtectedWallet;
    private LossProtectedWallet moduleManager;
    private ErrorManager errorManager;

    /**
     * DATA
     * */
    private List<BitcoinLossProtectedWalletSpend> allWalletSpendingList;
    private BitcoinLossProtectedWalletSpend bitcoinLossProtectedWalletSpend;
    String walletPublicKey = "loss_protected_wallet";
    long before = 0;
    long after = 0;
    boolean pressed = false;
    CircularProgressBar circularProgressBar;
    Thread background;

    // Fermat Managers

    private TextView txt_type_balance;
    private TextView txt_touch_to_change;
    private TextView txt_balance_amount;
    private TextView txt_balance_amount_type;
    private TextView txt_exchange_rate;
    private TextView txt_earnOrLost;
    private TextView txt_date_home;
    private ImageView earnOrLostImage;
    private LineChart chart;
    private TextView noDataInChart;
    private long balanceAvailable;
    private long realBalance;
    private View rootView;
    private Activity activity;

    private int progress1=1;

    private LinearLayout emptyListViewsContainer;
    private AnimationManager animationManager;
   // private FermatTextView txt_balance_amount_type;


    private UUID exchangeProviderId = null;

    private long exchangeRate = 0;

    public static HomeFragment newInstance(){ return new HomeFragment(); }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        try {
            lossProtectedWalletSession = appSession;
            lossProtectedWallet = lossProtectedWalletSession.getModuleManager().getCryptoWallet();
            errorManager = appSession.getErrorManager();


            settingsManager = lossProtectedWalletSession.getModuleManager().getSettingsManager();


            LossProtectedWalletSettings bitcoinWalletSettings;
            try {
                bitcoinWalletSettings = settingsManager.loadAndGetSettings(lossProtectedWalletSession.getAppPublicKey());
            }catch (Exception e){
                bitcoinWalletSettings = null;
            }
            if(bitcoinWalletSettings == null){
                bitcoinWalletSettings = new LossProtectedWalletSettings();
                bitcoinWalletSettings.setIsContactsHelpEnabled(true);
                bitcoinWalletSettings.setIsPresentationHelpEnabled(true);
                bitcoinWalletSettings.setNotificationEnabled(true);
                bitcoinWalletSettings.setLossProtectedEnabled(true);

                settingsManager.persistSettings(lossProtectedWalletSession.getAppPublicKey(), bitcoinWalletSettings);
            }

            //default btc network
            if(bitcoinWalletSettings.getBlockchainNetworkType()==null){
                bitcoinWalletSettings.setBlockchainNetworkType(BlockchainNetworkType.getDefaultBlockchainNetworkType());
            }

            settingsManager.persistSettings(lossProtectedWalletSession.getAppPublicKey(),bitcoinWalletSettings);

            blockchainNetworkType = settingsManager.loadAndGetSettings(lossProtectedWalletSession.getAppPublicKey()).getBlockchainNetworkType();

            final LossProtectedWalletSettings bitcoinWalletSettingsTemp = bitcoinWalletSettings;


            //default Exchange rate Provider
            try {
                if(lossProtectedWallet.getExchangeProvider()==null) {
                    List<CurrencyExchangeRateProviderManager> providers = new ArrayList(lossProtectedWallet.getExchangeRateProviderManagers());

                    exchangeProviderId = providers.get(0).getProviderId();
                    lossProtectedWallet.setExchangeProvider(exchangeProviderId);

                }
                else
                {
                    exchangeProviderId =lossProtectedWallet.getExchangeProvider();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Handler handlerTimer = new Handler();
            handlerTimer.postDelayed(new Runnable(){
                public void run() {
                    if(bitcoinWalletSettingsTemp.isPresentationHelpEnabled()){
                        setUpPresentation(false);
                    }
                }}, 500);

        } catch (Exception ex) {
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI,
                        UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, ex);
        }


    }

    private void setUpPresentation(boolean checkButton) {
        PresentationBitcoinWalletDialog presentationBitcoinWalletDialog =
                new PresentationBitcoinWalletDialog(
                        getActivity(),
                        lossProtectedWalletSession,
                        null,
                        (lossProtectedWallet.getActiveIdentities().isEmpty()) ? PresentationBitcoinWalletDialog.TYPE_PRESENTATION : PresentationBitcoinWalletDialog.TYPE_PRESENTATION_WITHOUT_IDENTITIES,
                        checkButton);

        presentationBitcoinWalletDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Object o = lossProtectedWalletSession.getData(SessionConstant.PRESENTATION_IDENTITY_CREATED);
                if (o != null) {
                    if ((Boolean) (o)) {
                        //invalidate();
                        lossProtectedWalletSession.removeData(SessionConstant.PRESENTATION_IDENTITY_CREATED);
                    }
                }
                try {
                    LossProtectedWalletIntraUserIdentity cryptoWalletIntraUserIdentity = lossProtectedWalletSession.getIntraUserModuleManager();
                    if (cryptoWalletIntraUserIdentity == null) {
                        getActivity().onBackPressed();
                    } else {
                        invalidate();
                    }
                } catch (CantListCryptoWalletIntraUserIdentityException e) {
                    e.printStackTrace();
                } catch (CantGetCryptoLossProtectedWalletException e) {
                    e.printStackTrace();
                }

            }
        });
        presentationBitcoinWalletDialog.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);
            animationManager = new AnimationManager(rootView,emptyListViewsContainer);
            getPaintActivtyFeactures().addCollapseAnimation(animationManager);
        } catch (Exception e){
            makeText(getActivity(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
            lossProtectedWalletSession.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        try {
            rootView = inflater.inflate(R.layout.lossprotected_home, container, false);
            setUp(inflater);


            return rootView;
        }
        catch (Exception e) {
            makeText(getActivity(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
            appSession.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, e);
        }

        return null;

    }


    private void setUp(LayoutInflater inflater){
        try {
            setUpHeader(inflater);
            setUpChart(inflater);
            //setUpDonut(inflater);

        }catch (Exception e){
            errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI,
                    UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, e);
        }
    }

    private void setUpHeader(LayoutInflater inflater){

        try {

            //Select all header Element
            txt_balance_amount      = (TextView) rootView.findViewById(R.id.txt_balance_amount);
            txt_balance_amount_type = (TextView) rootView.findViewById(R.id.txt_balance_amount_type);
            txt_type_balance        = (TextView) rootView.findViewById(R.id.txt_type_balance);
            txt_touch_to_change     = (TextView) rootView.findViewById(R.id.txt_touch_to_change);
            txt_exchange_rate       = (TextView) rootView.findViewById(R.id.txt_exchange_rate);

            //show Exchange Market Rate
            getAndShowMarketExchangeRateData(rootView);


            txt_type_balance.setOnTouchListener(new View.OnTouchListener() {
                long TIME_HOLD_TO_PRESS = 2000;

                final Handler _handler = new Handler();
                Runnable _longPressed = new Runnable() {
                    public void run() {
                        Log.i("info","LongPress");
                        GET("", getActivity());
                    }
                };

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                   if (event.getAction() == MotionEvent.ACTION_DOWN){
                       before = System.currentTimeMillis();
                       return true;
                   }else if (event.getAction() == MotionEvent.ACTION_UP){
                       after = System.currentTimeMillis();
                       if (after - before <= TIME_HOLD_TO_PRESS){
                           changeBalanceType(txt_type_balance, txt_balance_amount);
                           return true;
                       }else{
                           GET("", getActivity());
                           return true;
                       }
                   }
                    return false;
                }
            });

         

            //Event Click For change the balance type
            txt_touch_to_change.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    changeBalanceType(txt_type_balance, txt_balance_amount);
                }
            });

            //Event Click For change the balance type
            txt_type_balance.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    changeBalanceType(txt_type_balance,txt_balance_amount);
                }
            });

            //Event for change the balance amount
            txt_balance_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    changeAmountType();
                }
            });
            //Event for change the balance amount type
            txt_balance_amount_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    changeAmountType();
                }
            });

            long balance = 0;
            if (BalanceType.getByCode(lossProtectedWalletSession.getBalanceTypeSelected()).equals(BalanceType.AVAILABLE))
                balance = lossProtectedWallet.getBalance(BalanceType.AVAILABLE, lossProtectedWalletSession.getAppPublicKey(), blockchainNetworkType, "0");
            else
                balance = lossProtectedWallet.getRealBalance(lossProtectedWalletSession.getAppPublicKey(), blockchainNetworkType);

            txt_balance_amount.setText(WalletUtils.formatBalanceString(balance, lossProtectedWalletSession.getTypeAmount()));


        }catch (Exception e){
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! recovering from system error",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void setUpChart(LayoutInflater inflater){

        try {
            //configChart(rootView);
            //Select all header Element
            txt_earnOrLost          = (TextView) rootView.findViewById(R.id.txt_amount_lost_or_earn);
            earnOrLostImage         = (ImageView) rootView.findViewById(R.id.earnOrLostImage);
            txt_date_home           = (TextView) rootView.findViewById(R.id.txt_date_home);
            chart                   = (LineChart) rootView.findViewById(R.id.chart);
            noDataInChart           = (TextView) rootView.findViewById(R.id.noDataInChart);


            /*//set Earning or Losts Values
            double total = 0;
            //total = lossProtectedWallet.getEarningOrLostsWallet(lossProtectedWalletSession.getAppPublicKey());
            if (total>=0){
                txt_earnOrLost.setText("B "+WalletUtils.formatAmountString(total)+" earned");
                earnOrLostImage.setBackgroundResource(earning_icon);
            }else {
                txt_earnOrLost.setText("B "+WalletUtils.formatAmountString(total)+" losted");
                earnOrLostImage.setImageResource(R.drawable.lost_icon);
            }*/

            //set Actual Date

            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            Date actualDate = new Date();
            txt_date_home.setText(sdf.format(actualDate));


            //Get all wallet spending from the manager
            allWalletSpendingList = lossProtectedWallet.listAllWalletSpendingValue(lossProtectedWalletSession.getAppPublicKey());

            LineData data = getData(allWalletSpendingList);
            //LineData data = new LineData(labels, dataset);

            data.setValueTextSize(12f);
            data.setValueTextColor(Color.WHITE);

            chart.setData(data);



            chart.setData(data);
            chart.setDescription("");
            chart.setDrawGridBackground(false);
            chart.animateY(2000);
            chart.setTouchEnabled(true);
            chart.setDragEnabled(false);
            chart.setScaleEnabled(false);
            chart.setPinchZoom(false);
            chart.setDoubleTapToZoomEnabled(false);
            chart.setHighlightPerDragEnabled(true);
            chart.setHighlightPerTapEnabled(true);
            chart.setOnChartValueSelectedListener(this);



            YAxis yAxis = chart.getAxisLeft();
            yAxis.setEnabled(false);
            yAxis.setStartAtZero(false);
            yAxis.setAxisMaxValue(30);
            yAxis.setAxisMinValue(-30);

            YAxis yAxis1R = chart.getAxisRight();
            yAxis1R.setEnabled(false);
            yAxis1R.setAxisMaxValue(30);
            yAxis1R.setAxisMinValue(-30);

            XAxis xAxis = chart.getXAxis();
            xAxis.setEnabled(false);

            Legend legend = chart.getLegend();
            legend.setEnabled(false);



        }catch (CantListLossProtectedSpendingException e) {
        errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
        makeText(getActivity(), "Oooops! Error Exception : CantListLossProtectedSpendingException",
                Toast.LENGTH_SHORT).show();
        }catch (CantLoadWalletException e) {
        errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
        makeText(getActivity(), "Oooops! Error Exception : CantLoadWalletException",
                Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! recovering from system error In Graphic",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String getWalletAddress(String actorPublicKey) {
        String walletAddres="";
        try {
            //TODO parameters deliveredByActorId deliveredByActorType harcoded..
            CryptoAddress cryptoAddress = moduleManager.requestAddressToKnownUser(
                    lossProtectedWalletSession.getIntraUserModuleManager().getPublicKey(),
                    Actors.INTRA_USER,
                    actorPublicKey,
                    Actors.EXTRA_USER,
                    Platforms.CRYPTO_CURRENCY_PLATFORM,
                    VaultType.CRYPTO_CURRENCY_VAULT,
                    "BITV",
                    appSession.getAppPublicKey(),
                    ReferenceWallet.BASIC_WALLET_LOSS_PROTECTED_WALLET,
                    blockchainNetworkType
            );
            walletAddres = cryptoAddress.getAddress();
        } catch (CantRequestLossProtectedAddressException e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(e));
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();

        } catch (CantGetCryptoLossProtectedWalletException e) {
            e.printStackTrace();
        } catch (CantListCryptoWalletIntraUserIdentityException e) {
            e.printStackTrace();
        }
        return walletAddres;
    }


    public void GET(String url, final Context context){
        final Handler mHandler = new Handler();
        try {
            if(moduleManager.getRealBalance(appSession.getAppPublicKey(), blockchainNetworkType)<500000000L) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String receivedAddress = "";
                        final HttpClient Client = new DefaultHttpClient();
                        try {
                            String SetServerString = "";

                            // Create Request to server and get response

                            HttpGet httpget = new HttpGet("http://52.27.68.19:15400/mati/address/");
                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            SetServerString = Client.execute(httpget, responseHandler);
                            // Show response on activity

                            receivedAddress = SetServerString;
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        final String finalReceivedAddress = receivedAddress;

                        String response = "";
                        try {


                            String SetServerString = "";
                            CryptoAddress cryptoAddress = new CryptoAddress(finalReceivedAddress, CryptoCurrency.BITCOIN);
                            LossProtectedWalletContact cryptoWalletWalletContact = null;
                            try {
                                cryptoWalletWalletContact = moduleManager.createWalletContact(cryptoAddress, "regtest_bitcoins", "", "", Actors.EXTRA_USER, appSession.getAppPublicKey(), blockchainNetworkType);

                            } catch (Exception e) {

                            }

                            assert cryptoWalletWalletContact != null;
                            String myCryptoAddress = getWalletAddress(cryptoWalletWalletContact.getActorPublicKey());
                            HttpGet httpget = new HttpGet("http://52.27.68.19:15400/mati/hello/?address=" + myCryptoAddress);
                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            SetServerString = Client.execute(httpget, responseHandler);

                            response = SetServerString;
                        } catch (IOException e) {

                        }


                        final String finalResponse = response;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (!finalResponse.equals("transaccion fallida")) {
                                    Toast.makeText(context, "Regtest bitcoin arrived", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });
                thread.start();
            }
        } catch (CantGetLossProtectedBalanceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the LineData for the chart based on the all wallet Spending List
     *
     * @return the ListData object
     */
    private LineData getData(List<BitcoinLossProtectedWalletSpend> ListSpendigs) {

        ArrayList<Entry> entryList = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        //look for the size number of the spendings list
        int size = ListSpendigs.size();

        //if statement for validate if the list has values
        if (size > 0) {
            //loop into the spending transaction
            for (int i = 0; i < size; i++) {

                BitcoinLossProtectedWalletSpend listSpendig = ListSpendigs.get(i);

                //get the entry value for chart with getEarnOrLostOfSpending method
                final float valueEntry = getEarnOrLostOfSpending(
                        listSpendig.getAmount(),
                        listSpendig.getExchangeRate(),
                        listSpendig.getTransactionId());

                //Set entries values for the chart
                entryList.add(new Entry(valueEntry, i));
                xValues.add(String.valueOf(i));
            }
            chart.setVisibility(View.VISIBLE);
        }else{
            txt_earnOrLost.setText("$0 earned");
            earnOrLostImage.setImageResource(R.drawable.earning_icon);
            chart.setVisibility(View.GONE);
            noDataInChart.setVisibility(View.VISIBLE);
        }

        LineDataSet dataset = new LineDataSet(entryList, "dataSet");
        dataset.setColor(Color.WHITE); //
        dataset.setDrawCubic(false);
        dataset.setDrawValues(false);
        dataset.setDrawCircles(false);
        dataset.setValueFormatter(new LargeValueFormatter());
        dataset.setDrawHighlightIndicators(false);

        return new LineData(xValues, dataset);

    }

    private float getEarnOrLostOfSpending(float spendingAmount,double spendingExchangeRate, UUID transactionId){

        double totalInDollars = 0;

        try{

            //get the intra user login identity
            LossProtectedWalletIntraUserIdentity intraUserLoginIdentity = lossProtectedWalletSession.getIntraUserModuleManager();
            String intraUserPk = null;
            if (intraUserLoginIdentity != null) {
                intraUserPk = intraUserLoginIdentity.getPublicKey();
            }

            //Get the transaction of this Spending
            LossProtectedWalletTransaction transaction = lossProtectedWallet.getTransaction(
                    transactionId,
                    lossProtectedWalletSession.getAppPublicKey(),
                    intraUserPk);
            //calculate the Earned/Losted in dollars of the spending value
            totalInDollars = (spendingAmount*spendingExchangeRate)-(spendingAmount * transaction.getExchangeRate());

            //return the total
            return (float) totalInDollars;

        } catch (CantListLossProtectedTransactionsException e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! Error Exception : CantListLossProtectedTransactionsException",
                    Toast.LENGTH_SHORT).show();
        } catch (CantListCryptoWalletIntraUserIdentityException e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! Error Exception : CantListCryptoWalletIntraUserIdentityException",
                    Toast.LENGTH_SHORT).show();
        } catch (CantGetCryptoLossProtectedWalletException e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! Error Exception : CantGetCryptoLossProtectedWalletException",
                    Toast.LENGTH_SHORT).show();
        }

        return 0;
    }


    private void changeAmountType(){

        ShowMoneyType showMoneyType = (lossProtectedWalletSession.getTypeAmount()== ShowMoneyType.BITCOIN.getCode()) ? ShowMoneyType.BITS : ShowMoneyType.BITCOIN;
        lossProtectedWalletSession.setTypeAmount(showMoneyType);
        String moneyTpe = "";
        switch (showMoneyType){
            case BITCOIN:
                moneyTpe = "BTC";
                txt_balance_amount.setTextSize(24);
                break;
            case BITS:
                moneyTpe = "BITS";
                txt_balance_amount.setTextSize(24);
                break;
        }

        txt_balance_amount_type.setText(moneyTpe);
        updateBalances();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.add(0, LossProtectedWalletConstants.IC_ACTION_SEND, 0, "send").setIcon(R.drawable.ic_actionbar_send)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(1, LossProtectedWalletConstants.IC_ACTION_HELP_PRESENTATION, 1, "help").setIcon(R.drawable.loos_help_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //inflater.inflate(R.menu.home_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {

            int id = item.getItemId();

            if(id == LossProtectedWalletConstants.IC_ACTION_SEND){
                changeActivity(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_FORM_ACTIVITY,lossProtectedWalletSession.getAppPublicKey());
                return true;
            }else if(id == LossProtectedWalletConstants.IC_ACTION_HELP_PRESENTATION){
                setUpPresentation(settingsManager.loadAndGetSettings(lossProtectedWalletSession.getAppPublicKey()).isPresentationHelpEnabled());
                return true;
            }


        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! recovering from system error",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to change the balance type
     */
    private void changeBalanceType(TextView txt_type_balance,TextView txt_balance_amount) {
        updateBalances();
        try {
            if (lossProtectedWalletSession.getBalanceTypeSelected().equals(BalanceType.AVAILABLE.getCode())) {
                realBalance = loadBalance(BalanceType.REAL);
                txt_balance_amount.setText(WalletUtils.formatBalanceString(realBalance, lossProtectedWalletSession.getTypeAmount()));
                txt_type_balance.setText(R.string.real_balance_text);
                lossProtectedWalletSession.setBalanceTypeSelected(BalanceType.REAL);
            } else if (lossProtectedWalletSession.getBalanceTypeSelected().equals(BalanceType.REAL.getCode())) {
                balanceAvailable = loadBalance(BalanceType.AVAILABLE);
                txt_balance_amount.setText(WalletUtils.formatBalanceString(balanceAvailable, lossProtectedWalletSession.getTypeAmount()));
                txt_type_balance.setText(R.string.available_balance_text);
                lossProtectedWalletSession.setBalanceTypeSelected(BalanceType.AVAILABLE);
            }
        } catch (Exception e) {
            lossProtectedWalletSession.getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(e));
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
        }

    }

    private long loadBalance(BalanceType balanceType){
        long balance = 0;
        try {

            if(balanceType.equals(BalanceType.REAL))
                balance =  lossProtectedWallet.getRealBalance(lossProtectedWalletSession.getAppPublicKey(), blockchainNetworkType);

            if(balanceType.equals(BalanceType.AVAILABLE))
                balance =  lossProtectedWallet.getBalance(balanceType, lossProtectedWalletSession.getAppPublicKey(), blockchainNetworkType, String.valueOf(lossProtectedWalletSession.getActualExchangeRate()));


        } catch (CantGetLossProtectedBalanceException e) {
            e.printStackTrace();
        }

        return balance;
    }

    private void updateBalances(){
        realBalance = loadBalance(BalanceType.REAL);
        balanceAvailable = loadBalance(BalanceType.AVAILABLE);
        txt_balance_amount.setText(
                WalletUtils.formatBalanceString(
                        (lossProtectedWalletSession.getBalanceTypeSelected() == BalanceType.AVAILABLE.getCode())
                                ? balanceAvailable : realBalance,
                        lossProtectedWalletSession.getTypeAmount())
        );
    }



    private void getAndShowMarketExchangeRateData(final View container) {

        FermatWorker fermatWorker = new FermatWorker(getActivity()) {
            @Override
            protected Object doInBackground() throws Exception {

                ExchangeRate rate =  lossProtectedWallet.getCurrencyExchange(exchangeProviderId);
                return rate;
            }
        };

        fermatWorker.setCallBack(new FermatWorkerCallBack() {
            @Override
            public void onPostExecute(Object... result) {
                if (result != null && result.length > 0) {

                    ExchangeRate rate = (ExchangeRate) result[0];
                    // progressBar.setVisibility(View.GONE);
                    txt_exchange_rate.setText("1 BTC = " + String.valueOf(rate.getPurchasePrice()) + " USD");

                    //get available balance to actual exchange rate

                    lossProtectedWalletSession.setActualExchangeRate(rate.getPurchasePrice());

                    updateBalances();

                }
            }

            @Override
            public void onErrorOccurred(Exception ex) {
                //  progressBar.setVisibility(View.GONE);

                txt_exchange_rate.setVisibility(View.GONE);

                ErrorManager errorManager = lossProtectedWalletSession.getErrorManager();
                if (errorManager != null)
                    errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_BROKER_WALLET,
                            UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
                else
                    Log.e("Exchange Rate", ex.getMessage(), ex);
            }
        });

        fermatWorker.execute();
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e != null) {
            try {

                Log.i("VAL SELECTED",
                        "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                                + ", DataSet index: " + dataSetIndex);

                //total = lossProtectedWallet.getEarningOrLostsWallet(lossProtectedWalletSession.getAppPublicKey());
                if (e.getVal()>=0){

                    txt_earnOrLost.setText("$ "+WalletUtils.formatAmountString(e.getVal())+" earned");
                    earnOrLostImage.setImageResource(R.drawable.earning_icon);
                }else {
                    txt_earnOrLost.setText("$ "+WalletUtils.formatAmountString(e.getVal())+" losted");
                    earnOrLostImage.setImageResource(R.drawable.lost_icon);
                }
                chart.invalidate(); // refresh

            }catch (Exception el){
                el.getCause();
            }
        }
    }


    @Override
    public void onNothingSelected() {

    }
}
