import kai.NtpUtil;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 13-12-2
 */
public class JavaTest {

    public static void main(String[] args) {
        String testStr = "abcdabcdabcd";
        testStr = testStr.replace('c', 'x');
        System.out.println(testStr);

//        System.out.println(String.class.isAssignableFrom(CharSequence.class));
        System.out.println(String.class.isAssignableFrom(Object.class));
        System.out.println(Object.class.isAssignableFrom(String.class));
//        System.out.println(String.class.isAssignableFrom(JavaTest.class));

        System.out.println(Double.class.isAssignableFrom(Number.class));
        System.out.println(Number.class.isAssignableFrom(Double.class));

        System.out.println(Double.class.isAssignableFrom(Double.class));
//        System.out.println(Double.class.isAssignableFrom(JavaTest.class));

        System.out.print(NtpUtil.localClockOffset());
    }
}
