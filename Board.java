import java.util.ArrayList;

class Board
{
    private Mark[][] board;
    private Mark winingMark = Mark.EMPTY;
    private Move firstMove;

    public Board(Move firstMove) {
        this.firstMove = firstMove;
        board = new Mark[3][3];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                board[i][j] = Mark.EMPTY;
            }
        }
    }
    public Mark[][] getBoard(){
        return board;
    }

    public ArrayList<Move> getAllMoves(){
        ArrayList<Move> moves = new ArrayList<>();
        if (winingMark == Mark.EMPTY) {
            for(int i = 0; i < board.length; i++){
                for(int j = 0;  j < board[i].length; j++){
                    if (board[i][j] == Mark.EMPTY) {
                        Move move = new Move(firstMove.getRow() - i, (char)(firstMove.getCol() + j));
                        //System.out.print(move.getCol() + "" + move.getRow());
                        moves.add(move);
                    } else {
                        //System.out.print("XX");
                    }
                }
                //System.out.println();
            }
        }
        return moves;
    }
    public void printSmallBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Print the mark in the current cell
                System.out.print(board[i][j] == Mark.EMPTY ? "-" : board[i][j]);
                
                // Print a separator between columns
                if (j < 2) {
                    System.out.print(" | ");
                }
            }
            System.out.println(); // Move to the next line after each row
            
            // Print a horizontal line separator after each row except the last one
            if (i < 2) {
                System.out.println("---------");
            }
        }
    }

    public int evaluate(Mark mark){
        Mark markOpponent = getOpponentMark(mark);
        if(checkWin(mark)){
            winingMark = mark;
            return 100;}
        if(checkWin(markOpponent)){
            winingMark = markOpponent;
            return -100;}
        if(isFull())
            return 0;
        
        int score = 0;

        return score;
    }

    public boolean  checkWin(Mark mark){
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

    public Mark getOpponentMark(Mark mark){
        return (mark == Mark.X) ? Mark.O : Mark.X;
    }

    public boolean isFull(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if (board[i][j] == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public Mark getWiningMark(){
        return winingMark;
    }

    public void play(Move m, Mark mark) {
        int row = firstMove.getRow() - m.getRow();
        int col = m.getCol() - firstMove.getCol(); 
        // Vérifier si la case est vide avant de jouer
        if (board[row][col] == Mark.EMPTY) {
            board[row][col] = mark;
        } else {
            throw new IllegalArgumentException("Case déjà occupée !");
        }
        evaluate(mark);
    }

    public void undo(Move m){
        //System.out.println("Undo" + m.toString());
        int row = firstMove.getRow() - m.getRow();
        int col = m.getCol() - firstMove.getCol();
        if (board[row][col] != Mark.EMPTY) {
            board[row][col] = Mark.EMPTY;
            winingMark = Mark.EMPTY;
        }
    }
}