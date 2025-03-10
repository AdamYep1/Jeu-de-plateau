

class Move
{
    private int row;
    private char col;

    public Move(){
        row = -1;
        col = ' ';
    }

    public Move(int r, char c){
        row = r;
        col = c;
    }

    public int getRow(){
        return row;
    }

    public char getCol(){
        return col;
    }

    public void setRow(int r){
        row = r;
    }

    public void setCol(char c){
        col = c;
    }
    public Boolean equals(Move move){
        if (row == move.getRow() && col == move.getCol()) {
            return true;
        } else {
            return false;
        }
    }
    public String toString(){
        return col+ String.valueOf(row);
    }
}
