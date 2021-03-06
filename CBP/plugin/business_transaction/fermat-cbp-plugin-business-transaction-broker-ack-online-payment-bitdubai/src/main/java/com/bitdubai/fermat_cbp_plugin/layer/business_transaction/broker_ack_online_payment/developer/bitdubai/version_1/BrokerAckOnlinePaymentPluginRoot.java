package com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_online_payment.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.CantInitializeDatabaseException;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.CantStartServiceException;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.open_contract.events.NewContractOpened;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.interfaces.CustomerBrokerContractPurchaseManager;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.interfaces.CustomerBrokerContractSaleManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_purchase.interfaces.CustomerBrokerPurchaseNegotiationManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiationManager;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.interfaces.TransactionTransmissionManager;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_online_payment.developer.bitdubai.version_1.database.BrokerAckOnlinePaymentBusinessTransactionDao;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_online_payment.developer.bitdubai.version_1.database.BrokerAckOnlinePaymentBusinessTransactionDatabaseConstants;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_online_payment.developer.bitdubai.version_1.database.BrokerAckOnlinePaymentBusinessTransactionDatabaseFactory;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_online_payment.developer.bitdubai.version_1.database.BrokerAckOnlinePaymentBusinessTransactionDeveloperDatabaseFactory;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_online_payment.developer.bitdubai.version_1.event_handler.BrokerAckOnlinePaymentRecorderService;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_online_payment.developer.bitdubai.version_1.exceptions.CantInitializeBrokerAckOnlinePaymentBusinessTransactionDatabaseException;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_online_payment.developer.bitdubai.version_1.structure.BrokerAckOnlinePaymentMonitorAgent;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_online_payment.developer.bitdubai.version_1.structure.BrokerAckOnlinePaymentTransactionManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.events.IncomingMoneyNotificationEvent;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Created by Manuel Perez on 15/12/2015.
 */

public class BrokerAckOnlinePaymentPluginRoot extends AbstractPlugin implements
        DatabaseManagerForDevelopers,
        LogManagerForDevelopers {

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.LOG_MANAGER)
    LogManager logManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.NETWORK_SERVICE, plugin = Plugins.TRANSACTION_TRANSMISSION)
    private TransactionTransmissionManager transactionTransmissionManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.CONTRACT, plugin = Plugins.CONTRACT_PURCHASE)
    private CustomerBrokerContractPurchaseManager customerBrokerContractPurchaseManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.CONTRACT, plugin = Plugins.CONTRACT_SALE)
    private CustomerBrokerContractSaleManager customerBrokerContractSaleManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.NEGOTIATION, plugin = Plugins.NEGOTIATION_PURCHASE)
    private CustomerBrokerPurchaseNegotiationManager customerBrokerPurchaseNegotiationManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.NEGOTIATION, plugin = Plugins.NEGOTIATION_SALE)
    private CustomerBrokerSaleNegotiationManager customerBrokerSaleNegotiationManager;

    /**
     * Represents the plugin manager.
     */
    BrokerAckOnlinePaymentTransactionManager brokerAckOnlinePaymentTransactionManager;

    /**
     * Represents the plugin BrokerAckOnlinePaymentBusinessTransactionDatabaseFactory
     */
    BrokerAckOnlinePaymentBusinessTransactionDeveloperDatabaseFactory brokerAckOnlinePaymentBusinessTransactionDeveloperDatabaseFactory;

    /**
     * Represents the database
     */
    Database database;
    public BrokerAckOnlinePaymentPluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();

    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();
        returnedClasses.add("com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_online_payment.developer.bitdubai.version_1.BrokerAckOnlinePaymentPluginRoot");
        return returnedClasses;
    }

    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {
        try {
            for (Map.Entry<String, LogLevel> pluginPair : newLoggingLevel.entrySet()) {
                if (BrokerAckOnlinePaymentPluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                    BrokerAckOnlinePaymentPluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                    BrokerAckOnlinePaymentPluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
                } else {
                    BrokerAckOnlinePaymentPluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
                }
            }
        } catch (Exception exception) {
            this.errorManager.reportUnexpectedPluginException(Plugins.BROKER_ACK_ONLINE_PAYMENT, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
        }
    }

    ServiceStatus serviceStatus = ServiceStatus.CREATED;

    /**
     * This method initialize the database
     *
     * @throws CantInitializeDatabaseException
     */
    private void initializeDb() throws CantInitializeDatabaseException {

        try {
            /*
             * Open new database connection
             */
            this.database = this.pluginDatabaseSystem.openDatabase(
                    pluginId,
                    BrokerAckOnlinePaymentBusinessTransactionDatabaseConstants.DATABASE_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

            /*
             * The database exists but cannot be open. I can not handle this situation.
             */
            errorManager.reportUnexpectedPluginException(
                    Plugins.BROKER_ACK_ONLINE_PAYMENT,
                    UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                    cantOpenDatabaseException);
            throw new CantInitializeDatabaseException(cantOpenDatabaseException.getLocalizedMessage());

        } catch (DatabaseNotFoundException e) {

            /*
             * The database no exist may be the first time the plugin is running on this device,
             * We need to create the new database
             */
            BrokerAckOnlinePaymentBusinessTransactionDatabaseFactory brokerAckOnlinePaymentBusinessTransactionDatabaseFactory =
                    new BrokerAckOnlinePaymentBusinessTransactionDatabaseFactory(pluginDatabaseSystem);

            try {

                /*
                 * We create the new database
                 */
                this.database = brokerAckOnlinePaymentBusinessTransactionDatabaseFactory.createDatabase(
                        pluginId,
                        BrokerAckOnlinePaymentBusinessTransactionDatabaseConstants.DATABASE_NAME);

            } catch (CantCreateDatabaseException cantOpenDatabaseException) {

                /*
                 * The database cannot be created. I can not handle this situation.
                 */
                errorManager.reportUnexpectedPluginException(
                        Plugins.BROKER_ACK_ONLINE_PAYMENT,
                        UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                        cantOpenDatabaseException);
                throw new CantInitializeDatabaseException(cantOpenDatabaseException.getLocalizedMessage());

            }
        }

    }

    @Override
    public void start() throws CantStartPluginException {
        try {

            /**
             * Initialize database
             */
            initializeDb();

            /**
             * Initialize Developer Database Factory
             */
            brokerAckOnlinePaymentBusinessTransactionDeveloperDatabaseFactory = new
                    BrokerAckOnlinePaymentBusinessTransactionDeveloperDatabaseFactory(pluginDatabaseSystem,
                    pluginId);
            brokerAckOnlinePaymentBusinessTransactionDeveloperDatabaseFactory.initializeDatabase();

            /**
             * Initialize Dao
             */
            BrokerAckOnlinePaymentBusinessTransactionDao brokerAckOnlinePaymentBusinessTransactionDao=
                    new BrokerAckOnlinePaymentBusinessTransactionDao(pluginDatabaseSystem,
                            pluginId,
                            database,
                            errorManager);
            /**
             * Init Monitor Agent
             */
        //TODO: the following line is only for testing, please comment it when the testing finish
            //customerBrokerContractSaleManager=new CustomerBrokerContractSaleManagerMock();
            BrokerAckOnlinePaymentMonitorAgent brokerAckOnlinePaymentMonitorAgent=new BrokerAckOnlinePaymentMonitorAgent(
                    pluginDatabaseSystem,
                    logManager,
                    errorManager,
                    eventManager,
                    pluginId,
                    transactionTransmissionManager,
                    customerBrokerContractPurchaseManager,
                    customerBrokerContractSaleManager,
                    customerBrokerSaleNegotiationManager);
            brokerAckOnlinePaymentMonitorAgent.start();

            /**
             * Init event recorder service.
             */
            BrokerAckOnlinePaymentRecorderService brokerAckOnlinePaymentRecorderService=
                    new BrokerAckOnlinePaymentRecorderService(
                            brokerAckOnlinePaymentBusinessTransactionDao,
                            eventManager,
                            errorManager);

            brokerAckOnlinePaymentRecorderService.start();

            /**
             * Initialize plugin manager
             */
            this.brokerAckOnlinePaymentTransactionManager=new
                    BrokerAckOnlinePaymentTransactionManager(
                    brokerAckOnlinePaymentBusinessTransactionDao,
                    errorManager);

            this.serviceStatus = ServiceStatus.STARTED;
            //System.out.println("Broker Ack Online Payment Starting");
            //raiseNewContractEventTest();
            //testAck();
        } catch (CantInitializeBrokerAckOnlinePaymentBusinessTransactionDatabaseException exception) {
            throw new CantStartPluginException(
                    FermatException.wrapException(exception),
                    "Starting Broker Ack Online Payment Plugin",
                    "Cannot initialize the plugin database factory");
        } catch (CantInitializeDatabaseException exception) {
            throw new CantStartPluginException(
                    FermatException.wrapException(exception),
                    "Starting Broker Ack Online Payment Plugin",
                    "Cannot initialize the database plugin");
        } catch (CantStartAgentException exception) {
            throw new CantStartPluginException(
                    FermatException.wrapException(exception),
                    "Starting Broker Ack Online Payment Plugin",
                    "Cannot initialize the plugin monitor agent");
        } catch (CantStartServiceException exception) {
            throw new CantStartPluginException(
                    FermatException.wrapException(exception),
                    "Starting Broker Ack Online Payment Plugin",
                    "Cannot initialize the plugin recorder service");
        }catch (Exception exception){
            throw new CantStartPluginException(FermatException.wrapException(exception),
                    "Starting Broker Ack Online Payment Plugin",
                    "Unexpected error");
        }
    }

    @Override
    public void pause() {

        try{
            this.serviceStatus = ServiceStatus.PAUSED;
        }catch(Exception exception){
            this.errorManager.reportUnexpectedPluginException(Plugins.BROKER_ACK_ONLINE_PAYMENT,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,FermatException.wrapException(exception));
        }
    }

    @Override
    public void resume() {
        try{
            this.serviceStatus = ServiceStatus.STARTED;
        }catch(Exception exception){
            this.errorManager.reportUnexpectedPluginException(Plugins.BROKER_ACK_ONLINE_PAYMENT,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,FermatException.wrapException(exception));
        }
    }

    @Override
    public void stop() {
        try{
            this.serviceStatus = ServiceStatus.STOPPED;
        }catch(Exception exception){
            this.errorManager.reportUnexpectedPluginException(Plugins.BROKER_ACK_ONLINE_PAYMENT,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,FermatException.wrapException(exception));
        }

    }

    @Override
    public FermatManager getManager() {
        return this.brokerAckOnlinePaymentTransactionManager;
    }

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        return brokerAckOnlinePaymentBusinessTransactionDeveloperDatabaseFactory.getDatabaseList(
                developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        return brokerAckOnlinePaymentBusinessTransactionDeveloperDatabaseFactory.getDatabaseTableList(
                developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        return brokerAckOnlinePaymentBusinessTransactionDeveloperDatabaseFactory.getDatabaseTableContent(
                developerObjectFactory,
                developerDatabaseTable);
    }

    public static LogLevel getLogLevelByClass(String className) {
        try {
            String[] correctedClass = className.split((Pattern.quote("$")));
            return BrokerAckOnlinePaymentPluginRoot.newLoggingLevel.get(correctedClass[0]);
        } catch (Exception e) {
            System.err.println("CantGetLogLevelByClass: " + e.getMessage());
            return DEFAULT_LOG_LEVEL;
        }
    }

    private void testAck(){
        try{
            FermatEvent fermatEvent = eventManager.getNewEvent(EventType.INCOMING_MONEY_NOTIFICATION);
            IncomingMoneyNotificationEvent incomingMoneyNotificationEvent = (IncomingMoneyNotificationEvent) fermatEvent;
            incomingMoneyNotificationEvent.setSource(EventSource.OUTGOING_INTRA_USER);
            incomingMoneyNotificationEvent.setActorId("BrokerPublicKey");
            incomingMoneyNotificationEvent.setActorType(Actors.CBP_CRYPTO_BROKER);
            incomingMoneyNotificationEvent.setAmount(2000);
            incomingMoneyNotificationEvent.setCryptoCurrency(CryptoCurrency.BITCOIN);
            incomingMoneyNotificationEvent.setWalletPublicKey("TestWalletPublicKey");
            eventManager.raiseEvent(incomingMoneyNotificationEvent);
            System.out.println("Event raised:\n"+incomingMoneyNotificationEvent.toString());
        } catch(Exception e){
            System.out.println("Exception in Broker Ack Online Payment Test: "+e);
            e.printStackTrace();
        }
    }

    private void raiseNewContractEventTest(){
        try{
            FermatEvent fermatEvent = eventManager.getNewEvent(com.bitdubai.fermat_cbp_api.all_definition.events.enums.EventType.NEW_CONTRACT_OPENED);
            NewContractOpened newContractOpened = (NewContractOpened) fermatEvent;
            newContractOpened.setSource(EventSource.BUSINESS_TRANSACTION_OPEN_CONTRACT);
            newContractOpened.setContractHash("888052D7D718420BD197B647F3BB04128C9B71BC99DBB7BC60E78BDAC4DFC6E2");
            eventManager.raiseEvent(newContractOpened);
        } catch(Exception e){
            System.out.println("Exception in Broker Ack Online Payment Test: "+e);
        }
    }

}