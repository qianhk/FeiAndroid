public class TestListFragment extends PagingListWithActionBarFragment<Test> {

    @Override
    protected void onInitActionBar() {
        super.onInitActionBar();
        setTitle("Test Page");
    }

    @Override
    protected IPagingListAdapter<Test> onCreateAdapter(Context context) {
        resetSDate();
        return new TestListAdapter();
    }

    @Override
    public void onReloadData(int page, boolean auto) {
        // DataFetcher.fetch(this, , this::updateDataListWithResult);
    }

    @Override
    protected CharSequence lastPageFooterText(int count) {
        return String.format("共%d条明细", count);
    }

    @Override
    protected void onListItemClick(int position, long id, Test item, View view) {
        super.onListItemClick(position, id, item, view);
        
    }

    @Override
    public void onNewResume() {
        super.onNewResume();
        if (sFlushFromNet) {
            reloadData();
        }
        resetSDate();
    }

    private void resetSDate() {
        sFlushFromNet = false;
    }

    public static boolean sFlushFromNet;
}