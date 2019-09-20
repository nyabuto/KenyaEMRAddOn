/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.basicexample.metadata;

import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.form;

/**
 * Metadata constants
 */
@Component
public class FacesMetadata extends AbstractMetadataBundle {
	
	public static final String MODULE_ID = "basicexample";
	
	public static final class _EncounterType {
		
		public static final String Consultation = "465a92f2-baf8-42e9-9612-53064be868e8";
	}
	
	public static final class _Form {
		
		public static final String FACES_TRIAL_FORM = "a3f6cb91-4e40-48b5-b22b-01755a106fc2";
	}
	
	@Override
	public void install() throws Exception {
		// doing this in the scheduled task so that previous value set is preserved
		install(form("Faces Encounter Form", "Faces encounter form copied from KenyaEMR encounter form",
		    _EncounterType.Consultation, "2", _Form.FACES_TRIAL_FORM));
	}
	
}
