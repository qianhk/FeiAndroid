package com.njnu.kai.feisms;

import java.util.Stack;

import android.text.Spannable;
import android.text.SpannableString;

public class ChooseContactsForDisplay {
	public long mContactsId;
	public String mName;
	public String mPhone;
	public HanziToPinyin.Pinyin mPinyin;

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder(32);
		build.append(mName);
		while (build.length() < 3) {
			build.append('\u3000'); // 全角空格
		}
		build.append(": ");
		build.append(mPhone);
		return build.toString();
	}
	
	public SpannableString toString(String filterStr) {
		int[] matchesInfo = getMatchesInfo(filterStr);
		StringBuilder build = new StringBuilder(32);
		build.append(mName);
		while (build.length() < 3) {
			build.append('\u3000'); // 全角空格
		}
		build.append(": ");
		int prefixNameLen = build.length();
		build.append(mPhone);
		SpannableString ss = new SpannableString(build.toString());
		if (matchesInfo != null) {
			int beginPos = matchesInfo[1] + (matchesInfo[0] == 0 ? prefixNameLen : 0);
			ss.setSpan(FeiSMSConst.FOREGROUNDCOLORSPAN_YELLO, beginPos, beginPos + matchesInfo[2], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return ss;
	}
	
	private class KeyDigitalMatchesInfo {
		private int mT9KeyIndex;
		private int mDigitalIndex;
		private int mMatchesLen;
		
		public KeyDigitalMatchesInfo(int t9KeyIndex, int digitalIndex, int matchesLen) {
			mT9KeyIndex = t9KeyIndex;
			mDigitalIndex = digitalIndex;
			mMatchesLen = matchesLen;
		}
	}
	
	private boolean isRightMatchesWith(char[] t9KeyItem, char[] digitalText, int idxDigital) {
		boolean matches = true;
		int idxT9Key = 1;
		++idxDigital;
		for (; idxT9Key < t9KeyItem.length && idxDigital < digitalText.length; ++idxT9Key, ++idxDigital) {
			if (t9KeyItem[idxT9Key] != digitalText[idxDigital]) {
				matches = false;
				break;
			}
		}
		return matches;
	}

	private int isSubMatchesWith(char[] digitalText, char[][] t9Key, int t9KeyFromIndex, int[] placeHolder) {
		boolean matches = false;
		int digitalLen = digitalText.length;
		int idxDigital = 0;
		int idxT9Key = t9KeyFromIndex;
		int matchesLen = 0;
		Stack<KeyDigitalMatchesInfo> stack = new Stack<KeyDigitalMatchesInfo>();
		for (; idxT9Key < t9Key.length; ++idxT9Key) {
			char[] t9KeyItem = t9Key[idxT9Key];
			if (t9KeyItem == null) {
				matchesLen += placeHolder[idxT9Key];
				continue;
			}
			if (t9KeyItem[0] != digitalText[idxDigital]) {
				if (stack.empty()) {
					break;
				} else {
					KeyDigitalMatchesInfo kdmInfo = stack.pop();
					idxT9Key = kdmInfo.mT9KeyIndex;
					idxDigital = kdmInfo.mDigitalIndex;
					++idxDigital;
					matchesLen = kdmInfo.mMatchesLen;
				}
			} else {
				if (isRightMatchesWith(t9KeyItem, digitalText, idxDigital)) {
					if (idxDigital + t9KeyItem.length >= digitalLen) {
						matches = true;
						if (placeHolder[idxT9Key] > 1) {
							matchesLen += digitalLen - idxDigital;
						} else {
							++matchesLen;
						}
						break;
					} else {
						matchesLen += placeHolder[idxT9Key];
						stack.push(new KeyDigitalMatchesInfo(idxT9Key, idxDigital, matchesLen));
						idxDigital += t9KeyItem.length;
					}
				} else {
					++idxDigital;
					matchesLen += placeHolder[idxT9Key];
				}
			}
		}

		return matches ? matchesLen : 0;
	}

	public int[] getMatchesInfo(String digitalText) {
		boolean matches = false;
		int[] info = new int[3];
		int indexOfDigital = mPhone.indexOf(digitalText);
		if (indexOfDigital >= 0) {
			matches = true;
			info[0] = 0;
			info[1] = indexOfDigital;
			info[2] = digitalText.length();
		} else {
			info[0] = 1;
			char[] arrDigitalText = digitalText.toCharArray();
			char[][] t9Key = mPinyin.getT9Key();
			int[] placeHolder = mPinyin.getPlaceHolder();
			int matchOffset = 0;
			for (int idx = 0; idx < t9Key.length; ++idx) {
				char[] t9KeyItem = t9Key[idx];
				if (t9KeyItem != null) {
					int matchLen = isSubMatchesWith(arrDigitalText, t9Key, idx, placeHolder);
					if (matchLen > 0) {
						matches = true;
						info[1] = matchOffset;
						info[2] = matchLen;
						break;
					}
				}
				matchOffset += placeHolder[idx];
			}
		}

		return matches ? info : null;
	}
}
