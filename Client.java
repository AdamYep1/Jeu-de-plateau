import java.io.*;
import java.net.*;
import java.util.ArrayList;


class Client {
	public static void main(String[] args) {
         
	Socket MyClient;
	BufferedInputStream input;
	BufferedOutputStream output;
    int[][] board = new int[9][9];
	GiantBoard giantBoard;
	
	try {
		MyClient = new Socket("localhost", 8888);

	   	input    = new BufferedInputStream(MyClient.getInputStream());
		output   = new BufferedOutputStream(MyClient.getOutputStream());
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		giantBoard = new GiantBoard();
	   	while(1 == 1){
			char cmd = 0;
		   	
            cmd = (char)input.read();
            System.out.println(cmd);
            // Debut de la partie en joueur blanc
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

                System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
				giantBoard.getAllMoves("A0");
				System.out.println("Client ");
				Client client = new Client();
				String move =client.getNextMoveMinMax(giantBoard, 0, Mark.O, null).get(0).toString();
				System.out.println("Move : "+move);
				giantBoard.play(move, Mark.O);
                //move = console.readLine();
				output.write(move.getBytes(),0,move.length());
				output.flush();
            }
            // Debut de la partie en joueur Noir
            if(cmd == '2'){
                System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
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
		System.out.println("size :" + size);
		input.read(aBuffer,0,size);
				
		String s = new String(aBuffer);
		System.out.println("Dernier coup :"+ s);
		giantBoard.play(s, Mark.X);
		giantBoard.getAllMoves(s);

		System.out.println("Entrez votre coup : ");
		Client client = new Client();
		String move =client.getNextMoveMinMax(giantBoard, 0, Mark.O, "A0").get(0).toString();
		giantBoard.play(move, Mark.O);
		move = console.readLine();
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
	public ArrayList<Move> getNextMoveMinMax(GiantBoard board,int depth, Mark mark, String lastMove )
    {
		System.out.println("Last Move : "+lastMove);
		System.out.println("GetNextMoveMinMax");
        ArrayList<Move> coupsPossibles = board.getAllMoves(lastMove==null?"A0":lastMove);
        int meilleurScore= Integer.MIN_VALUE;
        ArrayList<Move> meilleursCoups = new ArrayList<>();

		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
        for(Move move: coupsPossibles)
        {
			System.out.println("Move : "+move.toString());
			String s = move.toString();
            board.play(s, mark);
			int score = minimax(board,false,mark,0,move,alpha,beta);

			if(score > meilleurScore)
            {
                meilleurScore = score;
                meilleursCoups.clear();
                meilleursCoups.add(move);
            }
            else if(score == meilleurScore)
            {
                meilleursCoups.add(move);
            }
			alpha = Math.max(alpha, meilleurScore);
			if(alpha >= beta)
			{
				break;
			}
			board.undoMove(move);
        }
		return meilleursCoups;
    }

	public int minimax(GiantBoard board, boolean joueurMax, Mark mark, int depth, Move lastMove, int alpha, int beta) {
        int score = board.evaluate(mark);

        // Condition d'arrêt
        if (depth >= 2 || Math.abs(score) == 100 || board.isFull()) {
            System.out.println("Score : "+score);
            return score;
        }

        int meilleurScore = joueurMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        mark = (mark == Mark.X) ? Mark.O : Mark.X;
        
        ArrayList<Move> coupsPossibles = board.getAllMoves(lastMove.toString());

        for (Move move : coupsPossibles) {
            board.play(move.toString(), mark);
            score = minimax(board, !joueurMax, mark, depth + 1, move, alpha, beta);
            board.undoMove(move);

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