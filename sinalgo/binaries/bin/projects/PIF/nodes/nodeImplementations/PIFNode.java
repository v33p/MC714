package projects.PIF.nodes.nodeImplementations;
import java.awt.Color;
import java.awt.Graphics;

import projects.PIF.nodes.messages.FEEDBACKMessage;
import projects.PIF.nodes.messages.INFMessage;
import projects.PIF.nodes.timers.PIF_FeedbackTimer;
import projects.PIF.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class PIFNode extends Node {
	private boolean reached = false;
	
	private boolean leafNode = true;
	private int hopToSource = 10000;
	private int childrenNodes = 0;
	private int receivedNodes = 0;
	
	public enum TNO {TNO_FEEDBACK };
	private PIF_FeedbackTimer feedbackTimer;
	
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;
		
		while(inbox.hasNext()) {
			Message msg = inbox.next();
			sender = inbox.getSender().ID;
			
			//Nó recebeu uma mensagem INF	
			if(msg instanceof INFMessage) {
				Tools.appendToOutput("No: "+this.ID+" Recebeu INF do no "+sender+"\n");
				
				INFMessage msgINF = (INFMessage) msg;
				
				if(!this.reached && this.hopToSource > msgINF.getHopToSource() )
				{
					this.setColor(Color.GREEN);
					this.reached = true;	
					this.hopToSource = msgINF.getHopToSource() + 1;
					msgINF.setHopToSource(this.hopToSource);
					MessageTimer infMSG = new MessageTimer(msgINF);
					infMSG.startRelative(0.1,this);
					Tools.appendToOutput("No: "+this.ID+" Transmitiu inf.\n");
					feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK);
					feedbackTimer.tnoStartRelative(10, this, TNO.TNO_FEEDBACK);	
				}
				
				//Nó mais distante que transmitiu - Armazeno como filho
				if(this.hopToSource < msgINF.getHopToSource()){
					this.setColor(Color.blue);
					this.childrenNodes = this.childrenNodes + 1;
					this.leafNode = false;
				}	
			}
			
			//Mensagem de Confirmação
			if(msg instanceof FEEDBACKMessage) {
				FEEDBACKMessage msgFeedback = (FEEDBACKMessage) msg;
				
				if(this.hopToSource < msgFeedback.getHopToSource() )
				{
					this.receivedNodes = this.receivedNodes + 1;
					
					if(this.receivedNodes == this.childrenNodes){
						msgFeedback.setHopToSource(this.hopToSource);
						MessageTimer feedbackMSG = new MessageTimer(msgFeedback);
						feedbackMSG.startRelative(0.1,this);
						this.setColor(Color.yellow);
						Tools.appendToOutput("Node: "+this.ID+" Recv Feedback.\n");
					}
				}
				
				
			}
			
		}
		
	}
		
	public void feedbackFromLeafNodes(){
		if (this.leafNode){
			this.setColor(Color.MAGENTA);
			MessageTimer feedbackMSG = new MessageTimer (new FEEDBACKMessage(this.hopToSource,this.ID));
	  		feedbackMSG.startRelative(0.1, this);
	  		Tools.appendToOutput("Leaf Node: "+this.ID+" Transmitiu feedback.\n");
		}
		
	}
	
	public void timeout(TNO tno){
		switch(tno){
			case TNO_FEEDBACK:
				feedbackFromLeafNodes();
				break;
			
		}
	}

    @Override
	public void init() {
		//Considerando que o nó 1 tem a mensagem inf
		if (this.ID==1){
			this.setColor(Color.RED);
			this.hopToSource = 0;
			this.leafNode = false;
			this.reached = true;
			MessageTimer infMSG = new MessageTimer (new INFMessage(this.hopToSource,this.ID));
	  		infMSG.startRelative(0.1, this);
	  		Tools.appendToOutput("No: "+this.ID+" Transmitiu inf.\n");
		}
	}
    
	@Override
	public void postStep() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preStep() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		// TODO Auto-generated method stub
		//if (this.ID == 1) highlight = true;
		super.drawNodeAsDiskWithText(g, pt, highlight, Integer.toString(this.hopToSource), 6, Color.WHITE);
		
		
	}
	
	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub
		
	}

}