import java.util.ArrayList;

public class GiantBoard {
    static final Move INVALIDE_MOVE = new Move(0, 'A');
    private Board[][] giantBoard;
    private static int max = 100;
    private static int min = -100;

    // Ne pas changer la signature de cette méthode
    public GiantBoard() {
        int cptR = 9;
        int cptC = 'A';
        giantBoard = new Board[3][3];
        for(int i = 0; i < giantBoard.length; i++){
            for(int j = 0; j < giantBoard[i].length; j++){
                Move firstMove = new Move(cptR, (char)(cptC));
                giantBoard[i][j] = new Board(firstMove);
                cptC = cptC + 3;
            }
            cptC = 'A';
            cptR = cptR - 3;
        }
    }

    public ArrayList<Move> getAllMoves(String lastMove){
        ArrayList<Move> moves = new ArrayList<>();
        Move m = stringToMove(lastMove);
        System.out.println("get all moves");
        int row = 2;
        int col = 0;
        for (int i = 0; i < m.getRow() - 1; i++){
            if (row == 0) {
                row = 2;
            } else {
                row--;
            }
            
        }
        for (int i = 0; i < m.getCol() - 'A'; i++){
            if (col == 2) {
                col = 0;
            } else {
                col++;
            }
        }
        if (m.equals(INVALIDE_MOVE) || giantBoard[col][row].getWiningMark() != Mark.EMPTY){
            for(int i = 0; i < giantBoard.length; i++){
                for(int j = 0;  j < giantBoard[i].length; j++){
                    moves.addAll(giantBoard[i][j].getAllMoves());
                }
            }
        } else {
            moves.addAll(giantBoard[row][col].getAllMoves());
        }

        return moves;
    }

    public int evaluate(Mark mark) {
        int score = 0; 
        
        Mark[][] boardState = new Mark[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardState[i][j] = giantBoard[i][j].getWiningMark();
            }
        }
        
        return evaluateBoard(mark, boardState);
    }

     public int evaluateBoard(Mark mark, Mark[][] board){
        Mark markOpponent = getOpponentMark(mark);
        if(checkWin(mark, board)){
            return 100;
        }
        if(checkWin(markOpponent, board)){
            return -100;
        }
        if(isFull())
            return 0;
        
        int score = 0;
        return score;
    }

    public boolean  checkWin(Mark mark, Mark[][]board){
        for(int i =0 ; i<3 ; i++)
        {
            if(board[i][0] == mark && board[i][1] == mark && board[i][2] == mark || 
               board[0][i] == mark && board[1][i] == mark && board[2][i] == mark)
                return true;
        }
        if(board[0][0] == mark && board[1][1] == mark && board[2][2] == mark||board[0][2] == mark && board[1][1] == mark && board[2][0] == mark)
            return true;
        return false;
    }

    public boolean isFull(){
        for(int i = 0; i < giantBoard.length; i++){
            for(int j = 0; j < giantBoard[i].length; j++){
                if (!giantBoard[i][j].isFull()) {
                    return false;
                }
            }
        }
        return true;
    }


    public void play(String s, Mark mark){  
        System.out.println("Play" +s);
        Move move = stringToMove(s);
        int row = (9 - move.getRow())/3;
        int col = (move.getCol() - 'A')/3;
        // System.out.println(row);
        // System.out.println(col);
        giantBoard[row][col].play(move, mark);
        System.out.println("Board global");
        printGiantBoard();
    }
    public void printGiantBoard() {
    // Iterate over the giant board's 3x3 grid of sub-grids
    for (int i = 0; i < giantBoard.length; i++) {
        for (int j = 0; j < giantBoard[i].length; j++) {
            // Print the sub-board at position [i][j]
            giantBoard[i][j].printSmallBoard();
            if (j < giantBoard[i].length - 1) {
                System.out.print(" | ");  // Print a separator between sub-grids
            }
        }
        System.out.println();  // Move to the next line after each row of sub-grids
        if (i < giantBoard.length - 1) {
            System.out.println("-----------");  // Print a separator between rows of sub-grids
        }
    }
}
    public Move stringToMove(String s) {
        char[] charArray = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            charArray[i] = s.charAt(i);
        }
       
    char col = s.charAt(0);  // Column is the first character (e.g., 'A')
    int row = Character.getNumericValue(s.charAt(1));  // Row is the second character (e.g., '1')

    return new Move(row, col);  // Assuming Move constructor takes row and column

    }


    public Mark getOpponentMark(Mark mark){
        return (mark == Mark.X) ? Mark.O : Mark.X;
    }

    public void undoMove(Move move) {
    int row = (9 - move.getRow()) / 3;
    int col = (move.getCol() - 'A') / 3;
    giantBoard[row][col].undo(move);  // Implémenter `removeMove()` dans `Board`
}

}
