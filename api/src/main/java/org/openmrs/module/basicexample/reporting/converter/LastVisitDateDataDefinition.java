package org.openmrs.module.basicexample.reporting.converter;

import org.openmrs.module.reporting.data.BaseDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

import java.util.Date;

@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
public class LastVisitDateDataDefinition extends BaseDataDefinition implements EncounterDataDefinition {
	
	public static final long serialVersionUID = 1L;
	
	/**
	 * Default Constructor
	 */
	public LastVisitDateDataDefinition() {
		super();
	}
	
	/**
	 * Constructor to populate name only
	 */
	public LastVisitDateDataDefinition(String name) {
		super(name);
	}
	
	//***** INSTANCE METHODS *****
	
	/**
	 * @see org.openmrs.module.reporting.data.DataDefinition#getDataType()
	 */
	public Class<?> getDataType() {
		return Date.class;
	}
}
