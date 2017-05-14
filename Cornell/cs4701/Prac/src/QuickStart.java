import structs.FrameData;
import structs.Key;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import enumerate.Action;


public class QuickStart {
	/*
	 * The purpose of this class is to give the ai a semi-intelligent hard coded basis
	 * so that it doesn't flounder early on before the KNN is populated with data.
	 */
	
	public static Deque<Action> decision(FrameData f, boolean player, Deque<Action>[] options){
		structs.CharacterData me = f.getMyCharacter(player);
		structs.CharacterData opp = f.getOpponentCharacter(player);
		switch(opp.getAction()){
			case AIR:
			case FOR_JUMP:
			{
				return options[1];
			}
			case FORWARD_WALK:
			{
				return options[0];
			}
			case STAND_A:
			case STAND_B:
			case CROUCH_A:
			case CROUCH_B:
				return options[4];
			case STAND:
				if(me.energy>25) 
					return options[2];
			default:
				return options[3];
		}
	}
	
	public static Deque<Action> copy_deque(Deque<Action> acts){
		Deque<Action> copy = new LinkedList<Action>();

		Iterator<Action> it = acts.iterator();
		Action ptr = it.next();
		while(ptr != null){
			copy.add(ptr);
		}
		return copy;
	}
	
	
	public static Key rand_key(){
		Key r = new Key();
		int i = (int) (Math.random()*256);
		String s = Integer.toBinaryString(i);
		String f = ("00000000" + s).substring(s.length());
		System.out.println(f);
		r.A = (Character.getNumericValue(f.charAt(1)) > 0);
		r.B = (Character.getNumericValue(f.charAt(2)) > 0);
		r.C = (Character.getNumericValue(f.charAt(3)) > 0);
		r.D = (Character.getNumericValue(f.charAt(4)) > 0);
		r.L = (Character.getNumericValue(f.charAt(5)) > 0);
		r.R = (Character.getNumericValue(f.charAt(6)) > 0);
		r.U = (Character.getNumericValue(f.charAt(7)) > 0);
		return r;
	}

}
