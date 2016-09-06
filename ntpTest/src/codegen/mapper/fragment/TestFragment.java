public class ${DataItemType}ListFragment extends PagingListWithActionBarFragment<${DataItemType}> {

    @Override
    protected void onInitActionBar() {
        super.onInitActionBar();
        setTitle("${DataItemType} Page");
    }

    @Override
    protected IPagingListAdapter<${DataItemType}> onCreateAdapter(Context context) {
        resetSDate();
        return new ${DataItemType}ListAdapter();
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
    protected void onListItemClick(int position, long id, ${DataItemType} item, View view) {
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