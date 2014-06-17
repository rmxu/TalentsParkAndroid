package de.fhkl.helloWorld.interfaces.actions.messages;

import java.util.Collection;

import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.messages.Message;
import de.fhkl.helloWorld.interfaces.model.messages.body.RelationShipRequestBody;
import de.fhkl.helloWorld.interfaces.model.messages.body.SimpleTextMessageBody;

public interface IMessageListener {

	/*
	 * called when new friendshiprequests are available, notfies the user in gui
	 */
	public boolean friendShipRequestReceived(
			Collection<Message<RelationShipRequestBody>> m);

	/*
	 * called when user has decided to accept the friendshiprequest
	 */
	public void friendShipRequestAccepted(Message<RelationShipRequestBody> r,
			RelationShipType relation);

	/*
	 * called when user has decided to decline the friendshiprequest
	 */
	public void friendShipRequestDeclined(Message<RelationShipRequestBody> r);

	public boolean simpleTextMessageReceived(
			Collection<Message<SimpleTextMessageBody>> m);
}
