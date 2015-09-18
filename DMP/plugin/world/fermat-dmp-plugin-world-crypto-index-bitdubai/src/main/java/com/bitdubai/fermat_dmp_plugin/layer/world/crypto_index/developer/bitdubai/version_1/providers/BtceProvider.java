package com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.providers;

import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_api.layer.dmp_world.crypto_index.exceptions.CryptoCurrencyNotSupportedException;
import com.bitdubai.fermat_api.layer.dmp_world.crypto_index.exceptions.FiatCurrencyNotSupportedException;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.exceptions.CantGetMarketPriceException;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.interfaces.CryptoIndexProvider;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.structure.HTTPJson;

/**
 * The class <code>com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.providers.BtceProvider</code>
 * haves all the logic to bring the market price for the provider Btce.<p/>
 * <p>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 16/09/2015.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class BtceProvider implements CryptoIndexProvider {

    private String getUrlAPI(String pair) {
        return "https://btc-e.com/api/3/ticker/"+ pair;
    }

    @Override
    public double getMarketPrice(CryptoCurrency cryptoCurrency,
                                 FiatCurrency fiatCurrency,
                                 long time) throws FiatCurrencyNotSupportedException, CryptoCurrencyNotSupportedException, CantGetMarketPriceException {

        HTTPJson jsonService = new HTTPJson();
        String pair = cryptoCurrency.getCode().toLowerCase() + "_" + fiatCurrency.getCode().toLowerCase();
        String urlApi = getUrlAPI(pair);
        String stringMarketPrice = jsonService.getJSONFromUrl(urlApi).getJSONObject(pair).get("last").toString();
        return Double.valueOf(stringMarketPrice);
    }
}
