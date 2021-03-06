package main;

import java.util.*;

import exceptions.InvalidArgumentException;


/*
 * Parses command-line arguments into flag/value pairs for easy access.
 */
public class ArgumentParser {

	/* Stores arguments in a map, where the key is a flag. */
	private final Map<String, String> argumentMap;
	private static String emptyString = "";
	

	/*
	 * Initializes an empty argument map. The method {@link #parseArguments(String[])}
	 * should eventually be called to populate the map.
	 * @param flagCount 
	 * @param valueCount 
	 */
	public ArgumentParser() {
		argumentMap = new HashMap<>();
		
	}

	/*
	 * Initializes the argument map with the provided command-line arguments.
	 * Uses {@link #parseArguments(String[])} to populate the map.
	 *
	 * @param args command-line arguments
	 * @throws InvalidArgumentException 
	 * @see #parseArguments(String[])
	 */
	public ArgumentParser(String[] args) throws InvalidArgumentException {
		this();
		parseArguments(args);
	}

	/*
	 * Iterates through the array of command-line arguments. It makes sure that the command line arguments are greater than 0
	 * Then I iterate through the args with a for loop. Within the for loop I check if the first statement is a flag calling 
	 * isFlag method and check if the argument after that is a value using my isValue method. If they are then I check if the 
	 * flag variables are correct by using if statement to see if they are either input, output, or order. If so I add the first
	 * argument as my key and the one after as my value in the Hashmap i created called argumentMap.
	 * 
	 *
	 * @param args command-line arguments
	 *
	 * @see #isFlag(String)
	 * @see #isValue(String)
	 */
	public void parseArguments(String[] args) throws InvalidArgumentException {
		if (args.length > 0) {
			
			for (int i = 0; i <= args.length -1; i = i + 2) {
				
				if ((isFlag(args[i]) == true && args[i].equals("-threads") && (i == (args.length-1)))) {
					
					if (args[i].equals("-threads") && (i == (args.length-1))) {

						argumentMap.put(args[i], emptyString);
					}
				}
				
				if ((isFlag(args[i]) == true && args[i].equals("-threads") && isFlag(args[i + 1]))) {
					
					
					argumentMap.put(args[i], emptyString);
				}
				
				else if (isFlag(args[i]) == true && isValue(args[i + 1]) == true) {
					if (args[i].equals("-input")) {
																
						argumentMap.put(args[i], args[i + 1]);
						
					}
					else if (args[i].equals("-output")) {
												
						argumentMap.put(args[i], args[i + 1]);
						
					}
					else if (args[i].equals("-order")) {
						
						argumentMap.put(args[i], args[i + 1]);
					}
					
					else if (args[i].equals("-threads")) {
	
						argumentMap.put(args[i], args[i + 1]);
					}
					
					else if (args[i].equals("-searchInput")) {
						
						argumentMap.put(args[i], args[i + 1]);
					}
					
					else if (args[i].equals("-searchOutput")) {
						
						argumentMap.put(args[i], args[i + 1]);
					}
					
					else {
						throw new InvalidArgumentException("Incorrect Command Line Arguments");
					}
					
				}
				
			}
			
			if (numFlags() < 3 || numFlags() > 6) { 
				
				throw new InvalidArgumentException("Not correct amount of flags");	
			}
			
			
		} else {
			
			throw new InvalidArgumentException("No command line arguments");
		}
	}
	
	

	/*
	 * Tests if the provided argument is a flag by checking that it starts with
	 * a "-" dash symbol, and is followed by at least one non-whitespace
	 * character. For example, "-a" and "-1" are valid flags, but "-" and "- "
	 * are not valid flags.
	 *
	 * @param arg command-line argument
	 * @return true if the argument is a flag
	 */
	public static boolean isFlag(String arg) {
		
		
		if (arg != null && arg.startsWith("-")){		
			if (arg.trim().length() <= 1) {
				return false;
			}
			else {
				return true;
			}
		}	
		else{
			return false;
		}
	}

	/*
	 * Tests if the provided argument is a value by checking that it does not
	 * start with a "-" dash symbol, and contains at least one non-whitespace
	 * character. For example, "a" and "1" are valid values, but "-" and " "
	 * are not valid values.
	 *
	 * @param arg command-line argument
	 * @return true if the argument is a value
	 */
	public static boolean isValue(String arg) {
	
		if (arg != null &&  !arg.trim().startsWith("-")) {
			if (arg.trim().length() >= 1) {
				return true;
			}
			else {
			   return false;
			}
		}
		else{
			return false;
		}
		
	}

	/*
	 * Returns the number of flags stored.
	 *
	 * @return number of flags
	 */
	public int numFlags() {
	
		return argumentMap.size();
	}
	
	/*
	 * Returns the value of the appropriate key.
	 */


	public String getValue(String flag) {
		
		if (argumentMap.get(flag) != null) {
			return argumentMap.get(flag);
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return argumentMap.toString();
	}
	

}