package gov.nara.nwts.ftapp.stats;

import java.io.File;

import gov.nara.nwts.ftapp.filetest.FileTest;

/**
 * Status objects that counts items by key.
 * @author TBrady
 *
 */
public class CountStats extends Stats {
	
	public static enum CountStatsItems implements StatsItemEnum {
		Type(StatsItem.makeStringStatsItem("Type")),
		Count(StatsItem.makeLongStatsItem("Count"));
		
		StatsItem si;
		CountStatsItems(StatsItem si) {this.si=si;}
		public StatsItem si() {return si;}
	}
	public static enum Generator implements StatsGenerator {
		INSTANCE;
		public CountStats create(String key) {return new CountStats(key);}
	}
	
	public static StatsItemConfig details = StatsItemConfig.create(CountStatsItems.class);

	public CountStats(String key) {
		super(CountStats.details, key);  
	}
	
	public Object compute(File f, FileTest fileTest) {
		sumVal(CountStatsItems.Count, 1);
		return fileTest.fileTest(f);
	}
}
