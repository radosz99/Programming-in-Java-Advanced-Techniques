package algs;

import java.util.LinkedList;
import java.util.List;

import base.*;

/**
 * Class which holds specific informations about Pigeonhole Sort algorithm and gives the generic sorting method to solve the problem
 * @author Radoslaw Lis
 */
public class PigeonHoleSort extends AbstractIntSorter{
    /**
     * @return algorithm's description - name  how and it works
    */
	@Override
	public String description() {
		return "Pigeonhole Sort";
	}

    /**
     *  @return logical value which gives information whether algorithm is stable. In other words it means whether
     *  two objects with the same values appear in the same order in output as they appear in input
    */
	@Override
	public boolean isStable() {
		return true;
	}

    /**
     *  @return logical value which gives information whether algorithm work in situ. In other words it means whether
     *  algorithm sorts the items without using additional temporary space to hold data (if yes it works in situ)
    */
	@Override
	public boolean isInSitu() {
		return false;
	}
	
    /**
     * h
     * @return the finally sorted list
    */
	@Override
	public <T extends IElement> List<T> solve(List<T> list) {
		int min=(int) list.get(0).getValue(), max, range;
		max = min;
		
		for (int i = 1; i < list.size(); i++) {
			if(list.get(i).getValue() > max)
				max = (int) list.get(i).getValue();
			if(list.get(i).getValue() < min)
				min = (int) list.get(i).getValue();
		}
		
		range = max - min + 1;
		
	    @SuppressWarnings("unchecked")
	    LinkedList<T>[] holes = (LinkedList<T>[]) new LinkedList[range];
	    
	    for(int i = 0; i < range; i++) {
	        holes[i] = new LinkedList<T>();
	    }
	    for(T t : list) {
	        holes[(int) (t.getValue()-min)].add(t);
	    }
	    
	    int k = 0;
	    for(int i = 0; i < range; i++) {
	        for(int j=0; j< holes[i].size();j++,k++) {
	            list.set(k, holes[i].get(j));
	         
	        }
	    }

		return list;
	}
}
