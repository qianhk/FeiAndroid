package com.njnu.kai.plugin.preference.persistence;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/8/30
 */
interface MvpDefaultTemplate {

    String LIST_FRAGMENT_DEFAULT_TEMPLATE = "public class ${DataItemType}ListFragment extends PagingListWithActionBarFragment<${DataItemType}> {\n" +
            "\n" +
            "    @Override\n" +
            "    protected void onInitActionBar() {\n" +
            "        super.onInitActionBar();\n" +
            "        setTitle(\"${DataItemType} Page\");\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    protected IPagingListAdapter<${DataItemType}> onCreateAdapter(Context context) {\n" +
            "        resetSDate();\n" +
            "        return new ${DataItemType}ListAdapter();\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void onReloadData(int page, boolean auto) {\n" +
            "        // DataFetcher.fetch(this, , this::updateDataListWithResult);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    protected CharSequence lastPageFooterText(int count) {\n" +
            "        return String.format(\"共%d条明细\", count);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    protected void onListItemClick(int position, long id, ${DataItemType} item, View view) {\n" +
            "        super.onListItemClick(position, id, item, view);\n" +
            "        \n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void onNewResume() {\n" +
            "        super.onNewResume();\n" +
            "        if (sFlushFromNet) {\n" +
            "            reloadData();\n" +
            "        }\n" +
            "        resetSDate();\n" +
            "    }\n" +
            "\n" +
            "    private void resetSDate() {\n" +
            "        sFlushFromNet = false;\n" +
            "    }\n" +
            "\n" +
            "    public static boolean sFlushFromNet;\n" +
            "}";

    String ACTIVITY_DEFAULT_TEMPLATE = "import android.os.Bundle;\n" +
            "import android.app.Activity;\n" +
            "${IMPORTS}\n" +
            "\n" +
            "public class ${CLASS_NAME}Activity extends Activity ${INTERFACES} {\n" +
            "\n" +
            "    ${FIELDS}\n" +
            "\n" +
            "    @Override\n" +
            "    protected void onCreate(Bundle savedInstanceState) {\n" +
            "        super.onCreate(savedInstanceState);\n" +
            "        setContentView(R.layout.${RESOURCE_NAME});\n" +
            "\n" +
            "        ${ASSIGNMENTS}\n" +
            "    }\n" +
            "\n" +
            "    ${METHODS}\n" +
            "}";

    String LIST_ADAPTER_DEFAULT_TEMPLATE = "public class ${DataItemType}Adapter extends PagingListAdapter<${DataItemType}> {\n" +
            "\n" +
            "    private View.OnClickListener mOnClickListener = new View.OnClickListener() {\n" +
            "\n" +
            "        @Override\n" +
            "        public void onClick(View v) {\n" +
            "            ${DataItemType}ViewHolder viewHolder = (${DataItemType}ViewHolder) v.getTag(R.id.tag_view_holder);\n" +
            "            int viewId = v.getId();\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    @Override\n" +
            "    public long getItemId(int position) {\n" +
            "        return position;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public View getView(int position, View convertView, ViewGroup parent) {\n" +
            "        if (convertView == null) {\n" +
            "            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout., parent, false);\n" +
            "            convertView.setTag(R.id.tag_view_holder, new ${DataItemType}ViewHolder(convertView, mOnClickListener));\n" +
            "        }\n" +
            "        ${DataItemType}ViewHolder viewHolder = (${DataItemType}ViewHolder) convertView.getTag(R.id.tag_view_holder);\n" +
            "        viewHolder.flushView(getItem(position));\n" +
            "        return convertView;\n" +
            "    }\n" +
            "\n" +
            "    public static class ${DataItemType}ViewHolder {\n" +
            "\n" +
            "        private View mRootView;\n" +
            "\n" +
            "        public ${DataItemType}ViewHolder(View view, View.OnClickListener onClickListener) {\n" +
            "            mRootView = view;\n" +
            "        }\n" +
            "\n" +
            "        public void flushView(${DataItemType} data) {\n" +
            "        }\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "}";

    String LIST_PRESENTER_DEFAULT_TEMPLATE = "public class ${DataItemType}Presenter extends BasePresenter<${DataItemType}InfoVO, I${DataItemType}View> {\n" +
            "\n" +
            "    private static final String TAG = \"{DataItemType}Presenter\";\n" +
            "    private long mId;\n" +
            "\n" +
            "    /**\n" +
            "     * @param pageDataLoadingView pageDataLoadingView\n" +
            "     * @param id id\n" +
            "     */\n" +
            "    public ${DataItemType}Presenter(I${DataItemType}View pageDataLoadingView, long id) {\n" +
            "        super(pageDataLoadingView);\n" +
            "        mId = id;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    protected void load(final int loadingPage, boolean isForceRefresh) {\n" +
            "//        execute(new ${DataItemType}UseCase(Repository.getInstance()).xxx(mId), new ${DataItemType}Subscriber());\n" +
            "    }\n" +
            "\n" +
            "    public class ${DataItemType}Subscriber extends APISubscriber<List<${DataItemType}InfoVO>> {\n" +
            "\n" +
            "        @Override\n" +
            "        public void onError(Throwable t) {\n" +
            "            super.onError(t);\n" +
            "            handleLoadFailure(t);\n" +
            "        }\n" +
            "\n" +
            "        @Override\n" +
            "        public void onNext(List<${MODEL}> list) {\n" +
            "            try {\n" +
            "                handleLoadSuccess(null, 0, 1);\n" +
            "            } catch (Exception e) {\n" +
            "                e.printStackTrace();\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";
}
