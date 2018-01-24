package FocCrawl;

import java.util.Comparator;
import java.util.Map;


public class comparator implements Comparator{
	Map map;
	
	public comparator(Map map) {
		this.map = map;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return ((Double) map.get(arg1)).compareTo((Double) map.get(arg0));
	}

}
