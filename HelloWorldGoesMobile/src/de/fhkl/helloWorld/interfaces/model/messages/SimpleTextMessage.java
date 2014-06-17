package de.fhkl.helloWorld.interfaces.model.messages;

public class SimpleTextMessage {

	private MessageHead messageHead;

	private String title;

	private String text;

	public SimpleTextMessage() {

	}

	public SimpleTextMessage(String title, String text) {
		this.title = title;
		this.text = text;
	}

	public MessageHead getMessageHead() {
		return messageHead;
	}

	public void setMessageHead(MessageHead messageHead) {
		this.messageHead = messageHead;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
