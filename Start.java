package rover;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class Start {

	static Random r = new Random();
	//static LinkedHashMap<int[], String> marsKarte; //Die Datenstruktur ist zu aufwedig, es geht viel einfacher mit einem CharArray
	
	static char[][] marsKarte; //Änderung der Datenstruktur dient zur Einfachheit
	static final int WIDTH_MARSES = 80;      		            
	static final int HEIGHT_MARS = 20;			
	static int rover_row = HEIGHT_MARS / 2;   
	static int rover_col = WIDTH_MARSES / 2 + 2;
	static final char up =   '^';
	static final char down = 'V';    
	static final char west = '<';
	static final char east = '>';
	
	
	public static void createMarsAndPutRoverinTheMiddle() {
	marsKarte=new char[HEIGHT_MARS ][WIDTH_MARSES];
		
		for (int col = 0; col < WIDTH_MARSES; col++) {
			for (int row = 0; row < HEIGHT_MARS; row++) {
				int[] position = new int[] { col, row };
				
				if (r.nextDouble() < 0.25 && !(rover_col == col && rover_row == row)){
					marsKarte[row][col]='#';
				}else{
					marsKarte[row][col]=' ';
				}
			}
		}
		
		marsKarte[rover_row][rover_col]= '^';
	}	
	
	
	
	
	
	public static char getPosition(int[] position) {
		int row=position[1];
		int col=position[0];
		return marsKarte[row][col];
	}

	
	public static void printField() {
		for (int row = 0; row < HEIGHT_MARS; row++) {
			for (int col = 0; col < WIDTH_MARSES; col++) {
				System.out.print( getPosition(new int[]{col,row}) );
			}
			System.out.println();
		}
		
		for (int i = 0; i < WIDTH_MARSES; i++) {
			System.out.print("=");
		}
		
		System.out.println();
	}

	public static void main(String[] args) {

		if (args.length > 1) {
			long seed = Long.parseLong(args[1]);
			r.setSeed(seed);
			System.out.println("Seed: " + seed);
		}
		System.out.println("erstelle karte");
		createMarsAndPutRoverinTheMiddle();
		
		String instructions = args[0];
		
		printField();
		
		for (int i = 0; i < instructions.length(); i++) {
			move(instructions.charAt(i));
			printField();
		}
	}
	
	public static void move(char c) { 
		System.out.println("bewege "+c);
		int[] aktuellePosition = findeRover();
		int[] newPosition=new int[2];
		switch(c){
			case 'f':
				newPosition = getPositionFacing();
				break;
			case 'b':
				newPosition = getPositionBack();
				break;
		}
		
		if(c=='f' || c=='b'){
			if(isFreePosition(newPosition)){
					
					marsKarte[newPosition[1]][newPosition[0]]=getOrientation();
					marsKarte[rover_row][rover_col]=' ';
					rover_row=newPosition[1];
					rover_col=newPosition[0];
			}else{
					System.out.println("fehler");
			}
		}
		
		
		if (c == 'l') {
			marsKarte[rover_row][rover_col]=rotate_left(getOrientation());
		} else if (c == 'r' ) {
			marsKarte[rover_row][rover_col]=rotate_right(getOrientation());
		} 
		
	}
	
	
		private static char rotate_left(char orientation){
			if(orientation==up) return west;
			if(orientation==west) return down;
			if(orientation==down) return east;
			if(orientation==east) return up;
		return up;
		}
		
		
		private static char getOrientation(){
			return getPosition(new int[]{rover_col,rover_row});
		}
	
		
		private static char rotate_right(char orientation){
			return rotate_left(rotate_left(rotate_left(orientation)));
		}
		
	
		private static int[] findeRover() {
			return new int[]{rover_col,rover_row};
		
		}
		
		private static boolean isFreePosition(int[] position){		//prüft ob die Stelle frei ist
			return isValidPosition(position) && getPosition(position) == ' ';
		}
		
		private static boolean isValidPosition(int[] position){
			int row=position[1];
			int col=position[0];
			return row>=0 && col >=0 && row < marsKarte.length && col < marsKarte[0].length;
		}
		
		
		
		private static int[] getPositionFacing(){    //Die Methode die das Vorwärtsgehen für den Rover zuständig ist
		int col=rover_col;
		int row=rover_row;
		char orientation = getOrientation();
		if(orientation==up) return new int[]{col,row-1};
		if(orientation==down) return new int[]{col,row+1};
		if(orientation==west) return new int[]{col-1,row};
		if(orientation==east) return new int[]{col+1,row};
		return findeRover();
	}
	
	private static int[] getPositionBack(){		////Die Methode die das Rückäwrtsgehen für den Rover zuständig ist
		int col=rover_col;
		int row=rover_row;
		char orientation = getOrientation();
		if(orientation==up) return new int[]{col,row+1};
		if(orientation==down) return new int[]{col,row-1};
		if(orientation==west) return new int[]{col+1,row};
		if(orientation==east) return new int[]{col-1,row};
		return findeRover();
	}
}
