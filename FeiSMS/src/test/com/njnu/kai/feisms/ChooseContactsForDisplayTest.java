package test.com.njnu.kai.feisms;

import java.util.ArrayList;

import com.njnu.kai.feisms.ChooseContactsForDisplay;
import com.njnu.kai.feisms.HanziToPinyin;
import com.njnu.kai.feisms.HanziToPinyin.Token;

import junit.framework.TestCase;

public class ChooseContactsForDisplayTest extends TestCase {

	private HanziToPinyin mHanziToPinyin = HanziToPinyin.getInstance();

	public ChooseContactsForDisplayTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
//		System.out.println("setUp");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private ChooseContactsForDisplay createDisplayContacts(long id, String name, String phone) {
		ChooseContactsForDisplay cfd = new ChooseContactsForDisplay();
		cfd.mContactsId = id;
		cfd.mName = name;
		cfd.mPhone = phone;
		cfd.mPinyin = mHanziToPinyin.getPinyin(name);
		return cfd;
	}

	public final void testToString() {
		ChooseContactsForDisplay c1 = createDisplayContacts(1, "测试匹配", "13300000000");
		assertEquals("测试匹配: 13300000000", c1.toString());

		ChooseContactsForDisplay c2 = createDisplayContacts(1, "天上人", "13300000000");
		assertEquals("天上人: 13300000000", c2.toString());

		ChooseContactsForDisplay c3 = createDisplayContacts(1, "天上", "13300000000");
		assertEquals("天上\u3000: 13300000000", c3.toString());

		ChooseContactsForDisplay c4 = createDisplayContacts(1, "天", "13300000000");
		assertEquals("天\u3000\u3000: 13300000000", c4.toString());
	}

	public final void testIsAccordWith_Simple() {
		ChooseContactsForDisplay c1 = createDisplayContacts(1, "测试匹配", "13300000000");
		HanziToPinyin.Pinyin py = new HanziToPinyin.Pinyin(4);
		c1.mPinyin = py;
		py.mPlaceHolder[0] = 1;
		py.mPlaceHolder[1] = 1;
		py.mPlaceHolder[2] = 1;
		py.mPlaceHolder[3] = 1;
		py.mT9Key[0] = new char[] {'1', '2', '3'};
		py.mT9Key[1] = new char[] {'2', '2', '3'};
		py.mT9Key[2] = new char[] {'3', '2', '3'};
		py.mT9Key[3] = new char[] {'4', '2', '3'};
		assertTrue(c1.isAccordWith("1"));
		assertTrue(c1.isAccordWith("2"));
		assertTrue(c1.isAccordWith("3"));
		assertTrue(c1.isAccordWith("4"));
		assertTrue(c1.isAccordWith("123"));
		assertTrue(c1.isAccordWith("223"));
		assertTrue(c1.isAccordWith("323"));
		assertTrue(c1.isAccordWith("423"));
		assertTrue(c1.isAccordWith("234"));
		assertFalse(c1.isAccordWith("233"));
		assertFalse(c1.isAccordWith("789"));
		assertTrue(c1.isAccordWith("232"));
	}

	public final void testIsAccordWith_Normal() {
		ChooseContactsForDisplay c1 = createDisplayContacts(1, "测试匹配", "13300000000");
		HanziToPinyin.Pinyin py = new HanziToPinyin.Pinyin(4);
		c1.mPinyin = py;
		py.mPlaceHolder[0] = 1;
		py.mPlaceHolder[1] = 1;
		py.mPlaceHolder[2] = 1;
		py.mPlaceHolder[3] = 1;
		py.mT9Key[0] = new char[] {'1', '2', '3'};
		py.mT9Key[1] = new char[] {'2', '2', '3'};
		py.mT9Key[2] = new char[] {'3', '2', '3'};
		py.mT9Key[3] = new char[] {'4', '2', '3'};
		assertTrue(c1.isAccordWith("123"));
		assertTrue(c1.isAccordWith("1234"));
		assertTrue(c1.isAccordWith("223"));
		assertTrue(c1.isAccordWith("2233"));
		assertTrue(c1.isAccordWith("223323"));
		assertTrue(c1.isAccordWith("234"));
		assertTrue(c1.isAccordWith("2342"));
		assertTrue(c1.isAccordWith("23423"));
		assertFalse(c1.isAccordWith("233"));
		assertTrue(c1.isAccordWith("232"));
		assertFalse(c1.isAccordWith("124"));
	}

	public final void testIsAccordWith_HaveNullKey() {
		ChooseContactsForDisplay c1 = createDisplayContacts(1, "测试匹_配", "13300000000");
		HanziToPinyin.Pinyin py = new HanziToPinyin.Pinyin(5);
		c1.mPinyin = py;
		py.mPlaceHolder[0] = 1;
		py.mPlaceHolder[1] = 1;
		py.mPlaceHolder[2] = 1;
		py.mPlaceHolder[3] = 1;
		py.mPlaceHolder[4] = 1;
		py.mT9Key[0] = new char[] {'1', '2', '3'};
		py.mT9Key[1] = new char[] {'2', '2', '3'};
		py.mT9Key[2] = new char[] {'3', '2', '3'};
		py.mT9Key[3] = null;
		py.mT9Key[4] = new char[] {'4', '2', '3'};
		assertTrue(c1.isAccordWith("123"));
		assertTrue(c1.isAccordWith("1234"));   //多次符合条件后又不符合再又符合
		assertTrue(c1.isAccordWith("223"));
		assertTrue(c1.isAccordWith("2233"));
		assertTrue(c1.isAccordWith("223323"));
		assertTrue(c1.isAccordWith("234"));
		assertTrue(c1.isAccordWith("2342"));
		assertTrue(c1.isAccordWith("23423"));
		assertFalse(c1.isAccordWith("233"));
		assertTrue(c1.isAccordWith("232"));
		assertFalse(c1.isAccordWith("124"));
	}

	public final void testIsAccordWith_All_Englist() {
		ChooseContactsForDisplay c1 = createDisplayContacts(1, "The day you went away", "13300000000");
		//843 329 968 9368 2929
		assertTrue(c1.isAccordWith("3992"));
		assertTrue(c1.isAccordWith("292"));
		assertTrue(c1.isAccordWith("9936"));
		assertTrue(c1.isAccordWith("936829"));
		assertTrue(c1.isAccordWith("8329993"));
		assertTrue(c1.isAccordWith("396892929"));
		assertTrue(c1.isAccordWith("396892"));
		assertTrue(c1.isAccordWith("3968929"));
		assertFalse(c1.isAccordWith("433"));
		assertFalse(c1.isAccordWith("299"));
		assertFalse(c1.isAccordWith("6893"));
	}

	public final void testIsAccordWith_Have_Englist() {
		ChooseContactsForDisplay c1 = createDisplayContacts(1, "中文  English 那啥", "13300000000");
		//94264 936 x 3645474 x 62 742
		//  1    1  2     7   1  1   1
		HanziToPinyin.Pinyin py = new HanziToPinyin.Pinyin(7);
		c1.mPinyin = py;
		py.mPlaceHolder[0] = 1;
		py.mPlaceHolder[1] = 1;
		py.mPlaceHolder[2] = 2;
		py.mPlaceHolder[3] = 7;
		py.mPlaceHolder[4] = 1;
		py.mPlaceHolder[5] = 1;
		py.mPlaceHolder[6] = 1;
		py.mT9Key[0] = new char[] {'9', '4', '2', '6', '4'};
		py.mT9Key[1] = new char[] {'9', '3', '6'};
		py.mT9Key[2] = null;
		py.mT9Key[3] = new char[] {'3', '6', '4', '5', '4', '7', '4'};
		py.mT9Key[4] = null;
		py.mT9Key[5] = new char[] {'6', '2'};
		py.mT9Key[6] = new char[] {'7', '4', '2'};

		assertTrue(c1.isAccordWith("9426493627"));   //多次符合条件后又不符合再又符合
		assertTrue(c1.isAccordWith("94264936274"));   //多次符合条件后又不符合再又符合
		assertTrue(c1.isAccordWith("942649362742"));   //多次符合条件后又不符合再又符合
		assertTrue(c1.isAccordWith("9362"));   //多次符合条件后又不符合再又符合
		assertTrue(c1.isAccordWith("93636"));
		assertTrue(c1.isAccordWith("627"));
		assertTrue(c1.isAccordWith("9367"));   //多次符合条件后又不符合再又符合
		assertTrue(c1.isAccordWith("367"));
		assertFalse(c1.isAccordWith("94936"));
		assertFalse(c1.isAccordWith("3636"));
		assertFalse(c1.isAccordWith("6272"));
		assertFalse(c1.isAccordWith("9426493627422"));
	}

//	public final void testHanziToPinyin_ZHONG() {
//		ArrayList<Token> arrToken = mHanziToPinyin.getOri("中");
//		assertEquals(1, arrToken.size());
//		assertEquals("ZHONG", arrToken.get(0).target); //JIN
//	}
//	
//	public final void testHanziToPinyin() {
//		ArrayList<Token> arrToken = mHanziToPinyin.get("中文  English 那啥");
//		assertEquals(7, arrToken.size());
////		assertEquals("ZHONG", arrToken.get(0).target); //JIN
////		assertEquals("WEN", arrToken.get(1).target);	//JIN
//		assertEquals("  ", arrToken.get(2).target);
////		assertEquals("ENGLISH", arrToken.get(3).target);
//		assertEquals(" ", arrToken.get(4).target);
////		assertEquals("NA", arrToken.get(5).target);
////		assertEquals("SHA", arrToken.get(6).target);
//	}
}
