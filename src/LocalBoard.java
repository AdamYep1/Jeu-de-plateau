import java.util.ArrayList;

class LocalBoard 
{
    private Mark[][] board;
    private Mark winingMark = Mark.EMPTY;
    private Move firstMove;

    public LocalBoard(Move firstMove) {
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
                        moves.add(move);
                    }
                }
            }
        }
        return moves;
    }
    public void printSmallBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] == Mark.EMPTY ? "-" : board[i][j]);
                
                if (j < 2) {
                    System.out.print(" | ");
                }
            }
            System.out.println(); 
            
            if (i < 2) {
                System.out.println("---------");
            }
        }
    }

    public void evaluate(Mark mark){
        Mark markOpponent = getOpponentMark(mark);
        if(checkWin(mark)){
            winingMark = mark;
        }
        if(checkWin(markOpponent)){
            winingMark = markOpponent;
        }
    }

    public boolean checkWin(Mark mark){
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

    public int localBoardPotential(Mark mark){
        int potential = 0;
        // Vérifie les lignes
        for (int i = 0; i < 3; i++) {
            int playerCount = 0;
            int emptyCount = 0;
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == mark) playerCount++;
                else if (board[i][j] == Mark.EMPTY) emptyCount++;
            }
            if (playerCount == 2 && emptyCount == 1) potential++;
        }
        
        // Vérifie les colonnes
        for (int j = 0; j < 3; j++) {
            int playerCount = 0;
            int emptyCount = 0;
            for (int i = 0; i < 3; i++) {
                if (board[i][j] == mark) playerCount++;
                else if (board[i][j] == Mark.EMPTY) emptyCount++;
            }
            if (playerCount == 2 && emptyCount == 1) potential++;
        }
        
        // Vérifie les diagonales
        int playerCount = 0;
        int emptyCount = 0;
        for (int i = 0; i < 3; i++) {
            if (board[i][i] == mark) playerCount++;
            else if (board[i][i] == Mark.EMPTY) emptyCount++;
        }
        if (playerCount == 2 && emptyCount == 1) potential++;
        
        playerCount = 0;
        emptyCount = 0;
        for (int i = 0; i < 3; i++) {
            if (board[i][2-i] == mark) playerCount++;
            else if (board[i][2-i] == Mark.EMPTY) emptyCount++;
        }
        if (playerCount == 2 && emptyCount == 1) potential++;

        return potential;
    }
    public int evaluateLocalControl(Mark mark) {
        int control = 0;
        
        // Cases stratégiques (centre et coins)
        if (board[1][1] == mark) control += 3; // Centre
        if (board[0][0] == mark) control += 2; // Coin
        if (board[0][2] == mark) control += 2; // Coin
        if (board[2][0] == mark) control += 2; // Coin
        if (board[2][2] == mark) control += 2; // Coin
        
        return control;
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
        int row = firstMove.getRow() - m.getRow();
        int col = m.getCol() - firstMove.getCol();
        if (board[row][col] != Mark.EMPTY) {
            board[row][col] = Mark.EMPTY;
            winingMark = Mark.EMPTY;
        }
    }
}