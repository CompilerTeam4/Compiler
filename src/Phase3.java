import java.io.IOException;
import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.Collections.sort;

public class Phase3 {

    public static void main(String[] args) throws IOException {

        Phase2.main(null);


        //import grammar:
        Grammar.importGrammar();
        Grammar.printGrammar();


        LL1Table.createTable();
        LL1Table.printTable();


    }
}
//pushing

class Grammar {
    static final String EPSILON = "ɛ" , ID = "?ID?", NUMBER = "?NUMBER?", STRING_LITERAL = "?STRING?";
    static HashMap<String, List<String>[]> allGrammars = new HashMap<>();
    String name;
    List<String>[] structures;

    @SafeVarargs
    Grammar(String name, List<String>... structures) {
        this.name = name;
        this.structures = structures;
        allGrammars.put(name, structures);
    }
    // function importgrammer
    static void importGrammar() {
        new Grammar("<program>", Arrays.asList("int main", "(", ")", "{", "<statements>", "}"));

        new Grammar("<if statement>", Arrays.asList("if", "(", "<condition>", ")", "{", "<statements>", "}"),
                Arrays.asList("if", "(", "<condition>", ")", "{", "<statements>", "}", "else", "{", "<statements>", "}"));

        new Grammar("<for statement>", Arrays.asList("for", "(", "<assignment>", "<condition>", ";", "<assignment>", ")", "{", "<statements>", "}"),
                Arrays.asList("for", "(", "<var declaration>", "<condition>", ";", "<assignment>", ")", "{", "<statements>", "}"));

        new Grammar("<while statement>", Arrays.asList("while", "(", "<condition>", ")", "{", "<statements>", "}"));

        new Grammar("<condition>", Arrays.asList("<expression>", "<relational operator>", "<expression>"));

        new Grammar("<relational operator>", singletonList("<"), singletonList(">"), singletonList("<="),
                singletonList(">="), singletonList("=="));

        new Grammar("<var declaration>", Arrays.asList("<var_type>", ID, ";"),
                Arrays.asList("<var_type>", ID, "=", "<expression>", ";"));

        new Grammar("<expression>", Arrays.asList("<term>", "<expression’>"));

        new Grammar("<expression'>", Arrays.asList("+", "<term>", "<expression'>"),
                Arrays.asList("-", "<term>", "<expression'>"), singletonList(EPSILON));

        new Grammar("<term>", Arrays.asList("<factor>", "<term'>"));

        new Grammar("<term'>", Arrays.asList("*", "<factor>", "<term'>"),
                Arrays.asList("/", "<factor>", "<term'>"), singletonList(EPSILON));

        new Grammar("<factor>", singletonList(ID), singletonList(NUMBER), Arrays.asList("(", "<expression>", ")")
                , singletonList(STRING_LITERAL));


        new Grammar("<statements>", Arrays.asList("<statement>", "<statements>"));

        new Grammar("<statement>", singletonList("<assignment>"), singletonList("<var declaration>"),
                singletonList("<if statement>"), singletonList("<for statement>"), singletonList("<while statement>"));

        new Grammar("<assignment>", Arrays.asList("ID", "=", "<expression>", ";"), Arrays.asList("ID", "++", ";"),
                Arrays.asList("ID", "--", ";"), Arrays.asList("id","<opt>", "=", "<expression>", ";"));

        new Grammar("<opt>", singletonList("+"), singletonList("-"), singletonList("/"), singletonList("*"));


        List<String> varTypes = new ArrayList<>(Tools.getVarTypes());
        List<String>[] varTypeLists = new List[varTypes.size()];

        for (int i = 0; i < varTypeLists.length; i++) {
            varTypeLists[i] = singletonList(varTypes.get(i));
        }
        new Grammar("<var_type>", varTypeLists);
    }

    static void printGrammar(){
        System.out.println("\t\t"+ "GRAMMAR" );
        Set<String> keys = allGrammars.keySet();
        for (String key : keys) {
            System.out.print(key + " : ");
            List<String>[] structure = allGrammars.get(key);
            for (List<String> list: structure){
                System.out.print(list + " , ");
            }
            System.out.println();
        }
        System.out.println("\t\t"+ "---------" );
    }
}

class LL1Table {
    static HashMap<String, Map<String, List<String>[]>> table = new HashMap<>();

    static void createTable() {
        HashMap<String, List<String>[]> allGrammars = Grammar.allGrammars;
        table.put("<program>", Map.of("int main", allGrammars.get("<program>")));
        table.put("<if statement>", Map.of("if", allGrammars.get("<if statement>")));
        table.put("<for statement>", Map.of("for", allGrammars.get("<for statement>")));

        //"<condition>"
        List<String>[] conditionStructure = allGrammars.get("<condition>");
        Map<String, List<String>[]> tRow = new HashMap<>();
        tRow.put("(", conditionStructure);
        for (String id : Phase2.identifiers) {
            tRow.put(id , conditionStructure);
        }
        for (String number: Phase2.numbers){
            tRow.put(number , conditionStructure);
        }
        for (String literal : Phase2.stringLiterals) {
            tRow.put(literal , conditionStructure);
        }
        table.put("<condition>", tRow);
        tRow = new HashMap<>();
        //....................


        //<relational operator>
        List<String>[] relationOperators = allGrammars.get("<relational operator>");
        for (List<String> list : relationOperators) {
            List[] lists = new List[1];
            lists[0] = singletonList(list.get(0));
            tRow.put(list.get(0), lists);
        }
        table.put("<relational operator>", tRow);
        tRow = new HashMap<>();

        //<var declration>
        List<String>[] varTypeStructure = allGrammars.get("<var declaration>");
        for (String varType : Tools.getVarTypes()) {
            tRow.put(varType, varTypeStructure);
        }
        table.put("<var declaration>", tRow);
        tRow = new HashMap<>();
        //....................

        //<expression>
        List<String>[] expStructure = allGrammars.get("<expression>");
        tRow.put("(", expStructure);
        for (String id : Phase2.identifiers) {
            tRow.put(id, expStructure);
        }
        for (String num : Phase2.numbers) {
            tRow.put(num, expStructure);
        }
        for (String str : Phase2.stringLiterals) {
            tRow.put(str, expStructure);
        }
        table.put("<expression>", tRow);
        tRow = new HashMap<>();
        //...................

        //<expression'>
        List<String>[] expPrimStructure = allGrammars.get("<expression'>");
        List<String>[] epsilon = new List[1];
        epsilon[0] = Arrays.asList(Grammar.EPSILON);
        String arr[] = {">=", "<=", ">", "<", "==", ";", ")"};
        for(String op : arr) {
            tRow.put(op, epsilon);
        }
        String arr2[] = {"+", "-"};
        for(String op1 : arr2) {
            if (op1.equals("+")) {

            } else {

            }
        }
        table.put("<expression'>", tRow);
        tRow = new HashMap<>();
        //.................

        //<term>
        List<String>[] termStructure = allGrammars.get("<term>");
        tRow.put("(", termStructure);
        for (String id : Phase2.identifiers) {
            tRow.put(id, termStructure);
        }
        for (String num : Phase2.numbers) {
            tRow.put(num, termStructure);
        }
        for (String str : Phase2.stringLiterals) {
            tRow.put(str, termStructure);
        }
        tRow = new HashMap<>();
        table.put("<term>", tRow);
        //....................

        //<term'>
        List<String>[] termPrimStructure = allGrammars.get("<term'>");
        String arr3[] = {">=", "<=", ">", "<", "==", "+", "-", "("};
        for(String op : arr3) {
            tRow.put(op, epsilon);
        }
        String arr4[] = {"*", "/"};
        for(String op1 : arr4) {
            if (op1.equals("*")) {

            } else {

            }
        }
        table.put("<term'>", tRow);
        tRow = new HashMap<>();
        //..................

        //<factor>
        List<String>[] factorStructure = allGrammars.get("<factor>");
        tRow.put("(", factorStructure);
        for (String id : Phase2.identifiers) {
            tRow.put(id, factorStructure);
        }
        for (String num : Phase2.numbers) {
            tRow.put(num, factorStructure);
        }
        for (String str : Phase2.stringLiterals) {
            tRow.put(str, factorStructure);
        }
        table.put("<factor>", tRow);
        tRow = new HashMap<>();
        //................

        //<statements>
        List<String>[] stateStructure = allGrammars.get("<statements>");
        for (String varType : Tools.getVarTypes()) {
            tRow.put(varType, stateStructure);
        }
        table.put("<statements>", tRow);
        for (String id : Phase2.identifiers) {
            tRow.put(id, stateStructure);
        }



    }

    static void printTable() {
        ArrayList<String> terminals = new ArrayList<>(Grammar.allGrammars.keySet());
        for (String terminal : terminals) {
            Map<String, List<String>[]> map = table.get(terminal);
            if (map == null)
                continue;
            System.out.println(terminal + "  : ");
            for (String key :
                    map.keySet()) {
                List<String>[] lists = map.get(key);
                System.out.print(key + " : ");
                for (List<String> list : lists) {
                    System.out.println(list);
                }

            }
            System.out.println();
        }
    }

    static List<String>[] getStructure(String terminal, String row) {
        return table.get(terminal).get(row);
    }

}