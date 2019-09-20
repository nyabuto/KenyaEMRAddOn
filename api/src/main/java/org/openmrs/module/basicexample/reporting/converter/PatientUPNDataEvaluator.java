package org.openmrs.module.basicexample.reporting.converter;

import org.openmrs.annotation.Handler;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.evaluator.EncounterDataEvaluator;
import org.openmrs.module.reporting.data.patient.EvaluatedPatientData;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.evaluator.PatientDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Handler(supports = PatientUPNDataDefinition.class, order = 53)
public class PatientUPNDataEvaluator implements PatientDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	public EvaluatedPatientData evaluate(PatientDataDefinition patientDataDefinition, EvaluationContext evaluationContext)
	        throws EvaluationException {
		EvaluatedPatientData c = new EvaluatedPatientData(patientDataDefinition, evaluationContext);
		
		String qry = "select e.patient_id,pi.identifier FROM openmrs.encounter e "
		        + "INNER JOIN patient_identifier pi ON e.patient_id=pi.patient_id AND e.voided=0 "
		        + "INNER JOIN patient_identifier_type pit ON pi.identifier_type=pit.patient_identifier_type_id "
		        + "AND pit.name in('Unique Patient Number') group by e.patient_id";
		
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		queryBuilder.append(qry);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class,
		    evaluationContext);
		c.setData(data);
		return c;
	}
}
