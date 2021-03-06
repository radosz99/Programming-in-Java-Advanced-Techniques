package game.algorithm;

import game.TilesValue;
import javafx.util.Pair;
import jdk.nashorn.api.scripting.JSObject;

import javax.script.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Handler {
    final static String scriptsPath = "src/main/resources/";
    final static String engineName = "nashorn";
    final static int boardSize = 5;

    public Pair<Integer,Integer> makeMove(String strategy, String lang, List<List<TilesValue>> tilesValue) {
        switch(lang){
            case "Javascript": {
                return runJSAlgorithm(strategy, tilesValue);
            }
            case "C++": {
                return runCppAlgorithm(strategy, tilesValue);
            }
        }
        return null;
    }

    private Pair<Integer, Integer> runJSAlgorithm(String strategy, List<List<TilesValue>> tilesValue){
        Pair<Integer, Integer> coords = null;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName(engineName);
        Bindings bind = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bind.put("board", convertBoardToIntegers(tilesValue));
        try {
            JSObject result = (JSObject) engine.eval(new FileReader(scriptsPath + strategy.toLowerCase() +".js"));
            coords = new Pair<>((Integer) result.getMember("x"), (Integer) result.getMember("y"));
        } catch (ScriptException | FileNotFoundException ex) {
            System.err.println("Błąd w skrypcie");
        }
        return coords;
    }

    private static Pair<Integer, Integer> runCppAlgorithm(String strategy, List<List<TilesValue>> tilesValue){
        Pair<Integer, Integer> coords = null;
        ArrayList<ArrayList<Integer>> board = convertBoardToIntegers(tilesValue);
        CppInterface cpp = new CppInterface();

        int[] tab = new int[(int) Math.pow(boardSize,2)];
        for(int i=0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                tab[i * boardSize + j] = board.get(i).get(j);
            }
        }
        int[] cppCoords;
        switch(strategy){
            case "Random":{
                cppCoords = cpp.makeRandom(tab);
                coords = new Pair<>(cppCoords[0],cppCoords[1]);
                break;
            }

            case "Greedy":{
                cppCoords = cpp.makeFirst(tab);
                coords = new Pair<>(cppCoords[0],cppCoords[1]);
                break;
            }
        }
        return coords;
    }

    private static ArrayList<ArrayList<Integer>> convertBoardToIntegers(List<List<TilesValue>> board){
        ArrayList<ArrayList<Integer>> convertedBoard = new ArrayList<>();
        for(List<TilesValue> row : board){
            ArrayList<Integer> newRow = new ArrayList<>();
            for(TilesValue val : row){
                switch(val){
                    case EMPTY:
                        newRow.add(0);
                        break;
                    case O:
                        newRow.add(-1);
                        break;
                    case X:
                        newRow.add(1);
                        break;
                }
            }
            convertedBoard.add(newRow);
        }
        return convertedBoard;
    }

}
