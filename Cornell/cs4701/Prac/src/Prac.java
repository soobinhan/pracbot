import structs.FrameData;
import structs.GameData;
import structs.Key;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.activation.MailcapCommandMap;

import commandcenter.CommandCenter;
import enumerate.Action;
import fighting.Attack;
import gameInterface.AIInterface;
import simulator.Simulator;


public class Prac implements AIInterface {

	private Key inputKey;
	private boolean player;
	private FrameData frameData;
	//private LinkedList<Move> actions = new LinkedList<Move>();

	private FrameData base_fd;
	private Simulator simulator;
	private int simulationLimit = 120;
	private LinkedList<Key> key_queue = new LinkedList<Key>();
	private kNN knn;                         // knn
	private Deque<Action> act_to_be_flushed; // to be recorded to knn
	private FrameData fd_to_be_flushed;      // to be recorded to knn
	private boolean action_ended;            // indicates first frame after action ended
	private Deque<Action> to_exec;           // actions to be executed
	private CommandCenter cmd;

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
		this.cmd = new CommandCenter();
		Toolkit.set_up_actions();
		this.act_to_be_flushed = new LinkedList<Action>();
		this.fd_to_be_flushed = null;
		System.out.println("I am aliveeee");
		return 0;
	}

	@Override
	public void getInformation(FrameData frameData) {
		// TODO Auto-generated method stub
		this.frameData = frameData;
		this.cmd.setFrameData(frameData, player);
		if(!frameData.getEmptyFlag()){
			if (fd_to_be_flushed != null && act_to_be_flushed !=  null 
					&& this.frameData.getFrameNumber() % 10 == 0){
				// At the 10th frame
				try{this.knn.record(fd_to_be_flushed, act_to_be_flushed);} // record the deq of actions
				catch(Exception e){}
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
	}

	@SuppressWarnings("deprecation")
	@Override
	public void processing() {
		
		if(!frameData.getEmptyFlag() && frameData.getRemainingTime() > 0){

			if(this.knn.isReady()){ // knn is ready to roll
				if(action_ended){ // Action executed
					action_ended = false;
					// Predict opp action
					Deque<Action> opp_act = knn.getNearest(frameData);
					// Construct potential actions
					Deque<Action>[] potential_actions = Toolkit.get_options();
					Deque<Action> copy = new LinkedList<Action>();
					copy.addAll(tree_search(potential_actions, opp_act, frameData));
					to_exec = copy;
				}

			}else{ // knn isn't hot yet
				if(action_ended){
					action_ended = false;
					Deque<Action> copy = new LinkedList<Action>();
					copy.addAll(QuickStart.decision(frameData, player, Toolkit.get_options()));
					 to_exec = copy;
					
				}

			}
		}
	}

	@Override
	public Key input() {
		if(frameData.getEmptyFlag()) return new Key();
		Action curr;
		//DQ
		if(to_exec == null || to_exec.peek()==null){
			action_ended = true;
			return new Key();
		}
		if(!frameData.getMyCharacter(player).control) return new Key();
		curr = to_exec.poll();
		//map act -> key
		cmd.commandCall(curr.name());
		inputKey = cmd.getSkillKey();
		//return key

		return inputKey;
	}

	//UTILITY FUNCTIONS

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
	
}
