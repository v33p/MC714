package projects.PI.nodes.messages;

import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	public int hopToSource;
	public int senderID;

	
	public INFMessage(int hopToSource, int senderID) {
		this.hopToSource = hopToSource;
		this.senderID = senderID;
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(this.hopToSource,this.senderID);
	}

	public int getHopToSink() {
		return hopToSource;
	}

	public void setHopToSink(int hopToSource) {
		this.hopToSource = hopToSource;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
}
