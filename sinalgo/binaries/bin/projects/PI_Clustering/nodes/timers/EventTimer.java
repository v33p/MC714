package projects.PI_Clustering.nodes.timers;

import projects.PI_Clustering.nodes.nodeImplementations.PI_ClusteringNode;
import sinalgo.nodes.Node;
import sinalgo.nodes.timers.Timer;

public class EventTimer extends Timer {
	
	int eventID = -1;
	
	public EventTimer(int eventID){
		this.eventID = eventID;
	}
	
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		if(((PI_ClusteringNode)getTargetNode()).insideEvent(getTargetNode().getPosition(),this.eventID))
			((PI_ClusteringNode)getTargetNode()).startDetection();
	}
	public void startEventAbsolute(double absoluteTime, Node n, int eventID){
		this.eventID = eventID;
		startAbsolute(absoluteTime, n);	
	}

}
