/*
 *  Copyright (c) 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.tractusx.edc.iam.ssi.miw;

import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.runtime.metamodel.annotation.Provider;
import org.eclipse.edc.spi.http.EdcHttpClient;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.spi.types.TypeManager;
import org.eclipse.tractusx.edc.iam.ssi.miw.api.MiwApiClient;
import org.eclipse.tractusx.edc.iam.ssi.miw.api.MiwApiClientImpl;
import org.eclipse.tractusx.edc.iam.ssi.miw.config.SsiMiwConfiguration;
import org.eclipse.tractusx.edc.iam.ssi.miw.oauth2.MiwOauth2Client;


@Extension(SsiMiwApiClientExtension.EXTENSION_NAME)
public class SsiMiwApiClientExtension implements ServiceExtension {

    public static final String EXTENSION_NAME = "SSI MIW Api Client";

    @Inject
    private MiwOauth2Client oauth2Client;

    @Inject
    private EdcHttpClient httpClient;

    @Inject
    private TypeManager typeManager;

    @Inject
    private Monitor monitor;

    @Inject
    private SsiMiwConfiguration miwConfiguration;

    @Override
    public String name() {
        return EXTENSION_NAME;
    }

    @Provider
    public MiwApiClient apiClient(ServiceExtensionContext context) {
        return new MiwApiClientImpl(httpClient, miwConfiguration.getUrl(), oauth2Client, context.getParticipantId(), miwConfiguration.getAuthorityId(), typeManager.getMapper(), monitor);
    }

}
