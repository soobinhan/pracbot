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
	private LinkedList<Key> key_queue = new LinkedList<Key>();


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
		if(!frameData.getEmptyFlag()){
			State s = new State(frameData,player);
			Move m = check_results(s);
			if(m!=null) System.out.println(m.get_result());
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
		return inputKey;
	}

	@Override
	public void processing() {
		// TODO Auto-generated method stub
		if(!frameData.getEmptyFlag()){
			State state = new State(frameData,player);
			//while knn is readying up
			if(key_queue.size()==0){
				LinkedList plan = QuickStart.decision(state);
				if(plan!=null) add_to_action_queue(plan);
			}else{
				inputKey = key_queue.poll();
			}
			//
			Move currMove = new Move(inputKey,state);
			if(!is_empty_key(inputKey)){
				actions.add(currMove);
			}
		}	
	}

	//UTILITY FUNCTIONS

	public Move check_results(State s){
		Move m = actions.peek();
		if (m==null) return null;
		//if it has been 20 frames since the move was performed
		if(m.get_state().get_frame() < s.get_frame() - 20){
			m = actions.removeLast();
			int nethp = 0;
			nethp =(s.get_hp() - m.get_state().get_hp()) 
					- (s.get_opp_hp() - m.get_state().get_opp_hp());
			if (nethp > 0) m.set_result(1);
			else if (nethp < 0) m.set_result(-1);
		}
		return m;
	}

	public boolean is_empty_key(Key k){
		if(k==null) return true;
		if(k.A || k.B || k.C || k.D || k.L || k.R || k.U) {
			//System.out.println("non empty key");
			return false;
		}
		return true;
	}

	public boolean is_same_key(Key k, Key k2){
		if(k==null||k2==null){
			return true;
		}
		if(k.A == k2.A && k.B == k2.B && k.C == k2.C
				&& k.D == k2.D && k.L == k2.L && k.R == k2.R && k.U == k2.U ){
			return true;
		}
		return false;
	}

	public void add_to_action_queue(LinkedList a){
		key_queue.addAll(a);
	}




}