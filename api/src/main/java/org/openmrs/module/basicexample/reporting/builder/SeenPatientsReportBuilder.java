package org.openmrs.module.basicexample.reporting.builder;

import org.openmrs.PatientIdentifierType;
import org.openmrs.module.basicexample.Metadata;
import org.openmrs.module.basicexample.reporting.converter.LastVisitDateDataDefinition;
import org.openmrs.module.basicexample.reporting.definition.SeenPatientCohortDefinition;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterTypeDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Builds({ "kenyaemr.faces.common.report.seenPatients" })
public class SeenPatientsReportBuilder extends AbstractReportBuilder {
	
	public static final String ENC_DATE_FORMAT = "yyyy/MM/dd";
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor,
	        ReportDefinition reportDefinition) {
		return Arrays.asList(ReportUtils.map(datasetColumns(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	protected DataSetDefinition datasetColumns() {
		
		EncounterDataSetDefinition dsd = new EncounterDataSetDefinition();
		dsd.setName("SeenPatients");
		dsd.setDescription("Number of patients seen within a period of time");
		dsd.addSortCriteria("Visit Date", SortCriteria.SortDirection.ASC);
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		String paramMapping = "startDate=${startDate},endDate=${endDate}";
		
		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName} {middleName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
		
		PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class,
		    Metadata.IdentifierType.UNIQUE_PATIENT_NUMBER);
		PatientIdentifierType serialno = MetadataUtils.existing(PatientIdentifierType.class,
		    Metadata.IdentifierType.PATIENT_CLINIC_NUMBER);
		
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition UPN = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(
		        upn.getName(), upn), identifierFormatter);
		DataDefinition SerialNo = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(
		        serialno.getName(), serialno), identifierFormatter);
		
		dsd.addColumn("PatientID", new PatientIdDataDefinition(), "");
		dsd.addColumn("UPN", UPN, null);
		dsd.addColumn("Serial No", SerialNo, "");
		dsd.addColumn("Patient Name", nameDef, "");
		dsd.addColumn("Date of Birth", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
		dsd.addColumn("Age", new AgeDataDefinition(), "");
		dsd.addColumn("Last Visit Date", new LastVisitDateDataDefinition(), "");
		dsd.addColumn("Encounter Date", new EncounterDatetimeDataDefinition(), "");
		dsd.addColumn("Encounter Type", new EncounterTypeDataDefinition(), "");
		
		SeenPatientCohortDefinition cd = new SeenPatientCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		dsd.addRowFilter(cd, paramMapping);
		return dsd;
		
	}
}
