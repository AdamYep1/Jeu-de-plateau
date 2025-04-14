import java.util.ArrayList;

public class GlobalBoard {
    static final Move INVALIDE_MOVE = new Move(0, 'A');
    private LocalBoard[][] globalBoard;

    public static final int WIN_SCORE = 10000;
    public static final int LOSE_SCORE = -10000;
    protected static final int LOCAL_WIN_SCORE = 100;
    protected static final int LOCAL_LOSE_SCORE = -100;
    protected static final int POTENTIAL_WIN_SCORE = 15;
    protected static final int STRATEGIC_BOARD_SCORE = 20;

    // Ne pas changer la signature de cette méthode
    public GlobalBoard() {
        int cptR = 9;
        int cptC = 'A';
        globalBoard = new LocalBoard[3][3];
        for(int i = 0; i < globalBoard.length; i++){
            for(int j = 0; j < globalBoard[i].length; j++){
                Move firstMove = new Move(cptR, (char)(cptC));
                globalBoard[i][j] = new LocalBoard(firstMove);
                cptC = cptC + 3;
            }
            cptC = 'A';
            cptR = cptR - 3;
        }
    }

    public GlobalBoard(GlobalBoard board) {
        this.globalBoard = board.getBoard();
    }
    public LocalBoard[][] getBoard(){
        return globalBoard;
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
        if (m.equals(INVALIDE_MOVE) || globalBoard[row][col].getWiningMark() != Mark.EMPTY){
            for(int i = 0; i < globalBoard.length; i++){
                for(int j = 0;  j < globalBoard[i].length; j++){
                    moves.addAll(globalBoard[i][j].getAllMoves());
                }
            }
        } else {
            moves.addAll(globalBoard[row][col].getAllMoves());
        }

        return moves;
    }

    public int evaluate(Mark mark) { 
        for (int i = 0; i < globalBoard.length; i++){
            for (int j = 0; j <globalBoard[i].length; j++){
                globalBoard[i][j].evaluate(mark);
            }
        }
        Mark opponent = getOpponentMark(mark);

        // 1) global win
        Mark globalWinner = globalBoardWinner(mark);
        if (globalWinner == mark) return WIN_SCORE;
        if (globalWinner == opponent) return LOSE_SCORE;

        int score = 0;
        
        // 2) num localBoard wins
        int playerWins = countLocalWins(mark);
        int opponentWins = countLocalWins(opponent);
        score += LOCAL_WIN_SCORE * (playerWins - opponentWins);
        
        // 3) potential win localBoard
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (globalBoard[i][j].getWiningMark() != Mark.EMPTY) continue;
                
                int playerPotential = globalBoard[i][j].localBoardPotential(mark);
                int opponentPotential = globalBoard[i][j].localBoardPotential(opponent);
                score += POTENTIAL_WIN_SCORE * (playerPotential - opponentPotential);
            }
        }
        
        // 4) strategic controle
        Coordinate[] strategicBoards = {
            new Coordinate(0, 0), new Coordinate(0, 2),
            new Coordinate(2, 0), new Coordinate(2, 2),
            new Coordinate(1, 1) // Centre
        };
        for (Coordinate coord : strategicBoards) {
            if (globalBoard[coord.row][coord.col].getWiningMark() == mark) {
                score += STRATEGIC_BOARD_SCORE;
            } else if (globalBoard[coord.row][coord.col].getWiningMark() == opponent){
                score -= STRATEGIC_BOARD_SCORE;
            } else {
                int playerControl = globalBoard[coord.row][coord.col].evaluateLocalControl(mark);
                int opponentControl = globalBoard[coord.row][coord.col].evaluateLocalControl(opponent);
                score += 5 * (playerControl - opponentControl);
            }
        }
        return score;
    }

    public Mark globalBoardWinner (Mark mark){
        Mark markOpponent = getOpponentMark(mark);
        if(checkGlobalWin(mark)){
            return mark;
        }
        if(checkGlobalWin(markOpponent)){
            return markOpponent;
        }
        if(isFull())
            return null;

        return null;
    }

    public boolean checkGlobalWin(Mark mark){
        for(int i =0 ; i<3 ; i++)
        {
            if(globalBoard[i][0].getWiningMark() == mark && globalBoard[i][1].getWiningMark() == mark && globalBoard[i][2].getWiningMark() == mark || 
            globalBoard[0][i].getWiningMark() == mark && globalBoard[1][i].getWiningMark() == mark && globalBoard[2][i].getWiningMark() == mark)
                return true;
        }
        if(globalBoard[0][0].getWiningMark() == mark && globalBoard[1][1].getWiningMark() == mark && globalBoard[2][2].getWiningMark() == mark || 
        globalBoard[0][2].getWiningMark() == mark && globalBoard[1][1].getWiningMark() == mark && globalBoard[2][0].getWiningMark() == mark)
            return true;
        return false;
    }

    public int countLocalWins(Mark mark){
        int count = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (globalBoard[i][j].getWiningMark() == mark){
                    count++;
                };
            }
        }
        return count;
    }

    public boolean isFull(){
        for(int i = 0; i < globalBoard.length; i++){
            for(int j = 0; j < globalBoard[i].length; j++){
                if (globalBoard[i][j].getWiningMark() == Mark.EMPTY) {
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
        globalBoard[row][col].play(move, mark);
    }

    public Move stringToMove(String s) {
        char col =' ';
        int row = 0;
       if(s.charAt(0)==' ')
       {
            col = s.charAt(1);
            row = Character.getNumericValue(s.charAt(2));
            
       }
       else
       {
            col = s.charAt(0);
            row = Character.getNumericValue(s.charAt(1));
       }

        return new Move(row, col);  
    }


    public Mark getOpponentMark(Mark mark){
        return (mark == Mark.X) ? Mark.O : Mark.X;
    }

    public void undoMove(Move move) {
        int row = (9 - move.getRow()) / 3;
        int col = (move.getCol() - 'A') / 3;
        globalBoard[row][col].undo(move);  
    }

}