package dst.ass2.di.agent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import dst.ass2.di.annotation.Component;

public class InjectorAgent implements ClassFileTransformer {
	
	private Instrumentation instrumentation;

	public InjectorAgent(String args, Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
		this.instrumentation.addTransformer(this);
	}

	public static void premain(String args, Instrumentation instrumentation) {
        new InjectorAgent(args, instrumentation);
    }
	
	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		
		String modifiedClassName = className.replaceAll("/", ".");
		
		ClassPool classPool = ClassPool.getDefault();
		CtClass ctClass;
		try {
			ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
			
			Component componentAnnotation = (Component) ctClass.getAnnotation(Component.class);
			
			if(componentAnnotation != null) {
				for(CtConstructor ctConstructor : ctClass.getConstructors()) {
					ctConstructor.insertAfter("dst.ass2.di.InjectionControllerFactory.getTransparentInstance().initialize(this);");
					System.out.println("inject constructor to " + modifiedClassName);
				}
				return ctClass.toBytecode();
			} else {
				return classfileBuffer;
			}
		} catch (IOException e) {
			throw new IllegalClassFormatException(e.getMessage());
		} catch (RuntimeException e) {
			throw new IllegalClassFormatException(e.getMessage());
		} catch (CannotCompileException e) {
			throw new IllegalClassFormatException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new IllegalClassFormatException(e.getMessage());
		}

	}

}
