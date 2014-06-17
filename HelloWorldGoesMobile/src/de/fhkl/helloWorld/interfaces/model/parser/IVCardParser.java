package de.fhkl.helloWorld.interfaces.model.parser;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Collection;

import de.fhkl.helloWorld.interfaces.model.account.hCard.HCard;

public interface IVCardParser {
	
	/**
	 * Reads one VCard or a complete adress book from a file and returns an HCard Object representing the VCard.
	 * @param is
	 * @return
	 */
	public Collection<HCard> parseFromFile(FileInputStream is);
	
	/**
	 * Generates VCard objects from the given HCards and returns an OutputStream to write it where you like to.
	 * @param hcard
	 * @return
	 */
	public OutputStream parseToFile(Collection<HCard> hcard);
	
	/**
	 * Copies the attributes from source to dest. Detectes duplicates in simples attributes like EMAIL and TEL
	 * by comparing the value strings. If override is set to ture, unique values like N, FN and BDAY are overriden
	 * with the attributes from source.
	 * @param dest
	 * @param source
	 * @param override
	 */
	public void joinHcards(HCard dest, HCard source, boolean override);
}
