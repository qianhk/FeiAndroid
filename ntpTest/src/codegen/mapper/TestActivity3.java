package codegen.mapper;

/**
 * Author: kai
 * Created on 2017/6/5
 */
public class TestActivity3 extends WrapFragmentActionBarActivity {

    @Override
    protected String getActionBarTitle() {
        return "title2 src";
    }

    @Override
    protected Class wrapFragmentClass() {
        return TestFragment.class;
    }

};
            