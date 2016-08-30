package com.njnu.kai.plugin.preference.persistence;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/8/30
 */
interface MvpDefaultTemplate {

    String FRAGMENT_DEFAULT_TEMPLATE = "public class ${DataItemType}ListFragment extends PagingListWithActionBarFragment<${DataItemType}> {\n" +
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
}
