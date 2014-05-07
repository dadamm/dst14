package dst.ass2.di.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import dst.ass2.di.IInjectionController;
import dst.ass2.di.InjectionException;
import dst.ass2.di.annotation.Component;
import dst.ass2.di.annotation.ComponentId;
import dst.ass2.di.annotation.Inject;
import dst.ass2.di.model.ScopeType;

public class InjectionController implements IInjectionController {
	
	private static AtomicLong id_seq;
	private ConcurrentMap<Class<?>, Object> singletonMap;

	public InjectionController() {
		id_seq = new AtomicLong(0L);
		singletonMap = new ConcurrentHashMap<Class<?>, Object>();
	}

	private Annotation getAnnotation(Annotation[] annotations, Class<? extends Annotation> clazz) {
		for(Annotation annotation : annotations) {
			if(annotation.annotationType().equals(clazz)) return annotation;
		}
		return null;
	}
	
	protected List<Field> getAllFields(Class<?> clazz, List<Field> fields) {
		Class<?> c = clazz.getSuperclass();
		if(c != null) {
			getAllFields(c, fields);
		}
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		return fields;
	}
	
	protected long getId() {
		return id_seq.getAndIncrement();
	}
	
	protected void setComponentId(Object obj) throws IllegalArgumentException, IllegalAccessException {
		long id = getId();
		for(Field field : getAllFields(obj.getClass(), new LinkedList<Field>())) {
			Annotation annotation = field.getAnnotation(ComponentId.class);
			if(annotation != null) {
				field.setAccessible(true);
				field.set(obj, id);
			}
		}
	}
	
	private boolean isCorrectComponentAnnotation(Class<?> clazz, ScopeType requiredScopeType) throws InjectionException {
		Annotation componentAnnotation = getAnnotation(clazz.getDeclaredAnnotations(), Component.class);
		if(componentAnnotation == null) {
			throw new InjectionException("Class " + clazz.getName() + " must be annotated with @Component for injection.");
		}
		
		if(!((Component) componentAnnotation).scope().equals(requiredScopeType)) {
			throw new InjectionException("It is not possible to to call method. Class " + getClass().getName() + " must have scope " + requiredScopeType.toString());
		}
		return true;
	}
	
	private void injectFields(Object obj) throws IllegalArgumentException, IllegalAccessException, InjectionException {
		for(Field field : getAllFields(obj.getClass(), new LinkedList<Field>())) {
			Annotation annotation = field.getAnnotation(Inject.class);
			if(annotation != null) {
				Inject inject = (Inject) annotation;
				field.setAccessible(true);
				
				Component componentAnnotation;
				Class<?> clazz;
				
				if(inject.specificType().equals(Void.class)) {
					clazz = field.getType();
					componentAnnotation = (Component) getAnnotation(clazz.getDeclaredAnnotations(), Component.class);
				} else {
					clazz = inject.specificType();
					componentAnnotation = (Component) getAnnotation(clazz.getDeclaredAnnotations(), Component.class);
				}
				
				if (componentAnnotation != null) {
					if (componentAnnotation.scope().equals(ScopeType.SINGLETON)) {
						field.set(obj, getSingletonInstance(clazz));
					} else {
						field.set(obj, getPrototypeInstance(clazz));
					}
				}
			}
		}
	}
	
	@Override
	public void initialize(Object obj) throws InjectionException {
		Component componentAnnotation = (Component) getAnnotation(obj.getClass().getDeclaredAnnotations(), Component.class);
		if(componentAnnotation == null) throw new InjectionException("Component Annotation is missing.");
		if(componentAnnotation.scope().equals(ScopeType.SINGLETON)) {
			if(singletonMap.containsKey(obj.getClass())) {
				throw new InjectionException("Not possible to initialize a singelton a second time.");
			} 
		} else if(!isCorrectComponentAnnotation(obj.getClass(), ScopeType.PROTOTYPE)) {
			throw new InjectionException("Class " + obj.getClass().getName() + " is not correct annotated.");
		}
		try {
			getInstance(obj, componentAnnotation.scope());
		} catch (IllegalArgumentException e) {
			new InjectionException("Error initializing class " + obj.getClass());
		}
	}
	
	private <T> T getInstance(T obj, ScopeType scopeType) {
		try {
			setComponentId(obj);
			injectFields(obj);
			if(scopeType.equals(ScopeType.SINGLETON)) singletonMap.put(obj.getClass(), obj);
			return obj;
		} catch (IllegalAccessException e) {
			throw new InjectionException("Cannot inject fields in class: " + obj.getClass().getName());
		}
	}
	
	private <T> T getPrototypeInstance(Class<T> clazz) throws InjectionException {
		if(!isCorrectComponentAnnotation(clazz, ScopeType.PROTOTYPE)) {
			throw new InjectionException("Class " + clazz.getName() + " is not correct annotated.");
		}
		
		try {
			return getInstance(clazz.newInstance(), ScopeType.PROTOTYPE);
		} catch (InstantiationException e) {
			throw new InjectionException("Error at creating new instance from class: " + clazz.getName());
		} catch (IllegalAccessException e) {
			throw new InjectionException("Error at creating new instance from class: " + clazz.getName());
		}
	}

	@Override
	public <T> T getSingletonInstance(Class<T> clazz) throws InjectionException {
		if(!isCorrectComponentAnnotation(clazz, ScopeType.SINGLETON)) {
			throw new InjectionException("Class " + clazz.getName() + " is not correct annotated.");
		}
		
		if(singletonMap.containsKey(clazz)) {
			@SuppressWarnings("unchecked")
			T obj = (T) singletonMap.get(clazz);
			return obj;
		} else {
			try {
				return getInstance(clazz.newInstance(), ScopeType.SINGLETON);
			} catch (InstantiationException e) {
				throw new InjectionException("Error at creating new instance from class: " + clazz.getName());
			} catch (IllegalAccessException e) {
				throw new InjectionException("Error at creating new instance from class: " + clazz.getName());
			}
		}
	}
	
}
