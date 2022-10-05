import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import .TokenType;

class Regex { //classe adaptada com a classe Token disponibilizada
	public boolean isNum(String token) {
    Pattern pattern = Pattern.compile("[0-9]+");
    Matcher matcher = pattern.matcher(token);
    boolean matchFound = matcher.find();
    return matchFound;
  }
	
	public boolean isOP(String token) {
    Pattern pattern = Pattern.compile("[-+*/]+");
    Matcher matcher = pattern.matcher(token);
    boolean matchFound = matcher.find();
    return matchFound;
  }

}

class Token { //classe adaptada com a classe Token disponibilizada
  public int value;
  public Token abaixo;
  public final TokenType type; // token type
	public final String lexeme; // token value

  Token(TokenType tktype, String caractere_entrada, int valor) {
    type = tktype;
    value = valor;
    lexeme = caractere_entrada;
    abaixo = null;
  }

  @Override
	public String toString() {
		return "Token [type=" + this.type + ", lexeme=" + this.lexeme + "]";
	}
}

class Stack {
  public Token topo;

  Stack() {
    topo = null;
  }

  public void push(Token elemento) {
    Token last_topo = topo;
    topo = elemento;
    elemento.abaixo = last_topo;
  }

  public int pop() {
    Token new_topo = topo.abaixo;
    Token old_topo = topo;
    topo = new_topo;
    return old_topo.value;
  }

  public int size() {
    Token elemento = this.topo;
    int size = 1;
    while (elemento.abaixo != null) {
      size++;
      elemento = elemento.abaixo;
    }
    return size;
  }
}

class UnexpectedCharacter extends Exception {
  UnexpectedCharacter(String message) {
    super(message);
  }
}

class Main {
  public static void main(String[] args) throws IOException, UnexpectedCharacter {
    operate();
    scanning();
    scanningRegex();
  }

  public static void operate () throws IOException, UnexpectedCharacter {
    Stack stack = new Stack();
    String file = "teste.txt";
    Scanner scanner = new Scanner(new File(file));
    scanner.useDelimiter("\n");
    while (scanner.hasNext()) {
      String entry = scanner.next();
      boolean isNumeric = entry.chars().allMatch(Character::isDigit);
      if (isNumeric) {
        Token numero = new Token(TokenType.NUM, entry, Integer.parseInt(entry));
        stack.push(numero);
      } else {
        if (entry.equals("+")) {
          int snd_oper = stack.pop();
          int fst_oper = stack.pop();
          int soma = fst_oper + snd_oper;
          Token result = new Token(TokenType.NUM, Integer.toString(soma), soma);
          stack.push(result);
        } else if (entry.equals("*")) {
          int snd_oper = stack.pop();
          int fst_oper = stack.pop();
          int mult = fst_oper * snd_oper;
          Token result = new Token(TokenType.NUM, Integer.toString(mult), mult);
          stack.push(result);
        } else if (entry.equals("-")) {
          int snd_oper = stack.pop();
          int fst_oper = stack.pop();
          int sub = fst_oper - snd_oper;
          Token result = new Token(TokenType.NUM, Integer.toString(sub), sub);
          stack.push(result);
        } else if (entry.equals("/")) {
          int snd_oper = stack.pop();
          int fst_oper = stack.pop();
          int div = fst_oper / snd_oper;
          Token result = new Token(TokenType.NUM, Integer.toString(div), div);
          stack.push(result);
        } else {
          throw new UnexpectedCharacter("Error: Unexpected character:"+entry);
        }
      }
    }
    int resultado_final = stack.pop();
    System.out.println("Operating result:");
    System.out.println(resultado_final);
    scanner.close();
  }

  public static void scanning() throws IOException,UnexpectedCharacter {
    Stack stack = new Stack();
    String file = "teste.txt";
    Scanner scanner = new Scanner(new File(file));
    scanner.useDelimiter("\n");
    while (scanner.hasNext()) {
      String entry = scanner.next();
      boolean isNumeric = entry.chars().allMatch(Character::isDigit);
      if (isNumeric) {
        Token numero = new Token(TokenType.NUM, entry, Integer.parseInt(entry));
        stack.push(numero);
      } else {
        if (entry.equals("+")) {
          Token op = new Token(TokenType.PLUS, entry, 0);
          stack.push(op);
        } else if (entry.equals("*")) {
          Token op = new Token(TokenType.STAR, entry, 0);
          stack.push(op);
        } else if (entry.equals("-")) {
          Token op = new Token(TokenType.MINUS, entry, 0);
          stack.push(op);
        } else if (entry.equals("/")) {
          Token op = new Token(TokenType.SLASH, entry, 0);
          stack.push(op);
        } else {
          throw new UnexpectedCharacter("Error: Unexpected character:"+entry);
        }
      }
    }
    scanner.close();
    Token token = stack.topo;
    String[] tokens = new String[stack.size()];
    int count = stack.size()-1;
    while (token != null){
      tokens[count] = token.toString();
      token = token.abaixo;
      count--;
    }
    System.out.println("Scanning result:");
    for (int i=0; i<tokens.length; i++) {
      System.out.println(tokens[i]);
    }
  }

  public static void scanningRegex() throws IOException,UnexpectedCharacter {
    Stack stack = new Stack();
    String file = "teste.txt";
    Scanner scanner = new Scanner(new File(file));
    scanner.useDelimiter("\n");
    while (scanner.hasNext()) {
      String entry = scanner.next();
      Regex reg = new Regex();
      boolean isNumeric = reg.isNum(entry);
      if (isNumeric) {
        Token numero = new Token(TokenType.NUM, entry, Integer.parseInt(entry));
        stack.push(numero);
      } else {
        if (reg.isOP(entry)) {
          if (entry.equals("+")) {
            Token op = new Token(TokenType.PLUS, entry, 0);
            stack.push(op);
          } else if (entry.equals("*")) {
            Token op = new Token(TokenType.STAR, entry, 0);
            stack.push(op);
          } else if (entry.equals("-")) {
            Token op = new Token(TokenType.MINUS, entry, 0);
            stack.push(op);
          } else if (entry.equals("/")) {
            Token op = new Token(TokenType.SLASH, entry, 0);
            stack.push(op);
          }
        } else {
          throw new UnexpectedCharacter("Error: Unexpected character:"+entry);
        }
      }
    }
    scanner.close();
    Token token = stack.topo;
    String[] tokens = new String[stack.size()];
    int count = stack.size()-1;
    while (token != null){
      tokens[count] = token.toString();
      token = token.abaixo;
      count--;
    }
    System.out.println("Scanning with regex result:");
    for (int i=0; i<tokens.length; i++) {
      System.out.println(tokens[i]);
    }
  }
}