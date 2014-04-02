package dst.ass1.jpa.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {WorkUnitCapicityValidator.class})
public @interface WorkUnitCapacity {
	
	String message() default "{dst.ass1.jpa.validator.WorkUnitCapacity.message}";
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	int min() default 0;
	
	int max() default Integer.MAX_VALUE;
}
