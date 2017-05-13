import java.util.LinkedList;

import structs.Key;

public class Move {
	
	private LinkedList<Key> action;
	private State state;
	private int result;
	
	public Move(LinkedList<Key> a, State s){
		action = a;
		state = s;
		result = 0;
	}
	
	public LinkedList<Key> get_action(){
		return action;
	}
	
	public State get_state(){
		return state;
	}
	
	public int get_result(){
		return result;
	}
	
	public void set_result(int r){
		result = r;
	}

}
