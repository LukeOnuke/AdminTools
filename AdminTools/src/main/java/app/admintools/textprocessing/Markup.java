package app.admintools.textprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author lukak
 */
public class Markup {

    public static String generateMarkupParagraph(String text) {
        ArrayList<String> strongArr = new ArrayList<>();
        ArrayList<String> splitText = new ArrayList<>(Arrays.asList(text.split("§")));
        String lastColor = ChatColour.GRAY;
        if (splitText.size() == 1) {
            strongArr.add(getParagraphElement(splitText.get(0), ChatColour.GRAY, "normal", "none"));
        } else {
            //For every individualSplitText in splitText
            for (String inSplitText : splitText) {
                if (inSplitText.startsWith("0")) {
                    //black
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.BLACK, "normal", "none"));
                    lastColor = ChatColour.BLACK;
                } else if (inSplitText.startsWith("1")) {
                    //dark blue
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.DARK_BLUE, "normal", "none"));
                    lastColor = ChatColour.DARK_BLUE;
                } else if (inSplitText.startsWith("2")) {
                    //dark green
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.DARK_GREEN, "normal", "none"));
                    lastColor = ChatColour.DARK_GREEN;
                } else if (inSplitText.startsWith("3")) {
                    //Dark Aqua
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.DARK_AQUA, "normal", "none"));
                    lastColor = ChatColour.DARK_AQUA;
                } else if (inSplitText.startsWith("4")) {
                    //Dark red
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.DARK_RED, "normal", "none"));
                    lastColor = ChatColour.DARK_RED;
                } else if (inSplitText.startsWith("5")) {
                    //Dark purple
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.DARK_PURPLE, "normal", "none"));
                    lastColor = ChatColour.DARK_PURPLE;
                } else if (inSplitText.startsWith("6")) {
                    //Gold
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.GOLD, "normal", "none"));
                    lastColor = ChatColour.GOLD;
                } else if (inSplitText.startsWith("7")) {
                    //Gray
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.GRAY, "normal", "none"));
                    lastColor = ChatColour.GRAY;
                } else if (inSplitText.startsWith("8")) {
                    //Dark gray
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.DARK_GRAY, "normal", "none"));
                    lastColor = ChatColour.DARK_GRAY;
                } else if (inSplitText.startsWith("9")) {
                    //Blue
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.BLUE, "normal", "none"));
                    lastColor = ChatColour.BLUE;
                } else if (inSplitText.startsWith("a")) {
                    //Green
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.GREEN, "normal", "none"));
                    lastColor = ChatColour.GREEN;
                } else if (inSplitText.startsWith("b")) {
                    //Aqua
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.AQUA, "normal", "none"));
                    lastColor = ChatColour.AQUA;
                } else if (inSplitText.startsWith("c")) {
                    //Red
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.RED, "normal", "none"));
                    lastColor = ChatColour.RED;
                } else if (inSplitText.startsWith("d")) {
                    //Light purple
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.LIGHT_PURPLE, "normal", "none"));
                    lastColor = ChatColour.LIGHT_PURPLE;
                } else if (inSplitText.startsWith("e")) {
                    //Yellow
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.YELLOW, "normal", "none"));
                    lastColor = ChatColour.YELLOW;
                } else if (inSplitText.startsWith("f")) {
                    //White
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.WHITE, "normal", "none"));
                    lastColor = ChatColour.WHITE;
                } else if (inSplitText.startsWith("k")) {
                    //obfuscated
                    int leftLimit = 97; // letter 'a'
                    int rightLimit = 122; // letter 'z'
                    Random random = new Random();

                    String generatedText = random.ints(leftLimit, rightLimit + 1)
                            .limit(inSplitText.length()) //limit the lengh to the lengh of the source string
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();

                    strongArr.add(getParagraphElement(inSplitText, lastColor, "normal", "none"));
                } else if (inSplitText.startsWith("l")) {
                    //bold
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), lastColor, "bold", "none"));
                } else if (inSplitText.startsWith("m")) {
                    //strikethroug
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), lastColor, "normal", "line-through"));
                } else if (inSplitText.startsWith("n")) {
                    //underline
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), lastColor, "normal", "underline"));
                } else if (inSplitText.startsWith("o")) {
                    //italic
                    strongArr.add("<strong style=\"color: " + lastColor + "; font-weight: normal; text-decoration: none; font-style: italic;\">" + text + "</strong>");
                } else if (inSplitText.startsWith("r")) { //Proveriti u slucaju greske
                    //reset
                    lastColor = ChatColour.GRAY;
                    strongArr.add(getParagraphElement(removeFirstChar(inSplitText), ChatColour.GRAY, "normal", "none"));
                } else {
                    lastColor = ChatColour.GRAY;
                    strongArr.add(getParagraphElement(inSplitText, ChatColour.GRAY, "normal", "none"));
                }
            }
        }
        //After the md is done , we can assemble the paragraph
        strongArr.add(text);
        String paragraph = "<p>";
        for (String strong : strongArr) {
            paragraph += strong;
        }
        paragraph += "</p>";
        return paragraph.replace("\n", "</br>");
    }

    private static String removeFirstChar(String s) {
        return s.substring(1);
    }

    private static String getParagraphElement(String text, String color, String fontWeight, String textDecoration) {
        return "<strong style=\"color: " + color + "; font-weight: " + fontWeight + "; text-decoration: " + textDecoration + ";\">" + text + "</strong>";
    }
}
