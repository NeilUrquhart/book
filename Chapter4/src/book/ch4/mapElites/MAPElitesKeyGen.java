package book.ch4.mapElites;

/*
 * Neil Urquhart 2021
 * An interface that defines the basic operations needed from 
 * a key generator when used with MAPElites
 * 
 */
public interface MAPElitesKeyGen {
	int getDimensions() ;
	int getBuckets();
	int[] getKey(SupermarketSolution i);

}