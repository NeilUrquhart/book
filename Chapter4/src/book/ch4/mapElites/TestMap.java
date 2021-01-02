package book.ch4.mapElites;

public class TestMap {

	public static void main(String[] args) {
		//Test all keys
		int dims = 4;
		int buckets = 12;

		MAP<String> map = new MAP<String>(dims,buckets);
		//Test ....
		for (int w=0; w <buckets; w ++) {
			for (int x=0; x <buckets; x ++) {
				for (int y=0; y <buckets; y ++) {
					for (int z=0; z <buckets; z ++) {
						int[] key = {w,x,y,z,};
						String test = "" + key[0] +key[1]+key[2] + key[3] ;
						System.out.println(test);
						try {
						if (map.get(key)!=null) {
							System.exit(-1);
						}
						
						map.put(test,key);
						if (!map.get(key).equals(test)) {
							System.exit(-1);
						}
						}catch(Exception e) {
							e.printStackTrace();
							System.exit(-1);
						}
					}
				}	
			}
		}
	}
}
