import java.util.Deque;
import java.util.Iterator;

import enumerate.Action;
import structs.FrameData;

public class kNN {
	Deque<Tuple> data;
	int size;
	final int sim_threshold = 100;
	final int size_limit = 3000;
	boolean player;
	
	public kNN(boolean player){
		this.data = null;
		this.size = 0;
		this.player = player;
	}
	
	public boolean isReady(){
		return this.size >= sim_threshold;
	}
	
	public float computeSim(State s1, State s2){
		return 0;
	}

	public Deque<Action> getNearest(State s){
		float[] scores = new float[this.size];
		Iterator<Tuple> it = data.iterator();
		Tuple cur_tuple;
		float max = 0;
		Deque<Action> max_moves = null;
		
		for(int i = 0; i < this.size; i++){
			cur_tuple = it.next();
			scores[i] = computeSim(s, cur_tuple.state);
			
			if (scores[i] > max){
				max = scores[i];
				max_moves = cur_tuple.opp_act;
			}
		}
		return max_moves;
	}
	
	public void record(FrameData fd, Deque<Action> a){
		this.data.push(new Tuple(new State(fd, this.player), a));
		this.size++;
	}
	
	public class Tuple{
		public State state;
		public Deque<Action> opp_act;
		Tuple(State s, Deque<Action> a){
			this.state = s;
			this.opp_act = a;
		}
	}
}
