import structs.Key;

public class Move {
	
	private Key key;
	private State state;
	private int result;
	
	public Move(Key k, State s){
		key = k;
		state = s;
		result = 0;
	}
	
	public Key get_key(){
		return key;
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
