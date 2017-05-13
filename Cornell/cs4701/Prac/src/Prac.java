import structs.FrameData;
import structs.GameData;
import structs.Key;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.activation.MailcapCommandMap;

import fighting.Attack;
import gameInterface.AIInterface;


public class Prac implements AIInterface {
	
	private Key inputKey;
	private boolean player;
	private FrameData frameData;
	private LinkedList<Move> actions = new LinkedList<Move>();

	
	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getCharacter() {
		// TODO Auto-generated method stub
		return CHARACTER_ZEN;
	}

	@Override
	public void getInformation(FrameData frameData) {
		// TODO Auto-generated method stub
		this.frameData = frameData;
		try{
		State s = new State(frameData,player);
		Move m = check_results(s);
		System.out.println(m.get_result());
		}catch(Exception e){
			System.out.println("Could not fetch framedata");
		}
	}

	@Override
	public int initialize(GameData arg0, boolean player) {
		// TODO Auto-generated method stub
		inputKey = new Key();
		this.player = player;
		frameData = new FrameData();
		return 0;
	}

	@Override
	public Key input() {
		// TODO Auto-generated method stub
		try{
		State state = new State(frameData,player);
		Move currMove = new Move(inputKey,state);
		actions.add(currMove);
		}catch(Exception e){
			System.out.println("could not fetch frameData");
		}
		return inputKey;
	}

	@Override
	public void processing() {
		// TODO Auto-generated method stub
		//create the environment vector
		
		if(!frameData.getEmptyFlag()){
			inputKey = frameData.getKeyData().getOpponentKey(player);
			inputKey.R = inputKey.R ? false : true;
			inputKey.L = inputKey.L ? false : true;
		}
	}
	
	public Move check_results(State s){
		Move m = actions.peek();
		if (m==null) return null;
		//if it has been 20 frames since the move was performed
		if(m.get_state().get_frame() < s.get_frame() - 20){
			m = actions.poll();
			int nethp = 0;
			nethp =(s.get_hp() - m.get_state().get_hp()) 
					- (s.get_opp_hp() - m.get_state().get_opp_hp());
			if (nethp > 0) m.set_result(1);
			else if (nethp < 0) m.set_result(-1);
		}
		return m;
	}
	

}