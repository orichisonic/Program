import java.io.UnsupportedEncodingException;

import java.nio.ByteBuffer;

import java.nio.CharBuffer;

import java.nio.charset.Charset;

 

/**

* ArrayUtil,һЩ����byte����Ĳ���������

* <p/>

* Author By: junshan

* Created Date: 2010-12-27 16:17:23

*/

public class ArrayUtil {

 

/**

* ���Ҳ��滻ָ��byte����

*

* @param org of type byte[] ԭ����

* @param search of type byte[] Ҫ���ҵ�����

* @param replace of type byte[] Ҫ�滻������

* @param startIndex of type int ��ʼ��������

* @return byte[] �����µ�����

* @throws UnsupportedEncodingException when

*/

public static byte[] arrayReplace(byte[] org, byte[] search, byte[] replace, int startIndex) throws UnsupportedEncodingException {

int index = indexOf(org, search, startIndex);

if (index != -1) {

int newLength = org.length + replace.length �C search.length;

byte[] newByte = new byte[newLength];

System.arraycopy(org, 0, newByte, 0, index);

System.arraycopy(replace, 0, newByte, index, replace.length);

System.arraycopy(org, index + search.length, newByte, index + replace.length, org.length �C index �C search.length);

int newStart = index + replace.length;

//String newstr = new String(newByte, ��GBK��);

//System.out.println(newstr);

if ((newByte.length �C newStart) > replace.length) {

return arrayReplace(newByte, search, replace, newStart);

}

return newByte;

} else {

return org;

}

}

 

/**

* ��ָ�������copyһ�������鲢����

*

* @param org of type byte[] ԭ����

* @param to �ϲ�һ��byte[]

* @return �ϲ�������

*/

public static byte[] append(byte[] org, byte[] to) {

byte[] newByte = new byte[org.length + to.length];

System.arraycopy(org, 0, newByte, 0, org.length);

System.arraycopy(to, 0, newByte, org.length, to.length);

return newByte;

}

 

/**

* ��ָ�������copyһ�������鲢����

*

* @param org of type byte[] ԭ����

* @param to �ϲ�һ��byte

* @return �ϲ�������

*/

public static byte[] append(byte[] org, byte to) {

byte[] newByte = new byte[org.length + 1];

System.arraycopy(org, 0, newByte, 0, org.length);

newByte[org.length] = to;

return newByte;

}

 

/**

* ��ָ�������copyһ�������鲢����

*

* @param org of type byte[] ԭ����

* @param from ��ʼ��

* @param append Ҫ�ϲ�������

*/

public static void append(byte[] org, int from, byte[] append) {

System.arraycopy(append, 0, org, from, append.length);

}

 

/**

* ��ָ�������copyһ�������鲢����

*

* @param original of type byte[] ԭ����

* @param from ��ʼ��

* @param to ������

* @return ����copy������

*/

public static byte[] copyOfRange(byte[] original, int from, int to) {

int newLength = to �C from;

if (newLength < 0)

throw new IllegalArgumentException(from + �� > �� + to);

byte[] copy = new byte[newLength];

System.arraycopy(original, from, copy, 0,

Math.min(original.length �C from, newLength));

return copy;

}

 

 

public static byte[] char2byte(String encode, char�� chars) {

Charset cs = Charset.forName(encode);

CharBuffer cb = CharBuffer.allocate(chars.length);

cb.put(chars);

cb.flip();

ByteBuffer bb = cs.encode(cb);

return bb.array();

}

 

/**

* ����ָ���������ʼ����

*

* @param org of type byte[] ԭ����

* @param search of type byte[] Ҫ���ҵ�����

* @return int ��������

*/

public static int indexOf(byte[] org, byte[] search) {

return indexOf(org, search, 0);

}

 

/**

* ����ָ���������ʼ����

*

* @param org of type byte[] ԭ����

* @param search of type byte[] Ҫ���ҵ�����

* @param startIndex ��ʼ����

* @return int ��������

*/

public static int indexOf(byte[] org, byte[] search, int startIndex) {

KMPMatcher kmpMatcher = new com.taobao.sketch.util.ArrayUtil.KMPMatcher();

kmpMatcher.computeFailure4Byte(search);

return kmpMatcher.indexOf(org, startIndex);

//return com.alibaba.common.lang.ArrayUtil.indexOf(org, search);

}

 

/**

* ����ָ����������һ�γ�����ʼ����

*

* @param org of type byte[] ԭ����

* @param search of type byte[] Ҫ���ҵ�����

* @return int ��������

*/

public static int lastIndexOf(byte[] org, byte[] search) {

return lastIndexOf(org, search, 0);

}

 

/**

* ����ָ����������һ�γ�����ʼ����

*

* @param org of type byte[] ԭ����

* @param search of type byte[] Ҫ���ҵ�����

* @param fromIndex ��ʼ����

* @return int ��������

*/

public static int lastIndexOf(byte[] org, byte[] search, int fromIndex) {

KMPMatcher kmpMatcher = new com.taobao.sketch.util.ArrayUtil.KMPMatcher();

kmpMatcher.computeFailure4Byte(search);

return kmpMatcher.lastIndexOf(org, fromIndex);

}

 

/**

* KMP�㷨��

* <p/>

* Created on 2011-1-3

*/

static class KMPMatcher {

private int[] failure;

private int matchPoint;

private byte[] bytePattern;

 

/**

* Method indexOf ��

*

* @param text of type byte[]

* @param startIndex of type int

* @return int

*/

public int indexOf(byte[] text, int startIndex) {

int j = 0;

if (text.length == 0 || startIndex > text.length) return -1;

 

for (int i = startIndex; i < text.length; i++) {

while (j > 0 && bytePattern[j] != text[i]) {

j = failure[j - 1];

}

if (bytePattern[j] == text[i]) {

j++;

}

if (j == bytePattern.length) {

matchPoint = i �C bytePattern.length + 1;

return matchPoint;

}

}

return -1;

}

 

/**

* �ҵ�ĩβ����ͷ��ʼ��

*

* @param text of type byte[]

* @param startIndex of type int

* @return int

*/

public int lastIndexOf(byte[] text, int startIndex) {

matchPoint = -1;

int j = 0;

if (text.length == 0 || startIndex > text.length) return -1;

int end = text.length;

for (int i = startIndex; i < end; i++) {

while (j > 0 && bytePattern[j] != text[i]) {

j = failure[j - 1];

}

if (bytePattern[j] == text[i]) {

j++;

}

if (j == bytePattern.length) {

matchPoint = i �C bytePattern.length + 1;

if ((text.length �C i) > bytePattern.length) {

j = 0;

continue;

}

return matchPoint;

}

//������м�ĳ��λ���ң��ҵ�ĩβû�ҵ�������ͷ��ʼ��

if (startIndex != 0 && i + 1 == end) {

end = startIndex;

i = -1;

startIndex = 0;

}

}

return matchPoint;

}

 

 

/**

* �ҵ�ĩβ�󲻻���ͷ��ʼ��

*

* @param text of type byte[]

* @param startIndex of type int

* @return int

*/

public int lastIndexOfWithNoLoop(byte[] text, int startIndex) {

matchPoint = -1;

int j = 0;

if (text.length == 0 || startIndex > text.length) return -1;

 

for (int i = startIndex; i < text.length; i++) {

while (j > 0 && bytePattern[j] != text[i]) {

j = failure[j - 1];

}

if (bytePattern[j] == text[i]) {

j++;

}

if (j == bytePattern.length) {

matchPoint = i �C bytePattern.length + 1;

if ((text.length �C i) > bytePattern.length) {

j = 0;

continue;

}

return matchPoint;

}

}

return matchPoint;

}

 

/**

* Method computeFailure4Byte ��

*

* @param patternStr of type byte[]

*/

public void computeFailure4Byte(byte[] patternStr) {

bytePattern = patternStr;

int j = 0;

int len = bytePattern.length;

failure = new int[len];

for (int i = 1; i < len; i++) {

while (j > 0 && bytePattern[j] != bytePattern[i]) {

j = failure[j - 1];

}

if (bytePattern[j] == bytePattern[i]) {

j++;

}

failure[i] = j;

}

}

}

 

public static void main(String[] args) {

try {

byte[] org = ��kadeadedcfdededghkk��.getBytes(��GBK��);

byte[] search = ��kk��.getBytes(��GBK��);

 

int last = lastIndexOf(org, search, 19);

long t1 = 0;

long t2 = 0;

int f1 = 0;

int f2 = 0;

for (int i = 0; i < 10000; i++) {

long s1 = System.nanoTime();

f1 = indexOf(org, search, 0);

long s2 = System.nanoTime();

f2 = com.alibaba.common.lang.ArrayUtil.indexOf(org, search);

long s3 = System.nanoTime();

t1 = t1 + (s2 �C s1);

t2 = t2 + (s3 �C s2);

}

System.out.println(��kmp=�� + t1 / 10000 + ��,ali=�� + t2 / 10000);

System.out.printf(��f1=�� + f1 + ��,f2=�� + f2);

} catch (UnsupportedEncodingException e) {

e.printStackTrace();

}

}

}
���ƴ���



import java.io.UnsupportedEncodingException;

import java.nio.ByteBuffer;

import java.nio.CharBuffer;

import java.nio.charset.Charset;

 

/**

* ArrayUtil,һЩ����byte����Ĳ���������

* <p/>

* Author By: junshan

* Created Date: 2010-12-27 16:17:23

*/

public class ArrayUtil {

 

/**

* ���Ҳ��滻ָ��byte����

*

* @param org of type byte[] ԭ����

* @param search of type byte[] Ҫ���ҵ�����

* @param replace of type byte[] Ҫ�滻������

* @param startIndex of type int ��ʼ��������

* @return byte[] �����µ�����

* @throws UnsupportedEncodingException when

*/

public static byte[] arrayReplace(byte[] org, byte[] search, byte[] replace, int startIndex) throws UnsupportedEncodingException {

int index = indexOf(org, search, startIndex);

if (index != -1) {

int newLength = org.length + replace.length �C search.length;

byte[] newByte = new byte[newLength];

System.arraycopy(org, 0, newByte, 0, index);

System.arraycopy(replace, 0, newByte, index, replace.length);

System.arraycopy(org, index + search.length, newByte, index + replace.length, org.length �C index �C search.length);

int newStart = index + replace.length;

//String newstr = new String(newByte, ��GBK��);

//System.out.println(newstr);

if ((newByte.length �C newStart) > replace.length) {

return arrayReplace(newByte, search, replace, newStart);

}

return newByte;

} else {

return org;

}

}

 

/**

* ��ָ�������copyһ�������鲢����

*

* @param org of type byte[] ԭ����

* @param to �ϲ�һ��byte[]

* @return �ϲ�������

*/

public static byte[] append(byte[] org, byte[] to) {

byte[] newByte = new byte[org.length + to.length];

System.arraycopy(org, 0, newByte, 0, org.length);

System.arraycopy(to, 0, newByte, org.length, to.length);

return newByte;

}

 

/**

* ��ָ�������copyһ�������鲢����

*

* @param org of type byte[] ԭ����

* @param to �ϲ�һ��byte

* @return �ϲ�������

*/

public static byte[] append(byte[] org, byte to) {

byte[] newByte = new byte[org.length + 1];

System.arraycopy(org, 0, newByte, 0, org.length);

newByte[org.length] = to;

return newByte;

}

 

/**

* ��ָ�������copyһ�������鲢����

*

* @param org of type byte[] ԭ����

* @param from ��ʼ��

* @param append Ҫ�ϲ�������

*/

public static void append(byte[] org, int from, byte[] append) {

System.arraycopy(append, 0, org, from, append.length);

}

 

/**

* ��ָ�������copyһ�������鲢����

*

* @param original of type byte[] ԭ����

* @param from ��ʼ��

* @param to ������

* @return ����copy������

*/

public static byte[] copyOfRange(byte[] original, int from, int to) {

int newLength = to �C from;

if (newLength < 0)

throw new IllegalArgumentException(from + �� > �� + to);

byte[] copy = new byte[newLength];

System.arraycopy(original, from, copy, 0,

Math.min(original.length �C from, newLength));

return copy;

}

 

 

public static byte[] char2byte(String encode, char�� chars) {

Charset cs = Charset.forName(encode);

CharBuffer cb = CharBuffer.allocate(chars.length);

cb.put(chars);

cb.flip();

ByteBuffer bb = cs.encode(cb);

return bb.array();

}

 

/**

* ����ָ���������ʼ����

*

* @param org of type byte[] ԭ����

* @param search of type byte[] Ҫ���ҵ�����

* @return int ��������

*/

public static int indexOf(byte[] org, byte[] search) {

return indexOf(org, search, 0);

}

 

/**

* ����ָ���������ʼ����

*

* @param org of type byte[] ԭ����

* @param search of type byte[] Ҫ���ҵ�����

* @param startIndex ��ʼ����

* @return int ��������

*/

public static int indexOf(byte[] org, byte[] search, int startIndex) {

KMPMatcher kmpMatcher = new com.taobao.sketch.util.ArrayUtil.KMPMatcher();

kmpMatcher.computeFailure4Byte(search);

return kmpMatcher.indexOf(org, startIndex);

//return com.alibaba.common.lang.ArrayUtil.indexOf(org, search);

}

 

/**

* ����ָ����������һ�γ�����ʼ����

*

* @param org of type byte[] ԭ����

* @param search of type byte[] Ҫ���ҵ�����

* @return int ��������

*/

public static int lastIndexOf(byte[] org, byte[] search) {

return lastIndexOf(org, search, 0);

}

 

/**

* ����ָ����������һ�γ�����ʼ����

*

* @param org of type byte[] ԭ����

* @param search of type byte[] Ҫ���ҵ�����

* @param fromIndex ��ʼ����

* @return int ��������

*/

public static int lastIndexOf(byte[] org, byte[] search, int fromIndex) {

KMPMatcher kmpMatcher = new com.taobao.sketch.util.ArrayUtil.KMPMatcher();

kmpMatcher.computeFailure4Byte(search);

return kmpMatcher.lastIndexOf(org, fromIndex);

}

 

/**

* KMP�㷨��

* <p/>

* Created on 2011-1-3

*/

static class KMPMatcher {

private int[] failure;

private int matchPoint;

private byte[] bytePattern;

 

/**

* Method indexOf ��

*

* @param text of type byte[]

* @param startIndex of type int

* @return int

*/

public int indexOf(byte[] text, int startIndex) {

int j = 0;

if (text.length == 0 || startIndex > text.length) return -1;

 

for (int i = startIndex; i < text.length; i++) {

while (j > 0 && bytePattern[j] != text[i]) {

j = failure[j - 1];

}

if (bytePattern[j] == text[i]) {

j++;

}

if (j == bytePattern.length) {

matchPoint = i �C bytePattern.length + 1;

return matchPoint;

}

}

return -1;

}

 

/**

* �ҵ�ĩβ����ͷ��ʼ��

*

* @param text of type byte[]

* @param startIndex of type int

* @return int

*/

public int lastIndexOf(byte[] text, int startIndex) {

matchPoint = -1;

int j = 0;

if (text.length == 0 || startIndex > text.length) return -1;

int end = text.length;

for (int i = startIndex; i < end; i++) {

while (j > 0 && bytePattern[j] != text[i]) {

j = failure[j - 1];

}

if (bytePattern[j] == text[i]) {

j++;

}

if (j == bytePattern.length) {

matchPoint = i �C bytePattern.length + 1;

if ((text.length �C i) > bytePattern.length) {

j = 0;

continue;

}

return matchPoint;

}

//������м�ĳ��λ���ң��ҵ�ĩβû�ҵ�������ͷ��ʼ��

if (startIndex != 0 && i + 1 == end) {

end = startIndex;

i = -1;

startIndex = 0;

}

}

return matchPoint;

}

 

 

/**

* �ҵ�ĩβ�󲻻���ͷ��ʼ��

*

* @param text of type byte[]

* @param startIndex of type int

* @return int

*/

public int lastIndexOfWithNoLoop(byte[] text, int startIndex) {

matchPoint = -1;

int j = 0;

if (text.length == 0 || startIndex > text.length) return -1;

 

for (int i = startIndex; i < text.length; i++) {

while (j > 0 && bytePattern[j] != text[i]) {

j = failure[j - 1];

}

if (bytePattern[j] == text[i]) {

j++;

}

if (j == bytePattern.length) {

matchPoint = i �C bytePattern.length + 1;

if ((text.length �C i) > bytePattern.length) {

j = 0;

continue;

}

return matchPoint;

}

}

return matchPoint;

}

 

/**

* Method computeFailure4Byte ��

*

* @param patternStr of type byte[]

*/

public void computeFailure4Byte(byte[] patternStr) {

bytePattern = patternStr;

int j = 0;

int len = bytePattern.length;

failure = new int[len];

for (int i = 1; i < len; i++) {

while (j > 0 && bytePattern[j] != bytePattern[i]) {

j = failure[j - 1];

}

if (bytePattern[j] == bytePattern[i]) {

j++;

}

failure[i] = j;

}

}

}

 

public static void main(String[] args) {

try {

byte[] org = ��kadeadedcfdededghkk��.getBytes(��GBK��);

byte[] search = ��kk��.getBytes(��GBK��);

 

int last = lastIndexOf(org, search, 19);

long t1 = 0;

long t2 = 0;

int f1 = 0;

int f2 = 0;

for (int i = 0; i < 10000; i++) {

long s1 = System.nanoTime();

f1 = indexOf(org, search, 0);

long s2 = System.nanoTime();

f2 = com.alibaba.common.lang.ArrayUtil.indexOf(org, search);

long s3 = System.nanoTime();

t1 = t1 + (s2 �C s1);

t2 = t2 + (s3 �C s2);

}

System.out.println(��kmp=�� + t1 / 10000 + ��,ali=�� + t2 / 10000);

System.out.printf(��f1=�� + f1 + ��,f2=�� + f2);

} catch (UnsupportedEncodingException e) {

e.printStackTrace();

}

}

}