package de.fhkl.helloWorld.interfaces.model.parser;

import java.io.BufferedInputStream;

import org.w3c.dom.Document;

import de.fhkl.helloWorld.interfaces.model.messages.MessageWrapper;

public interface IMessageWrapperParser {

	/**
	 * Generates an MessageWrapper from a given XML document
	 * 
	 * @param is
	 * @return the account
	 */
	public MessageWrapper parse(BufferedInputStream is);

	/**
	 * Parses an MessageWrapper into XML
	 * 
	 * @param messageWrapper
	 * @return the document
	 */
	public Document parse(MessageWrapper messageWrapper);
}
