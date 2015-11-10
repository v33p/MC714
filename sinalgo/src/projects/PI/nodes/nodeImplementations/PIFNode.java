package projects.PI.nodes.nodeImplementations;
import java.awt.Color;
import java.awt.Graphics;
import projects.PIF.nodes.messages.INFMessage;
import projects.PIF.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class PIFNode extends Node {
	private boolean reached = false;
	private int hopToSource;
	private int soonList;
		
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;
		
		while(inbox.hasNext()) {
			Message msg = inbox.next();
			sender = inbox.getSender().ID;
			
			//Nó recebeu uma mensagem INF	
			if(msg instanceof INFMessage) {
				//Verifica se é a primeira vez que o nó recebe INF
				if(!this.reached){
					this.setColor(Color.GREEN);
					this.reached = true;	
					MessageTimer infMSG = new MessageTimer(msg);
					infMSG.startRelative(1,this);	
				}
			}
			
		}
		
	}
		

    @Override
	public void init() {
		//Considerando que o nó 1 tem a mensagem inf
		if (this.ID==1){
			this.setColor(Color.RED);
			this.hopToSource = 0;
			MessageTimer infMSG = new MessageTimer (new INFMessage(1,this.ID));
	  		infMSG.startRelative(0.1, this);
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
		if (this.ID == 1) highlight = true;
		super.drawNodeAsDiskWithText(g, pt, highlight, Integer.toString(this.ID), 8, Color.WHITE);
		
		
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