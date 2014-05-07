package dst.ass2.di.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;

import dst.ass2.di.IInjectionController;
import dst.ass2.di.annotation.ComponentId;

public class TransparentInjectionController extends InjectionController implements IInjectionController {
	
	public TransparentInjectionController() {
		super();
	}

	@Override
	protected void setComponentId(Object obj) throws IllegalArgumentException, IllegalAccessException {
		long currentId = -1;
		for(Field field : getAllFields(obj.getClass(), new LinkedList<Field>())) {
			Annotation annotation = field.getAnnotation(ComponentId.class);
			if(annotation != null && field.get(obj) == null) {
				field.setAccessible(true);
				if(currentId < 0) {
					currentId = getId();
				}
				field.set(obj, currentId);
			}
		}
	}
	
}
