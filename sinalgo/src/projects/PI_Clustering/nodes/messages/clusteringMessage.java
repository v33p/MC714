package projects.PI_Clustering.nodes.messages;

import sinalgo.nodes.messages.Message;

public class clusteringMessage extends Message {
	
	private final int senderID;
	private final int hopsToSink;
	private final int eventID;
	private final int hopsToCoordinator;
	private final int ownerID;
	private final int ownerHopsToSink;
	
	
	public clusteringMessage(int senderID, int hopsToSink, int eventID, int hopsToCoordinator, int ownerID, int ownerHopsToSink) {
		super();
		this.senderID = senderID;
		this.hopsToSink = hopsToSink;
		this.eventID = eventID;
		this.hopsToCoordinator = hopsToCoordinator;
		this.ownerID = ownerID;
		this.ownerHopsToSink = ownerHopsToSink;
	}
	
	

	public int getSenderID() {
		return senderID;
	}

	public int getHopsToSink() {
		return hopsToSink;
	}

	public int getEventID() {
		return eventID;
	}

	public int getHopsToCoordinator() {
		return hopsToCoordinator;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public int getOwnerHopsToSink() {
		return ownerHopsToSink;
	}
	

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return this;
	}

}
