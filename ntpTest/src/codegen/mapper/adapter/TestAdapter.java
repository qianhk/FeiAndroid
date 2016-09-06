public class TestAdapter extends PagingListAdapter<Test> {

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            TestViewHolder viewHolder = (TestViewHolder) v.getTag(R.id.tag_view_holder);
            int viewId = v.getId();
        }
    };

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout., parent, false);
            convertView.setTag(R.id.tag_view_holder, new TestViewHolder(convertView, mOnClickListener));
        }
        TestViewHolder viewHolder = (TestViewHolder) convertView.getTag(R.id.tag_view_holder);
        viewHolder.flushView(getItem(position));
        return convertView;
    }

    public static class TestViewHolder {

        private View mRootView;

        public TestViewHolder(View view, View.OnClickListener onClickListener) {
            mRootView = view;
        }

        public void flushView(Test data) {
        }

    }

}