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
		assertNotNull(c1.getAccordInfo("1"));
		assertNotNull(c1.getAccordInfo("2"));
		assertNotNull(c1.getAccordInfo("3"));
		assertNotNull(c1.getAccordInfo("4"));
		assertNotNull(c1.getAccordInfo("123"));
		assertNotNull(c1.getAccordInfo("223"));
		assertNotNull(c1.getAccordInfo("323"));
		assertNotNull(c1.getAccordInfo("423"));
		assertNotNull(c1.getAccordInfo("234"));
		assertNull(c1.getAccordInfo("233"));
		assertNull(c1.getAccordInfo("789"));
		assertNotNull(c1.getAccordInfo("232"));
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
		assertNotNull(c1.getAccordInfo("123"));
		assertNotNull(c1.getAccordInfo("1234"));
		assertNotNull(c1.getAccordInfo("223"));
		assertNotNull(c1.getAccordInfo("2233"));
		assertNotNull(c1.getAccordInfo("223323"));
		assertNotNull(c1.getAccordInfo("234"));
		assertNotNull(c1.getAccordInfo("2342"));
		assertNotNull(c1.getAccordInfo("23423"));
		assertNull(c1.getAccordInfo("233"));
		assertNotNull(c1.getAccordInfo("232"));
		assertNull(c1.getAccordInfo("124"));
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
		assertNotNull(c1.getAccordInfo("123"));
		assertNotNull(c1.getAccordInfo("1234"));   //多次符合条件后又不符合再又符合
		assertNotNull(c1.getAccordInfo("223"));
		assertNotNull(c1.getAccordInfo("2233"));
		assertNotNull(c1.getAccordInfo("223323"));
		assertNotNull(c1.getAccordInfo("234"));
		assertNotNull(c1.getAccordInfo("2342"));
		assertNotNull(c1.getAccordInfo("23423"));
		assertNull(c1.getAccordInfo("233"));
		assertNotNull(c1.getAccordInfo("232"));
		assertNull(c1.getAccordInfo("124"));
	}

	public final void testIsAccordWith_All_Englist() {
		ChooseContactsForDisplay c1 = createDisplayContacts(1, "The day you went away", "13300000000");
		//843 329 968 9368 2929
		assertNotNull(c1.getAccordInfo("3992"));
		assertNotNull(c1.getAccordInfo("292"));
		assertNotNull(c1.getAccordInfo("9936"));
		assertNotNull(c1.getAccordInfo("936829"));
		assertNotNull(c1.getAccordInfo("8329993"));
		assertNotNull(c1.getAccordInfo("396892929"));
		assertNotNull(c1.getAccordInfo("396892"));
		assertNotNull(c1.getAccordInfo("3968929"));
		assertNull(c1.getAccordInfo("433"));
		assertNull(c1.getAccordInfo("299"));
		assertNull(c1.getAccordInfo("6893"));
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

		assertNotNull(c1.getAccordInfo("9426493627"));   //多次符合条件后又不符合再又符合
		assertNotNull(c1.getAccordInfo("94264936274"));   //多次符合条件后又不符合再又符合
		assertNotNull(c1.getAccordInfo("942649362742"));   //多次符合条件后又不符合再又符合
		assertNotNull(c1.getAccordInfo("9362"));   //多次符合条件后又不符合再又符合
		assertNotNull(c1.getAccordInfo("93636"));
		assertNotNull(c1.getAccordInfo("627"));
		assertNotNull(c1.getAccordInfo("9367"));   //多次符合条件后又不符合再又符合
		assertNotNull(c1.getAccordInfo("367"));
		assertNull(c1.getAccordInfo("94936"));
		assertNull(c1.getAccordInfo("3636"));
		assertNull(c1.getAccordInfo("6272"));
		assertNull(c1.getAccordInfo("9426493627422"));
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
