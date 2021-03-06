// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.descriptors.interfaces;

import org.jcryptool.crypto.keystore.keys.KeyType;

public interface INewEntryDescriptor {
    String getContactName();

    String getPassword();

    KeyType getKeyStoreEntryType();

    String getDisplayedName();

    String getAlgorithmName();

    String getProvider();

    int getKeyLength();
}
