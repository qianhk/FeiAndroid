package test.com.njnu.kai.feisms;

import com.njnu.kai.feisms.ChooseContactsForDisplay;
import com.njnu.kai.feisms.HanziToPinyin;

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
	
//	public final void testIsAccordWith_Normal() {
//		ChooseContactsForDisplay c1 = createDisplayContacts(1, "测试匹配", "13300000000");
//		HanziToPinyin.Pinyin py = new HanziToPinyin.Pinyin(4); 
//	c1.mPinyin = py;
//		py.mPlaceHolder[0] = 1;
//		py.mPlaceHolder[1] = 1;
//		py.mPlaceHolder[2] = 1;
//		py.mPlaceHolder[3] = 1;
//		py.mT9Key[0] = new char[] {'1', '2', '3'};
//		py.mT9Key[1] = new char[] {'2', '2', '3'};
//		py.mT9Key[2] = new char[] {'3', '2', '3'};
//		py.mT9Key[3] = new char[] {'4', '2', '3'};
//		assertTrue(c1.isAccordWith("123"));
//		assertTrue(c1.isAccordWith("1234"));
//		assertTrue(c1.isAccordWith("223"));
//		assertTrue(c1.isAccordWith("2234"));
//		assertTrue(c1.isAccordWith("223423"));
//		assertTrue(c1.isAccordWith("234"));
//		assertTrue(c1.isAccordWith("2342"));
//		assertTrue(c1.isAccordWith("23423"));
//		assertFalse(c1.isAccordWith("233"));
//		assertFalse(c1.isAccordWith("232"));
//		assertFalse(c1.isAccordWith("124"));
//	}
//	
//	public final void testIsAccordWith_HaveNullKey() {
//		ChooseContactsForDisplay c1 = createDisplayContacts(1, "测试匹_配", "13300000000");
//		HanziToPinyin.Pinyin py = new HanziToPinyin.Pinyin(5); 
//	c1.mPinyin = py;
//		py.mPlaceHolder[0] = 1;
//		py.mPlaceHolder[1] = 1;
//		py.mPlaceHolder[2] = 1;
//		py.mPlaceHolder[3] = 1;
//		py.mPlaceHolder[4] = 1;
//		py.mT9Key[0] = new char[] {'1', '2', '3'};
//		py.mT9Key[1] = new char[] {'2', '2', '3'};
//		py.mT9Key[2] = new char[] {'3', '2', '3'};
//		py.mT9Key[3] = null;
//		py.mT9Key[4] = new char[] {'4', '2', '3'};
//		assertTrue(c1.isAccordWith("123"));
//		assertTrue(c1.isAccordWith("1234"));
//		assertTrue(c1.isAccordWith("223"));
//		assertTrue(c1.isAccordWith("2234"));
//		assertTrue(c1.isAccordWith("223423"));
//		assertTrue(c1.isAccordWith("234"));
//		assertTrue(c1.isAccordWith("2342"));
//		assertTrue(c1.isAccordWith("23423"));
//		assertFalse(c1.isAccordWith("233"));
//		assertFalse(c1.isAccordWith("232"));
//		assertFalse(c1.isAccordWith("124"));
//	}
	
//	public final void testIsAccordWith_All_Englist() {
//		ChooseContactsForDisplay c1 = createDisplayContacts(1, "The day you went away", "13300000000");
//	}
	
//	public final void testIsAccordWith_Have_Englist() {
//		ChooseContactsForDisplay c1 = createDisplayContacts(1, "中文 English 那啥", "13300000000");
//	}

}
