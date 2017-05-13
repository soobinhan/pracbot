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
	public int initialize(GameData gd, boolean player) {
		// TODO Auto-generated method stub
		this.inputKey = new Key();
		this.player = player;
		this.frameData = new FrameData();
		this.simulator = new Simulator(gd);
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