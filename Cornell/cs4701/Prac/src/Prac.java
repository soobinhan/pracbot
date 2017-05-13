import structs.FrameData;
import structs.GameData;
import structs.Key;

import java.util.Deque;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.activation.MailcapCommandMap;

import enumerate.Action;
import fighting.Attack;
import gameInterface.AIInterface;
import simulator.Simulator;


public class Prac implements AIInterface {

	private Key inputKey;
	private boolean player;
	private FrameData frameData;
	private LinkedList<Move> actions = new LinkedList<Move>();

	private FrameData base_fd;
	private Simulator simulator;
	private int simulationLimit = 20;
	private LinkedList<Key> key_queue = new LinkedList<Key>();
	private kNN knn;                         // knn
	private Deque<Action> act_to_be_flushed; // to be recorded to knn
	private FrameData fd_to_be_flushed;      // to be recorded to knn
	private boolean action_ended;            // indicates first frame after action ended
	private Deque<Action> to_exec;           // actions to be executed

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
	public int initialize(GameData gd, boolean player) {
		// TODO Auto-generated method stub
		this.inputKey = new Key();
		this.player = player;
		this.frameData = new FrameData();
		this.simulator = new Simulator(gd);
		this.knn = new kNN(this.player);
		this.action_ended = true;
		Toolkit.set_up_actions();
		return 0;
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

		if (this.frameData.getFrameNumber() % 10 == 0){
			// At the 10th frame
			this.knn.record(fd_to_be_flushed, act_to_be_flushed); // record the deq of actions
			this.act_to_be_flushed = new LinkedList<Action>(); // reset action to be flushed
			fd_to_be_flushed = null; // reset fd to be flushed
		}else{
			// Other frames
			if(fd_to_be_flushed == null){
				fd_to_be_flushed = frameData; // begin collecting
			}
			this.act_to_be_flushed.push(frameData.getOpponentCharacter(player).getAction());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void processing() {

		if(!frameData.getEmptyFlag() && frameData.getRemainingTime() > 0){
			State state = new State(frameData,player);


			if(this.knn.isReady()){ // knn is ready to roll
				if(action_ended){ // Action executed
					action_ended = false;
					// Predict opp action
					Deque<Action> opp_act = knn.getNearest(state);
					// Construct potential actions
					Deque<Action>[] potential_actions = Toolkit.get_options();
					to_exec = tree_search(potential_actions, opp_act, frameData);
				}

			}else{ // knn isn't hot yet
//				LinkedList plan = QuickStart.decision(state);
//				if(plan!=null) add_to_action_queue(plan);
//

			}
		}
	}

	@Override
	public Key input() {

		Action curr;
		//DQ
		if(actions.peek()==null){
			return new Key();
		}
		curr = actions.poll();
		//map act -> key
		cmd.commandCall(curr.toString());
		inputKey = cmd.getSkillKey();
		//return key

		return inputKey;
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

	public int net_HP_change(FrameData fd_before, FrameData fd_after){
		int delta_my = fd_after.getMyCharacter(player).getHp() -
				fd_before.getMyCharacter(player).getHp();
		int delta_opp = fd_after.getOpponentCharacter(player).getHp() -
				fd_before.getOpponentCharacter(player).getHp();
		return delta_my - delta_opp;
	}

	public Deque<Action> tree_search(Deque<Action>[] act_list, Deque<Action> opp_act, FrameData cur_fd){

		Deque<Action> best_action = null;
		Integer best_change = null;
		int cur_change;
		FrameData sim_fd;

		for (int i = 0; i< act_list.length; i++){
			sim_fd = simulator.simulate(cur_fd, player, act_list[i], opp_act, simulationLimit);
			cur_change = net_HP_change(cur_fd, sim_fd);
			if(best_action == null || best_change < cur_change){
				best_change = cur_change;
				best_action = act_list[i];
			}
		}

		return best_action;
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
