public class Main {
    public static final char NOCHARACTER = (char) 0;
    public static void main(String args[]) {
        MarkovModel newModel = new MarkovModel(3, 100);
        newModel.initializeText("abaadadsasdasnbgdashjkgdkasjhdasjkhdkasjhksdaj asdaslskdja sgfkjfsdlkfsdlj qiuoqeuwi aslodjalkdj bvkhbvcjkb iouwqeopiqeuwpqw.");
        String kgram = "aba";
        // System.out.println(newModel.nextCharacter(kgram));
        String fullString = kgram;
        for(int i = 0; i < 15; i++) {
            // System.out.println(newModel.nextCharacter(kgram));
            if (newModel.nextCharacter(kgram) == NOCHARACTER)
                break;

            fullString += newModel.nextCharacter(kgram);
            kgram = kgram.substring(1) + newModel.nextCharacter(kgram);
        }
        System.out.println(fullString);
    }
}
