import java.util.ArrayList;

public class GiantBoard {
    static final Move INVALIDE_MOVE = new Move(0, 'A');
    private Board[][] giantBoard;

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
}
