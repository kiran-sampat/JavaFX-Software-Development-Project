package com.app.engine;

/**
 * Enum with the game objects used within the game for maps
 *
 */
public enum GameObject {
    WALL('W'),
    VOID('W'),
    FLOOR(' '),
    CRATE('C'),
    DIAMOND('D'),
    KEEPER('S'),
    CRATE_ON_DIAMOND('O'),
    DEBUG_OBJECT('=');

    private final char symbol;

    GameObject(final char symbol) {
        this.symbol = symbol;
    }

    /**
     * makes sure all symbols are in upper case format
     *
     * @param c the character from the enum
     * @return the game object corresponding to symbol
     */
    public static GameObject fromChar(char c) {
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.symbol) {
                return t;
            }
        }

        return WALL;
    }

    /**
     * returns the symbol as a string
     *
     * @return the symbol
     */
    public String getStringSymbol() {
        return String.valueOf(symbol);
    }

    /**
     * returns the symbol as a char
     *
     * @return the symbol
     */
    public char getCharSymbol() {
        return symbol;
    }
}