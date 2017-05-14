import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import enumerate.Action;
import structs.FrameData;

public class kNN {
	Deque<Tuple> data;
	int size;
	final int sim_threshold = 100;
	final int size_limit = 3000;
	boolean player;
	int toggled;

	public kNN(boolean player){
		this.data = new LinkedList<Tuple>();
		this.size = 0;
		this.player = player;
		this.toggled = 0;
	}

	public boolean isReady(){
		return this.size >= sim_threshold;
	}

	public double computeSim(FrameData f1, FrameData f2){
		int relX_f1 = Math.abs(f1.getOpponentCharacter(player).left -
							         f1.getOpponentCharacter(player).left);
		int relY_f1 = Math.abs(f1.getOpponentCharacter(player).bottom -
								       f1.getOpponentCharacter(player).bottom);
		int relX_f2 = Math.abs(f1.getMyCharacter(player).left -
										   f1.getMyCharacter(player).left);
		int relY_f2 = Math.abs(f1.getMyCharacter(player).bottom -
											 f1.getMyCharacter(player).bottom);
		return Math.sqrt(Math.pow(relX_f2-relX_f1, 2) + Math.pow(relY_f2 - relY_f1, 2));
	}

	public Deque<Action> getNearest(FrameData s){
		
		if(toggled == 0){
			System.out.println("KNN activated");
			toggled++;
		}
		double[] scores = new double[this.size];
		Iterator<Tuple> it = data.iterator();
		Tuple cur_tuple;
		double max = 0;
		Deque<Action> max_moves = null;

		for(int i = 0; i < this.size; i++){
			cur_tuple = it.next();
			scores[i] = computeSim(s, cur_tuple.fd);

			if (scores[i] > max){
				max = scores[i];
				max_moves = cur_tuple.opp_act;
			}
		}
		print_actions(max_moves);
		return max_moves;
	}
	
	public void print_actions(Deque<Action> acts){

		Iterator<Action> it = acts.iterator();
		Action ptr = it.next();
		while(ptr != null){
			System.out.println(ptr.name());
		}
		
	}

	public void record(FrameData fd, Deque<Action> a) throws Exception{
		if(a == null){
			throw new Exception();
		}
		this.data.push(new Tuple(fd, a));
		this.size++;
	}

	public class Tuple{
		public FrameData fd;
		public Deque<Action> opp_act;
		Tuple(FrameData fd, Deque<Action> a){
			this.fd = fd;
			this.opp_act = a;
		}
	}
}