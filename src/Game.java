package com.codegym.games.game2048;
import com.codegym.engine.cell.*;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;

    @Override
    public void initialize(){
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    private void createGame(){
        gameField = new int[SIDE][SIDE];
        score = 0;
        setScore(score);
        createNewNumber();
        createNewNumber();

    }

    private void drawScene(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    private void setCellColoredNumber(int x, int y, int value){
        Color color = getColorByValue(value);
        String str = value > 0 ? "" + value : "";
        setCellValueEx(x, y, color, str);

    }

    private boolean compressRow(int[] row){
        int position = 0;
        boolean result = false;
        for (int x = 0; x < SIDE; x++) {
            if (row[x] > 0) {
                if (x != position) {
                    row[position] = row[x];
                    row[x] = 0;
                    result = true;
                }
                position++;
            }
        }
        return result;

    }

    private Color getColorByValue(int value){
        switch (value) {
            case 0:
                return Color.WHITE;
            case 2:
                return Color.TURQUOISE;
            case 4:
                return Color.BROWN;
            case 8:
                return Color.MAGENTA;
            case 16:
                return Color.YELLOW;
            case 32:
                return Color.CYAN;
            case 64:
                return Color.GREEN;
            case 128:
                return Color.ORANGE;
            case 256:
                return Color.LIME;
            case 512:
                return Color.NAVY;
            case 1024:
                return Color.PINK;
            case 2048:
                return Color.BLUE;
            default:
                return Color.NONE;
        }
    }

    private boolean mergeRow(int[] row){
        boolean result = false;
        for(int i = 0; i < row.length - 1; i++){
            if(row[i] != 0 && row[i] == row[i + 1]){
                row[i] += row[i + 1];
                score += row[i];
                setScore(score);
                row[i + 1] = 0;
                result = true;
            }
        }
        return result;
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.SPACE) {
            if (isGameStopped) {
                isGameStopped = false;
                createGame();
                drawScene();
            }
        } else if (canUserMove()) {
            if (!isGameStopped) {
                if (key == Key.UP) {
                    moveUp();
                } else if (key == Key.RIGHT) {
                    moveRight();
                } else if (key == Key.DOWN) {
                    moveDown();
                } else if (key == Key.LEFT) {
                    moveLeft();
                } else {
                    return;
                }
                drawScene();
            }
        } else gameOver();
    }


    private void moveLeft(){
        boolean isNewNumberNeeded = false;
        for (int[] row : gameField) {
            boolean wasCompressed = compressRow(row);
            boolean wasMerged = mergeRow(row);
            if (wasMerged) {
                compressRow(row);
            }
            if (wasCompressed || wasMerged) {
                isNewNumberNeeded = true;
            }
        }
        if(isNewNumberNeeded){
            createNewNumber();
        }
    }

    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }

    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();

    }

    private void rotateClockwise(){
        int[][] result = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++){
            for (int j = 0; j < SIDE; j++){
                result[j][SIDE - 1 - i] = gameField[i][j];
            }
        }
        gameField = result;
    }

    private int getMaxTileValue(){
        int max = gameField[0][0];
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++){
                if (gameField[y][x] > max) {
                    max = gameField[y][x];
                }
            }
        }
        return max;
    }

    private void win(){
        showMessageDialog(Color.NONE, "Congratulations WINNER!", Color.BLUE, 50);
        isGameStopped = true;
    }

    private void gameOver(){
        showMessageDialog(Color.NONE, "YOU LOSE!", Color.RED, 50);
        isGameStopped = true;
    }

    private boolean canUserMove(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++){
                if (gameField[y][x] == 0) {
                    return true;
                } else if (y < SIDE -1 && gameField[y][x] == gameField[y + 1][x]){
                    return true;
                }else if (x < SIDE - 1 && gameField[y][x] == gameField[y][x + 1]){
                    return true;
                }
            }
        }
        return false;

    }



    private void createNewNumber(){
        if(getMaxTileValue() >= 2048){
            win();
            return;
        }

        boolean isCreated = false;
        do{
            int x = getRandomNumber(SIDE);
            int y = getRandomNumber(SIDE);
            if (gameField[y][x] == 0) {
                gameField[y][x] = getRandomNumber(10) == 9 ? 4 : 2;
                isCreated = true;
            }
        }
        while(!isCreated);
    }

}


