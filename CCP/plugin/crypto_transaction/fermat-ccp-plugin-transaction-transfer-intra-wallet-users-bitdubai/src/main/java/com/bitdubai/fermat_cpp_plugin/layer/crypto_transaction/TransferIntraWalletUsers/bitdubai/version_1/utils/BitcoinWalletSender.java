package com.bitdubai.fermat_cpp_plugin.layer.crypto_transaction.TransferIntraWalletUsers.bitdubai.version_1.utils;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.ReferenceWallet;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_manager.CantLoadWalletsException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantCalculateBalanceException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantFindTransactionException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantRegisterCreditException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantRegisterDebitException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.loss_protected_wallet.interfaces.BitcoinLossProtectedWalletManager;

import java.util.UUID;

;

/**
 * Created by Joaquin Carrasquero on 17/03/16.
 */
public class BitcoinWalletSender {

    long cryptoAmount;
    String sendingWalletPublicKey;
    ReferenceWallet sendingWallet;
    ReferenceWallet receivingWallet;
    BlockchainNetworkType blockchainNetworkType;
    String notes;
    Actors actorType;






    //Wallet Managers
    private BitcoinWalletManager bitcoinWalletManager;
    private BitcoinLossProtectedWalletManager bitcoinLossProtectedWalletManager;

    public BitcoinWalletSender(long cryptoAmount, String sendingWalletPublicKey, ReferenceWallet sendingWallet, ReferenceWallet receivingWallet, String notes, BlockchainNetworkType blockchainNetworkType, Actors actorType) {
        this.cryptoAmount = cryptoAmount;
        this.sendingWalletPublicKey = sendingWalletPublicKey;
        this.sendingWallet = sendingWallet;
        this.receivingWallet = receivingWallet;
        this.notes = notes;
        this.blockchainNetworkType = blockchainNetworkType;
        this.actorType = actorType;
    }

    public void sendBtcToWallet(){

        Long balanceBeforeCredit = null;
        Long balanceAfterCredit = null;

        BitcoinWalletTransactionWalletRecord bitcoinWalletTransactionWalletRecord =  buildBitcoinWalletRecord();
        BitcoinLossProtectedWalletTransactionWalletRecord bitcoinLossProtectedWalletTransactionWalletRecord =  buildLossWalletRecord();




        switch (receivingWallet){

           case  BASIC_WALLET_BITCOIN_WALLET:



               try {
                   balanceBeforeCredit =   this.bitcoinWalletManager.loadWallet(sendingWalletPublicKey).getBalance(BalanceType.AVAILABLE).getBalance(blockchainNetworkType);
               } catch (CantCalculateBalanceException e) {
                   e.printStackTrace();
               } catch (CantLoadWalletsException e) {
                   e.printStackTrace();
               }

               if (balanceBeforeCredit!=null){
                   try {
                       this.bitcoinWalletManager.loadWallet(sendingWalletPublicKey).getBalance(BalanceType.BOOK).credit(bitcoinWalletTransactionWalletRecord);
                       this.bitcoinWalletManager.loadWallet(sendingWalletPublicKey).getBalance(BalanceType.AVAILABLE).credit(bitcoinWalletTransactionWalletRecord);
                   } catch (CantRegisterCreditException e) {
                       e.printStackTrace();
                   } catch (CantLoadWalletsException e) {
                       e.printStackTrace();
                   }
               }


               try {
                   balanceAfterCredit =   this.bitcoinWalletManager.loadWallet(sendingWalletPublicKey).getTransactionById(bitcoinWalletTransactionWalletRecord.getTransactionId()).getAmount();
               } catch (CantFindTransactionException e) {
                   e.printStackTrace();
               } catch (CantLoadWalletsException e) {
                   e.printStackTrace();
               }

               if (balanceAfterCredit == cryptoAmount){

                   switch (sendingWallet) {

                       case BASIC_WALLET_BITCOIN_WALLET:
                           break;
                       case BASIC_WALLET_DISCOUNT_WALLET:
                           break;
                       case BASIC_WALLET_FIAT_WALLET:
                           break;
                       case BASIC_WALLET_LOSS_PROTECTED_WALLET:

                           try {
                               this.bitcoinLossProtectedWalletManager.loadWallet(sendingWalletPublicKey).getBalance(BalanceType.AVAILABLE).debit(bitcoinLossProtectedWalletTransactionWalletRecord);
                               this.bitcoinLossProtectedWalletManager.loadWallet(sendingWalletPublicKey).getBalance(BalanceType.BOOK).debit(bitcoinLossProtectedWalletTransactionWalletRecord);
                           } catch (CantRegisterDebitException e) {
                               e.printStackTrace();
                           } catch (CantLoadWalletException e) {
                               e.printStackTrace();
                           }

                           break;


                   }

               }



               break;
           case  BASIC_WALLET_DISCOUNT_WALLET:
               break;
           case  BASIC_WALLET_FIAT_WALLET:
               break;
           case  BASIC_WALLET_LOSS_PROTECTED_WALLET:


               try {
                   balanceBeforeCredit =   this.bitcoinLossProtectedWalletManager.loadWallet(sendingWalletPublicKey).getBalance(BalanceType.AVAILABLE).getBalance(blockchainNetworkType);
               } catch (CantCalculateBalanceException e) {
                   e.printStackTrace();
               } catch (CantLoadWalletException e) {
                   e.printStackTrace();
               }

               if (balanceBeforeCredit!=null){
                   try {
                       this.bitcoinLossProtectedWalletManager.loadWallet(sendingWalletPublicKey).getBalance(BalanceType.BOOK).credit(bitcoinLossProtectedWalletTransactionWalletRecord);
                       this.bitcoinLossProtectedWalletManager.loadWallet(sendingWalletPublicKey).getBalance(BalanceType.AVAILABLE).credit(bitcoinLossProtectedWalletTransactionWalletRecord);
                   } catch (CantLoadWalletException e) {
                       e.printStackTrace();
                   } catch (CantRegisterCreditException e) {
                       e.printStackTrace();
                   }
               }


                   try {
                       balanceAfterCredit =   this.bitcoinLossProtectedWalletManager.loadWallet(sendingWalletPublicKey).getTransactionById(bitcoinLossProtectedWalletTransactionWalletRecord.getTransactionId()).getAmount();
                   } catch (CantFindTransactionException e) {
                       e.printStackTrace();
                   } catch (CantLoadWalletException e) {
                       e.printStackTrace();
                   }

               if (balanceAfterCredit == cryptoAmount){

                   switch (sendingWallet) {

                       case BASIC_WALLET_BITCOIN_WALLET:

                           try {

                               try {
                                   this.bitcoinWalletManager.loadWallet(sendingWalletPublicKey).getBalance(BalanceType.AVAILABLE).debit(bitcoinWalletTransactionWalletRecord);
                                   this.bitcoinWalletManager.loadWallet(sendingWalletPublicKey).getBalance(BalanceType.BOOK).debit(bitcoinWalletTransactionWalletRecord);
                               } catch (CantRegisterDebitException e) {
                                   e.printStackTrace();
                               }

                           } catch (CantLoadWalletsException e) {
                               e.printStackTrace();
                           }
                             break;
                       case BASIC_WALLET_DISCOUNT_WALLET:
                           break;
                       case BASIC_WALLET_FIAT_WALLET:
                           break;
                       case BASIC_WALLET_LOSS_PROTECTED_WALLET:
                           break;


                      }

                   }


               break;
           case    COMPOSITE_WALLET_MULTI_ACCOUNT:
               break;


       }

    }



public BitcoinLossProtectedWalletTransactionWalletRecord buildLossWalletRecord(){


    UUID pluginId = UUID.randomUUID();



    return null;

    }


    public BitcoinWalletTransactionWalletRecord buildBitcoinWalletRecord(){


        UUID pluginId = UUID.randomUUID();

        BitcoinWalletTransactionWalletRecord bitcoinLossProtectedWalletTransactionRecord = new BitcoinWalletTransactionWalletRecord(pluginId,
                null,
                null,
                cryptoAmount,
                null,
                notes,
                System.currentTimeMillis(),
                "",
                "",
                "",
                actorType,
                actorType,
                blockchainNetworkType);


        return bitcoinLossProtectedWalletTransactionRecord;

    }



}
