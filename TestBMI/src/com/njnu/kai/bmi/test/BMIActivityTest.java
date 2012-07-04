package com.njnu.kai.bmi.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.njnu.kai.bmi.BMIActivity;

public class BMIActivityTest extends ActivityInstrumentationTestCase2<BMIActivity> {
//  public class MyApplicationTests extends ApplicationTestCase<ApiDemosApplication>
//	public class ForwardingTest extends ActivityUnitTestCase<Forwarding>
//	public class LocalServiceTest extends ServiceTestCase<LocalService>
//	public class MorseCodeConverterTest extends TestCase
//	public class Focus2AndroidTest extends AndroidTestCase
//  public class NotePadProviderTest extends ProviderTestCase2<NotePadProvider>

//	public BMIActivityTest(Class<BMIActivity> activityClass) {
//		super(activityClass);
//	}

	BMIActivity bmiActivity = null;
	private static final String TAG = "BMIActivityTest";

	public BMIActivityTest() {
//		Log.v(TAG, "BMIActivityTest()");
		super(BMIActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bmiActivity = getActivity();
		Log.v(TAG, "setup getActivity " + bmiActivity);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCalcBMI() {
		double bmi = bmiActivity.calcBMI(100, 10);
		Log.v(TAG, "clac BMi value is: " + bmi);
		assertTrue("calcBMI error", bmi > 0.99 && bmi < 1.01);
	}

}
