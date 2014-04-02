package dst.ass1.jpa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WorkUnitCapicityValidator implements ConstraintValidator<WorkUnitCapacity, Integer> {
	
	private int min;
	private int max;

	@Override
	public void initialize(WorkUnitCapacity constraintAnnotation) {
		this.min = constraintAnnotation.min();
		this.max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		// TODO add message
		return (value >= min && value <= max);
	}

}
