/*
 * Copyright (c) 2023 European Commission
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.europa.ec.eudi.pidissuer.adapter.out.jose

import arrow.core.raise.Raise
import eu.europa.ec.eudi.pidissuer.domain.*
import eu.europa.ec.eudi.pidissuer.port.input.IssueCredentialError.InvalidProof

class ValidateProof(
    private val credentialIssuerId: CredentialIssuerId,
) {

    context (Raise<InvalidProof>)
    operator fun invoke(
        unvalidatedProof: UnvalidatedProof,
        expectedCNonce: CNonce,
        credentialMetaData: CredentialMetaData,
    ): CredentialKey {
        fun jwt(jwt: UnvalidatedProof.Jwt): CredentialKey =
            validateJwtProof(credentialIssuerId, jwt, expectedCNonce, credentialMetaData)

        fun cwt(cwt: UnvalidatedProof.Cwt): CredentialKey =
            raise(InvalidProof("Supporting only JWT proof"))

        return when (unvalidatedProof) {
            is UnvalidatedProof.Jwt -> jwt(unvalidatedProof)
            is UnvalidatedProof.Cwt -> cwt(unvalidatedProof)
        }
    }
}
