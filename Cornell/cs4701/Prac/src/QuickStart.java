import structs.Key;

import java.util.LinkedList;

import enumerate.Action;


public class QuickStart {
	/*
	 * The purpose of this class is to give the ai a semi-intelligent hard coded basis
	 * so that it doesn't flounder early on before the KNN is populated with data.
	 */
	
	public static LinkedList<Key> decision(State s){
		LinkedList<Key> actions = new LinkedList<Key>();
		Key key = new Key();
		System.out.println(s.get_opp_stat());
		switch(s.get_opp_stat()){
			case AIR:
			case FOR_JUMP:
			{
				key.D = true;
				actions.add(key);
				for(int i=0;i<19;i++){
					actions.add(key);
				}
				Key key2 = new Key();
				key2.A = true;
				key2.D = true;
				key2.L = (s.get_front()>0) ? false : true;
				key2.R = (s.get_front()>0) ? true : false;
				actions.add(key2);
				break;
			}
			case FORWARD_WALK:
			{
				key.L = (s.get_front()>0) ? false : true;
				key.R = (s.get_front()>0) ? true : false;
				actions.add(key);
				for(int i=0;i<2;i++){
					actions.add(new Key());
				}
				Key key2 = new Key();
				key2.B = true;
				key2.D = true;
				key2.L = (s.get_front()>0) ? false : true;
				key2.R = (s.get_front()>0) ? true : false;
				actions.add(key2);
				break;
			}
			default:
				if(s.get_my_stat()==Action.CROUCH){
					actions.add(new Key());
					return actions;
				}
				if(s.get_my_stat()!= Action.STAND){
					return null;
				}
		}
		return actions;
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
