// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.engines.listener;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.core.types.ActionItem;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.BlockCipherDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.SecureRandomDescriptor;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEngine;
import org.jcryptool.crypto.flexiprovider.engines.cipher.AsymmetricBlockCipherEngine;
import org.jcryptool.crypto.flexiprovider.engines.cipher.AsymmetricHybridCipherEngine;
import org.jcryptool.crypto.flexiprovider.engines.cipher.BlockCipherEngine;
import org.jcryptool.crypto.flexiprovider.engines.cipher.CipherEngine;
import org.jcryptool.crypto.flexiprovider.engines.mac.MacEngine;
import org.jcryptool.crypto.flexiprovider.engines.messagedigest.MessageDigestEngine;
import org.jcryptool.crypto.flexiprovider.engines.securerandom.SecureRandomEngine;
import org.jcryptool.crypto.flexiprovider.engines.signature.SignatureEngine;
import org.jcryptool.crypto.flexiprovider.operations.engines.IPerfomOperationListener;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.keys.IKeyStoreAlias;

public class PerformOperationListener implements IPerfomOperationListener {
    public void performOperation(IFlexiProviderOperation operation) {
        LogUtil.logInfo("perform operation triggered: " + operation.getEntryName()); //$NON-NLS-1$
        FlexiProviderEngine engine = null;
        switch (operation.getRegistryType()) {
            case ASYMMETRIC_BLOCK_CIPHER: {
                engine = new AsymmetricBlockCipherEngine();
                break;
            }
            case ASYMMETRIC_HYBRID_CIPHER: {
                engine = new AsymmetricHybridCipherEngine();
                break;
            }
            case BLOCK_CIPHER: {
                engine = new BlockCipherEngine();
                break;
            }
            case CIPHER: {
                engine = new CipherEngine();
                break;
            }
            case MAC: {
                engine = new MacEngine();
                break;
            }
            case MESSAGE_DIGEST: {
                engine = new MessageDigestEngine();
                break;
            }
            case SECURE_RANDOM: {
                engine = new SecureRandomEngine();
                break;
            }
            case SIGNATURE: {
                engine = new SignatureEngine();
                break;
            }
        }
        if (engine != null) {           
            engine.perform(engine.init(operation));
            addActionItem(operation);
        }
    }

    private void addActionItem(IFlexiProviderOperation operation) {
        ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(
                ICommandService.class);

        if ((Boolean) service.getCommand("org.jcryptool.actions.recordCommand"). //$NON-NLS-1$
                getState("org.jcryptool.actions.recordCommand.toggleState").getValue()) { //$NON-NLS-1$
            AlgorithmDescriptor descriptor = operation.getAlgorithmDescriptor();
            ActionItem item = new ActionItem(EditorsManager.getInstance().getActiveEditorTitle(), descriptor.getAlgorithmName()); //$NON-NLS-1$

            item.setPluginId(operation.getRegistryType().getName());

            if (operation.getOperation() == OperationType.DECRYPT) {
                item.setActionType("decrypt"); //$NON-NLS-1$
            } else if (operation.getOperation() == OperationType.ENCRYPT) {
                item.setActionType("encrypt"); //$NON-NLS-1$
            } else if (operation.getOperation() == OperationType.SIGN) {
                item.setActionType("sign"); //$NON-NLS-1$
            } else if (operation.getOperation() == OperationType.VERIFY) {
                item.setActionType("verify"); //$NON-NLS-1$
            }
            //item.addParam("input", operation.getInput()); //$NON-NLS-1$
            //item.addParam("output", operation.getOutput()); //$NON-NLS-1$
            item.addParam("signature", operation.getSignature()); //$NON-NLS-1$
            item.addParam("algorithm type", operation.getAlgorithmDescriptor().getType().getName()); //$NON-NLS-1$
            IKeyStoreAlias alias = operation.getKeyStoreAlias();
            if (alias != null) {
                item.addParam("contact", alias.getContactName()); //$NON-NLS-1$
                item.addParam("key alias", alias.getAliasString()); //$NON-NLS-1$
                item.addParam("key length", String.valueOf(alias.getKeyLength())); //$NON-NLS-1$

                if (operation.getPassword() != null) {
                    item.addParam("key password", String.valueOf(operation.getPassword())); //$NON-NLS-1$
                }
            }
            
            if (descriptor instanceof BlockCipherDescriptor){
            	item.addParam("mode", ((BlockCipherDescriptor) descriptor).getMode()); //$NON-NLS-1$
            	item.addParam("padding scheme", ((BlockCipherDescriptor) descriptor).getPadding());   //$NON-NLS-1$
            }
            else if (descriptor instanceof SecureRandomDescriptor){
            	item.addParam("random size", ""+((SecureRandomDescriptor) descriptor).getLength()); //$NON-NLS-1$ //$NON-NLS-2$
            	//byte[][] alphabets = ((SecureRandomDescriptor) descriptor).getAlphabet();
            	//TODO push alphapet to items property "alphabet"
            }

            ActionCascadeService.getInstance().addItem(item);
        }
    }
}