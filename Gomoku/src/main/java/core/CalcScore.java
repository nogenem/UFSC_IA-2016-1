package core;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/* Alguns dados...

-- tempo ms - score [pos]
com start v3
   7750 - 2240 [centro]
   10455 - 2260 [centro.x-1]
sem start v3
   7729 - 2240 [centro]
   10462 - 2260 [centro.x-1]

com start v2
   15576 - 4520 [centro]
   23025 - 4560 [centro.x+1]
sem start v2
   37595 - 2255 [centro]
   22990 - 2280 [centro.x+1]
 */
public class CalcScore {
	
	private HashMap<String, Integer> twosScore;
	private String twosRegEx = "";
	private final int maxValueTwos = 896;//448;
	
	private HashMap<String, Integer> threesScore;
	private String threesRegEx = "";
	private final int maxValueThrees = 67_800;//33_975;
	
	private HashMap<String, Integer> foursScore;
	private String foursRegEx = "";	
	private final int maxValueFours = 3_797_360;//1_902_936;
	
	private final int valueFives = 3_866_057;//1_937_360;
	
	public CalcScore() {
		createTwos();
		createThrees();
		createFours();
	}
	
	public int getPossibilitiesSum(String possibilities, char ia, char user){
		// Pega a soma de sequencias de 2, 3 e 4 peças
		int value = getTwosSum(possibilities, ""+ia, ""+user) + 
				getThreesSum(possibilities, ""+ia, ""+user) +
				getFoursSum(possibilities, ""+ia, ""+user);
		
		// Pega a soma de sequencias de 5 peças
		String strCheck = new String(new char[5])
				.replace("\0", ""+ia);
		if(possibilities.contains(strCheck))
			value += valueFives;
		else{
			strCheck = new String(new char[5])
					.replace("\0", ""+user);
			if(possibilities.contains(strCheck))
				value -= valueFives;
		}
		
		return value;
	}
	
	private int getTwosSum(String possibilities, String player, String otherPlayer){
		int value = 0;
		Pattern p = Pattern.compile(twosRegEx.replaceAll("X", player).replaceAll("Y", otherPlayer), Pattern.MULTILINE);
		Matcher m = p.matcher(possibilities);
		
		int start = 0;
		while(m.find(start)){
			String v = m.group().replaceAll(player, "X").replaceAll(otherPlayer, "Y").replaceAll("\\.", "\\\\.");
			//System.out.print(v + " | ");
			value += twosScore.get(v);
			start = m.start()+1;
		}
		//System.out.println();
		return value;
	}
	
	private int getThreesSum(String possibilities, String player, String otherPlayer){
		int value = 0;
		Pattern p = Pattern.compile(threesRegEx.replaceAll("X", player).replaceAll("Y", otherPlayer), Pattern.MULTILINE);
		Matcher m = p.matcher(possibilities);
		
		int start = 0, tmp = 0;
		while(m.find(start)){
			String v = m.group().replaceAll(player, "X").replaceAll(otherPlayer, "Y").replaceAll("\\.", "\\\\.");
			//System.out.print(v + " | ");
			tmp = threesScore.get(v);
			value += tmp < 0 ? -maxValueTwos+tmp : maxValueTwos+tmp;
			start = m.start()+1;
		}
		//System.out.println();
		return value;
	}
	
	private int getFoursSum(String possibilities, String player, String otherPlayer){
		int value = 0;
		Pattern p = Pattern.compile(foursRegEx.replaceAll("X", player).replaceAll("Y", otherPlayer), Pattern.MULTILINE);
		Matcher m = p.matcher(possibilities);
		
		int start = 0, tmp = 0;
		while(m.find(start)){
			String v = m.group().replaceAll(player, "X").replaceAll(otherPlayer, "Y").replaceAll("\\.", "\\\\.");
			//System.out.print(v + " | ");
			tmp = foursScore.get(v);
			value += tmp < 0 ? -maxValueThrees+tmp : maxValueThrees+tmp;
			start = m.start()+1;
		}
		//System.out.println("\n");
		return value;
	}
	
	private void createTwos(){
		twosScore = new HashMap<>();
		
		// v1
		/*twosScore.put("YXX\\.", 2);
		twosScore.put("YX\\.XY", 1);
		twosScore.put("\\.XXY", 2);
		twosScore.put("\\.XX\\.", 4);
		twosScore.put("\\.X\\.XY", 3);
		twosScore.put("YX\\.X\\.", 3);*/
		
		// v2
		twosScore.put("YXX", 		1);
		twosScore.put("XXY", 		1);
		twosScore.put("\\.XX", 		3);
		twosScore.put("XX\\.", 		3);
		twosScore.put("YX\\.X", 	2);
		twosScore.put("X\\.XY", 	2);
		twosScore.put("X\\.X\\.", 	4);
		twosScore.put("\\.X\\.X", 	4);
		
		twosScore.put("XYY", 		-1);
		twosScore.put("YYX", 		-1);
		twosScore.put("\\.YY", 		-3);
		twosScore.put("YY\\.", 		-3);
		twosScore.put("XY\\.Y", 	-2);
		twosScore.put("Y\\.YX", 	-2);
		twosScore.put("Y\\.Y\\.", 	-4);
		twosScore.put("\\.Y\\.Y", 	-4);
		
		// v3
		/*twosScore.put("YXXY", 1);
		twosScore.put("\\.XX\\.", 3);
		twosScore.put("YXX\\.", 2);
		twosScore.put("\\.XXY", 2);
		
		twosScore.put("XYYX", -1);
		twosScore.put("\\.YY\\.", -3);
		twosScore.put("XYY\\.", -2);
		twosScore.put("\\.YYX", -2);*/
		
		// v4
		/*twosScore.put("YXX", 1);
		twosScore.put("XXY", 1);
		twosScore.put("\\.XX", 3);
		twosScore.put("XX\\.", 3);
		
		twosScore.put("XYY", -1);
		twosScore.put("YYX", -1);
		twosScore.put("\\.YY", -3);
		twosScore.put("YY\\.", -3);*/
		
		twosRegEx = twosScore.keySet()
				.stream().collect(Collectors.joining("|"));
		
		//System.out.println(twosRegEx);
	}
	
	private void createThrees(){
		threesScore = new HashMap<>();
		
		// v1
		/*threesScore.put("YXXX\\.", 2);
		threesScore.put("YXXXY", 1);
		threesScore.put("YXX\\.XY", 1);
		threesScore.put("YX\\.XXY", 1);
		threesScore.put("\\.XXXY", 2);
		threesScore.put("\\.XXX\\.", 5);
		threesScore.put("\\.XX\\.X\\.", 3);
		threesScore.put("\\.X\\.XXY", 4);
		threesScore.put("YX\\.XX\\.", 5);
		threesScore.put("YXX\\.X\\.", 4);*/
		
		// v2
		threesScore.put("YXXX", 		1);
		threesScore.put("XXXY", 		1);
		threesScore.put("\\.XXX", 		3);
		threesScore.put("XXX\\.", 		3);
		threesScore.put("YX\\.XX", 		2);
		threesScore.put("X\\.XXY", 		2);
		threesScore.put("\\.X\\.XX", 	4);
		threesScore.put("X\\.XX\\.", 	4);
		threesScore.put("YXX\\.X", 		2);
		threesScore.put("XX\\.XY", 		2);
		threesScore.put("\\.XX\\.X", 	4);
		threesScore.put("XX\\.X\\.", 	4);
		
		threesScore.put("XYYY", 		-1);
		threesScore.put("YYYX", 		-1);
		threesScore.put("\\.YYY", 		-3);
		threesScore.put("YYY\\.", 		-3);
		threesScore.put("XY\\.YY", 		-2);
		threesScore.put("Y\\.YYX", 		-2);
		threesScore.put("\\.Y\\.YY", 	-4);
		threesScore.put("Y\\.YY\\.", 	-4);
		threesScore.put("XYY\\.Y", 		-2);
		threesScore.put("YY\\.YX", 		-2);
		threesScore.put("\\.YY\\.Y", 	-4);
		threesScore.put("YY\\.Y\\.", 	-4);
		
		// v3
		/*threesScore.put("YXXXY", 1);
		threesScore.put("\\.XXX\\.", 3);
		threesScore.put("YXXX\\.", 2);
		threesScore.put("\\.XXXY", 2);
		
		threesScore.put("XYYYX", -1);
		threesScore.put("\\.YYY\\.", -3);
		threesScore.put("XYYY\\.", -2);
		threesScore.put("\\.YYYX", -2);*/
		
		// v4
		/*threesScore.put("YXXX", 1);
		threesScore.put("XXXY", 1);
		threesScore.put("\\.XXX", 3);
		threesScore.put("XXX\\.", 3);
		
		threesScore.put("XYYY", -1);
		threesScore.put("YYYX", -1);
		threesScore.put("\\.YYY", -3);
		threesScore.put("YYY\\.", -3);*/
		
		threesRegEx = threesScore.keySet()
				.stream().collect(Collectors.joining("|"));
		
		//System.out.println(threesRegEx);
	}
	
	private void createFours(){
		foursScore = new HashMap<>();
		// v1
		/*foursScore.put("YXXXX\\.", 2);
		foursScore.put("YXXX\\.XY", 1);
		foursScore.put("YXX\\.XXY", 1);
		foursScore.put("YX\\.XXXY", 2);
		foursScore.put("\\.XXXXY", 3);
		foursScore.put("\\.XXXX\\.", 6);
		foursScore.put("\\.XXX\\.XY", 5);
		foursScore.put("\\.XX\\.XXY", 4);
		foursScore.put("\\.X\\.XXXY", 4);
		foursScore.put("YX\\.XXX\\.", 5);
		foursScore.put("YXX\\.XX\\.", 4);
		foursScore.put("YXXX\\.X\\.", 4);*/
		
		// v2
		foursScore.put("YXXXX", 		1);
		foursScore.put("XXXXY", 		1);
		foursScore.put("\\.XXXX", 		3);
		foursScore.put("XXXX\\.", 		3);
		foursScore.put("YX\\.XXX", 		2);
		foursScore.put("X\\.XXXY", 		2);
		foursScore.put("\\.X\\.XXX", 	5);
		foursScore.put("X\\.XXX\\.", 	5);
		foursScore.put("YXX\\.XX", 		4);
		foursScore.put("XX\\.XXY", 		4);
		foursScore.put("\\.XX\\.XX", 	5);
		foursScore.put("XX\\.XX\\.", 	5);
		foursScore.put("YXXX\\.X", 		4);
		foursScore.put("XXX\\.XY", 		4);
		foursScore.put("\\.XXX\\.X", 	5);
		foursScore.put("XXX\\.X\\.", 	5);
		
		foursScore.put("XYYYY", 		-1);
		foursScore.put("YYYYX", 		-1);
		foursScore.put("\\.YYYY", 		-3);
		foursScore.put("YYYY\\.", 		-3);
		foursScore.put("XY\\.YYY", 		-2);
		foursScore.put("Y\\.YYYX",	 	-2);
		foursScore.put("\\.Y\\.YYY", 	-5);
		foursScore.put("Y\\.YYY\\.", 	-5);
		foursScore.put("XYY\\.YY", 		-4);
		foursScore.put("YY\\.YYX", 		-4);
		foursScore.put("\\.YY\\.YY", 	-5);
		foursScore.put("YY\\.YY\\.", 	-5);
		foursScore.put("XYYY\\.Y", 		-4);
		foursScore.put("YYY\\.YX", 		-4);
		foursScore.put("\\.YYY\\.Y", 	-5);
		foursScore.put("YYY\\.Y\\.", 	-5);
		
		// v3
		/*foursScore.put("YXXXXY", 1);
		foursScore.put("\\.XXXX\\.", 3);
		foursScore.put("\\.XXXXY", 2);
		foursScore.put("YXXXX\\.", 2);
		
		foursScore.put("XYYYYX", -1);
		foursScore.put("\\.YYYY\\.", -3);
		foursScore.put("XYYYY\\.", -2);
		foursScore.put("\\.YYYYX", -2);*/
		
		// v4
		/*foursScore.put("YXXXX", 1);
		foursScore.put("XXXXY", 1);
		foursScore.put("\\.XXXX", 3);
		foursScore.put("XXXX\\.", 3);
		
		foursScore.put("XYYYY", -1);
		foursScore.put("YYYYX", -1);
		foursScore.put("\\.YYYY", -3);
		foursScore.put("YYYY\\.", -3);*/
		
		foursRegEx = foursScore.keySet()
				.stream().collect(Collectors.joining("|"));
		
		//System.out.println(foursRegEx);
	}
}
