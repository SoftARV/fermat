package unit.com.bitdubai.fermat_cry_plugin.layer.crypto_module.wallet_address_book.developer.bitdubai.version_1.structure.WalletAddressBookCryptoModuleRegistry;

import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.PlatformWalletType;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.wallet_address_book.exceptions.CantRegisterWalletAddressBookException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_module.wallet_address_book.developer.bitdubai.version_1.structure.WalletAddressBookCryptoModuleRegistry;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RegisterWalletAddressBookTest extends TestCase {

    @Mock
    ErrorManager errorManager;

    @Mock
    PluginDatabaseSystem pluginDatabaseSystem;

    @Mock
    Database database;

    @Mock
    DatabaseTable databaseTable;

    @Mock
    DatabaseTableRecord databaseTableRecord;

    UUID walletId;

    PlatformWalletType platformWalletType;

    CryptoAddress cryptoAddress;


    WalletAddressBookCryptoModuleRegistry registry;

    UUID pluginId;

    @Before
    public void setUp() throws Exception {
        walletId = UUID.randomUUID();
        platformWalletType = PlatformWalletType.BASIC_WALLET_BITCOIN_WALLET;
        cryptoAddress = new CryptoAddress("asdadas", CryptoCurrency.BITCOIN);
        pluginId = UUID.randomUUID();
        registry = new WalletAddressBookCryptoModuleRegistry();
        registry.setErrorManager(errorManager);
        registry.setPluginDatabaseSystem(pluginDatabaseSystem);
        registry.setPluginId(pluginId);
        when(pluginDatabaseSystem.openDatabase(pluginId, pluginId.toString())).thenReturn(database);
        registry.initialize();

    }

    @Test
    public void testRegister_NotNull() throws Exception {
        when(database.getTable(anyString())).thenReturn(databaseTable);
        when(databaseTable.getEmptyRecord()).thenReturn(databaseTableRecord);
        registry.registerWalletCryptoAddressBook(cryptoAddress, platformWalletType, walletId);
    }

    @Test(expected=CantRegisterWalletAddressBookException.class)
    public void testRegister_CantInsertRecordException_CantRegisterWalletAddressBookException() throws Exception {
        when(database.getTable(anyString())).thenReturn(databaseTable);
        when(databaseTable.getEmptyRecord()).thenReturn(databaseTableRecord);
        doThrow(new CantInsertRecordException()).when(databaseTable).insertRecord(any(DatabaseTableRecord.class));

        registry.registerWalletCryptoAddressBook(cryptoAddress, platformWalletType, walletId);
    }

    @Test(expected=CantRegisterWalletAddressBookException.class)
    public void testRegister_walletIdNull_CantRegisterWalletAddressBookException() throws Exception {
        registry.registerWalletCryptoAddressBook(cryptoAddress, platformWalletType, null);
    }

    @Test(expected=CantRegisterWalletAddressBookException.class)
    public void testRegister_platformWalletTypeNull_CantRegisterWalletAddressBookException() throws Exception {
        registry.registerWalletCryptoAddressBook(cryptoAddress, null, walletId);
    }

    @Test(expected=CantRegisterWalletAddressBookException.class)
    public void testRegister_cryptoAddressNull_CantRegisterWalletAddressBookException() throws Exception {
        registry.registerWalletCryptoAddressBook(null, platformWalletType, walletId);
    }
}
