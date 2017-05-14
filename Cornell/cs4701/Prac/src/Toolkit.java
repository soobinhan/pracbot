import java.util.Deque;
import java.util.LinkedList;

import enumerate.Action;

public class Toolkit {
	
	private static Deque<Action>[] options;
	/*
	 * The options are as follows:
	 * 0: block_and_sweep
	 * 1: anti_air
	 * 2: fireball
	 * 3; mash jab
	 * 4; spam_guard
	 */
	
	//Initializer function for all of the actions in the toolkit
	public static void set_up_actions(){
		options = new LinkedList[5];
		
		//classic block and sweep maneouver
		Deque<Action> block_and_sweep = new LinkedList<Action>();
		for(int i=0;i<5;i++){
			block_and_sweep.add(Action.CROUCH_GUARD);
		}
		block_and_sweep.add(Action.CROUCH_FB);
		
		options[0] = block_and_sweep;
		//
		
		//classic anti air punch
		
		Deque<Action> anti_air = new LinkedList<Action>();
		for(int i=0;i<19;i++){
			anti_air.add(Action.STAND);
		}
		anti_air.add(Action.CROUCH_FA);
		
		options[1] = anti_air;
		//
		
		//standard fireball
		
		Deque<Action> fireball = new LinkedList<Action>();
		fireball.add(Action.STAND_D_DF_FA);
		for(int i=0;i<15;i++){
			fireball.add(Action.STAND_GUARD);
		}
		
		options[2] = fireball;
		//
		
		Deque<Action> mash_jab = new LinkedList<Action>();
		for(int i=0;i<10;i++){
			mash_jab.add(Action.STAND_A);
		}
		
		options[3] = mash_jab;
		//
		
		//good old block spamming
		Deque<Action> block = new LinkedList<Action>();
		for(int i=0;i<6;i++){
			block.add(Action.STAND_GUARD);
		}
		
		options[4] = block;
		
		
	}
	
	public static Deque<Action>[] get_options(){
		return options;
	}
	
}
