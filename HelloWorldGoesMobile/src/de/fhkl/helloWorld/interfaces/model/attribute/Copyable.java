package de.fhkl.helloWorld.interfaces.model.attribute;

/**
 * Java interface Cloneable doesn't define the method clone! So here is the solution
 */
public interface Copyable<T extends Object> {
	
	/**
	 * @return a deep clone of this object
	 */
	public T clone();

}
