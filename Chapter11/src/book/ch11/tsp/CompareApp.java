package book.ch11.tsp;
import java.text.DecimalFormat;
import java.util.ArrayList;

import book.ch1.ExhaustiveTSPSolver;
import book.ch1.Hybrid;
import book.ch1.NearestNTSPSolver;
import book.ch1.TSPProblem;
import book.ch1.TSPSolver;
import book.ch1.TwoOptTSPSolver;
import book.ch1.Visit;

/*
 * Neil Urquhart 2021
 * 
 * This programme asssess the TSP solvers used in the Santa Claus problem
 */

public class CompareApp {
	public static void main(String[] args)
	{
		//Setup a new TSP problem
		TSPProblem problem = new EnhancedTSP();

		problem.setStart(new Visit("start",55.919007,-3.212547));

		Visit[] cities = new Visit[]{	
					new Visit("3 Greenbank Park",55.9152583,-3.2210094),
				new Visit("4 Greenbank Park",55.9150502,-3.2211811),
				new Visit("5 Greenbank Park",55.9152692,-3.2207919),
				new Visit("6 Greenbank Park",55.9150814,-3.2209704),
				new Visit("7 Greenbank Park",55.9152816,-3.2205777),
				new Visit("8 Greenbank Park",55.9150682,-3.2207553),
				new Visit("9 Greenbank Park",55.9153164,-3.2203734),
				new Visit("10 Greenbank Park",55.9151001,-3.2205447),
				new Visit("11 Greenbank Park",55.9153319,-3.2201607),
				new Visit("12 Greenbank Park",55.9150816,-3.2203243),
				new Visit("13 Greenbank Park",55.9153471,-3.2199476),
				new Visit("14 Greenbank Park",55.9151116,-3.2201113),
				new Visit("15 Greenbank Park",55.9153312,-3.2197299),
				new Visit("16 Greenbank Park",55.9151154,-3.2198965),
				new Visit("17 Greenbank Park",55.9153366,-3.2195158),
				new Visit("18 Greenbank Park",55.915105,-3.2196816),
				new Visit("19 Greenbank Park",55.9153438,-3.2193011),
				new Visit("20 Greenbank Park",55.9151194,-3.2194624),
				new Visit("21 Greenbank Park",55.9153563,-3.2190762),
				new Visit("22 Greenbank Park",55.9151299,-3.2192514),
				new Visit("23 Greenbank Park",55.9153655,-3.2188717),
				new Visit("24 Greenbank Park",55.9151421,-3.2190392),
				new Visit("25 Greenbank Park",55.9153733,-3.2186519),
				new Visit("26 Greenbank Park",55.9151523,-3.2188227),
				new Visit("27 Greenbank Park",55.9153858,-3.2184267),
				new Visit("28 Greenbank Park",55.915168,-3.2186066),
				new Visit("29 Greenbank Park",55.9153942,-3.2182004),
				new Visit("30 Greenbank Park",55.9152111,-3.2183906),
				new Visit("31 Greenbank Park",55.9154056,-3.2179675),
				new Visit("32 Greenbank Park",55.9151917,-3.2181515),
				new Visit("33 Greenbank Park",55.9154175,-3.2177293),
				new Visit("34 Greenbank Park",55.9151996,-3.2179159),
				new Visit("35 Greenbank Park",55.9152329,-3.2196848),
				new Visit("36 Greenbank Park",55.9152114,-3.2176941),
				new Visit("2 Greenbank Crescent",55.9193554,-3.2130031),
				new Visit("3 Greenbank Crescent",55.9190049,-3.2138037),
				new Visit("4 Greenbank Crescent",55.9193065,-3.2132851),
				new Visit("5 Greenbank Crescent",55.9189447,-3.2140983),
				new Visit("6 Greenbank Crescent",55.9192986,-3.2133942),
				new Visit("7 Greenbank Crescent",55.9189246,-3.2141775),
				new Visit("8 Greenbank Crescent",55.9192756,-3.2137034),
				new Visit("9 Greenbank Crescent",55.9188595,-3.2144592),
				new Visit("10 Greenbank Crescent",55.9192613,-3.2138335),
				new Visit("11 Greenbank Crescent",55.9188419,-3.21453),
				new Visit("12 Greenbank Crescent",55.9192398,-3.2140045),
				new Visit("13 Greenbank Crescent",55.9187732,-3.2148126),
				new Visit("14 Greenbank Crescent",55.9192143,-3.2141292),
				new Visit("15 Greenbank Crescent",55.9187574,-3.2148735),
				new Visit("16 Greenbank Crescent",55.9191873,-3.214261),
				new Visit("17 Greenbank Crescent",55.9186551,-3.2151722),
				new Visit("18 Greenbank Crescent",55.9191611,-3.2143895),
				new Visit("19 Greenbank Crescent",55.9186303,-3.2152386),
				new Visit("20 Greenbank Crescent",55.9191574,-3.2146487),
				new Visit("21 Greenbank Crescent",55.9184964,-3.2155339),
				new Visit("22 Greenbank Crescent",55.9190885,-3.2148899),
				new Visit("23 Greenbank Crescent",55.918466,-3.2155826),
				new Visit("24 Greenbank Crescent",55.919019,-3.2151293),
				new Visit("25 Greenbank Crescent",55.9182719,-3.2158426),
				new Visit("26 Greenbank Crescent",55.918949,-3.2153668),
				new Visit("27 Greenbank Crescent",55.9182423,-3.2158706),
				new Visit("28 Greenbank Crescent",55.918847,-3.21568),
				new Visit("29 Greenbank Crescent",55.9180808,-3.2160126),
				new Visit("30 Greenbank Crescent",55.918838,-3.2157853),
				new Visit("31 Greenbank Crescent",55.9180508,-3.216037),
				new Visit("32 Greenbank Crescent",55.9186049,-3.2161097),
				new Visit("33 Greenbank Crescent",55.9178925,-3.216145),
				new Visit("34 Greenbank Crescent",55.9185454,-3.216186),
				new Visit("35 Greenbank Crescent",55.9178623,-3.2161671),
				new Visit("36 Greenbank Crescent",55.9184488,-3.2162915),
				new Visit("37 Greenbank Crescent",55.9176983,-3.2162677),
				new Visit("38 Greenbank Crescent",55.9183909,-3.2163539),
				new Visit("39 Greenbank Crescent",55.9176678,-3.2162882),
				new Visit("40 Greenbank Crescent",55.9182941,-3.21645),
				new Visit("41 Greenbank Crescent",55.9174875,-3.2163657),
				new Visit("42 Greenbank Crescent",55.9182299,-3.2165078),
				new Visit("43 Greenbank Crescent",55.9174565,-3.2163855),
				new Visit("44 Greenbank Crescent",55.9181236,-3.2165736),
				new Visit("45 Greenbank Crescent",55.9172705,-3.2164699),
				new Visit("46 Greenbank Crescent",55.9180655,-3.2166087),
				new Visit("47 Greenbank Crescent",55.9172396,-3.2164878),
				new Visit("48 Greenbank Crescent",55.9179418,-3.2166735),
				new Visit("49 Greenbank Crescent",55.9170515,-3.2165639),
				new Visit("50 Greenbank Crescent",55.9178881,-3.2167087),
				new Visit("51 Greenbank Crescent",55.9170196,-3.2165808),
				new Visit("52 Greenbank Crescent",55.917657,-3.2168274),
				new Visit("53 Greenbank Crescent",55.9167096,-3.2166583),
				new Visit("54 Greenbank Crescent",55.9175985,-3.2168557),
				new Visit("55 Greenbank Crescent",55.916514,-3.2166987),
				new Visit("56 Greenbank Crescent",55.917455,-3.216918),
				new Visit("57 Greenbank Crescent",55.9162851,-3.2167514),
				new Visit("58 Greenbank Crescent",55.9173829,-3.2169524),
				new Visit("59 Greenbank Crescent",55.9161925,-3.2167523),
				new Visit("60 Greenbank Crescent",55.9172247,-3.2170323),
				new Visit("61 Greenbank Crescent",55.9161269,-3.2167423),
				new Visit("62 Greenbank Crescent",55.9171537,-3.2170622),
				new Visit("63 Greenbank Crescent",55.9159775,-3.2167279),
				new Visit("64 Greenbank Crescent",55.9169902,-3.2170993),
				new Visit("65 Greenbank Crescent",55.915774,-3.2166845),
				new Visit("66 Greenbank Crescent",55.9169106,-3.2171287),
				new Visit("67 Greenbank Crescent",55.9155859,-3.2166462),
				new Visit("68 Greenbank Crescent",55.9165935,-3.217196),
				new Visit("69 Greenbank Crescent",55.9154044,-3.216608),
				new Visit("70 Greenbank Crescent",55.9165108,-3.2172007),
				new Visit("71 Greenbank Crescent",55.9119329,-3.2185779),
				new Visit("72 Greenbank Crescent",55.9163527,-3.2172882),
				new Visit("73 Greenbank Crescent",55.9152119,-3.2166705),
				new Visit("74 Greenbank Crescent",55.9161845,-3.2172915),
				new Visit("75 Greenbank Crescent",55.9150475,-3.2166306),
				new Visit("76 Greenbank Crescent",55.9160158,-3.2172749),
				new Visit("77 Greenbank Crescent",55.9148807,-3.2166596),
				new Visit("78 Greenbank Crescent",55.9158438,-3.2171999),
				new Visit("79 Greenbank Crescent",55.9147097,-3.2167417),
				new Visit("80 Greenbank Crescent",55.9156813,-3.217158),
				new Visit("81 Greenbank Crescent",55.9145803,-3.2167764),
				new Visit("82 Greenbank Crescent",55.9155129,-3.2170661),
				new Visit("83 Greenbank Crescent",55.9143997,-3.2167951),
				new Visit("84 Greenbank Crescent",55.9119329,-3.2185779),
				new Visit("85 Greenbank Crescent",55.9119329,-3.2185779),
				new Visit("86 Greenbank Crescent",55.915216,-3.2170443),
				new Visit("87 Greenbank Crescent",55.9141832,-3.2168969),
				new Visit("88 Greenbank Crescent",55.9150868,-3.2171224),
				new Visit("89 Greenbank Crescent",55.914034,-3.2169782),
				new Visit("90 Greenbank Crescent",55.9150143,-3.2171294),
				new Visit("91 Greenbank Crescent",55.9139072,-3.2170909),
				new Visit("92 Greenbank Crescent",55.9148977,-3.2171382),
				new Visit("93 Greenbank Crescent",55.9137573,-3.2172002),
				new Visit("94 Greenbank Crescent",55.9147622,-3.2171801),
				new Visit("95 Greenbank Crescent",55.9136186,-3.2173131),
				new Visit("96 Greenbank Crescent",55.9146139,-3.217226),
				new Visit("97 Greenbank Crescent",55.9135084,-3.2174462),
				new Visit("98 Greenbank Crescent",55.9144838,-3.2172092),
				new Visit("2 Greenbank Grove",55.9142476,-3.2214144),
				new Visit("3 Greenbank Grove",55.9145323,-3.220986),
				new Visit("4 Greenbank Grove",55.9143543,-3.2209199),
				new Visit("5 Greenbank Grove",55.9145855,-3.220794),
				new Visit("6 Greenbank Grove",55.914347,-3.2206957),
				new Visit("7 Greenbank Grove",55.9145775,-3.2205593),
				new Visit("8 Greenbank Grove",55.9143663,-3.220486),
				new Visit("9 Greenbank Grove",55.9145962,-3.2203455),
				new Visit("10 Greenbank Grove",55.9143875,-3.2202693),
				new Visit("11 Greenbank Grove",55.9146359,-3.2201465),
				new Visit("12 Greenbank Grove",55.9144314,-3.2200574),
				new Visit("13 Greenbank Grove",55.9146467,-3.2199393),
				new Visit("14 Greenbank Grove",55.9144416,-3.2198351),
				new Visit("15 Greenbank Grove",55.9146253,-3.2197092),
				new Visit("16 Greenbank Grove",55.914411,-3.2196361),
				new Visit("17 Greenbank Grove",55.9146334,-3.2194966),
				new Visit("18 Greenbank Grove",55.9144111,-3.2195285),
				new Visit("19 Greenbank Grove",55.9146472,-3.2192953),
				new Visit("20 Greenbank Grove",55.9144536,-3.2193375),
				new Visit("21 Greenbank Grove",55.9146219,-3.2190569),
				new Visit("22 Greenbank Grove",55.9144465,-3.2191095),
				new Visit("23 Greenbank Grove",55.9146165,-3.2188283),
				new Visit("24 Greenbank Grove",55.9144,-3.2188955),
				new Visit("25 Greenbank Grove",55.9146197,-3.2185889),
				new Visit("26 Greenbank Grove",55.9143968,-3.2186505),
				new Visit("27 Greenbank Grove",55.9146452,-3.2183416),
				new Visit("28 Greenbank Grove",55.9143861,-3.2184338),
				new Visit("29 Greenbank Grove",55.9145527,-3.2180819),
				new Visit("30 Greenbank Grove",55.9143546,-3.2181998),
				new Visit("31 Greenbank Grove",55.9145485,-3.2178297),
				new Visit("32 Greenbank Grove",55.914325,-3.2179494),
				new Visit("94 Greenbank Road",55.9150596,-3.2220604),
				new Visit("95 Greenbank Road",55.9155618,-3.2215893),
				new Visit("96 Greenbank Road",55.9149249,-3.2220531),
				new Visit("97 Greenbank Road",55.9154449,-3.2216646),
				new Visit("98 Greenbank Road",55.9148066,-3.2220319),
				new Visit("99 Greenbank Road",55.915266,-3.2216563),
				new Visit("100 Greenbank Road",55.91469,-3.2220028),
				new Visit("101 Greenbank Road",55.9150061,-3.2217139),
				new Visit("102 Greenbank Road",55.9145702,-3.2219541),
				new Visit("103 Greenbank Road",55.9148578,-3.2216852),
				new Visit("104 Greenbank Road",55.9144479,-3.2219116),
				new Visit("105 Greenbank Road",55.9147379,-3.2216419),
				new Visit("106 Greenbank Road",55.9143267,-3.2218503),
				new Visit("107 Greenbank Road",55.9146333,-3.2216494),
				new Visit("108 Greenbank Road",55.9142088,-3.2218071),
				new Visit("109 Greenbank Road",55.9144986,-3.2215862),
				new Visit("1 Greenbank Crescent",55.9190188,-3.2137259),
				new Visit("99 Greenbank Crescent",55.9133672,-3.2175436),
				new Visit("100 Greenbank Crescent",55.9142244,-3.2173319),
				new Visit("102 Greenbank Crescent",55.9141062,-3.2174053),
				new Visit("104 Greenbank Crescent",55.9139661,-3.2174938),
				new Visit("106 Greenbank Crescent",55.9137881,-3.2176511),
				new Visit("108 Greenbank Crescent",55.9136655,-3.2177259),
				new Visit("110 Greenbank Crescent",55.9135446,-3.2178323),
				new Visit("112 Greenbank Crescent",55.9133595,-3.2181434),
				new Visit("114 Greenbank Crescent",55.9131702,-3.2183105),
				new Visit("116 Greenbank Crescent",55.9130808,-3.2183945),
				new Visit("118 Greenbank Crescent",55.9129339,-3.2185194),
				new Visit("120 Greenbank Crescent",55.9127753,-3.2186054),
				new Visit("122 Greenbank Crescent",55.9126174,-3.2186215),
				new Visit("101 Greenbank Crescent",55.9132356,-3.2176912),
				new Visit("203 Greenbank Crescent",55.9119329,-3.2185779),
				new Visit("105 Greenbank Crescent",55.9129741,-3.2179831),
				new Visit("107 Greenbank Crescent",55.9128627,-3.2180821),
				new Visit("109 Greenbank Crescent",55.9127318,-3.2181713),
				new Visit("111 Greenbank Crescent",55.9126044,-3.2182308),
				new Visit("113 Greenbank Crescent",55.9124876,-3.2182662),
				new Visit("103 Greenbank Crescent",55.9131085,-3.2178816),
				new Visit("115 Greenbank Crescent",55.9123541,-3.2182974),
				new Visit("117 Greenbank Crescent",55.9122362,-3.2183245),
				new Visit("3 Greenbank Row",55.9138009,-3.2206804),
				new Visit("5 Greenbank Row",55.9138173,-3.2204684),
				new Visit("7 Greenbank Row",55.9138278,-3.2202529),
				new Visit("9 Greenbank Row",55.9138393,-3.2200384),
				new Visit("11 Greenbank Row",55.9138319,-3.2198268),
				new Visit("13 Greenbank Row",55.9138271,-3.2196118),
				new Visit("15 Greenbank Row",55.9138161,-3.2194002),
				new Visit("17 Greenbank Row",55.9137963,-3.2191882),
				new Visit("19 Greenbank Row",55.9137745,-3.2189743),
				new Visit("21 Greenbank Row",55.9137711,-3.2187595),
				new Visit("23 Greenbank Row",55.9136721,-3.2184785),
				new Visit("4 Greenbank Row",55.9136174,-3.220593),
				new Visit("6 Greenbank Row",55.9136417,-3.2203726),
				new Visit("8 Greenbank Row",55.9136478,-3.2201495),
				new Visit("10 Greenbank Row",55.9136509,-3.2199321),
				new Visit("12 Greenbank Row",55.9136425,-3.2197672),
				new Visit("14 Greenbank Row",55.913638,-3.2195977),
				new Visit("16 Greenbank Row",55.9136307,-3.2193966),
				new Visit("18 Greenbank Row",55.9136144,-3.2191623),
				new Visit("20 Greenbank Row",55.9135838,-3.2189282),
				new Visit("22 Greenbank Row",55.913533,-3.2186991),
				new Visit("2 Greenbank Rise",55.9124415,-3.2198746),
				new Visit("4 Greenbank Rise",55.9126904,-3.2198722),
				new Visit("8 Greenbank Rise",55.9131063,-3.219928),
				new Visit("10 Greenbank Rise",55.9131205,-3.2194964),
				new Visit("12 Greenbank Rise",55.9129205,-3.219344),
				new Visit("14 Greenbank Rise",55.9126922,-3.2195572),
				new Visit("16 Greenbank Rise",55.9124467,-3.2195624),
				new Visit("3 Greenbank Lane",55.918602,-3.2214854),
				new Visit("5 Greenbank Lane",55.9185127,-3.2213456),
				new Visit("7 Greenbank Lane",55.9184166,-3.221207),
				new Visit("9 Greenbank Lane",55.9183077,-3.2210804),
				new Visit("11 Greenbank Lane",55.9182455,-3.2210097),
				new Visit("2 Greenbank Lane",55.9186642,-3.2220232),
				new Visit("4 Greenbank Lane",55.9185842,-3.2218816),
				new Visit("6 Greenbank Lane",55.9184997,-3.2217249),
				new Visit("8 Greenbank Lane",55.9184077,-3.221583),
				new Visit("10 Greenbank Lane",55.9183091,-3.2214388),
				new Visit("12 Greenbank Lane",55.9182088,-3.2213132),
				new Visit("5 Greenbank Loan",55.9177407,-3.2174036),
				new Visit("7 Greenbank Loan",55.917725,-3.2176576),
				new Visit("9 Greenbank Loan",55.917701,-3.2179099),
				new Visit("11 Greenbank Loan",55.9176126,-3.2181016),
				new Visit("13 Greenbank Loan",55.9176079,-3.2183663),
				new Visit("2 Greenbank Loan",55.9179501,-3.2173071),
				new Visit("4 Greenbank Loan",55.9179517,-3.2173698),
				new Visit("6 Greenbank Loan",55.9179546,-3.2174888),
				new Visit("8 Greenbank Loan",55.9179561,-3.2175473),
				new Visit("10 Greenbank Loan",55.9179594,-3.2178623),
				new Visit("12 Greenbank Loan",55.9178991,-3.2180894),
				new Visit("14 Greenbank Loan",55.9178692,-3.2183173),
				new Visit("16 Greenbank Loan",55.9178247,-3.2185301),
				new Visit("18 Greenbank Loan",55.9177649,-3.2187488),
				new Visit("15 Greenbank Loan",55.9175175,-3.2188165),
				new Visit("17 Greenbank Loan",55.9174743,-3.219031),
				new Visit("19 Greenbank Loan",55.9174291,-3.219246),
				new Visit("21 Greenbank Loan",55.9173833,-3.2194779),
				new Visit("23 Greenbank Loan",55.9173396,-3.2196864),
				new Visit("25 Greenbank Loan",55.9172973,-3.2198949),
				new Visit("27 Greenbank Loan",55.9170432,-3.2198031),
				new Visit("29 Greenbank Loan",55.9169188,-3.2197655),
				new Visit("31 Greenbank Loan",55.9168319,-3.2197458),
				new Visit("33 Greenbank Loan",55.9166749,-3.2197192),
				new Visit("35 Greenbank Loan",55.9165315,-3.2197379),
				new Visit("20 Greenbank Loan",55.9177226,-3.2189695),
				new Visit("22 Greenbank Loan",55.9176775,-3.2191876),
				new Visit("24 Greenbank Loan",55.9176353,-3.219406),
				new Visit("26 Greenbank Loan",55.9175901,-3.2196264),
				new Visit("28 Greenbank Loan",55.917544,-3.2198459),
				new Visit("30 Greenbank Loan",55.9174983,-3.2200594),
				new Visit("32 Greenbank Loan",55.9174239,-3.2202553),
				new Visit("34 Greenbank Loan",55.9172907,-3.2203336),
				new Visit("36 Greenbank Loan",55.9172113,-3.2203164),
				new Visit("38 Greenbank Loan",55.9170803,-3.2201891),
				new Visit("40 Greenbank Loan",55.9169452,-3.2201323),
				new Visit("42 Greenbank Loan",55.9168595,-3.2201144),
				new Visit("44 Greenbank Loan",55.9167076,-3.2201175),
				new Visit("46 Greenbank Loan",55.9165832,-3.2201334),
				new Visit("48 Greenbank Loan",55.9164964,-3.2201299),
				new Visit("1 Greenbank Gardens",55.9160572,-3.2208266),
				new Visit("3 Greenbank Gardens",55.916073,-3.2206125),
				new Visit("5 Greenbank Gardens",55.9160882,-3.2203936),
				new Visit("7 Greenbank Gardens",55.9161043,-3.2201861),
				new Visit("2 Greenbank Gardens",55.9158677,-3.220954),
				new Visit("4 Greenbank Gardens",55.9158829,-3.2207428),
				new Visit("6 Greenbank Gardens",55.9158975,-3.2205265),
				new Visit("8 Greenbank Gardens",55.9159148,-3.2203143),
				new Visit("10 Greenbank Gardens",55.915932,-3.2200921),
				new Visit("9 Greenbank Gardens",55.9161364,-3.2197256),
				new Visit("11 Greenbank Gardens",55.9161522,-3.2195088),
				new Visit("13 Greenbank Gardens",55.9161701,-3.2192974),
				new Visit("15 Greenbank Gardens",55.9161854,-3.2190696),
				new Visit("17 Greenbank Gardens",55.9162387,-3.2187128),
				new Visit("19 Greenbank Gardens",55.916452,-3.2186189),
				new Visit("21 Greenbank Gardens",55.916604,-3.2185907),
				new Visit("23 Greenbank Gardens",55.9167114,-3.2185749),
				new Visit("25 Greenbank Gardens",55.9168658,-3.2185465),
				new Visit("27 Greenbank Gardens",55.917029,-3.2185145),
				new Visit("29 Greenbank Gardens",55.9171896,-3.2185375),
				new Visit("12 Greenbank Gardens",55.9159471,-3.2198769),
				new Visit("14 Greenbank Gardens",55.9159659,-3.2196556),
				new Visit("16 Greenbank Gardens",55.9159586,-3.2194272),
				new Visit("18 Greenbank Gardens",55.9159963,-3.2192103),
				new Visit("20 Greenbank Gardens",55.9160111,-3.2189806),
				new Visit("22 Greenbank Gardens",55.9160063,-3.2186942),
				new Visit("24 Greenbank Gardens",55.9159741,-3.2184721),
				new Visit("26 Greenbank Gardens",55.9160079,-3.2182814),
				new Visit("28 Greenbank Gardens",55.9161276,-3.2182429),
				new Visit("30 Greenbank Gardens",55.9162555,-3.2182922),
				new Visit("32 Greenbank Gardens",55.916416,-3.2183188),
				new Visit("34 Greenbank Gardens",55.9165342,-3.2182694),
				new Visit("36 Greenbank Gardens",55.9167146,-3.2182342),
				new Visit("38 Greenbank Gardens",55.9168882,-3.2181427),
				new Visit("40 Greenbank Gardens",55.9170217,-3.2180938),
				new Visit("42 Greenbank Gardens",55.9171444,-3.2180947),
				new Visit("44 Greenbank Gardens",55.9172856,-3.2181448),
				new Visit("8 Greenbank Road",55.9188811,-3.2161278),
				new Visit("10 Greenbank Road",55.9189052,-3.2163381),
				new Visit("12 Greenbank Road",55.9189221,-3.2165991),
				new Visit("14 Greenbank Road",55.9188793,-3.2168439),
				new Visit("16 Greenbank Road",55.9188564,-3.2170895),
				new Visit("18 Greenbank Road",55.9188323,-3.2173329),
				new Visit("20 Greenbank Road",55.9187902,-3.2175708),
				new Visit("22 Greenbank Road",55.9187842,-3.2178167),
				new Visit("24 Greenbank Road",55.9187617,-3.2180579),
				new Visit("26 Greenbank Road",55.9187414,-3.2182827),
				new Visit("28 Greenbank Road",55.9186821,-3.2185566),
				new Visit("30 Greenbank Road",55.9186463,-3.2188017),
				new Visit("32 Greenbank Road",55.9186031,-3.219026),
				new Visit("34 Greenbank Road",55.9185866,-3.2192505),
				new Visit("36 Greenbank Road",55.9185249,-3.219445),
				new Visit("38 Greenbank Road",55.9184824,-3.2196492),
				new Visit("40 Greenbank Road",55.9184525,-3.2198972),
				new Visit("42 Greenbank Road",55.9184237,-3.2200456),
				new Visit("44 Greenbank Road",55.9183785,-3.2202841),
				new Visit("46 Greenbank Road",55.9183329,-3.2205002),
				new Visit("21 Greenbank Road",55.9186424,-3.2168152),
				new Visit("23 Greenbank Road",55.918625,-3.2170268),
				new Visit("25 Greenbank Road",55.9186051,-3.2171915),
				new Visit("27 Greenbank Road",55.9185823,-3.2174164),
				new Visit("29 Greenbank Road",55.9185581,-3.2176494),
				new Visit("31 Greenbank Road",55.9185345,-3.2178716),
				new Visit("33 Greenbank Road",55.9185122,-3.2180936),
				new Visit("35 Greenbank Road",55.9185099,-3.218347),
				new Visit("37 Greenbank Road",55.9184751,-3.2185856),
				new Visit("39 Greenbank Road",55.9184382,-3.2188144),
				new Visit("41 Greenbank Road",55.9183972,-3.2190512),
				new Visit("43 Greenbank Road",55.9183529,-3.2192799),
				new Visit("45 Greenbank Road",55.9183025,-3.2195151),
				new Visit("47 Greenbank Road",55.9182603,-3.2197416),
				new Visit("49 Greenbank Road",55.9182091,-3.2199985),
				new Visit("51 Greenbank Road",55.9181783,-3.2201511),
				new Visit("53 Greenbank Road",55.9181276,-3.2204001),
				new Visit("55 Greenbank Road",55.9180876,-3.2205973),
				new Visit("57 Greenbank Road",55.918041,-3.2208232),
				new Visit("59 Greenbank Road",55.9180097,-3.2209743),
				new Visit("61 Greenbank Road",55.9178988,-3.2212213),
				new Visit("63 Greenbank Road",55.9178176,-3.2212746),
				new Visit("65 Greenbank Road",55.9176783,-3.2213359),
				new Visit("67 Greenbank Road",55.9175529,-3.2213169),
				new Visit("69 Greenbank Road",55.9174326,-3.221357),
				new Visit("71 Greenbank Road",55.917308,-3.2213428),
				new Visit("73 Greenbank Road",55.9171709,-3.2213477),
				new Visit("75 Greenbank Road",55.9170813,-3.2213625),
				new Visit("77 Greenbank Road",55.9169382,-3.2213559),
				new Visit("79 Greenbank Road",55.9168196,-3.2213678),
				new Visit("81 Greenbank Road",55.9166363,-3.2213682),
				new Visit("83 Greenbank Road",55.9165149,-3.2213999),
				new Visit("85 Greenbank Road",55.9164003,-3.2214061),
				new Visit("87 Greenbank Road",55.9162745,-3.2214142),
				new Visit("89 Greenbank Road",55.9160574,-3.2213825),
				new Visit("48 Greenbank Road",55.9179458,-3.2216433),
				new Visit("50 Greenbank Road",55.9178215,-3.2216528),
				new Visit("52 Greenbank Road",55.9176722,-3.2217132),
				new Visit("54 Greenbank Road",55.917585,-3.2217173),
				new Visit("56 Greenbank Road",55.9174539,-3.2216745),
				new Visit("58 Greenbank Road",55.9173293,-3.2216856),
				new Visit("60 Greenbank Road",55.9171792,-3.2216928),
				new Visit("62 Greenbank Road",55.917089,-3.2216982),
				new Visit("64 Greenbank Road",55.9169547,-3.2217059),
				new Visit("66 Greenbank Road",55.9168293,-3.2217109),
				new Visit("68 Greenbank Road",55.9166156,-3.221717),
				new Visit("70 Greenbank Road",55.916496,-3.2217213),
				new Visit("72 Greenbank Road",55.9163764,-3.2217276),
				new Visit("74 Greenbank Road",55.9162557,-3.2217352),
				new Visit("76 Greenbank Road",55.9161367,-3.2217431),
				new Visit("78 Greenbank Road",55.9160252,-3.2217522),
				new Visit("91 Greenbank Road",55.9157963,-3.2214106),
				new Visit("93 Greenbank Road",55.9156805,-3.2215414),
				new Visit("80 Greenbank Road",55.9158832,-3.2217896),
				new Visit("82 Greenbank Road",55.9157582,-3.2218409),
				new Visit("84 Greenbank Road",55.9156432,-3.2219094),
				new Visit("86 Greenbank Road",55.9155248,-3.2219709),
				new Visit("88 Greenbank Road",55.9154045,-3.2220156),
				new Visit("90 Greenbank Road",55.9152864,-3.2220461),
				new Visit("92 Greenbank Road",55.9151648,-3.2220589),
				new Visit("113 Greenbank Road",55.9141078,-3.221398),
				new Visit("115 Greenbank Road",55.9140347,-3.221371),
				new Visit("117 Greenbank Road",55.9139027,-3.2213611),
				new Visit("119 Greenbank Road",55.9137719,-3.2213114),
				new Visit("110 Greenbank Road",55.9140794,-3.221797),
				new Visit("112 Greenbank Road",55.9140021,-3.2217681),
				new Visit("114 Greenbank Road",55.9138667,-3.2216747),
				new Visit("116 Greenbank Road",55.9137615,-3.2216357),
				new Visit("118 Greenbank Road",55.9136295,-3.2215858),
				new Visit("121 Greenbank Road",55.9135024,-3.2212093),
				new Visit("123 Greenbank Road",55.9133829,-3.2211638),
				new Visit("125 Greenbank Road",55.9132491,-3.2210714),
				new Visit("127 Greenbank Road",55.913173,-3.2210419),
				new Visit("129 Greenbank Road",55.9130342,-3.2210274),
				new Visit("131 Greenbank Road",55.9129093,-3.2209782),
				new Visit("133 Greenbank Road",55.9127914,-3.2209323),
				new Visit("135 Greenbank Road",55.9126735,-3.2208858),
				new Visit("137 Greenbank Road",55.9125607,-3.2208421),
				new Visit("139 Greenbank Road",55.9124132,-3.2207594),
				new Visit("141 Greenbank Road",55.9122635,-3.2204818),
				new Visit("143 Greenbank Road",55.9122544,-3.2201377),
				new Visit("120 Greenbank Road",55.9135126,-3.2215397),
				new Visit("122 Greenbank Road",55.9133935,-3.2214979),
				new Visit("124 Greenbank Road",55.9132782,-3.2214538),
				new Visit("126 Greenbank Road",55.9131764,-3.2214148),
				new Visit("128 Greenbank Road",55.9130393,-3.2213598),
				new Visit("130 Greenbank Road",55.9129228,-3.2213145),
				new Visit("132 Greenbank Road",55.9128071,-3.2212712),
				new Visit("134 Greenbank Road",55.9126898,-3.2212234),
				new Visit("136 Greenbank Road",55.912567,-3.221173),
				new Visit("138 Greenbank Road",55.9124477,-3.2211148),
				new Visit("140 Greenbank Road",55.9123325,-3.2210779),
				new Visit("142 Greenbank Road",55.9122251,-3.2209736),
				new Visit("144 Greenbank Road",55.9121439,-3.2208201),
				new Visit("146 Greenbank Road",55.9120859,-3.220637),
				new Visit("148 Greenbank Road",55.91204,-3.2204371),
				new Visit("150 Greenbank Road",55.912052,-3.2202147),
				new Visit("152 Greenbank Road",55.9120997,-3.2199911),
				new Visit("154 Greenbank Road",55.9121465,-3.2197366),
				new Visit("161 Greenbank Road",55.912468,-3.2188947),
				new Visit("156 Greenbank Road",55.9121672,-3.2195115),
				new Visit("158 Greenbank Road",55.9122269,-3.2193198),
				new Visit("160 Greenbank Road",55.9122686,-3.2191014),
				new Visit("162 Greenbank Road",55.9123136,-3.2187729)
		};

		//Solvers to be tested
		TSPSolver[] solvers = new TSPSolver[] {new NearestNTSPSolver(),new EASolver(), /*new Hybrid(), new TwoOptTSPSolver()*/};

		for (int c=0; c < cities.length; c++){ //Add cities to problem
			problem.addVisit(cities[c]);
		}
		System.out.print("\nVisits , "+ problem.getSize());

		for (TSPSolver solver : solvers){
			//Repeat 10 times
			double[] dist=  new double[10];
			double[] time = new double[10];
			for (int x=0; x < 10; x ++){
				time[x] = System.currentTimeMillis();
				problem.shuffle();
				problem.solve(solver);
				dist[x] = ((EnhancedTSP)problem).getFinalDist();
				time[x] = System.currentTimeMillis()-time[x];
			}
			DecimalFormat df = new DecimalFormat("#.##");
			System.out.print(","+solver +","    );
			for (double d : time) System.out.print(df.format(d)+",");
			System.out.print("avg,"+ df.format(avg(time))+",");
			for (double d : dist) System.out.print(df.format(d)+",");
			System.out.print("min(avg),"+df.format(min(dist))+"("+df.format(avg(dist))+")");
		}

	}

	public static double avg(double[] data) {
		double avg=0;
		for(double d : data)
			avg += d;
		avg = avg/data.length;
		return avg;
	}

	public static  double min(double[] data) {
		double min=Double.MAX_VALUE;
		for(double d : data)
			if (d < min)
				min = d;
		return min;
	}
}
