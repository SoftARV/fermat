package com.bitdubai.fermat_cbp_plugin.layer.network_service.transaction_transmission.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.CantInitializeDatabaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.data_base.CommunicationNetworkServiceDatabaseConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The final Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.template.database.TransactionTransmissionNetworkServiceDeveloperDatabaseFactory</code> have
 * contains the methods that the Developer Database Tools uses to show the information.
 * <p/>
 *
 * Created by lnacosta (laion.cj91@gmail.com) on 30/10/2015.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */

public final class TransactionTransmissionNetworkServiceDeveloperDatabaseFactory {

    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID                 pluginId            ;

    private       Database             database            ;

    public TransactionTransmissionNetworkServiceDeveloperDatabaseFactory(final PluginDatabaseSystem pluginDatabaseSystem,
                                                                         final UUID pluginId) {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId             = pluginId            ;
    }

    public void initializeDatabase(final String tableId) throws CantInitializeDatabaseException {

        switch (tableId) {

            case TransactionTransmissionNetworkServiceDatabaseConstants.DATA_BASE_NAME:

                try {

                    database = this.pluginDatabaseSystem.openDatabase(pluginId, TransactionTransmissionNetworkServiceDatabaseConstants.DATA_BASE_NAME);

                } catch (final CantOpenDatabaseException e) {

                    throw new CantInitializeDatabaseException(e, "tableId: " + tableId, "Error trying to open the database.");

                } catch (final DatabaseNotFoundException e) {

                    final TransactionTransmissionDatabaseFactory databaseFactory = new TransactionTransmissionDatabaseFactory(pluginDatabaseSystem);

                    try {

                        database = databaseFactory.createDatabase(pluginId, tableId);

                    } catch (final CantCreateDatabaseException z) {

                        throw new CantInitializeDatabaseException(z, "tableId: " + tableId, "Error trying to create the database.");
                    }
                }
                break;

            case CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME:
                try {

                    this.database = this.pluginDatabaseSystem.openDatabase(pluginId, CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

                } catch (CantOpenDatabaseException e) {

                    throw new CantInitializeDatabaseException(e, "tableId: " + tableId, "Error trying to open the database.");

                } catch (DatabaseNotFoundException e) {

                    com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.data_base.CommunicationNetworkServiceDatabaseFactory communicationLayerNetworkServiceDatabaseFactory = new com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.data_base.CommunicationNetworkServiceDatabaseFactory(pluginDatabaseSystem);

                    try {

                        this.database = communicationLayerNetworkServiceDatabaseFactory.createDatabase(pluginId, CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

                    } catch (CantCreateDatabaseException z) {

                        throw new CantInitializeDatabaseException(z, "tableId: " + tableId, "Error trying to create the database.");
                    }
                }
        }
    }


    public final List<DeveloperDatabase> getDatabaseList(final DeveloperObjectFactory developerObjectFactory) {

        List<DeveloperDatabase> databases = new ArrayList<>();

        databases.add(developerObjectFactory.getNewDeveloperDatabase(
                "Transaction Transmission Network Service",
                TransactionTransmissionNetworkServiceDatabaseConstants.DATA_BASE_NAME
        ));

        databases.add(developerObjectFactory.getNewDeveloperDatabase(
                "Network Service Template",
                CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME
        ));
        return databases;
    }


    public List<DeveloperDatabaseTable> getDatabaseTableList(final DeveloperObjectFactory developerObjectFactory,
                                                             final DeveloperDatabase      developerDatabase     ) {

        List<DeveloperDatabaseTable> tables = new ArrayList<>();

        switch (developerDatabase.getId()) {

            case TransactionTransmissionNetworkServiceDatabaseConstants.DATA_BASE_NAME:
                /**
                 * Table contract hash columns.
                 */
                List<String> contractHashColumns = new ArrayList<>();

                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_TRANSMISSION_ID_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_CONTRACT_HASH_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_CONTRACT_STATUS_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_SENDER_PUBLIC_KEY_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_SENDER_TYPE_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_RECEIVER_PUBLIC_KEY_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_RECEIVER_TYPE_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_CONTRACT_ID_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_NEGOTIATION_ID_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_TRANSACTION_TYPE_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_TIMESTAMP_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_STATE_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_PENDING_FLAG_COLUMN_NAME);
                contractHashColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_REMOTE_BUSINESS_TRANSACTION);
                /**
                 * Table contract hash addition.
                 */
                DeveloperDatabaseTable contractHashTable = developerObjectFactory.getNewDeveloperDatabaseTable(TransactionTransmissionNetworkServiceDatabaseConstants.TRANSACTION_TRANSMISSION_HASH_TABLE_NAME, contractHashColumns);
                tables.add(contractHashTable);

                /**
                 * Table COMPONENT VERSIONS DETAILS columns.
                 */
                List<String> componentVersionsDetailsColumns = new ArrayList<String>();

                componentVersionsDetailsColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.COMPONENT_VERSIONS_DETAILS_ID_COLUMN_NAME);
                componentVersionsDetailsColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.COMPONENT_VERSIONS_DETAILS_ACTOR_PUBLIC_KEY_COLUMN_NAME);
                componentVersionsDetailsColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.COMPONENT_VERSIONS_DETAILS_IPK_COLUMN_NAME);
                componentVersionsDetailsColumns.add(TransactionTransmissionNetworkServiceDatabaseConstants.COMPONENT_VERSIONS_DETAILS_LAST_CONNECTION_COLUMN_NAME);
                /**
                 * Table COMPONENT VERSIONS DETAILS addition.
                 */
                DeveloperDatabaseTable componentVersionsDetailsTable = developerObjectFactory.getNewDeveloperDatabaseTable(TransactionTransmissionNetworkServiceDatabaseConstants.COMPONENT_VERSIONS_DETAILS_TABLE_NAME, componentVersionsDetailsColumns);
                tables.add(componentVersionsDetailsTable);
                break;
            case CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME:

                /**
                 * Table incoming messages columns.
                 */
                List<String> incomingMessagesColumns = new ArrayList<>();

                incomingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_ID_COLUMN_NAME                );
                incomingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_SENDER_ID_COLUMN_NAME         );
                incomingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_RECEIVER_ID_COLUMN_NAME       );
                incomingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_TYPE_COLUMN_NAME              );
                incomingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_SHIPPING_TIMESTAMP_COLUMN_NAME);
                incomingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_DELIVERY_TIMESTAMP_COLUMN_NAME);
                incomingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_STATUS_COLUMN_NAME            );
                incomingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_TEXT_CONTENT_COLUMN_NAME      );
                /**
                 * Table incoming messages addition.
                 */
                DeveloperDatabaseTable incomingMessagesTable = developerObjectFactory.getNewDeveloperDatabaseTable(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_TABLE_NAME, incomingMessagesColumns);
                tables.add(incomingMessagesTable);

                /**
                 * Table outgoing messages columns.
                 */
                List<String> outgoingMessagesColumns = new ArrayList<>();

                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_ID_COLUMN_NAME                );
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_SENDER_ID_COLUMN_NAME         );
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_SENDER_TYPE_COLUMN_NAME       );
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_SENDER_NS_TYPE_COLUMN_NAME    );
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_RECEIVER_ID_COLUMN_NAME       );
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_RECEIVER_TYPE_COLUMN_NAME     );
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_RECEIVER_NS_TYPE_COLUMN_NAME  );
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_TYPE_COLUMN_NAME              );
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_SHIPPING_TIMESTAMP_COLUMN_NAME);
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_DELIVERY_TIMESTAMP_COLUMN_NAME);
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_FAIL_COUNT_COLUMN_NAME        );
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_STATUS_COLUMN_NAME            );
                outgoingMessagesColumns.add(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_TEXT_CONTENT_COLUMN_NAME      );
                /**
                 * Table outgoing messages addition.
                 */
                DeveloperDatabaseTable outgoingMessagesTable = developerObjectFactory.getNewDeveloperDatabaseTable(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGES_TABLE_NAME, outgoingMessagesColumns);
                tables.add(outgoingMessagesTable);
                break;
        }


        return tables;
    }

    public final List<DeveloperDatabaseTableRecord> getDatabaseTableContent(final DeveloperObjectFactory developerObjectFactory,
                                                                            final DeveloperDatabase      developerDatabase     ,
                                                                            final DeveloperDatabaseTable developerDatabaseTable) {

        try {

            initializeDatabase(developerDatabase.getId());

            final List<DeveloperDatabaseTableRecord> returnedRecords = new ArrayList<>();

            final DatabaseTable selectedTable = database.getTable(developerDatabaseTable.getName());

            try {

                selectedTable.loadToMemory();
            } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {

                return returnedRecords;
            }

            final List<DatabaseTableRecord> records = selectedTable.getRecords();

            List<String> developerRow;

            for (DatabaseTableRecord row : records) {

                developerRow = new ArrayList<>();

                for (DatabaseRecord field : row.getValues())
                    developerRow.add(field.getValue());

                returnedRecords.add(developerObjectFactory.getNewDeveloperDatabaseTableRecord(developerRow));
            }
            return returnedRecords;

        } catch (Exception e) {

            System.err.println(e.toString());
            return new ArrayList<>();
        }
    }
}