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
                        System.out.print(move.getCol() + "" + move.getRow());
                        moves.add(move);
                    } else {
                        System.out.print("XX");
                    }
                }
                System.out.println();
            }
        }
        return moves;
    }

    public Mark getWiningMark(){
        return winingMark;
    }

    public void play(Move m, Mark mark){
        int row = firstMove.getRow() - m.getRow();
        int col = m.getCol() - firstMove.getCol();
        if (board[row][col] == Mark.EMPTY) {
            board[row][col] = mark;
        }
    }
}
