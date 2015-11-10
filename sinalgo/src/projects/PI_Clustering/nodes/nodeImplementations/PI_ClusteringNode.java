package projects.PI_Clustering.nodes.nodeImplementations;
import java.awt.Color;
import java.awt.Graphics;
import projects.PI_Clustering.nodes.messages.clusteringMessage;
import projects.PI_Clustering.nodes.messages.INFMessage;
import projects.PI_Clustering.nodes.timers.EventTimer;
import projects.PI_Clustering.nodes.timers.MessageTimer;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;
import sinalgo.tools.logging.Logging;

public class PI_ClusteringNode extends Node {
	
	//******VARIAVEIS DO Nє **********///
	public enum Roles {SINK, COLLABORATOR, COORDINATOR, NULL };
	public Roles myRole;	
	private int hopToSink = 1000;
	int eventID=0;
	boolean clusterCandidate;
	int ownerID;
	int hopsToCoordinator;
	int ownerHopsToSink;
	boolean reached =false;
	
//**************************************************************//	
	//********PARAMETROS DE SIMULACAO*******/////
	public static int	eventsAmount;
	public static int	eventSize;
//*************************************************************//
	
	//********* Variaveis utilizadas para armazenar valores de logs ******//
	public static int overheads = 0;
	public static int overheadsClusters = 0;
	public static int detects = 0;
	
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;
		
		while(inbox.hasNext()) {
			Message msg = inbox.next();
			sender = inbox.getSender().ID;
			
			//Nodes that is receiving the message to configuration of HopToSink, increment the HoptoSink value of message and retransmit it	
			if(msg instanceof INFMessage) 
				this.InitialConfiguration(msg);
				
			if(msg instanceof clusteringMessage)
				this.ElectionLeader(msg);	
			
		}	
	}
	
	public boolean insideEvent(sinalgo.nodes.Position p, int eventID){
		double xc=0,yc=0, r=0;
		try{
			xc=sinalgo.configuration.Configuration.getDoubleParameter("Event/Xposition"+eventID);
			yc=sinalgo.configuration.Configuration.getDoubleParameter("Event/Yposition"+eventID);
			r = sinalgo.configuration.Configuration.getDoubleParameter("Event/EventSize");
		}catch (CorruptConfigurationEntryException e){
			e.printStackTrace();
			System.exit(0);			
		}
		
		if (Math.pow((xc - p.xCoord),2) + Math.pow((yc -p.yCoord),2) < Math.pow(r/2,2)){
			this.eventID = eventID;
			return true;
		}
		else 
			return false;
	}

	public void InitialConfiguration( Message msg){
		INFMessage msgINF = (INFMessage) msg;
		
		if(this.hopToSink > msgINF.getHopToSink() && !this.reached )
		{
			this.setColor(Color.GREEN);
			this.hopToSink = msgINF.getHopToSink() + 1;
			Tools.appendToOutput("No: "+this.ID+" PROGRAMANDO.\n");
			Tools.appendToOutput("Hops: "+this.hopToSink+"..\n");
			msgINF.setHopToSink(this.hopToSink);
			MessageTimer infMSG = new MessageTimer(msgINF);
			infMSG.startRelative(0.001,this);
			overheads = overheads + 1;
			this.reached = true;
		}
		
	}

	public void startDetection(){
		this.setColor(Color.blue);
		this.myRole = Roles.COORDINATOR;
		this.clusterCandidate = true;
		detects = detects + 1;
		this.hopsToCoordinator = 0;
		this.ownerID = this.ID;
		this.ownerHopsToSink = this.hopToSink;
		MessageTimer newEvent = new MessageTimer (new clusteringMessage(this.ID, this.hopToSink, this.eventID, this.hopsToCoordinator, this.ownerID, this.ownerHopsToSink));
		newEvent.startRelative(0.00001, this);
		overheads = overheads +1;
		overheadsClusters = overheadsClusters + 1;
	}
	
	public void ElectionLeader(Message msg){
		if (myRole == Roles.COORDINATOR){
			
			if( this.hopToSink > ((clusteringMessage)msg).getOwnerHopsToSink() ||
				(  (this.hopToSink == ((clusteringMessage)msg).getOwnerHopsToSink()) &&
				   (this.ownerID > ((clusteringMessage)msg).getOwnerID())
				)			
			  )
			{
				this.clusterCandidate = false;
				this.myRole = Roles.COLLABORATOR;
				this.ownerID = ((clusteringMessage)msg).getOwnerID();
				this.ownerHopsToSink = ((clusteringMessage)msg).getOwnerHopsToSink();
				this.hopsToCoordinator = ((clusteringMessage)msg).getHopsToCoordinator();
				this.setColor(Color.CYAN);
				
				MessageTimer newEvent = new MessageTimer (new clusteringMessage(this.ID, this.hopToSink, this.eventID, this.hopsToCoordinator+1, this.ownerID, this.ownerHopsToSink));
				newEvent.startRelative(0.0000001, this);				
				overheads = overheads + 1;
				overheadsClusters = overheadsClusters + 1;		
			}
		}else if (this.myRole == Roles.COLLABORATOR){
			if(  this.ownerHopsToSink  > ((clusteringMessage)msg).getOwnerHopsToSink() ||
				(this.ownerHopsToSink == ((clusteringMessage)msg).getOwnerHopsToSink() &&   
		         this.ownerID > ((clusteringMessage)msg).getOwnerID())
			  )
			{
				this.ownerID = ((clusteringMessage)msg).getOwnerID();
				this.ownerHopsToSink = ((clusteringMessage)msg).getOwnerHopsToSink();
				this.hopsToCoordinator = ((clusteringMessage)msg).getHopsToCoordinator();
				
				MessageTimer newEvent = new MessageTimer (new clusteringMessage(this.ID, this.hopToSink, this.eventID, this.hopsToCoordinator+1, this.ownerID, this.ownerHopsToSink));
				newEvent.startRelative(0.0000001, this);				
				overheads = overheads + 1;
				overheadsClusters = overheadsClusters + 1;
			
			}
		}
		
	}
	
    @Override
	public void init() {
    	
      	myRole = Roles.NULL;	
		try {
			eventsAmount = sinalgo.configuration.Configuration.getIntegerParameter("Event/NumEvents");
	
			///*** Schedule events to occur during the simulation ***///
			for (int i = 1; i <= eventsAmount; i++) {
				EventTimer t = new EventTimer(i);
				t.startEventAbsolute(sinalgo.configuration.Configuration.getDoubleParameter("Event/EventStart"+i), this, i);	
			}
			
			eventSize = sinalgo.configuration.Configuration.getIntegerParameter("Event/EventSize");
			
		}catch (CorruptConfigurationEntryException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(0);
		}
		
		//Creates message to start the setup hop's nodes
		if (this.ID==1){
			this.setColor(Color.RED);
			this.hopToSink = 0;
			this.myRole = Roles.SINK;
			MessageTimer infMSG = new MessageTimer (new INFMessage(this.hopToSink,this.ID));
	  		infMSG.startRelative(0.001, this);
	  		overheads = overheads +1;
	  		Tools.appendToOutput("No: "+this.ID+" Transmitiu inf.\n");
		}
	}

    
    
    
 //иииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииии//   
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
		super.drawNodeAsDiskWithText(g, pt, highlight, Integer.toString(this.hopToSink), 6, Color.WHITE);
		
		
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