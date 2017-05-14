import structs.FrameData;
import structs.GameData;
import structs.Key;
import structs.CharacterData;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import enumerate.Action;
import fighting.Attack;

public class State {

	private int frame;
	private int front;
	private int dist;
	private int hp;
	private int opphp;
	private Key mykey;
	private Action mystat;
	private Action oppstat;
	

	public State(FrameData in, boolean player){
		CharacterData me = in.getMyCharacter(player);
		CharacterData opp = in.getOpponentCharacter(player);
		frame = in.getFrameNumber();
		front = me.front ? 1 : 0;
		dist = Math.abs(me.getGraphicCenterX() - opp.getGraphicCenterX());
		hp = me.getHp();
		opphp = opp.getHp();
		mystat = me.getAction();
		oppstat = opp.getAction();
	}

	public int get_frame(){
		return frame;
	}

	public int get_front(){
		return front;
	}

	public int get_dist(){
		return dist;
	}

	public int get_hp(){
		return hp;
	}

	public int get_opp_hp(){
		return opphp;
	}

	public Action get_my_stat(){
		return mystat;
	}

	public Action get_opp_stat(){
		return oppstat;
	}

	/*
	 * [int[]] to_arr takes in a State s and returns an array
	 * with the information of s represented as follows:
	 * frame,distance,hp,opponent hp, action hash code, opp action hashcode
	 */
	public int[] to_arr(){
		int[] env = new int[7];
		env[0] = frame;
		env[1] = front;
		env[2] = dist;
		env[3] = hp;
		env[4] = opphp;
		env[5] = mystat.hashCode();
		env[6] = oppstat.hashCode();
		return env;
	}

}
