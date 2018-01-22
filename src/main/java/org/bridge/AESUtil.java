package org.bridge;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @模块：[AESUtil]
 * @描述: AES高级加密工具类
 * @作者: BaiQingyu
 * @日期: 2018年1月19日 上午11:27:35
 * <p>版权所有:(C)1998-2018 CIECC</p>
 */
public class AESUtil {
	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";// 默认的加密算法
	private static final String PASSWORD = "i1,x2.g3!r4@w5*b6^j7%s8$e9#n0p~";// 密码(发送和接收端需保持一致)

	/**
	 * AES 加密操作
	 * @param content 待加密内容
	 * @return 返回Base64转码后的加密数据
	 */
	public static String encrypt(String content) {
		try {
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());// 初始化为加密模式的密码器
			byte[] result = cipher.doFinal(byteContent);// 加密
			return Base64.encodeBase64String(result);// 通过Base64转码返回
		} catch (Exception ex) {
			Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	/**
	 * AES 解密操作
	 * @param content
	 * @return 返回解密后的字符串
	 */
	public static String decrypt(String content) {
		try {
			// 实例化
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
			// 执行操作
			byte[] result = cipher.doFinal(Base64.decodeBase64(content));
			return new String(result, "utf-8");
		} catch (Exception ex) {
			Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	/**
	 * 生成加密秘钥
	 * @return 返回加密秘钥
	 */
	private static SecretKeySpec getSecretKey() {
		// 返回生成指定算法密钥生成器的 KeyGenerator 对象
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
			// AES 要求密钥长度为 128
			kg.init(128, new SecureRandom(PASSWORD.getBytes()));
			// 生成一个密钥
			SecretKey secretKey = kg.generateKey();
			return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println("PASSWORD:"+PASSWORD);
		String s = "hello,您好,dsafsafidsaiofiosdafiosd放到防守发哦一都我告诉爱哦施工队港交所的骄傲过凶巴巴 是打发ID覅偶撒酒疯is大姐夫iOS见覅欧锦赛伏见司发发送对接覅圣诞节覅机电司哦啊见覅圣诞节覅将发送带发酵萨迪时间啊迪欧伏见司爱打飞机啥都平均分师大肌肤决赛破飞机上打破附件iOS爬到发ISO都怕就发兄弟撒娇佛萨基哦发动机赛欧批发价is阿斗平均分iOS大盘鸡覅偶深怕的加分撒的加分IP是大家fisdapfisdaufewjf8sajf偏大附件师大肌肤iOS大盘鸡费帕金森佛教第四票房就是啊飞机手帕见覅配送费及撒娇发的";
		System.out.println("原句:" + s);
		String s1 = AESUtil.encrypt(s);
		System.out.println("加密:" + s1);
		System.out.println("解密:" + AESUtil.decrypt(s1));
	}

}