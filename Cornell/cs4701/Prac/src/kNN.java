import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.DoubleStream;

import enumerate.Action;
import structs.FrameData;

public class kNN {
	LinkedList<Tuple> data;
	int size;
	final int sim_threshold = 100;
	final int size_limit = 3000;
	boolean player;
	int toggled;
	int k = 5;

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
		return -Math.sqrt(Math.pow(relX_f2-relX_f1, 2) + Math.pow(relY_f2 - relY_f1, 2));
	}
	
	public Integer[] argsort( final double[] a )
    {
        Integer[] idx = new Integer[ a.length ];
        for ( int i = 0 ; i < a.length ; i++ )
        	idx[ i ] = new Integer( i );
        Arrays.sort
        ( idx, new Comparator<Integer>()
	        {
			    @Override public int compare( final Integer i1, final Integer i2)
			    {
			        return Double.compare( a[ i1 ], a[ i2 ] );
			    }
		    }
	    );
	    return idx;
    }
	
	public Deque<Action> getNearest(FrameData s){
		
		if(toggled == 0){
			System.out.println("KNN activated");
			toggled++;
		}
		double[] scores = new double[this.size];
		Iterator<Tuple> it = data.iterator();
		Tuple cur_tuple;
		Double max = null;
		Deque<Action> max_moves = null;

		for(int i = 0; i < this.size; i++){
			cur_tuple = it.next();
			scores[i] = computeSim(s, cur_tuple.fd);
//			if (max == null || scores[i] > max){
//				max = scores[i];
//				max_moves = cur_tuple.opp_act;
//			}
		}

		Integer[] argsorted = argsort(scores);
		double[] sorted = new double[k];
		double sum = 0;
		for (int i = 0; i < k; i++){
			sorted[i] = scores[argsorted[i]];
			sum += scores[argsorted[i]];
		}
		for(int i = 0; i < k; i++){
			sorted[i] /= sum;
		}
		double[] prob = new double[k];
		double sum_so_far = 0;
		for(int i = 0; i < k; i++){
			prob[i] = sum_so_far + prob[i];
			sum_so_far += prob[i];
		}
		
		double rn = Math.random();
		int idx;
		for(idx = 0; idx < k; idx++){
			if(rn>=prob[idx]){break;}
		}
		
		return data.get(idx).opp_act;
	}
	
	public void print_actions(Deque<Action> acts){
		System.out.println(acts == null);
		Iterator<Action> it = acts.iterator();
		Action ptr = it.next();
		while(ptr != null){
			System.out.println(ptr.name());
			ptr = it.next();
		}
		
	}

	public void record(FrameData fd, Deque<Action> a) throws Exception{
		if(a == null){System.out.println("adding null action");}
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