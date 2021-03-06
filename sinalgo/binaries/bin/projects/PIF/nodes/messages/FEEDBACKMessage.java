package projects.PIF.nodes.messages;

import sinalgo.nodes.messages.Message;

public class FEEDBACKMessage extends Message {
	public int hopToSource;
	public int senderID;

	
	public FEEDBACKMessage(int hopToSource, int senderID) {
		this.hopToSource = hopToSource;
		this.senderID = senderID;
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new FEEDBACKMessage(this.hopToSource,this.senderID);
	}

	public int getHopToSource() {
		return hopToSource;
	}

	public void setHopToSource(int hopToSource) {
		this.hopToSource = hopToSource;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
}
