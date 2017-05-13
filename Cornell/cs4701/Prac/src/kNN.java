import java.util.Deque;
import java.util.Iterator;

public class kNN {
	Deque<Tuple> data;
	int size;
	int sim_threshold;
	
	public kNN(){
		this.data = null;
		this.size = 0;
	}
	
	public float computeSim(State s1, State s2){
		return 0;
	}

	public Deque<Move> getNearest(State s){
		float[] scores = new float[this.size];
		Iterator<Tuple> it = data.iterator();
		Tuple cur_tuple;
		float max = 0;
		Deque<Move> max_moves = null;
		
		for(int i = 0; i < this.size; i++){
			cur_tuple = it.next();
			scores[i] = computeSim(s, cur_tuple.state);
			
			if (scores[i] > max){
				max = scores[i];
				max_moves = cur_tuple.moves;
			}
		}
		return max_moves;
	}
	
	
	public class Tuple{
		public State state;
		public Deque<Move> moves;
		Tuple(State s, Deque<Move> m){
			this.state = s;
			this.moves = m;
		}
	}
}
