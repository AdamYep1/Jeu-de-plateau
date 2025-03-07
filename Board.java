import java.util.ArrayList;

// IMPORTANT: Il ne faut pas changer la signature des méthodes
// de cette classe, ni le nom de la classe.
// Vous pouvez par contre ajouter d'autres méthodes (ça devrait 
// être le cas)
class Board
{
    private Mark[][] board;
    private Mark winingMark = Mark.EMPTY;
    private Move firstMove;

    // Ne pas changer la signature de cette méthode
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

    // Place la pièce 'mark' sur le plateau, à la
    // position spécifiée dans Move
    //
    // Ne pas changer la signature de cette méthode
    // public void play(Move m, Mark mark){
    //     if (board[m.getRow()][m.getCol()] == Mark.EMPTY) {
    //         board[m.getRow()][m.getCol()] = mark;
    //     }
    // }


    // retourne  100 pour une victoire
    //          -100 pour une défaite
    //           0   pour un match nul
    // Ne pas changer la signature de cette méthode
    // public int evaluate(Mark mark){
    //     for (int i = 0; i < board.length; i++){
    //         if (board[i][0] != Mark.EMPTY && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
    //             if (board[i][0] == mark) {
    //                 return 100;
    //             } else {
    //                 return -100;
    //             }
    //         }
    //         if (board[0][i] != Mark.EMPTY && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
    //             if (board[0][i] == mark) {
    //                 return 100;
    //             } else {
    //                 return -100;
    //             }
    //         }
    //     }
    //     if (board[0][0] != Mark.EMPTY && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
    //         if (board[0][0] == mark) {
    //             return 100;
    //         } else {
    //             return -100;
    //         }
    //     }
    //     if (board[0][2] != Mark.EMPTY && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
    //         if (board[0][2] == mark) {
    //             return 100;
    //         } else {
    //             return -100;
    //         }
    //     }

    //     return 0;
    // }

    public ArrayList<Move> getAllMoves(){
        ArrayList<Move> moves = new ArrayList<>();
        if (winingMark == Mark.EMPTY) {
            for(int i = 0; i < board.length; i++){
                for(int j = 0;  j < board[i].length; j++){
                    if (board[i][j] == Mark.EMPTY) {
                        Move move = new Move(i + 1, (char)(firstMove.getCol() + j));
                        moves.add(move);
                    }
                }
            }
        }
        return moves;
    }

    // public void undo(Move m){
    //     board[m.getRow()][m.getCol()] = Mark.EMPTY;
    // }
}
