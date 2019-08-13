
import java.util.Optional;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 *
 * @author Lam Du Thach
 */
public class OptionalIntroduce {

    // this file is just introduce one api in java8 vs Optional
    // Optional is just api to avoid style code check null and function style, its not "silver bullet"
    // Optional have static api that java8 already implement
    // Doc about Optional -> https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
    // Some good way and bad way to use Optional -> https://dzone.com/articles/using-optional-correctly-is-not-optional
    BusinessClass businessClass;

    private synchronized BusinessClass getBusinessClass() {
        if (businessClass == null) {
            businessClass = new BusinessClass();
        }
        return businessClass;
    }

    private synchronized BusinessClass getBusinessClassInstance() {
        return Optional.<BusinessClass>ofNullable(businessClass).orElseGet(BusinessClass::new);
    }

    class BusinessClass {

        public String getClassName(boolean isNull) {
            return (isNull) ? null : this.getClass().getName();
        }
    }

    public static void main(String[] args) {
        OptionalIntroduce ot = new OptionalIntroduce();
        ot.testBasic();
        ot.testObject();
    }

    public void testBasic() {
        // In this code block introduce some api of Optional
        System.out.println("-----------------------------------------");
        System.out.println("testBasic");
        OptionalIntroduce ot = new OptionalIntroduce();
        // str1 is null one
        String str1 = ot.getBusinessClassInstance().getClassName(true);
        String str2 = ot.getBusinessClassInstance().getClassName(false);
        // if null Optional will return Optional.empty and we could check it with isPresent api
        System.out.println("ofNullable on Non-Empty Optional: " + Optional.ofNullable(str1)); // Optional.empty
        System.out.println("ofNullable on Empty Optional: " + Optional.ofNullable(str2)); // Optional[com.faber.mieruca.testapi.OptionalTest$BusinessClass]

        try {
            // if using .of it will throw NullPointerException immediately
            Optional.<String>of(str1);
        } catch (NullPointerException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }

        // .orElse is api that we could set default value return
        str1 = Optional.<String>ofNullable(str1).orElse("thach dap chai");
        System.out.println("str1 ofNullable orElse 1 : " + str1);

        // when str1 is set value or have value Optional will return that value immediately
        str1 = Optional.<String>ofNullable(str1).orElse("thach is here");
        System.out.println("str1 ofNullable orElse 2 : " + str1);

        /// if the object isPresent the code in ifPresent will continue
        Optional.<String>ofNullable(str1).ifPresent((str) -> {
            System.out.println("ifPresent : " + str);
        });

        Optional<String> nonEmptyGender = Optional.of("male");
        Optional<String> emptyGender = Optional.empty();

        // Option, map and flatmap api with The Double Colon Operator
        // you could see The Double Colon Operator here -> https://www.baeldung.com/java-8-double-colon-operator
        System.out.println("Non-Empty Optional:: " + nonEmptyGender.map(String::toLowerCase)); // Optional[male]
        System.out.println("Empty Optional    :: " + emptyGender.map(String::toUpperCase)); // Optional.empty

        Optional<Optional<String>> nonEmptyOtionalGender = Optional.of(Optional.of("male"));
        System.out.println("Optional value   :: " + nonEmptyOtionalGender); // Optional[Optional[male]]
        System.out.println("Optional.map     :: " + nonEmptyOtionalGender.map(gender -> gender.map(String::toUpperCase))); // Optional[Optional[MALE]]
        System.out.println("Optional.flatMap :: " + nonEmptyOtionalGender.flatMap(gender -> gender.map(String::toUpperCase))); // Optional[MALE]
        System.out.println("Optional.flatMap value :: " + nonEmptyOtionalGender.flatMap(gender -> gender.map(String::toUpperCase)).get()); // MALE

        String notMale = "not male";
        System.out.println(nonEmptyGender.orElse(notMale)); // male
        System.out.println(emptyGender.orElse(notMale)); // not male
        System.out.println(nonEmptyGender.orElseGet(() -> notMale)); // male
        System.out.println(emptyGender.orElseGet(() -> notMale)); // not male
    }

    public void testObject() {
        System.out.println("-----------------------------------------");
        // this block code is introduce some idea using Optional with DTO
        // this is just idea its not "silver bullet"
        // in case using deeper deeper null check code style like in getAnameWithoutOptional1(), getAnameWithoutOptional2()
        // we code use like getAnameWithOptional(), but we must implement Optional in DTO
        System.out.println("testObject");
        ClassA classA = new ClassA(1, "A");
        ClassA classNullA = null;

        ClassB classB = new ClassB(2, "B", classA);
        ClassC classC = new ClassC(3, "C", classB);
        String name = getAnameWithoutOptional1(classC);
        System.out.println("name : " + name); // A

        ClassB classNullB = new ClassB(2, "B", classNullA);
        ClassC classNullC = new ClassC(3, "C", classNullB);

        String nameNull = getAnameWithoutOptional1(classNullC);
        System.out.println("nameNull : " + nameNull); // nope

        ClassB classOpB = new ClassB(2, "B", Optional.of(classA));
        ClassC classOpC = new ClassC(3, "C", Optional.of(classOpB));
        String nameOp = getAnameWithOptional(Optional.ofNullable(classOpC));
        System.out.println("nameOp : " + nameOp); // A

        ClassB classOpNullB = new ClassB(2, "B", Optional.ofNullable(classNullA));
        ClassC classOpNullC = new ClassC(3, "C", Optional.ofNullable(classOpNullB));
        String nameNullOp = getAnameWithOptional(Optional.ofNullable(classOpNullC));
        System.out.println("nameNullOp : " + nameNullOp); // nope

        System.out.println("test Optional empty");
        String nameOpEmpty = getAnameWithOptional(Optional.empty());
        System.out.println("nameOpEmpty : " + nameOpEmpty); // nope
    }

    public String getAnameWithoutOptional1(ClassC classC) {
        if (classC != null) {
            ClassB classB = classC.getClassB();
            if (classB != null) {
                ClassA classA = classB.getClassA();
                if (classA != null) {
                    return classA.getName();
                }
            }
        }
        return "nope";
    }

    public String getAnameWithoutOptional2(ClassC classC) {
        if (classC == null) {
            return "nope";
        }

        ClassB classB = classC.getClassB();
        if (classB == null) {
            return "nope";
        }

        ClassA classA = classB.getClassA();
        if (classA == null) {
            return "nope";
        }
        return classA.getName();
    }

    public String getAnameWithOptional(Optional<ClassC> opClassC) {
        return opClassC.flatMap(ClassC::getOpClassB).flatMap(ClassB::getOpClassA).map(ClassA::getName).orElse("nope");
    }

    public String getBnameWithOptional(Optional<ClassC> opClassC) {
        return opClassC.flatMap(ClassC::getOpClassB).map(ClassB::getName).orElse("nope");
    }

    public class ClassA {

        private Integer id;
        private String name;

        public ClassA(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class ClassB {

        private Integer id;
        private String name;
        private ClassA classA;
        private Optional<ClassA> opClassA;

        public ClassB(Integer id, String name, ClassA classA) {
            this.id = id;
            this.name = name;
            this.classA = classA;
        }

        public ClassB(Integer id, String name, Optional<ClassA> opClassA) {
            this.id = id;
            this.name = name;
            this.opClassA = opClassA;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ClassA getClassA() {
            return classA;
        }

        public void setClassA(ClassA classA) {
            this.classA = classA;
        }

        public Optional<ClassA> getOpClassA() {
            return opClassA;
        }

        public void setOpClassA(Optional<ClassA> opClassA) {
            this.opClassA = opClassA;
        }
    }

    public class ClassC {

        private Integer id;
        private String name;
        private ClassB classB;
        private Optional<ClassB> opClassB;

        public ClassC(Integer id, String name, ClassB classB) {
            this.id = id;
            this.name = name;
            this.classB = classB;
        }

        public ClassC(Integer id, String name, Optional<ClassB> opClassB) {
            this.id = id;
            this.name = name;
            this.opClassB = opClassB;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ClassB getClassB() {
            return classB;
        }

        public void setClassB(ClassB classB) {
            this.classB = classB;
        }

        public Optional<ClassB> getOpClassB() {
            return opClassB;
        }

        public void setOpClassB(Optional<ClassB> opClassB) {
            this.opClassB = opClassB;
        }
    }
}
