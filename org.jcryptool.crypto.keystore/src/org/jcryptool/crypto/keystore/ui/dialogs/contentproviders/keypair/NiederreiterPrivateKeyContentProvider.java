// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.keypair;

import java.util.ArrayList;
import java.util.List;

import org.jcryptool.crypto.keystore.ui.dialogs.TableEntry;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.AbstractKeyNodeContentProvider;
import org.jcryptool.crypto.keystore.ui.dialogs.contentproviders.Messages;

import de.flexiprovider.api.exceptions.InvalidKeySpecException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.pqc.ecc.niederreiter.NiederreiterKeyFactory;
import de.flexiprovider.pqc.ecc.niederreiter.NiederreiterPrivateKey;
import de.flexiprovider.pqc.ecc.niederreiter.NiederreiterPrivateKeySpec;

/**
 * @author Anatoli Barski
 * 
 */
public class NiederreiterPrivateKeyContentProvider extends AbstractKeyNodeContentProvider {

    @Override
    protected List<TableEntry> getAlgorithmElements(Object inputElement) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            NiederreiterPrivateKey key = (NiederreiterPrivateKey) inputElement;
            if (key == null)
                return null;

            paramElements.add(new TableEntry(Messages.ContentProvider_k, "" + key.getK())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_m, "" + key.getM())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_n, "" + key.getN())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_t, "" + key.getT())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_p, "" + key.getP())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_field, "" + key.getField())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_goppapoly, "" + key.getGoppaPoly())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_qinv, "" + key.getQInv())); //$NON-NLS-2$
            paramElements.add(new TableEntry(Messages.ContentProvider_sinv, "" + key.getSInv())); //$NON-NLS-2$

        } catch (ClassCastException e) {
            return null;
        }
        return paramElements;
    }

    @Override
    protected List<TableEntry> getKeySpecElements(Key key) {

        List<TableEntry> paramElements = new ArrayList<TableEntry>();

        try {
            NiederreiterKeyFactory keyFactory = new NiederreiterKeyFactory();
            NiederreiterPrivateKeySpec keySpec = (NiederreiterPrivateKeySpec) keyFactory.getKeySpec(key,
                    NiederreiterPrivateKeySpec.class);
            if (keySpec == null)
                return null;

        } catch (ClassCastException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
        return paramElements;
    }

}
