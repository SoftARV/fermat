package com.bitdubai.fermat_api.layer.all_definition.navigation_structure;

import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatHeader;

/**
 * Created by Matias Furszyfer on 2015.10.01..
 */
public class Header implements FermatHeader{

    String label;
    boolean hasExpandable = false;


    public Header() {
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean hasExpandable() {
        return hasExpandable;
    }

    public void setHasExpandable(boolean hasExpandable) {
        this.hasExpandable = hasExpandable;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
