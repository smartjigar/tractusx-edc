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

package org.eclipse.tractusx.edc.helpers;


import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import org.eclipse.edc.connector.policy.spi.PolicyDefinition;
import org.eclipse.edc.policy.model.AtomicConstraint;

import java.util.Map;
import java.util.stream.Stream;

import static org.eclipse.edc.jsonld.spi.JsonLdKeywords.CONTEXT;
import static org.eclipse.edc.jsonld.spi.JsonLdKeywords.ID;
import static org.eclipse.edc.jsonld.spi.JsonLdKeywords.TYPE;
import static org.eclipse.edc.jsonld.spi.PropertyAndTypeNames.ODRL_CONSTRAINT_TYPE;
import static org.eclipse.edc.jsonld.spi.PropertyAndTypeNames.ODRL_LOGICAL_CONSTRAINT_TYPE;
import static org.eclipse.edc.spi.CoreConstants.EDC_NAMESPACE;

public class PolicyHelperFunctions {

    private static final String BUSINESS_PARTNER_EVALUATION_KEY = "BusinessPartnerNumber";

    /**
     * Creates a {@link PolicyDefinition} using the given ID, that contains equality constraints for each of the given BusinessPartnerNumbers:
     * each BPN is converted into an {@link AtomicConstraint} {@code BusinessPartnerNumber EQ [BPN]}.
     */
    public static JsonObject businessPartnerNumberPolicy(String id, String... bpns) {
        return policyDefinitionBuilder(bnpPolicy(bpns))
                .add(ID, id)
                .build();
    }

    /**
     * Creates a {@link PolicyDefinition} using the given ID, that contains equality constraints for each of the given BusinessPartnerNumbers:
     * each BPN is converted into an {@link AtomicConstraint} {@code BusinessPartnerNumber EQ [BPN]}.
     */
    public static JsonObject frameworkPolicy(String id, Map<String, String> permissions) {
        return policyDefinitionBuilder(frameworkPolicy(permissions))
                .add(ID, id)
                .build();
    }

    public static JsonObjectBuilder policyDefinitionBuilder() {
        return Json.createObjectBuilder()
                .add(TYPE, EDC_NAMESPACE + "PolicyDefinitionDto");
    }

    public static JsonObjectBuilder policyDefinitionBuilder(JsonObject policy) {
        return policyDefinitionBuilder()
                .add(EDC_NAMESPACE + "policy", policy);
    }

    public static JsonObject noConstraintPolicyDefinition(String id) {
        return policyDefinitionBuilder(noConstraintPolicy())
                .add(ID, id)
                .build();
    }

    private static JsonObject noConstraintPolicy() {
        return Json.createObjectBuilder()
                .add(TYPE, "use")
                .build();
    }

    private static JsonObject bnpPolicy(String... bnps) {
        return Json.createObjectBuilder()
                .add(CONTEXT, "http://www.w3.org/ns/odrl.jsonld")
                .add("permission", Json.createArrayBuilder()
                        .add(permission(bnps)))
                .build();
    }

    private static JsonObject permission(String... bpns) {

        var bpnConstraints = Stream.of(bpns)
                .map(bpn -> atomicConstraint(BUSINESS_PARTNER_EVALUATION_KEY, "eq", bpn))
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add);

        return Json.createObjectBuilder()
                .add("action", "USE")
                .add("constraint", Json.createObjectBuilder()
                        .add(TYPE, ODRL_LOGICAL_CONSTRAINT_TYPE)
                        .add("or", bpnConstraints)
                        .build())
                .build();
    }

    private static JsonObject frameworkPolicy(Map<String, String> permissions) {
        return Json.createObjectBuilder()
                .add(CONTEXT, "http://www.w3.org/ns/odrl.jsonld")
                .add("permission", Json.createArrayBuilder()
                        .add(frameworkPermission(permissions)))
                .build();
    }

    private static JsonObject frameworkPermission(Map<String, String> permissions) {

        var constraints = permissions.entrySet().stream()
                .map(permission -> atomicConstraint(permission.getKey(), "eq", permission.getValue()))
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add);

        return Json.createObjectBuilder()
                .add("action", "USE")
                .add("constraint", Json.createObjectBuilder()
                        .add(TYPE, ODRL_LOGICAL_CONSTRAINT_TYPE)
                        .add("or", constraints)
                        .build())
                .build();
    }

    private static JsonObject atomicConstraint(String leftOperand, String operator, Object rightOperand) {
        return Json.createObjectBuilder()
                .add(TYPE, ODRL_CONSTRAINT_TYPE)
                .add("leftOperand", leftOperand)
                .add("operator", operator)
                .add("rightOperand", rightOperand.toString())
                .build();
    }
}
