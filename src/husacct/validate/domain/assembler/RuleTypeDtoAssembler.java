package husacct.validate.domain.assembler;

import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.List;

public class RuleTypeDtoAssembler {

	private ViolationTypeDtoAssembler violationtypeassembler;

	public RuleTypeDtoAssembler() {
		this.violationtypeassembler = new ViolationTypeDtoAssembler();
	}

	public RuleTypeDTO[] createRuleTypeDTO(List<RuleType> rules) {
		RuleTypeDTO[] returnValue;
		List<RuleTypeDTO> ruletypes = new ArrayList<RuleTypeDTO>();
		for (RuleType rule : rules) {
			RuleTypeDTO dto = createRuleTypeDTOWithViolationtypes(rule);
			ruletypes.add(dto);
		}
		returnValue = ruletypes.toArray(new RuleTypeDTO[ruletypes.size()]);
		return returnValue;
	}

	private RuleTypeDTO createRuleTypeDTOWithViolationtypes(RuleType rule) {
		String key = rule.getKey();
		String description = rule.getDescriptionKey();
		ViolationTypeDTO[] violationtypes = createViolationTypeDTO(rule.getViolationTypes());
		final RuleTypeDTO[] exceptionRuleTypes = createExceptionRuleTypeDTO(rule);

		return new RuleTypeDTO(key, description, violationtypes, exceptionRuleTypes);
	}

	private ViolationTypeDTO[] createViolationTypeDTO(List<ViolationType> violationTypes) {
		return violationtypeassembler.createViolationTypeDTO(violationTypes);
	}

	public RuleTypeDTO createRuleTypeDTO(RuleType rule, ViolationType violationtype) {
		String key = rule.getKey();
		String description = rule.getDescriptionKey();
		ViolationTypeDTO violationtypeDTO = violationtypeassembler.createViolationTypeDTO(violationtype);
		final RuleTypeDTO[] exceptionRuleTypes = createExceptionRuleTypeDTO(rule);

		return new RuleTypeDTO(key, description, new ViolationTypeDTO[] {violationtypeDTO}, exceptionRuleTypes);
	}

	private RuleTypeDTO[] createExceptionRuleTypeDTO(RuleType rule) {
		List<RuleTypeDTO> exceptionRuleTypes = new ArrayList<RuleTypeDTO>();
		if (rule.getExceptionRules() != null) {
			for (RuleType ruletype : rule.getExceptionRules()) {
				final RuleTypeDTO exceptionRuleType = createRootRuleTypeDTOWithViolationtypes(ruletype);
				exceptionRuleTypes.add(exceptionRuleType);
			}
		}
		return exceptionRuleTypes.toArray(new RuleTypeDTO[] {});
	}

	private RuleTypeDTO createRootRuleTypeDTOWithViolationtypes(RuleType rule) {
		String key = rule.getKey();
		String description = rule.getDescriptionKey();
		ViolationTypeDTO[] violationtypes = createViolationTypeDTO(rule.getViolationTypes());

		return new RuleTypeDTO(key, description, violationtypes, new RuleTypeDTO[] {});
	}
}