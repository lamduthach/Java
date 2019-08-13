import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Lam Du Thach
 */
public class StringJoinerIntroduce {

    // StringJoiner is a new class added in Java 8 under java.util package.
    // Simply put, it can be used for joining Strings making use of a delimiter, prefix, and suffix.
    String simplyHandleStringJoiner(String[] str) {
        String result = "";
        for (String string : str) {
            // add String delimiter at the end
            result += string + ",";
        }
        // then remove last delimiter
        result = result.substring(0, result.length() - 1);
        // if we want string have PREFIX, SUFFIX
        // result = "{" + result.substring(0, result.length() - 1) + "}";
        return result;
    }

    // StringUtils.join is using for loop with StringBuilder 
    String stringUtilsWithoutCompute(String[] str) {
        return StringUtils.join(str, ",");
    }

    enum Color {
        Red, Green, Blue
    }

    class ClassDTO {

        int id;
        Color color;

        public ClassDTO(Color color) {
            // id of enum is ordinal()
            this.id = color.ordinal();
            this.color = color;
        }

    }

    // but if there is some thing need to compute
    // get String id joiner
    String stringUtilsWithCompute(String[] str) {
        // prepare new List
        List<String> strIdList = new ArrayList<>();
        for (String string : str) {
            ClassDTO classDTO = new ClassDTO(Color.valueOf(string));
            strIdList.add(classDTO.id + "");
        }

        // call StringUtils.join
        return StringUtils.join(strIdList, ",");
    }

    // or we could use StringJoiner - this introduce in Java8
    // StringJoiner extend StringBuilder
    String stringJoinerWithoutCompute(String[] str) {
        StringJoiner joiner = new StringJoiner(",");
        for (String string : str) {
            joiner.add(string);
        }
        return joiner.toString();
    }

    // with StringJoiner we dont need to define any List or Collection
    String stringJoinerWithCompute(String[] str) {
        StringJoiner joiner = new StringJoiner(",");
        for (String string : str) {
            ClassDTO classDTO = new ClassDTO(Color.valueOf(string));
            joiner.add(classDTO.id + "");
        }
        return joiner.toString();
    }

    public static void main(String[] args) {
        // We will see that StringJoiner faster than other
        StringJoinerIntroduce t = new StringJoinerIntroduce();
        String[] input = {"Red", "Blue", "Green"};
        int repeatTime = 1000000;

        System.out.println("-----------------------------");
        System.out.println("simplyHandleStringJoiner");
        long startTime = System.currentTimeMillis();
        t.testFunction(t::simplyHandleStringJoiner, input, repeatTime);
        long endTime = System.currentTimeMillis();
        System.out.println("Time : " + (endTime - startTime));

        System.out.println("-----------------------------");
        System.out.println("stringUtilsWithoutCompute");
        startTime = System.currentTimeMillis();
        t.testFunction(t::stringUtilsWithoutCompute, input, repeatTime);
        endTime = System.currentTimeMillis();
        System.out.println("Time : " + (endTime - startTime));

        System.out.println("-----------------------------");
        System.out.println("stringJoinerWithoutCompute");
        startTime = System.currentTimeMillis();
        t.testFunction(t::stringJoinerWithoutCompute, input, repeatTime);
        endTime = System.currentTimeMillis();
        System.out.println("Time : " + (endTime - startTime));

        System.out.println("-----------------------------");
        System.out.println("stringUtilsWithCompute");
        startTime = System.currentTimeMillis();
        t.testFunction(t::stringUtilsWithCompute, input, repeatTime);
        endTime = System.currentTimeMillis();
        System.out.println("Time : " + (endTime - startTime));

        System.out.println("-----------------------------");
        System.out.println("stringJoinerWithCompute");
        startTime = System.currentTimeMillis();
        t.testFunction(t::stringJoinerWithCompute, input, repeatTime);
        endTime = System.currentTimeMillis();
        System.out.println("Time : " + (endTime - startTime));
    }

    void testFunction(Function<String[], String> func, String[] input, int repeatTime) {
        for (int i = 0; i < repeatTime; i++) {
            func.apply(input);
        }
    }

    // More over with StringJoiner we could easy add PREFIX, SUFFIX
    String stringJoinerWithPREFIXAndSUFFIX(String[] str) {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        for (String string : str) {
            joiner.add(string);
        }
        return joiner.toString(); // output : {Red,Green,Blue}
    }

    // We could merge 2 joiner with different delimiter
    String stringJoinerWithMerge(String[] str1, String[] str2) {
        StringJoiner joiner1 = new StringJoiner(",");
        for (String string : str1) {
            joiner1.add(string);
        }

        StringJoiner joiner2 = new StringJoiner("-");
        for (String string : str2) {
            joiner2.add(string);
        }

        joiner1.merge(joiner2);
        return joiner1.toString(); // output : {Red,Green,Blue,White-Yellow-Black}
    }
}
