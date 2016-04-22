package core;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Classe responsável por fazer o calculo básico da heurística do tabuleiro passado a ela.
 * 
 * @author Gilney N. Mathias
 */
public class CalcScore {
	
	//map contendo os regEx das duplas e os seus valores
	private HashMap<String, Integer> twosScore;
	//regEx completo juntando todos os regExs do map acima com o operador de '|', OU.
	private String twosRegEx = "";
	//estimativa do valor maximo que as duplas podem atingir
	private final int maxValueTwos = 112 * 7;//(225/2) * maxValue
	
	//map contendo os regEx das triplas e os seus valores
	private HashMap<String, Integer> threesScore;
	//regEx completo juntando todos os regExs do map acima com o operador de '|', OU.
	private String threesRegEx = "";
	//estimativa do valor maximo que as triplas podem atingir
	private final int maxValueThrees = 75 * maxValueTwos * 8;//(225/3) * maxTwos * maxValue
	
	//map contendo os regEx das quadras e os seus valores
	private HashMap<String, Integer> foursScore;
	//regEx completo juntando todos os regExs do map acima com o operador de '|', OU.
	private String foursRegEx = "";	
	//estimativa do valor maximo que as quadras podem atingir
	private final int maxValueFours = 56 * maxValueThrees * 7;//(225/4) * maxThrees * maxValue
	
	//valor de uma quíntupla
	private final int valueFives = (maxValueTwos+maxValueThrees+maxValueFours)+100;// Sum(maxTwos, maxThrees, maxFours) + 100
	
	public CalcScore() {
		createTwos();
		createThrees();
		createFours();
	}
	
	/**
	 * Retorna a soma dos valores encontrados para duplas, triplas, quadras
	 *  e quintuplas neste tabuleiro.
	 * 
	 * @param possibilities			String representando todas as possiveis linhas, colunas
	 * 								 e diagonais do tabuleiro.
	 * @param ia					Valor que representa a IA neste jogo.
	 * @param user					Valor que representa o usuário neste jogo.
	 * @return						A heurística básica do tabuleiro.
	 */
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
	
	/**
	 * Faz a soma dos valores das duplas deste tabuleiro usando o regEx e o HashMap
	 *  das duplas.
	 * 
	 * @param possibilities			String representando todas as possiveis linhas, colunas
	 * 								 e diagonais do tabuleiro.
	 * @param ia					Valor que representa a IA neste jogo.
	 * @param user					Valor que representa o usuário neste jogo.
	 * @return						Valor das duplas deste tabuleiro.
	 */
	private int getTwosSum(String possibilities, String ia, String user){
		int value = 0;
		Pattern p = Pattern.compile(twosRegEx.replaceAll("X", ia).replaceAll("Y", user), Pattern.MULTILINE);
		Matcher m = p.matcher(possibilities);
		
		while(m.find()){
			String v = m.group().replaceAll(ia, "X").replaceAll(user, "Y").replaceAll("\\.", "\\\\.");
			value += twosScore.get(v);
		}
		return value;
	}
	
	/**
	 * Faz a soma dos valores das triplas deste tabuleiro usando o regEx e o HashMap
	 *  das triplas.
	 * 
	 * @param possibilities			String representando todas as possiveis linhas, colunas
	 * 								 e diagonais do tabuleiro.
	 * @param ia					Valor que representa a IA neste jogo.
	 * @param user					Valor que representa o usuário neste jogo.
	 * @return						Valor das triplas deste tabuleiro.
	 */
	private int getThreesSum(String possibilities, String ia, String user){
		int value = 0;
		Pattern p = Pattern.compile(threesRegEx.replaceAll("X", ia).replaceAll("Y", user), Pattern.MULTILINE);
		Matcher m = p.matcher(possibilities);
		
		while(m.find()){
			String v = m.group().replaceAll(ia, "X").replaceAll(user, "Y").replaceAll("\\.", "\\\\.");
			value += threesScore.get(v);
		}
		return value;
	}
	
	/**
	 * Faz a soma dos valores das quadras deste tabuleiro usando o regEx e o HashMap
	 *  das quadras.
	 * 
	 * @param possibilities			String representando todas as possiveis linhas, colunas
	 * 								 e diagonais do tabuleiro.
	 * @param ia					Valor que representa a IA neste jogo.
	 * @param user					Valor que representa o usuário neste jogo.
	 * @return						Valor das quadras deste tabuleiro.
	 */
	private int getFoursSum(String possibilities, String ia, String user){
		int value = 0;
		Pattern p = Pattern.compile(foursRegEx.replaceAll("X", ia).replaceAll("Y", user), Pattern.MULTILINE);
		Matcher m = p.matcher(possibilities);
		
		while(m.find()){
			String v = m.group().replaceAll(ia, "X").replaceAll(user, "Y").replaceAll("\\.", "\\\\."); 
			value += foursScore.get(v);
		}
		return value;
	}
	
	/**
	 * Preenche o HashMap e cria o regEx completo das duplas.
	 */
	private void createTwos(){
		twosScore = new HashMap<>();

		// geral [pc]
		twosScore.put("YXXY", 			1);
		twosScore.put("\\.XX\\.",		7);
		twosScore.put("\\.XXY", 		3);
		twosScore.put("YXX\\.", 		3);
		twosScore.put("\\.X\\.X\\.",	6);
		
		// cantos [human]
		twosScore.put("YXX", 			1);
		twosScore.put("XXY", 			1);
		twosScore.put("\\.XX", 			2);
		twosScore.put("XX\\.", 			2);
		twosScore.put("XX", 			1);
		
		// geral [pc]
		twosScore.put("XYYX", 			-1);
		twosScore.put("\\.YY\\.",		-7);
		twosScore.put("\\.YYX", 		-3);
		twosScore.put("XYY\\.", 		-3);
		twosScore.put("\\.Y\\.Y\\.",	-6);
		
		// cantos [human]
		twosScore.put("XYY", 			-1);
		twosScore.put("YYX", 			-1);
		twosScore.put("\\.YY", 			-2);
		twosScore.put("YY\\.", 			-2);
		twosScore.put("YY", 			-1);
		
		twosRegEx = twosScore.keySet()
				.stream().sorted((e1, e2) -> e2.length() - e1.length())
				.collect(Collectors.joining("|"));
	}
	
	/**
	 * Preenche o HashMap e cria o regEx completo das triplas.
	 */
	private void createThrees(){
		threesScore = new HashMap<>();

		// geral [pc]
		threesScore.put("YXXXY", 		maxValueTwos* 1);
		threesScore.put("\\.XXX\\.", 	maxValueTwos* 8);//maxValueThrees-1
		threesScore.put("\\.XXXY", 		maxValueTwos* 3);
		threesScore.put("YXXX\\.", 		maxValueTwos* 3);
		threesScore.put("\\.X\\.XX\\.", maxValueTwos* 4);
		threesScore.put("\\.XX\\.X\\.", maxValueTwos* 4);
		
		//cantos [pc]
		threesScore.put("YXXX", 		maxValueTwos* 1);
		threesScore.put("XXXY", 		maxValueTwos* 1);
		threesScore.put("\\.XXX", 		maxValueTwos* 2);
		threesScore.put("XXX\\.", 		maxValueTwos* 2);
		threesScore.put("XXX", 			maxValueTwos* 1);
		
		// geral [human]
		threesScore.put("XYYYX", 		-(maxValueTwos* 1));
		threesScore.put("\\.YYY\\.", 	-(maxValueTwos* 8));//maxValueThrees-1
		threesScore.put("\\.YYYX", 		-(maxValueTwos* 3));
		threesScore.put("XYYY\\.", 		-(maxValueTwos* 3));
		threesScore.put("\\.Y\\.YY\\.", -(maxValueTwos* 4));
		threesScore.put("\\.YY\\.Y\\.", -(maxValueTwos* 4));
		
		//cantos [human]
		threesScore.put("XYYY", 		-(maxValueTwos* 1));
		threesScore.put("YYYX", 		-(maxValueTwos* 1));
		threesScore.put("\\.YYY", 		-(maxValueTwos* 2));
		threesScore.put("YYY\\.", 		-(maxValueTwos* 2));
		threesScore.put("YYY", 			-(maxValueTwos* 1));

		threesRegEx = threesScore.keySet()
				.stream().sorted((e1, e2) -> e2.length() - e1.length())
				.collect(Collectors.joining("|"));
	}
	
	/**
	 * Preenche o HashMap e cria o regEx completo das quadras.
	 */
	private void createFours(){
		foursScore = new HashMap<>();

		// geral [pc]
		foursScore.put("YXXXXY", 		maxValueThrees* 1);
		foursScore.put("\\.XXXX\\.", 	maxValueThrees* 7);
		foursScore.put("\\.XXXXY", 		maxValueThrees* 3);
		foursScore.put("YXXXX\\.", 		maxValueThrees* 3);
		
		// cantos [human]
		foursScore.put("YXXXX", 		maxValueThrees* 1);
		foursScore.put("XXXXY", 		maxValueThrees* 1);
		foursScore.put("\\.XXXX", 		maxValueThrees* 2);
		foursScore.put("XXXX\\.", 		maxValueThrees* 2);
		foursScore.put("XXXX", 			maxValueThrees* 1);
		
		// geral [pc]
		foursScore.put("XYYYYX", 		-(maxValueThrees* 1));
		foursScore.put("\\.YYYY\\.", 	-(maxValueThrees* 7));
		foursScore.put("\\.YYYYX", 		-(maxValueThrees* 3));
		foursScore.put("XYYYY\\.", 		-(maxValueThrees* 3));
		
		// cantos [human]
		foursScore.put("XYYYY", 		-(maxValueThrees* 1));
		foursScore.put("YYYYX", 		-(maxValueThrees* 1));
		foursScore.put("\\.YYYY", 		-(maxValueThrees* 2));
		foursScore.put("YYYY\\.", 		-(maxValueThrees* 2));
		foursScore.put("YYYY", 			-(maxValueThrees* 1));
		
		foursRegEx = foursScore.keySet()
				.stream().sorted((e1, e2) -> e2.length() - e1.length())
				.collect(Collectors.joining("|"));
	}
}
