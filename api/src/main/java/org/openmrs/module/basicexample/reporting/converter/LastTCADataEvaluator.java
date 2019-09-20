package org.openmrs.module.basicexample.reporting.converter;

import org.openmrs.annotation.Handler;
import org.openmrs.module.reporting.data.patient.EvaluatedPatientData;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.evaluator.PatientDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

@Handler(supports = LastTCADataDefinition.class, order = 50)
public class LastTCADataEvaluator implements PatientDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	@Override
	public EvaluatedPatientData evaluate(PatientDataDefinition definition, EvaluationContext context)
	        throws EvaluationException {
		EvaluatedPatientData c = new EvaluatedPatientData(definition, context);
		String qry = "SELECT e.patient_id as patient_id,"
		        + " SUBSTRING_INDEX(GROUP_CONCAT(IF(o.concept_id IN(5096),DATE(o.value_datetime),null) ORDER BY e.encounter_datetime SEPARATOR '|'),'|',-1) AS Next_TCA "
		        + "FROM openmrs.obs o " + "JOIN openmrs.encounter e ON o.encounter_id=e.encounter_id "
		        + "AND o.voided=0 and e.voided=0 and o.concept_id in(5096) "
		        + "AND DATE(e.encounter_datetime) <= DATE(:endDate) "
		        + "JOIN openmrs.encounter_type et ON e.encounter_type=et.encounter_type_id "
		        + "AND et.name IN('HIV Enrollment','HIV Consultation') and et.retired=0 group by patient_id";
		SqlQueryBuilder builder = new SqlQueryBuilder();
		builder.append(qry);
		Date endDate = (Date) context.getParameterValue("endDate");
		builder.addParameter("endDate", endDate);
		Map<Integer, Object> data = evaluationService.evaluateToMap(builder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
}
