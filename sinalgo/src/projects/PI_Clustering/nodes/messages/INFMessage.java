package projects.PI_Clustering.nodes.messages;

import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	public int hopToSink;
	public int senderID;

	
	public INFMessage(int hopToSource, int senderID) {
		this.hopToSink = hopToSource;
		this.senderID = senderID;
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(this.hopToSink,this.senderID);
	}

	public int getHopToSink() {
		return hopToSink;
	}

	public void setHopToSink(int hopToSink) {
		this.hopToSink = hopToSink;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
}
