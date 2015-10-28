package util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 15-10-28
 */
public class LyricXmlParseUtils {

    public static class LyricItem {
        public String mLrc;
        public String mQrc;
        public String mTrans;
        public String mTxt;
        public String mRoma;
    }

    public static List<LyricItem> parserXmlBySax(InputStream inputStream) throws Exception {
        //    创建一个SAXParserFactory解析工厂类
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //    实例化一个SAXParser解析类
        SAXParser parser = factory.newSAXParser();
        //    实例化我们的MyHandler类
        MyHandler myHandler = new MyHandler();
        //    根据我们自定义的Handler来解析xml文档
        parser.parse(inputStream, myHandler);

        return myHandler.getLyricList();
    }

    public static class MyHandler extends DefaultHandler {

        private List<LyricItem> lyricList;
        private LyricItem mLyricItem;

        private StringBuilder strBuilder = new StringBuilder();

        public List<LyricItem> getLyricList() {
            return lyricList;
        }

        //    当解析到文档开始时的回调方法
        @Override
        public void startDocument() throws SAXException {
            lyricList = new ArrayList<LyricItem>();
        }

        //    当解析到xml的标签时的回调方法
        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            if ("item".equals(qName)) {
                mLyricItem = new LyricItem();
            }
            //    设置当前的标签名
//            currentTag = qName;
            strBuilder.setLength(0);
        }

        //    当解析到xml的文本内容时的回调方法
        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            //    得到当前的文本内容
            strBuilder.append(ch, start, length);
        }

        //    当解析到标签的结束时的回调方法
        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if ("item".equals(qName)) {
                lyricList.add(mLyricItem);
                mLyricItem = null;
            } else {
                final String currentValue = strBuilder.toString();
                if (StringUtils.isNotEmpty(currentValue) && !"\n".equals(currentValue)) {
                    //    判断当前的currentTag是哪个标签
                    if ("lrc".equals(qName)) {
                        mLyricItem.mLrc = currentValue;
                    } else if ("qrc".equals(qName)) {
                        mLyricItem.mQrc = currentValue;
                    } else if ("trans".equals(qName)) {
                        mLyricItem.mTrans = currentValue;
                    } else if ("txt".equals(qName)) {
                        mLyricItem.mTxt = currentValue;
                    } else if ("roma".equals(qName)) {
                        mLyricItem.mRoma = currentValue;
                    }
                }
            }
            strBuilder.setLength(0);
        }
    }
}
