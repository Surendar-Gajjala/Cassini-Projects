package com.cassinisys.platform.model.dto;


import com.cassinisys.platform.security.EvaluationConstraints;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class CriteriaDto {
    @JsonProperty("lhs")
    private LhsDto lhs;
    @JsonProperty("operator")
    private OperatorDto operator;
    @JsonProperty("person")
    private PersonDto person;
    @JsonProperty("rhs")
    private String rhs;
    @JsonProperty("mergeType")
    private String mergeType;
    String lhsString = null;
    String expression = "";
    ExpressionParser parser = new SpelExpressionParser();
    boolean isString = false;

    public String getSpel(String criteria) throws Exception {
        expression = "";
        ObjectMapper objectMapper = new ObjectMapper();
        CriteriaDto[] o1 = objectMapper.readValue(criteria, CriteriaDto[].class);
        for (CriteriaDto criteriaDto1 : o1) {
            if (criteriaDto1.getLhs() != null) {
                lhsString = "";
                getLhsString(criteriaDto1.getLhs());
                String mergeType = "";
                if (criteriaDto1.getMergeType() != null) mergeType = criteriaDto1.getMergeType();
                if (criteriaDto1.getOperator().getType().equals("method") && criteriaDto1.getOperator().getName().equals("isNotEmpty")) {
                    if (checkStringType(criteriaDto1.getLhs()))
                        expression += "object." + lhsString + " != null and object." + lhsString + ".trim() != \"\" " + mergeType + " ";
                    else expression += "object." + lhsString + " != null " + mergeType + " ";
                } else if (criteriaDto1.getOperator().getType().equals("method") && criteriaDto1.getOperator().getName().equals("isEmpty")) {
                    if (checkStringType(criteriaDto1.getLhs()))
                        expression += "(object." + lhsString + " == null or object." + lhsString + " == \"\") " + mergeType + " ";
                    else expression += "object." + lhsString + " == null " + mergeType + " ";
                } else if (criteriaDto1.getOperator().getType().equals("method") && criteriaDto1.getLhs().getDataType().equalsIgnoreCase("Date")) {
                    expression += "object." + lhsString + " != null and ";
                    String dateString = String.format(" compareDates(%s, \"%s\", \"%s\") ", "object." + lhsString, criteriaDto1.getOperator().getName(), criteriaDto1.getRhs());
                    expression += dateString + mergeType + " ";
                } else if (criteriaDto1.getOperator().getType().equals("method")
                        && (!criteriaDto1.getOperator().getName().equals("isEmpty") && !criteriaDto1.getOperator().getName().equals("isNotEmpty"))
                        && !criteriaDto1.getLhs().getDataType().equalsIgnoreCase("Date")) {
                    if (checkStringType(criteriaDto1.getLhs()))
                        expression += "object." + lhsString + " != null and object." + lhsString + ".trim() != \"\" " + mergeType + " ";
                    else expression += "object." + lhsString + " != null " + mergeType + " ";
                    if (!mergeType.equals("")) expression += "object." + lhsString;
                    else expression += "and object." + lhsString;
                } else {
                    expression += "object." + lhsString + " ";
                }

            }
            if (criteriaDto1.getOperator() != null) {
                OperatorDto operatorDto = criteriaDto1.getOperator();
                if (operatorDto.getType().equals("operator")) {
                    if (criteriaDto1.getPerson() != null) {
                        if (criteriaDto1.getPerson().getName().equals("$loginuser"))
                            criteriaDto1.setRhs("subject.person.id");
                        else criteriaDto1.setRhs(criteriaDto1.getPerson().getId());
                    }
                    expression += operatorDto.getName() + " ";
                    if (criteriaDto1.getRhs() != null) {
                        if (isString) criteriaDto1.setRhs(String.format("'%s'", criteriaDto1.getRhs()));
                        if (criteriaDto1.getMergeType() != null)
                            expression += criteriaDto1.getRhs() + " " + criteriaDto1.getMergeType() + " ";
                        else expression += criteriaDto1.getRhs();
                    }
                } else if (operatorDto.getType().equals("method") && !(operatorDto.getName().equals("isEmpty") || operatorDto.getName().equals("isNotEmpty"))) {
                    if (criteriaDto1.getRhs() != null) {
                        if (isString) {
                            String stringValue = String.format("(\"%s\")", criteriaDto1.getRhs());
                            criteriaDto1.setRhs(operatorDto.getName() + stringValue);
                        } else {
                            String value;
                            value = String.format("(%s)", criteriaDto1.getRhs());
                            criteriaDto1.setRhs(operatorDto.getName() + value);
                        }
                        if (!criteriaDto1.getLhs().getDataType().equalsIgnoreCase("Date")) {
                            if (criteriaDto1.getMergeType() != null)
                                expression += "." + criteriaDto1.getRhs() + " " + criteriaDto1.getMergeType() + " ";
                            else expression += "." + criteriaDto1.getRhs();
                        }
                    }
                }
            }
        }
        return expression;
    }

    @Transactional(readOnly = true)
    private String getLhsString(LhsDto lhsDto) {
        isString = false;
        if (lhsDto != null) {
            if (lhsDto.getType().equals("ATTRIBUTE")) {
                lhsString += String.format(EvaluationConstraints.ATTRIBUTEMAP, lhsDto.getName());
            }
            if (lhsDto.getType().equals("PROPERTY")) {
                if(lhsDto.getDataType().equals("Enum")) lhsString += String.format(EvaluationConstraints.TOSTRING,lhsDto.getName());
                else lhsString += lhsDto.getName();
            }
            if (lhsDto.getObject() != null) {
                lhsString += ".";
                getLhsString(lhsDto.getObject());
            } else {
                if(checkStringType(lhsDto))
                    isString = true;
            }
        }
        return lhsString;
    }

    public boolean checkStringType(LhsDto lhsDto) {
        return lhsDto.getDataType().equals("String") || lhsDto.getDataType().equals("TEXT")
                || lhsDto.getDataType().equals("LONGTEXT") || lhsDto.getDataType().equals("RICHTEXT") || lhsDto.getDataType().equals("Enum");
    }
}
