package org.bridge;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONArray;
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
	private static String encrypt(String content) {
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
	private static String decrypt(String content) {
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
	
	/**
	 * 
	 * <p>方法名称: encodeJson|描述: 加密</p>
	 * @param object
	 * @return
	 */
	public static String encodeJson(Object object) {
		try {
			if(null!=object) {
				JSONArray jsonArray = JSONArray.fromObject(object);
				jsonArray.add(StringUtil.encodeToMD5(jsonArray.get(0).toString()));
				return AESUtil.encrypt(jsonArray.toString());
			}
		} catch (Exception ex) {
			Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	/**
	 * 
	 * <p>方法名称: decodeJson|描述: 解密</p>
	 * @param jsonStr
	 * @return
	 */
	public static Object decodeJson(String jsonStr) {
		try {
			if(!StringUtil.isNullOrEmpty(jsonStr)) {
				jsonStr = AESUtil.decrypt(jsonStr);
				JSONArray jsonArray = JSONArray.fromObject(jsonStr);
				if(jsonArray.size()>1) {
					String Nmd5 = StringUtil.encodeToMD5(jsonArray.get(0).toString());
					String Omd5 = jsonArray.get(1).toString();
					if(Nmd5.equals(Omd5)) {
						return jsonArray.get(0);
					}
				}
			}
		} catch (Exception ex) {
			Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	public static void main(String[] args) {
//		System.out.println("PASSWORD:"+PASSWORD);
//		String s = "[ {\"UserID\":11, \"Name\":{\"FirstName\":\"Truly\",\"LastName\":\"Zhu\"}, \"Email\":\"zhuleipro◎hotmail.com\"}, {\"UserID\":12, \"Name\":{\"FirstName\":\"Jeffrey\",\"LastName\":\"Richter\"}, \"Email\":\"xxx◎xxx.com\"}, {\"UserID\":13, \"Name\":{\"FirstName\":\"Scott\",\"LastName\":\"Gu\"}, \"Email\":\"xxx2◎xxx2.com\"} ]";
//		System.out.println("原句:" + s);
//		String s1 = AESUtil.encrypt(s);
//		System.out.println("加密:" + s1);
//		System.out.println("解密:" + AESUtil.decrypt(s1));

		DeptOpinionModel deptOpinion = new DeptOpinionModel();
		deptOpinion.setId(1);
		deptOpinion.setTitle("标题");
		deptOpinion.setContent("内容部分**123~.。");
		deptOpinion.setUser_code("xb");
		
		JSONArray array=JSONArray.fromObject(deptOpinion);
		System.out.println("原句:"+array.get(0));
		
		String decodeStr = AESUtil.encodeJson(deptOpinion);
		System.out.println("传输中:"+decodeStr);
		
		System.out.println("解密并核对后:"+AESUtil.decodeJson(decodeStr).toString());

	}
}