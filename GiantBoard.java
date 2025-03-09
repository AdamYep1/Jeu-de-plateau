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
                    System.out.println();
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
        if(isFull(board))
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

    public boolean isFull(Mark[][] board){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if (board[i][j] == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }


    public void play(String s, Mark mark){  
        Move move = stringToMove(s);
        int row = (9 - move.getRow())/3;
        int col = (move.getCol() - 'A')/3;
        // System.out.println(row);
        // System.out.println(col);
        giantBoard[row][col].play(move, mark);
    }

    public Move stringToMove(String s){
        char[] charArray = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            charArray[i] = s.charAt(i);
        }
        char col = charArray[1];
        int row = Character.getNumericValue(charArray[2]);
        return new Move(row, col);
    }
    public Mark getOpponentMark(Mark mark){
        return (mark == Mark.X) ? Mark.O : Mark.X;
    }

}
