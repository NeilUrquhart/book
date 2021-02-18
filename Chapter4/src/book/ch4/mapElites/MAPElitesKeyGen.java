package book.ch4.mapElites;

public interface MAPElitesKeyGen {

	int getDimensions() ;

	int getBuckets();

	int[] getKey(EliteIndividual i);

}