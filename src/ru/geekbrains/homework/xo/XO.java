package ru.geekbrains.homework.xo;

import java.util.Random;
import java.util.Scanner;

public class XO {
    private static final char DOT_X = 'X', DOT_O = 'O', DOT_EMPTY = ' ';

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field;
    private static int fieldSizeY, fieldSizeX, size;
    private static int winLength;

    public static void main(String[] args) {
        System.out.println("What size board(3-5)?");
        while(true) {
            size = scanner.nextInt();
            if(!(size <=5 && size >= 3))
                System.out.println("Incorrect board size. Try again");
            else {break;}
        }
        winLength = size >= 4 ? 4 : 3;
        initField(size);
        printField();

        while(true) {
            humanTurn();
            printField();
            if(checkWin(DOT_X)) {
                System.out.println("You won!");
                break;
            }
            if(checkDraw()) {
                System.out.println("Draw");
                break;
            }
            aiTurn();
            printField();
            if(checkWin(DOT_O)) {
                System.out.println("AI won!");
                break;
            }
            if(checkDraw()) {
                System.out.println("Draw");
                break;
            }
        }
        System.out.println("Game over!");
    }


    private static void initField(int size) {
        fieldSizeY = size;
        fieldSizeX = size;
        field = new char[fieldSizeY][fieldSizeX];
        for(int y = 0;y < fieldSizeY;y++) {
            for(int x = 0;x < fieldSizeX;x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        System.out.print("   ");
        for(int i = 0;i < fieldSizeX;i++) {
            System.out.print("  " + (int)(i + 1) + "  ");
        }
        System.out.println();
        System.out.print("  +");
        for(int i = 0;i < fieldSizeX;i++) {
            System.out.print("----+");
        }
        System.out.println();
        for(int i = 0; i < fieldSizeY;i++) {
            System.out.print(i + 1 + " | ");
            for(int j = 0; j < fieldSizeX;j++) {
                System.out.print(" " + field[i][j] + " | ");
            }
            System.out.println();
            System.out.print("  +");
            for(int j = 0;j < fieldSizeX;j++) {
                System.out.print("----+");
            }
            System.out.println();
        }
    }

    private static void humanTurn() {
        int x, y;
        do {
            System.out.println("Please enter coordinates x & y >>>");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }while (!isCellValid(y,x));

        field[y][x] = DOT_X;
    }

    private static void aiTurn() {
        int x = 0, y = 0; // пришлось инициилизировать нулями. странно
        boolean humanPreWin = false;

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if(isCellFree(i,j)) {
                    field[i][j] = DOT_X;
                    if(checkWin(DOT_X)) {
                        humanPreWin = true;
                        field[i][j] = DOT_EMPTY;
                        y = i;
                        x = j;
                        break;
                    }else {
                        field[i][j] = DOT_EMPTY;
                    }
                }
            }
        }

        if(humanPreWin == false) {
            do {
                x = random.nextInt(fieldSizeX);
                y = random.nextInt(fieldSizeY);
            }while (!isCellValid(y,x));
        }

        field[y][x] = DOT_O;
    }

    private static boolean isCellValid(int y,int x) {
        return x >=0 && y >=0 && x < fieldSizeX &&y < fieldSizeY && field[y][x] == DOT_EMPTY;
    }

    private static boolean isCellFree(int y, int x) {
        if(field[y][x] == DOT_EMPTY) return true;
        return false;
    }

    private static boolean checkDraw() {
        for(int y = 0;y < fieldSizeY;y++) {
            for(int x = 0;x < fieldSizeX;x++) {
                if(field[y][x] == DOT_EMPTY)
                    return false;
            }
        }
        return true;
    }

    private static boolean checkWin(char dot) {
        for(int y = 0;y <= size - winLength;y++) {
            for(int x = 0;x <= size - winLength;x++) {
                if(checkDiagonals(dot, y, x)||checkRowsAndColumns(dot,y,x)) return true;
            }
        }
        return false;
    }

    private static boolean checkRowsAndColumns(char dot, int shiftX, int shiftY) {
        boolean row;
        boolean column;
        for(int y = shiftY;y < winLength + shiftY;y++) {
            row = true;
            column = true;
            for(int x = shiftX;x < winLength + shiftX;x++) {
                row = row && (field[y][x] == dot);
                column = column && (field[x][y] == dot);
            }
            if(row||column) return true;
        }
        return false;
    }

    private static boolean checkDiagonals(char dot, int shiftX, int shiftY) {
        boolean toDown = true;
        boolean toUpper = true;
        for(int i = shiftX,y = shiftY,j = shiftY + winLength - 1;i < winLength;i++,y++,j--) {
            toDown = toDown && (field[i][y] == dot);
            toUpper = toUpper && (field[i][j] == dot);
        }
        if(toDown||toUpper) return true;
        return false;
    }
}
