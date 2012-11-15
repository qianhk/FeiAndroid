package test.com.njnu.kai.feisms;

import java.util.ArrayList;

import com.njnu.kai.feisms.ChooseContactsForDisplay;
import com.njnu.kai.feisms.HanziToPinyin;
import com.njnu.kai.feisms.HanziToPinyin.Pinyin;
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

	public final void testIsMatchesWith_Simple() {
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

		String keyString = py.toString();
		assertEquals("4,123,1,223,1,323,1,423,1", keyString);
//		Pinyin parse = HanziToPinyin.Pinyin.parse(keyString);
//		assertEquals("4,123,1,223,1,323,1,423,1", parse.toString());

		int[] matchesInfo = c1.getMatchesInfo("33");
		assertNotNull(matchesInfo);
		assertEquals(0, matchesInfo[0]);
		assertEquals(1, matchesInfo[1]);
		assertEquals(2, matchesInfo[2]);
		assertNotNull(c1.getMatchesInfo("2"));
		assertNotNull(c1.getMatchesInfo("3"));
		assertNotNull(c1.getMatchesInfo("4"));
		assertNotNull(c1.getMatchesInfo("123"));
		matchesInfo = c1.getMatchesInfo("223");
		assertNotNull(matchesInfo);
		assertEquals(1, matchesInfo[0]);
		assertEquals(1, matchesInfo[1]);
		assertEquals(1, matchesInfo[2]);
		assertNotNull(c1.getMatchesInfo("323"));
		assertNotNull(c1.getMatchesInfo("423"));
		matchesInfo = c1.getMatchesInfo("234");
		assertNotNull(matchesInfo);
		assertEquals(1, matchesInfo[0]);
		assertEquals(1, matchesInfo[1]);
		assertEquals(3, matchesInfo[2]);
		assertNull(c1.getMatchesInfo("233"));
		assertNull(c1.getMatchesInfo("789"));
		assertNotNull(c1.getMatchesInfo("232"));
	}

	public final void testIsMatchesWith_Normal() {
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
		assertNotNull(c1.getMatchesInfo("123"));
		assertNotNull(c1.getMatchesInfo("1234"));
		assertNull(c1.getMatchesInfo("12345"));
		assertNotNull(c1.getMatchesInfo("123423"));
		assertNull(c1.getMatchesInfo("1234231"));
		assertNotNull(c1.getMatchesInfo("223"));
		assertNotNull(c1.getMatchesInfo("2233"));
		assertNotNull(c1.getMatchesInfo("223323"));
		assertNotNull(c1.getMatchesInfo("234"));
		assertNotNull(c1.getMatchesInfo("2342"));
		assertNotNull(c1.getMatchesInfo("23423"));
		assertNull(c1.getMatchesInfo("233"));
		assertNotNull(c1.getMatchesInfo("232"));
		assertNull(c1.getMatchesInfo("124"));
	}

	public final void testIsMatchesWith_HaveNullKey() {
		ChooseContactsForDisplay c1 = createDisplayContacts(1, "测试匹_'配", "13300000000");
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
		assertNotNull(c1.getMatchesInfo("123"));
		assertNotNull(c1.getMatchesInfo("1234"));   //多次符合条件后又不符合再又符合
		assertNull(c1.getMatchesInfo("12345"));   //多次符合条件后又不符合再又符合
		assertNotNull(c1.getMatchesInfo("223"));
		assertNotNull(c1.getMatchesInfo("2233"));
		assertNotNull(c1.getMatchesInfo("223323"));
		assertNotNull(c1.getMatchesInfo("234"));
		assertNotNull(c1.getMatchesInfo("2342"));
		assertNotNull(c1.getMatchesInfo("23423"));
		assertNull(c1.getMatchesInfo("233"));
		assertNotNull(c1.getMatchesInfo("232"));
		assertNull(c1.getMatchesInfo("124"));
	}

	public final void testIsMatchesWith_All_Englist() {
		ChooseContactsForDisplay c1 = createDisplayContacts(1, "The day you went away", "13300000000");
		//843 329 968 9368 2929
		int[] matchesInfo = c1.getMatchesInfo("3992");
		assertNotNull(matchesInfo);
		assertEquals(1, matchesInfo[0]);
		assertEquals(4, matchesInfo[1]);
		assertEquals(14, matchesInfo[2]);
		assertNotNull(c1.getMatchesInfo("292"));
		matchesInfo = c1.getMatchesInfo("9936");
		assertNotNull(matchesInfo);
		assertEquals(1, matchesInfo[0]);
		assertEquals(8, matchesInfo[1]);
		assertEquals(7, matchesInfo[2]);
		assertNotNull(c1.getMatchesInfo("936829"));
		assertNotNull(c1.getMatchesInfo("8329993"));
		assertNotNull(c1.getMatchesInfo("396892929"));
		assertNotNull(c1.getMatchesInfo("396892"));
		assertNotNull(c1.getMatchesInfo("3968929"));
		assertNull(c1.getMatchesInfo("433"));
		assertNull(c1.getMatchesInfo("299"));
		assertNull(c1.getMatchesInfo("6893"));
	}

	public final void testIsMatchesWith_Have_Englist() {
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

		String keyString = py.toString();
		assertEquals("7,94264,1,936,1,0,2,3645474,7,0,1,62,1,742,1", keyString);
//		Pinyin parse = HanziToPinyin.Pinyin.parse(keyString);
//		assertEquals("7,94264,1,936,1,0,2,3645474,7,0,1,62,1,742,1", parse.toString());

		int[] matchesInfo = c1.getMatchesInfo("942649362742");
		assertNotNull(c1.getMatchesInfo("9426493627"));   //多次符合条件后又不符合再又符合
		assertNotNull(c1.getMatchesInfo("94264936274"));   //多次符合条件后又不符合再又符合
		matchesInfo = c1.getMatchesInfo("942649362742");
		assertNotNull(matchesInfo);   //多次符合条件后又不符合再又符合
		assertEquals(1, matchesInfo[0]);
		assertEquals(0, matchesInfo[1]);
		assertEquals(14, matchesInfo[2]);
		matchesInfo = c1.getMatchesInfo("9362");
		assertNotNull(matchesInfo);   //多次符合条件后又不符合再又符合
		assertEquals(1, matchesInfo[0]);
		assertEquals(1, matchesInfo[1]);
		assertEquals(12, matchesInfo[2]);
		matchesInfo = c1.getMatchesInfo("93636");
		assertNotNull(matchesInfo);
		assertEquals(1, matchesInfo[0]);
		assertEquals(1, matchesInfo[1]);
		assertEquals(5, matchesInfo[2]);
		assertNotNull(c1.getMatchesInfo("627"));
		assertNotNull(c1.getMatchesInfo("9367"));   //多次符合条件后又不符合再又符合
		matchesInfo = c1.getMatchesInfo("367");
		assertNotNull(matchesInfo);
		assertEquals(1, matchesInfo[0]);
		assertEquals(4, matchesInfo[1]);
		assertEquals(10, matchesInfo[2]);
		assertNull(c1.getMatchesInfo("94936"));
		assertNull(c1.getMatchesInfo("3636"));
		assertNull(c1.getMatchesInfo("6272"));
		assertNull(c1.getMatchesInfo("9426493627422"));
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
