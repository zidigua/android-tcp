package com.threepapa.vmtcp.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 类描述：MD5加密工具类
 * @author huangchao
 * @since 1.0
 */
public class MD5Util {

	/** * 16进制字符集 */
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/** * 指定算法为MD5的MessageDigest */
	private static MessageDigest messageDigest = null;

	/** * 初始化messageDigest的加密算法为MD5 */
	static {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * * 获取文件的MD5值
	 * 
	 * @param file目标文件
	 * 
	 * @return MD5字符串
	 */
	public static String getFileMD5String(File file) {
		String md5Str = "";
		FileInputStream in = null;
		BufferedInputStream bis = null;
		try {
			in = new FileInputStream(file);
			bis = new BufferedInputStream(in);
			int len = 0;
			byte[] buffer = new byte[4096];
			while ((len = bis.read(buffer)) != -1) {
				messageDigest.update(buffer, 0, len);
			}
			md5Str = bytesToHex(messageDigest.digest());
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return md5Str;
	}

	/**
	 * * 获取文件的MD5值
	 * 
	 * @param fileName目标文件的完整名称
	 * 
	 * @return MD5字符串
	 */
	public static String getFileMD5String(String fileName) {
		return getFileMD5String(new File(fileName));
	}

	/**
	 * * MD5加密字符串
	 * 
	 * @param str目标字符串
	 * 
	 * @return MD5加密后的字符串
	 */

	public static String getMD5String(String str) {

		return getMD5String(str.getBytes());
	}

	/**
	 * * MD5加密以byte数组表示的字符串
	 * 
	 * @param bytes目标byte数组
	 * 
	 * @return MD5加密后的字符串
	 */

	public static String getMD5String(byte[] bytes) {
		messageDigest.update(bytes);
		return bytesToHex(messageDigest.digest());
	}

	/**
	 * * 校验密码与其MD5是否一致
	 * 
	 * @param pwd密码字符串
	 * 
	 * @param md5
	 *            基准MD5值
	 * 
	 * @return 检验结果
	 */
	public static boolean checkPassword(String pwd, String md5) {
		return getMD5String(pwd).equalsIgnoreCase(md5);
	}

	/**
	 * * 校验密码与其MD5是否一致
	 * 
	 * @param pwd以字符数组表示的密码
	 * 
	 * @param md5基准MD5值
	 * 
	 * @return 检验结果
	 */
	public static boolean checkPassword(char[] pwd, String md5) {
		return checkPassword(new String(pwd), md5);

	}

	/**
	 * * 检验文件的MD5值
	 * 
	 * @param file目标文件
	 * 
	 * @param md5基准MD5值
	 * 
	 * @return 检验结果
	 */
	public static boolean checkFileMD5(File file, String md5) {
		return getFileMD5String(file).equalsIgnoreCase(md5);

	}

	/**
	 * * 检验文件的MD5值
	 * 
	 * @param fileName目标文件的完整名称
	 * 
	 * @param md5基准MD5值
	 * 
	 * @return 检验结果
	 */
	public static boolean checkFileMD5(String fileName, String md5) {
		return checkFileMD5(new File(fileName), md5);

	}

	/**
	 * * 将字节数组转换成16进制字符串
	 * 
	 * @param bytes目标字节数组
	 * 
	 * @return 转换结果
	 */
	public static String bytesToHex(byte bytes[]) {
		return bytesToHex(bytes, 0, bytes.length);

	}

	/**
	 * * 将字节数组中指定区间的子数组转换成16进制字符串
	 * 
	 * @param bytes目标字节数组
	 * 
	 * @param start
	 *            起始位置（包括该位置）
	 * 
	 * @param end结束位置
	 *            （不包括该位置）
	 * 
	 * @return 转换结果
	 */
	public static String bytesToHex(byte bytes[], int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < start + end; i++) {
			sb.append(byteToHex(bytes[i]));
		}
		return sb.toString();

	}

	/**
	 * * 将单个字节码转换成16进制字符串
	 * 
	 * @param bt目标字节
	 * 
	 * @return 转换结果
	 */
	public static String byteToHex(byte bt) {
		return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];

	}

	// //
	// public static void main(String[] args) throws IOException {
	// long begin = System.currentTimeMillis();
	// String md5 = getFileMD5String(new File("c://aa.cer"));
	// long end = System.currentTimeMillis();
	// System.out.println("MD5:\t" + md5 + "\nTime:\t" + (end - begin) + "ms");
	//
	// }


    /****************************************************************************/
    // 参数用MD5加密
    public static String toMD5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(toHex(messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    // 转成16进制，用于生成signiture。
    private static String toHex(byte b) {
        String s = Integer.toHexString(0xFF & b);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }
    /****************************************************************************/

}
