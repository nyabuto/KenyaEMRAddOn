package org.openmrs.module.basicexample.reporting.definition;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.definition.configuration.ConfigurationProperty;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;
import org.openmrs.module.reporting.query.BaseQuery;
import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;

import java.util.Date;

@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
@Localized("reporting.SeenPatientCohortDefinition")
public class SeenPatientCohortDefinition extends BaseQuery<Encounter> implements EncounterQuery {
	
	@ConfigurationProperty
	private Date asOfDate;
	
	public SeenPatientCohortDefinition() {
	}
	
	public Date getAsOfDate() {
		return asOfDate;
	}
	
	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}
}
