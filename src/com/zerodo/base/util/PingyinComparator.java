package com.zerodo.base.util;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import com.zerodo.base.common.CommonModel;

public class PingyinComparator implements Comparator {

	private Collator collator = Collator.getInstance();

	public PingyinComparator() {
	}

	/**
	 * compare ʵ������
	 * 
	 * @param arg1
	 *            Object
	 * @param arg2
	 *            Object
	 * @return int
	 */
	public int compare(Object arg1, Object arg2) {
		CommonModel model1=(CommonModel) arg1;
		CommonModel model2=(CommonModel) arg2;
		// ���ַ���ת��Ϊһϵ�б��أ����ǿ����Ա�����ʽ�� CollationKeys ��Ƚ�
		CollationKey key1 = collator.getCollationKey(getPinYinHeadChar(model1.getFdName()));// Ҫ�벻���ִ�Сд���бȽ���o1.toString().toLowerCase()
		CollationKey key2 = collator.getCollationKey(getPinYinHeadChar(model2.getFdName()));
		return key1.compareTo(key2);
	}
	
	
    /**
     * �õ���������ĸ
     * 
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {

        String convert = "";
        if(str.length()>0){
            char word = str.charAt(0);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        //���´���õ�ÿ�����ĵ�����ĸ
//        for (int j = 0; j < str.length(); j++) {
//            char word = str.charAt(j);
//            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
//            if (pinyinArray != null) {
//                convert += pinyinArray[0].charAt(0);
//            } else {
//                convert += word;
//            }
//        }
        return convert;
    }
	/**
     * �õ� ȫƴ
     * 
     * @param src
     * @return
     */
    public static String getPingYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // �ж��Ƿ�Ϊ�����ַ�
                if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else {
                    t4 += java.lang.Character.toString(t1[i]);
                }
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }
    
    /**
     * ���ַ���ת��ΪASCII��
     * 
     * @param cnStr
     * @return
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            // System.out.println(Integer.toHexString(bGBK[i]&0xff));
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }
}