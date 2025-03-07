import java.util.ArrayList;

public class GiantBoard {
    static final Move INVALIDE_MOVE = new Move(0, 'A');
    private Board[][] giantBoard;

    // Ne pas changer la signature de cette méthode
    public GiantBoard() {
        int cptR = 1;
        int cptC = 'a';
        giantBoard = new Board[3][3];
        for(int i = 0; i < giantBoard.length; i++){
            for(int j = 0; j < giantBoard[i].length; j++){
                Move firstMove = new Move(i + cptR, (char)(cptC));
                giantBoard[i][j] = new Board(firstMove);
                cptC = cptC + 3;
            }
            cptR = cptR + 3;
        }
    }

    public ArrayList<Move> getAllMoves(Move lastMove){
        ArrayList<Move> moves = new ArrayList<>();
        if (lastMove.equals(INVALIDE_MOVE)){
            for(int i = 0; i < giantBoard.length; i++){
                for(int j = 0;  j < giantBoard[i].length; j++){
                    moves.addAll(giantBoard[i][j].getAllMoves());
                }
            }
        } else {
            for(int i = 0; i < giantBoard.length; i++){
                for(int j = 0;  j < giantBoard[i].length; j++){

                }
            } 
        }

        return moves;
    }
}
