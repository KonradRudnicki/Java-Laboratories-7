

import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document implements IWithName {
    private static final int MODVALUE = 100000000;
    public String name;
    public TwoWayCycledOrderedListWithSentinel<Link> link;

    public Document(String name) {
        this.name = name;
    }

    public Document(String name, Scanner scan) {
        this.name = name;
        link = new TwoWayCycledOrderedListWithSentinel<>();
        load(scan);
    }

    public void load(Scanner scan) {
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.equals("eod")) return;

            String[] oneLine = line.split(" ");
            String regex = "link=(.+)";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher;

            for (String string : oneLine) {
                matcher = pattern.matcher(string);

                if (matcher.matches())
                    if (isCorrectLink(matcher.group(1))) {
                        link.add(new Link(matcher.group(1).toLowerCase()));
                    } else if (checkWithWeight(matcher.group(1))) {
                        link.add(createLink(matcher.group(1).toLowerCase()));
                    }
            }
        }
    }

    public static boolean checkWithWeight(String id) {
        String regex = "[a-zA-Z][a-zA-Z_0-9]*(.*)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(id);

        if (matcher.matches()) {
            String weightReg = "(\\({1}([1-9]{1}[0-9]*)\\){1})";
            Pattern weightPat = Pattern.compile(weightReg);
            Matcher weightMatch = weightPat.matcher(matcher.group(1));

            return weightMatch.matches();
        }

        return false;
    }

    public static boolean isCorrectLink(String id) {
        String regex = "[a-zA-Z][a-zA-Z_0-9]*";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(id);

        return matcher.matches();
    }

    static Link createLink(String link) {
        String regex = "([a-zA-Z][a-zA-Z_0-9]*)\\((.*)\\)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);

        if (matcher.matches()) {
            return new Link(matcher.group(1), Integer.parseInt(matcher.group(2)));
        }

        regex = "([a-zA-Z][a-zA-Z_0-9]*)";
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(link);

        if (matcher.matches()) {
            return new Link(matcher.group(1));
        }

        return null;
    }

    public static boolean isCorrectId(String id) {
        String regex = "[a-z]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);

        return matcher.matches();
    }


    @Override
    public String toString() {
        return "Document: " + name + link.toString().toLowerCase();
    }

    public String toStringReverse() {
        return "Document: " + name + link.toStringReverse().toLowerCase();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Document document = (Document) o;

        return Objects.equals(name, document.name);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        char[] characters = this.name.toCharArray();
        int iteration = 0;

        for (char character : characters)
            switch (iteration) {
                case 0: {
                    hashCode = character;
                    iteration++;
                    break;
                }
                case 1: {
                    hashCode = (hashCode * 7 + character) % MODVALUE;
                    iteration++;
                    break;
                }
                case 2: {
                    hashCode = (hashCode * 11 + character) % MODVALUE;
                    iteration++;
                    break;
                }
                case 3: {
                    hashCode = (hashCode * 13 + character) % MODVALUE;
                    iteration++;
                    break;
                }
                case 4: {
                    hashCode = (hashCode * 17 + character) % MODVALUE;
                    iteration++;
                    break;
                }
                case 5: {
                    hashCode = (hashCode * 19 + character) % MODVALUE;
                    iteration = 1;
                    break;
                }
            }

        return hashCode;
    }
}
