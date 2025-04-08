import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


class Client {
    ///public static Mark markAI = Mark.EMPTY;
	public static void main(String[] args) {
         
        Scanner scanner = new Scanner(System.in);
        System.out.print("Host: ");
        String host = scanner.nextLine(); 
        System.out.print("Port: ");
        int port = scanner.nextInt(); 
        System.out.println("host: " + host + ", port: " + port);
        scanner.close(); 
        
	    Socket MyClient;
	    BufferedInputStream input;
	    BufferedOutputStream output;
        int[][] board = new int[9][9];
	    GlobalBoard globalBoard;
        Mark markAI = Mark.EMPTY;
        
	    try {
	    	MyClient = new Socket(host, port);
        
	       	input    = new BufferedInputStream(MyClient.getInputStream());
	    	output   = new BufferedOutputStream(MyClient.getOutputStream());
	    	BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        
	    	globalBoard = new GlobalBoard();
	       	while(1 == 1){
	    		char cmd = 0;
            
                cmd = (char)input.read();
                //System.out.println(cmd);
                // Debut de la partie en joueur rouge
                if(cmd == '1'){
                    byte[] aBuffer = new byte[1024];
                
	    			int size = input.available();
	    			//System.out.println("size " + size);
	    			input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        x++;
                        if(x == 9){
                            x = 0;
                            y++;
                        }
                    }
                
                    System.out.println("Nouvelle partie! Vous jouer rouge, entrez votre premier coup : ");
                    markAI = Mark.X;
	    			//globalBoard.getAllMoves("A0");
	    			System.out.println("Client ");
	    			String move = getNextMoveMinMax(globalBoard, 0, markAI, null).get(0).toString();
	    			System.out.println("Move : "+move);
	    			globalBoard.play(move, markAI);
                    //move = console.readLine();
	    			output.write(move.getBytes(),0,move.length());
	    			output.flush();
                }
                // Debut de la partie en joueur Noir
                if(cmd == '2'){
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des rouges");
                    markAI = Mark.O;
                    byte[] aBuffer = new byte[1024];
                
	    			int size = input.available();
	    			//System.out.println("size " + size);
	    			input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println("m" +s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        x++;
                        if(x == 9){
                            x = 0;
                            y++;
                        }
                    }
                }
            
	    		// Le serveur demande le prochain coup
	    		// Le message contient aussi le dernier coup joue.
	            if(cmd == '3'){
	    	        byte[] aBuffer = new byte[16];
                
	    	        int size = input.available();
	    	        //System.out.println("size :" + size);
	    	        input.read(aBuffer,0,size);
                
	    	        String s = new String(aBuffer);
	    	        System.out.println("Dernier coup :"+ s);
	    	        globalBoard.play(s, markAI==Mark.X?Mark.O:Mark.X);
	    	        //globalBoard.getAllMoves(s);
                
	    	        System.out.println("Entrez votre coup : ");
	    	        String move = getNextMoveMinMax(globalBoard, 0, markAI, s).get(0).toString();
	    	        globalBoard.play(move, markAI);
                    System.out.println("Move : "+move);
	    	        //move = console.readLine();
	    	        output.write(move.getBytes(),0,move.length());
	    	        output.flush();			
	            }
	    	    // Le dernier coup est invalide
	    	    if(cmd == '4'){
	    			System.out.println("Coup invalide, entrez un nouveau coup : ");
	               	String move = null;
	        		move = console.readLine();
	    	    	output.write(move.getBytes(),0,move.length());
	    			output.flush();
                    
	        	}
                // La partie est terminée
	            if(cmd == '5'){
                    byte[] aBuffer = new byte[16];
                    int size = input.available();
                    input.read(aBuffer,0,size);
	    	        String s = new String(aBuffer);
	    	        System.out.println("Partie Terminé. Le dernier coup joué est: "+s);
                    Mark winner = globalBoard.globalBoardWinner(markAI);
                    Mark opponentMark = markAI==Mark.X?Mark.O:Mark.X;
                    System.out.println("============");
                    if (winner == markAI) {
                        System.out.println("WIN");
                    } else if (winner == opponentMark) {
                        System.out.println("LOSE");
                    } else {
                        System.out.println("DRAW");
                    }
                    System.out.println("============");
	    	        String move = null;
	    	        move = console.readLine();
	    	        output.write(move.getBytes(),0,move.length());
	    	        output.flush();
                
	            }
            }
	    }
	    catch (IOException e) {
   	    	System.out.println(e);
	    }
	
    }

    private static long StartTime; 
    private static long timeLimit = 3000; // 3 secondes
	public static ArrayList<Move> getNextMoveMinMax(GlobalBoard board,int depth, Mark mark, String lastMove )
    {
        ArrayList<Move> possibleMoves = board.getAllMoves(lastMove==null?"A0":lastMove);
        int bestScore= Integer.MIN_VALUE;
        ArrayList<Move> bestMoves = new ArrayList<>();

		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;

        StartTime = System.currentTimeMillis();

        //=========
        // ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // List<Future<ScoredMove>> futures = new ArrayList<>();
        // for (Move move : possibleMoves) {
        //     futures.add(executor.submit(() -> {
        //         //GlobalBoard newBoard = new GlobalBoard(board);
        //         board.play(move.toString(), mark);
        //         int score = minimax(board, false, mark, 0, move, alpha, beta);
        //         board.undoMove(move);
        //         return new ScoredMove(move, score);
        //     }));
        // }

        // for (Future<ScoredMove> future : futures) {
        //     try {
        //         ScoredMove scoredMove = future.get();
        //         if (scoredMove.score > bestScore) {
        //             bestScore = scoredMove.score;
        //             bestMoves.clear();
        //             bestMoves.add(scoredMove.move);
        //         } else if (scoredMove.score == bestScore) {
        //             bestMoves.add(scoredMove.move);
        //         }
        //     } catch (InterruptedException | ExecutionException e) {
        //         e.printStackTrace();
        //     }
        // }
    
        // executor.shutdown();
        // return bestMoves;
        //=========

        for(Move move: possibleMoves)
        {
            if (System.currentTimeMillis() - StartTime > timeLimit) {
                System.out.println("**out of time**");
            }
			String s = move.toString();
            board.play(s, mark);
			int score = minimax(board,false,mark,0,move,alpha,beta);
            board.undoMove(move);

			if(score > bestScore)
            {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(move);
            }
            else if(score == bestScore)
            {
                bestMoves.add(move);
            }
			alpha = Math.max(alpha, bestScore);
			if(alpha >= beta)
			{
				break;
			}
			
        }
		return bestMoves;
    }
    //record ScoredMove(Move move, int score) {}

	public static int minimax(GlobalBoard globalBoard, boolean joueurMax, Mark mark, int depth, Move lastMove, int alpha, int beta) {
        int score = globalBoard.evaluate(mark);

        // Condition d'arrêt
        if (depth >= 7 || Math.abs(score) >= 10000 || globalBoard.isFull() || System.currentTimeMillis() - StartTime > timeLimit) {
            return score;
        }

        int meilleurScore = joueurMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Mark joueurActuel = joueurMax ? mark: mark == Mark.X ? Mark.O : Mark.X;
        //mark = (mark == Mark.X) ? Mark.O : Mark.X;
        
        ArrayList<Move> allMoves = globalBoard.getAllMoves(lastMove.toString());
        if(allMoves.size() == 0) // Cause une erreur puisque le jeux essaie de refaire le meme move
        {
            return 0;
        }
        for (Move move : allMoves) {
            //GlobalBoard newBoard = new GlobalBoard(globalBoard);
            globalBoard.play(move.toString(), joueurActuel);
            score = minimax(globalBoard, !joueurMax, mark, depth + 1, move, alpha, beta);
            globalBoard.undoMove(move);

            if (joueurMax) {
                meilleurScore = Math.max(meilleurScore, score);
                alpha = Math.max(alpha, score);
            } else {
                meilleurScore = Math.min(meilleurScore, score);
                beta = Math.min(beta, score);
            }

            // Coupure alpha-beta
            if (beta <= alpha) {
                break;
            }
        }

    return meilleurScore;
}

}