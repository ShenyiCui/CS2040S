public class Main {
    public static final char NOCHARACTER = (char) 0;
    public static void main(String args[]) {
        MarkovModel newModel = new MarkovModel(3, 100);
        newModel.initializeText("abaadadsasdasnbgdashjkgdkasjhdasjkhdkasjhksdaj asdaslskdja sgfkjfsdlkfsdlj qiuoqeuwi aslodjalkdj bvkhbvcjkb iouwqeopiqeuwpqw.");
        String kgram = "aba";
        // System.out.println(newModel.nextCharacter(kgram));
        String fullString = kgram;
        for(int i = 0; i < 12; i++) {
            // System.out.println(newModel.nextCharacter(kgram));
            Character nextChar = newModel.nextCharacter(kgram);
            if (nextChar == NOCHARACTER)
                break;

            fullString += nextChar;
            kgram = kgram.substring(1) + nextChar;
        }
        System.out.println(fullString);
    }
}
