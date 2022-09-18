import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("Введите два римских или два арабских числа и один из оператов +-*/");
		String numbers = sc.nextLine();
		System.out.println(calc(numbers));
	}
         // преобразование введенной строки в массив "число-оператор-число" в случае
	// соответствия строки формату, иначе выбрасывает Exception
	static String[] reg(String str) throws Exception {
		str = str.replace(" ", "");
		Pattern pattern = null;
		Matcher matcher = null;
		pattern = Pattern.compile("^([^\\+\\-\\*\\/])*$");
		matcher = pattern.matcher(str);
		if(matcher.find()) throw new Exception("строка не является математической операцией");
				
		pattern = Pattern.compile("^([1-9]|10)([\\+\\-\\*\\/]){1}(I|II|III|IV|V|VI|VII|VIII|IX|X)$"
				+ "|^(I|II|III|IV|V|VI|VII|VIII|IX|X)([\\+\\-\\*\\/]){1}([1-9]|10)$");
		matcher = pattern.matcher(str);
		if(matcher.find()) throw new Exception("используются одновременно разные системы счисления");
		
		pattern = Pattern.compile("^([1-9]|10)([\\+\\-\\*\\/]){1}([1-9]|10)$"
		+ "|^(I|II|III|IV|V|VI|VII|VIII|IX|X)([\\+\\-\\*\\/]){1}(I|II|III|IV|V|VI|VII|VIII|IX|X)$");
	    matcher = pattern.matcher(str);
	    String[] arr = new String[6];
	    if(matcher.find() == true) { 
	    	for(int x = 0; x < 6; x++) {
	        arr[x] = matcher.group(x + 1);
	    	}    	
	    	}
	    else {
	    	throw new Exception("формат математической операции не удовлетворяет заданию - два операнда от 1 до 10 и один оператор (+, -, /, *)");
	    }
	    matcher.start();
	    return arr;
	}
	// преобразование римского числа в арабское и возврат в виде String
	public static int romToAr(String roman) {
	switch(roman){
	case ("I"): return 1;
	case ("II"): return 2;
	case ("III"): return 3;
	case ("IV"): return 4;
	case ("V"): return 5;
	case ("VI"): return 6;
	case("VII"): return 7;
	case("VIII"): return 8;
	case("IX"): return 9;
	case("X"): return 10;
	}
	return -1;
	}
	// подсчет и возврат результата в нужном формате
	public static String calc(String input) throws Exception {
		int op1 = 0, op2 = 0, result = 0;
		String[] arr = reg(input);
		//Arrays.asList(arr).forEach(t -> System.out.println(t));
		if(arr[3] != null) {
			if(romToAr(arr[3]) - romToAr(arr[5]) < 1 && arr[4].equals("-")) throw new Exception("в римской системе нет отрицательных чисел");
		}
		if(arr[0]!=null) op1 = Integer.parseInt(arr[0]);
		if(arr[2]!=null) op2 = Integer.parseInt(arr[2]);
		if(arr[3]!=null) op1 = romToAr(arr[3]);
		if(arr[5]!=null) op2 = romToAr(arr[5]);
		if(arr[1]!=null&&arr[1].equals("+") || arr[4]!=null&&arr[4].equals("+")) result = Operation.SUM.action(op1, op2);
		else if(arr[1]!=null&&arr[1].equals("-") || arr[4]!=null&&arr[4].equals("-")) result = Operation.SUBTRACT.action(op1, op2);
		else if(arr[1]!=null&&arr[1].equals("*") || arr[4]!=null&&arr[4].equals("*")) result = Operation.MULTIPLY.action(op1, op2);
		else if(arr[1]!=null&&arr[1].equals("/") || arr[4]!=null&&arr[4].equals("/")) result = Operation.DIVIDE.action(op1, op2);
		if(arr[3] != null) return arabicToRoman(result);
		return String.valueOf(result);
	}
	// Перевод арабского числа в римское и возврат в виде строки
	public static String arabicToRoman(int number) {
	    List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

	    int i = 0;
	    StringBuilder sb = new StringBuilder();

	    while ((number > 0) && (i < romanNumerals.size())) {
	        RomanNumeral currentSymbol = romanNumerals.get(i);
	        if (currentSymbol.getValue() <= number) {
	            sb.append(currentSymbol.name());
	            number -= currentSymbol.getValue();
	        } else {
	            i++;
	        }
	    }
	    return sb.toString();
	}
}

enum Operation{
    SUM{
        public int action(int x, int y){ return x + y;}
    },
    SUBTRACT{
        public int action(int x, int y){ return x - y;}
    },
    MULTIPLY{
        public int action(int x, int y){ return x * y;}
    },
    DIVIDE{
    	public int action(int x, int y){ return x/y;}
    };
    public abstract int action(int x, int y);
}

enum RomanNumeral {
    I(1), IV(4), V(5), IX(9), X(10), 
    XL(40), L(50), XC(90), C(100);

    private int value;

    RomanNumeral(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static List<RomanNumeral> getReverseSortedValues() {
        return Arrays.stream(values())
          .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
          .collect(Collectors.toList());
    }
}
